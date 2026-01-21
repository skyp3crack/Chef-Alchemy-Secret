import axios from 'axios'

const API_URL = import.meta.env.VITE_APP_API_URL || 'http://localhost:8080/api'

export interface LoginResponse {
    id: number
    username: string
    email: string
    roles: string[]
    tokenType: string
    accessToken: string
}

export interface UserData {
    id: number
    username: string
    email: string
    roles: string[]
    token: string
}

class AuthService {
    async register(username: string, email: string, password: string): Promise<string> {
        try {
            const response = await axios.post<string>(`${API_URL}/auth/register`, {
                username,
                email,
                password
            })
            return response.data
        } catch (error: any) {
            if (error.response) {
                throw new Error(error.response.data || 'Registration failed')
            } else if (error.request) {
                throw new Error('No response from server. Is the backend running?')
            } else {
                throw new Error('Error during registration: ' + error.message)
            }
        }
    }

    async login(username: string, password: string, rememberMe: boolean): Promise<UserData> {
        try {
            const response = await axios.post<LoginResponse>(`${API_URL}/auth/login`, {
                username,
                password
            })

            if (response.data.accessToken) {
                const userData: UserData = {
                    id: response.data.id,
                    username: response.data.username,
                    email: response.data.email,
                    roles: response.data.roles,
                    token: response.data.accessToken
                }
                // Store user data in localStorage
                if (rememberMe) {
                    localStorage.setItem('user', JSON.stringify(userData))
                } else {
                    sessionStorage.setItem('user', JSON.stringify(userData))
                }
                return userData
            }
            throw new Error('No token received')
        } catch (error: any) {
            if (error.response) {
                throw new Error(error.response.data || 'Login failed')
            } else if (error.request) {
                throw new Error('No response from server. Is the backend running?')
            } else {
                throw new Error('Error during login: ' + error.message)
            }
        }
    }

    logout(): void {
        localStorage.removeItem('user')
        sessionStorage.removeItem('user')
    }

    getCurrentUser(): UserData | null {
        const userStr = localStorage.getItem('user') || sessionStorage.getItem('user')
        if (userStr) {
            return JSON.parse(userStr)
        }
        return null
    }

    isLoggedIn(): boolean {
        return this.getCurrentUser() !== null
    }

    getToken(): string | null {
        const user = this.getCurrentUser()
        return user ? user.token : null
    }

}


export default new AuthService()