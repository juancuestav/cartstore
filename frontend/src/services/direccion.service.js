import axios from 'axios';

const API_URL = 'http://localhost:3000';

class DireccionService {
    add_direccion(direccion) {
        return axios.post(`${API_URL}/direccion/`, {
            dir_direccion: direccion.dir_direccion,
            dir_referencia: direccion.dir_referencia,
            dir_telefono: direccion.dir_telefono,
            dir_usu_id: direccion.dir_usu_id
        });
    }

    get_direcciones(user_id) {
        return axios.get(`${API_URL}/direccion/${user_id}`);
    }

    delete_direccion(dir_id) {
        return axios.delete(`${API_URL}/direccion/${dir_id}`);
    }

    update_direccion(direccion) {
        return axios.put(`${API_URL}/direccion/${direccion.dir_id}`, {
            dir_direccion: direccion.dir_direccion,
            dir_referencia: direccion.dir_referencia,
            dir_telefono: direccion.dir_telefono
        });
    }
}

export default new DireccionService();