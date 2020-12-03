import axios from 'axios';

const API_URL = 'http://localhost:5000';

class AuthService {
    login(user) {
        return axios
            .post(API_URL + '/login/', {
                us_dni: user.us_dni,
                us_password: user.us_password
            })
            .then(response => {
                if (response.data.token) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    logout() {
        localStorage.removeItem('user');
    }

    register(user) {
        return axios.post(API_URL + '/register/', {
            us_dni: user.us_dni,
            us_nombres: user.us_nombres,
            us_apellidos: user.us_apellidos,
            us_email: user.us_email,
            us_password: user.us_password,
            us_rol : user.us_rol
        });
    }

    getUser(id) {
        return axios.get(`${API_URL}/user/${id}`).then(response => {
            return response.data;
        })
    }

    updateUser(user){
        return axios.post(`${API_URL}/user/`, {
            us_id: user.us_id,
            us_nombres: user.us_nombres,
            us_apellidos: user.us_apellidos,
            us_email: user.us_email,
            us_password: user.us_password
        });
    }
}

export default new AuthService();