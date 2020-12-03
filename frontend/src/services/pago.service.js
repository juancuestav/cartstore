import axios from 'axios';

const API_URL = 'http://localhost:3000';

class PagoService {
    add_pago(pago) {
        return axios.post(`${API_URL}/pago/`, {
            pago_numero_tarj: pago.pago_numero_tarj,
            pago_nombre_tarj: pago.pago_nombre_tarj,
            pago_mes_venc: pago.pago_mes_venc,
            pago_anio_venc: pago.pago_anio_venc,
            pago_usu_id: pago.pago_usu_id
        });
    }

    get_pago(user_id) {
        return axios.get(`${API_URL}/pago/${user_id}`);
    }

    delete_pago(user_id) {
        return axios.delete(`${API_URL}/pago/${user_id}`);
    }

    update_pago(pago) {
        return axios.put(`${API_URL}/pago/${pago.pago_usu_id}`, {
            pago_numero_tarj: pago.pago_numero_tarj,
            pago_nombre_tarj: pago.pago_nombre_tarj,
            pago_mes_venc: pago.pago_mes_venc,
            pago_anio_venc: pago.pago_anio_venc,
        });
    }
}

export default new PagoService();