from flask import Flask, request, jsonify, json
from flask_cors import CORS
import jwt #pip install pyjwt
import cx_Oracle
from cryptography.fernet import Fernet

app = Flask(__name__)
CORS(app)

dsn_tns = cx_Oracle.makedsn('192.168.1.10', 1521, service_name='orcl')
conn = cx_Oracle.connect(user=r'ecommerce', password='12345', dsn=dsn_tns)

cursor_oracle = conn.cursor()

# Change this to your secret key (can be anything, it's for extra protection)
app.secret_key = 'opt3-proy-utmach2020'

# Ruta de login
@app.route('/login/', methods=['GET', 'POST'])
def login():
    try:
        if request.method == 'POST' and 'us_dni' in request.get_json() and 'us_password' in request.get_json():
            # Almacena los campos en un diccionario
            data = request.get_json()
            # Obtiene la constraseña del usuario ingresado
            user_cr = cursor_oracle.callfunc('login', cx_Oracle.CURSOR, [
                data['us_dni'].upper()])
            # Convertir a json
            user_data = {}
            for column in user_cr.description:
                user_data[column[0].lower()] = ''
            for row in user_cr:
                user_data['us_password'] = row[0]
                user_data['us_id'] = row[1]
                user_data['us_rol'] = row[2]
            if user_data['us_password'] == '':
                return jsonify({'error': 'Usuario incorrecto'})
            else:
                # Encripta la contraseña y compara las encriptaciones
                key = load_key()
                f = Fernet(key)
                decoded_pass_db = f.decrypt(user_data['us_password'].encode()).decode()
                if (decoded_pass_db == data['us_password']):
                    auth_token = encode_auth_token(
                        data['us_dni'].upper()).decode("utf-8")
                    return jsonify({"token": auth_token, "id": user_data['us_id']})
                else:
                    return jsonify({'error': 'Contraseña incorrecta'})
    except cx_Oracle.DatabaseError as dbe:
        print(dbe)
        return jsonify({'error': 'Usuario incorrecto'})

# Ruta de registrar (us_dni, us_nombres, us_apellidos, us_direccion, us_email, us_password, ur_rol)
@app.route('/register/', methods=['GET', 'POST'])
def register():
    try:
        if (request.method == 'POST' and 'us_dni' in request.get_json() and 'us_nombres' in request.get_json()
            and 'us_apellidos' in request.get_json() and 'us_email' in request.get_json() and 'us_password' in request.get_json()
                and 'us_rol' in request.get_json()):
            # Almacena los campos en un diccionario
            data = request.get_json()
            # Encripta la contraseña
            key = load_key()
            f = Fernet(key)
            encoded_pass = data['us_password'].encode()
            encrypted_pass = f.encrypt(encoded_pass)
            decoded_pass = encrypted_pass.decode()
            # Convierte los campos a mayusculas
            data['us_nombres'] = data['us_nombres'].upper()
            data['us_apellidos'] = data['us_apellidos'].upper()
            data['us_email'] = data['us_email'].upper()
            data['us_rol'] = data['us_rol'].upper()

            cursor_oracle.callproc('registrar_usuario',
                                   [data['us_dni'], data['us_nombres'], data['us_apellidos'],
                                    data['us_email'], decoded_pass,
                                    data['us_rol']])
            conn.commit()

            return jsonify({'exito': 'Usuario registrado'})
    except cx_Oracle.IntegrityError as ie:
        print(ie)
        return jsonify({'error': 'Ya existe un usuario con ese DNI o correo'})
    except cx_Oracle.DatabaseError as dberror:        
        arg_error = dberror.args[0]
        if arg_error.code == 20001:
                return jsonify({'error': 'Cédula inválida'})
    except Exception as error:
        print(error)
        return jsonify({'error': 'Error al registrar usuario'})

# Metodo para obtener token
def encode_auth_token(user_id):
    try:
        payload = {
            'sub': user_id
        }
        return jwt.encode(payload, app.secret_key, algorithm='HS256')
    except Exception as e:
        print(e)
        return e

# Ruta para obtener datos de usuario
@app.route('/user/<id>/', methods=['GET', 'POST'])
def getUser(id):
    try:
        if request.method == 'GET':
            # Obtiene los datos del usuario
            user_cr = cursor_oracle.callfunc('obtener_datos_usuario', cx_Oracle.CURSOR, [id])
            # Convertir a json
            user_data = {}
            for column in user_cr.description:
                user_data[column[0].lower()] = ''
            for row in user_cr:
                user_data['us_dni'] = row[0]
                user_data['us_nombres'] = row[1]
                user_data['us_apellidos'] = row[2]
                user_data['us_email'] = row[3]
                user_data['us_password'] = row[4]
                user_data['us_rol'] = row[5]
            if user_data['us_dni'] == '':
                return jsonify({'error': 'Usuario no registrado en el sistema'})
            else:
                key = load_key()
                f = Fernet(key)
                user_data['us_password'] = f.decrypt(user_data['us_password'].encode()).decode()
                return jsonify(user_data)
    except Exception as ex:
        print(ex)
        return jsonify({'error': 'Error al buscar usuario'})

# Ruta de registrar (us_dni, us_nombres, us_apellidos, us_direccion, us_email, us_password, ur_rol)
@app.route('/user/', methods=['GET', 'POST'])
def update_user():
    try:
        if (request.method == 'POST' and 'us_id' in request.get_json() and 'us_nombres' in request.get_json() 
        and 'us_apellidos' in request.get_json() and 'us_email' in request.get_json() and 'us_password' in request.get_json()):
            # Almacena los campos en un diccionario
            data = request.get_json()

            # Encripta la contraseña
            key = load_key()
            f = Fernet(key)
            encoded_pass = data['us_password'].encode()
            encrypted_pass = f.encrypt(encoded_pass)
            decoded_pass = encrypted_pass.decode()

            # Convierte los campos a mayusculas
            data['us_nombres'] = data['us_nombres'].upper()
            data['us_apellidos'] = data['us_apellidos'].upper()
            data['us_email'] = data['us_email'].upper()

            cursor_oracle.callproc('actualizar_usuario',
                                   [data['us_id'], data['us_nombres'], data['us_apellidos'],
                                    data['us_email'], decoded_pass])
            conn.commit()
            return jsonify({'exito': 'Usuario actualizado'})
    except cx_Oracle.IntegrityError as ie:
        print(ie)
        return jsonify({'error': 'Ya existe un usuario con ese correo'})
    except Exception as error:
        print(error)
        return jsonify({'error': 'Error al registrar usuario'})

'''@staticmethod
def decode_auth_token(auth_token):
    try:
        payload = jwt.decode(auth_token, app.secret_key)
        return payload['sub']
    except jwt.InvalidTokenError:
        return 'Token invalido'
'''

def load_key():
    file = open('key.key', 'rb')  # Open the file as wb to read bytes
    key = file.read()  # The key will be type bytes
    file.close()
    return key

if __name__ == "__main__":
    app.run(debug=True)
