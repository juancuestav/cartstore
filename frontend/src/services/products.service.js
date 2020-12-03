import axios from 'axios';

const API_URL = 'http://localhost:3000';

class ProductService {
    add_product(formData) {
        return axios.post(`${API_URL}/product`, formData).then(function () {
            console.log('SUCCESS!!');
        }).catch(function () {
            console.log('FAILURE!!');
        });
    }
}