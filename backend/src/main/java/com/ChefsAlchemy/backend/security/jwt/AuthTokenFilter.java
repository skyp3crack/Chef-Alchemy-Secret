package com.ChefsAlchemy.backend.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//This filter will intercept incoming requests, extract the JWT, validate it, and set the user's authentication context.
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService; // is used to load user-specific data during authentication
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request); // get jwt from request
            logger.info("AuthTokenFilter: Parsing JWT. Found: {}", (jwt != null ? "Yes" : "No"));

            if (jwt != null) {
                boolean isValid = jwtUtils.validateJwtToken(jwt);
                logger.info("AuthTokenFilter: JWT validity: {}", isValid);

                if (isValid) {
                    String username = jwtUtils.getUserNameFromJwtToken(jwt);
                    logger.info("AuthTokenFilter: Username from JWT: {}", username);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    logger.info("AuthTokenFilter: UserDetails loaded for: {}, Authorities: {}",
                            userDetails.getUsername(), userDetails.getAuthorities());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()); // set authentication
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // set
                                                                                                           // details
                    SecurityContextHolder.getContext().setAuthentication(authentication); // set authentication in
                                                                                          // context
                    logger.info("AuthTokenFilter: Authentication set in SecurityContext");
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage(), e);
        }
        filterChain.doFilter(request, response); // pass request to next filter
    }

    private String parseJwt(HttpServletRequest request) { // this method is used to extract jwt from request
        String headerAuth = request.getHeader("Authorization"); // get jwt from request
        logger.info("AuthTokenFilter: Authorization Header: {}", headerAuth);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) { // check if jwt is present and starts
                                                                                   // with "Bearer "
            return headerAuth.substring(7); // return jwt without "Bearer "
        }
        return null;
    }

}
