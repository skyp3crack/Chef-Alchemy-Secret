//class represent user table in the database postgresql
package com.ChefsAlchemy.backend.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "users", uniqueConstraints = { // unique constraints is for the username and email to be unique meaning
                                             // cannot be the same
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})

public class User {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id; // long meaning it can hold large numbers

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // For now, we'll keep roles simple. We can expand this later.
    // For a basic registration, a user might just have a default 'ROLE_USER'.
    // We'll add a default role in the service layer.
    @ElementCollection(fetch = FetchType.EAGER) // Eagerly load roles with user
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    // Constructor is use to create a new object
    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles.add("ROLE_USER");
    }

    // why do we create getter and setter? //Getter is use to get the value of a
    // variable
    // Setter is use to set the value of a variable

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addRole(String role) { // why dont we have get roles?
        this.roles.add(role);
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getRoles() {
        return roles;
    }

}
