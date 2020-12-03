export default class User {
    constructor(dni, nombres, apellidos, email, contrase_a, rol) {
        this.us_dni = dni;
        this.us_nombres = nombres;
        this.us_apellidos = apellidos;
        this.us_email = email;
        this.us_password = contrase_a;
        this.us_rol = rol;
    }
}