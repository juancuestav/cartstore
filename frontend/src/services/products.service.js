import axios from 'axios';

const API_URL = 'http://localhost:8080';

class ProductService {
    add_product(formData) {
        return axios.post(`${API_URL}/productos/guardar`, formData);
    }

    get_products() {
        return axios.get(`${API_URL}/productos/listar`);
    }

    delete_product(prod_id) {
        return axios.delete(`${API_URL}/productos/eliminar/${prod_id}`);
    }

    update_product(prod_id, formData) {
        return axios.put(`${API_URL}/productos/actualizar/${prod_id}`, formData);
    }

    registrar_compra(formData) {
        return axios.post(`${API_URL}/productos/comprar`, formData);
    }

    registrar_detalle_compra(formData) {
        return axios.post(`${API_URL}/productos/detallecompra`, formData);
    }
}

export default new ProductService();