import axios from 'axios'

const API_URL = import.meta.env.VITE_APP_API_URL || 'http://localhost:8080/api'

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
}

export default new AuthService()