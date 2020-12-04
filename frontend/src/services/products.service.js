import axios from 'axios';

const API_URL = 'http://localhost:8080';

class ProductService {
    add_product(formData) {
        return axios.post(`${API_URL}/productos/guardar`, formData);
    }

    get_products() {
        return axios.get(`${API_URL}/productos/listar`);
    }

    delete_products(prod_id) {
        return axios.delete(`${API_URL}/productos/eliminar/${prod_id}`);
    }
}

export default new ProductService();