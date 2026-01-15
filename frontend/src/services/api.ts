import axios from 'axios';

import AuthService from "./auth.service";

const API_URL = import.meta.env.VITE_APP_API_URL || 'http://localhost:8080/api'

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    }
})

// Add JWT token to all requests
api.interceptors.request.use(
    (config) => {
        const token = AuthService.getToken()
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// Handle 401 errors (token expired)
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            AuthService.logout()
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

export default api