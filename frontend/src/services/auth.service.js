import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_APP_API_URL || 'http://localhost:8080/api';


class AuthService {
    async register(username, email, password) {
        try {
            const response = await axios.post(API_BASE_URL + '/auth/register', {
                username,
                email,
                password,
            });
            return response.data;
        } catch (err) {
            if (err.response) {
                throw new Error(err.response.data || 'registeration failed');
            } else if (err.request) {
                throw new Error('no response from server, please check connection');
            } else {
                throw new Error('Failed to register user' + err.message);
            }
        }
    }

    //will add login,logout etc later on.
}

export default new AuthService;