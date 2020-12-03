-----------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------- VALIDACIONES ------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-- FUNCION VALIDACION SOLO NUMEROS
CREATE OR REPLACE FUNCTION FUNC_SOLO_NUMEROS (p_cadena IN VARCHAR2)
   RETURN NUMBER 
IS
   v_numero NUMBER;
BEGIN
 IF p_cadena IS NOT NULL THEN
   v_numero := TO_NUMBER( REPLACE(p_cadena, ',', '') );
   RETURN v_numero;
 ELSE
  RETURN 0;
 END IF;
EXCEPTION
WHEN VALUE_ERROR THEN
   RETURN 0;
END FUNC_SOLO_NUMEROS;


-- FUNCION VALIDACION DE CEDULA
CREATE OR REPLACE FUNCTION FUNC_VALIDAR_CEDULA(CEDULA IN VARCHAR2)
RETURN BOOLEAN AS BANDERA BOOLEAN:=FALSE;
DIGITO_REGION VARCHAR2(2);
ULTIMO_DIGITO VARCHAR(2);
SUMA_PARES NUMBER:=0;
IMPARES NUMBER:=0;
SUMA_IMPARES NUMBER:=0;
SUMA NUMBER:=0;
RESTA_DECENA NUMBER:=0;
COMPROBACION NUMBER:=0;
ULTIMO_NUMERO NUMBER:=0;
BEGIN
  IF (FUNC_SOLO_NUMEROS(CEDULA))<>0 THEN
    IF(LENGTH(CEDULA)=10) THEN
      --OBTENEMOS LOS DOS PRIMEROS DIGITOS QUE PERTENECEN A LA REGION
      DIGITO_REGION:=SUBSTR(CEDULA,0,2);
      --PREGUNTAR SI LA PROVICNIA DE LA CEDULA EXISTE (24 PROVINCIAS)
      IF (DIGITO_REGION >= 1 AND DIGITO_REGION <=24) THEN
      --OBTENEMOS EL ULTIMO DIGITO
      ULTIMO_DIGITO:=SUBSTR(CEDULA,10,1);
      FOR P IN 1..9 LOOP
      --AGRUPAMOS TODOS LOS PARES Y LOS SUMAMOS
      IF MOD(P,2)=0 THEN
        SUMA_PARES:=SUMA_PARES+TO_NUMBER(SUBSTR(CEDULA, P, 1));
        --MULTIPLICAMOS * 2 LOS IMPARES
        ELSE
        IMPARES:=0;
        IMPARES:=TO_NUMBER(SUBSTR(CEDULA,P,1)*2);
        --LE RESTAMOS 9 SI SON MAYORES A 9
        IF IMPARES > 9 THEN
          IMPARES:=IMPARES-9;
          END IF;
          --SUMAMOS LOS IMPARES
          SUMA_IMPARES:=SUMA_IMPARES+IMPARES;
        END IF;
      END LOOP;
      --SUMA DE PARES E IMPARES
      SUMA:=SUMA_PARES+SUMA_IMPARES;
      --RESTAR LA SUMA DE LA DECENA SUPERIOR
      RESTA_DECENA:=(TO_NUMBER(SUBSTR(SUMA,0,1)+1)*10)- SUMA;
      IF (RESTA_DECENA=ULTIMO_DIGITO)THEN
        BANDERA:=TRUE;
      ELSE
        BANDERA:=FALSE;
      END IF;
    END IF;
  END IF;
  END IF;
RETURN BANDERA;
END FUNC_VALIDAR_CEDULA;



-----------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------- AUTENTICACION -----------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-- PROCEDIMIENTO PARA REGISTRAR NUEVO USUARIO
CREATE OR REPLACE PROCEDURE registrar_usuario(
dni usuario.us_dni%type, nombres usuario.us_nombres%type, apellidos usuario.us_apellidos%type,
email usuario.us_email%type, pass usuario.us_password%type, rol usuario.us_rol%type
)
AS BEGIN
    if(FUNC_VALIDAR_CEDULA(dni)=false) then   
        RAISE_APPLICATION_ERROR(-20001, 'C�dula invalida');
    else  
        INSERT INTO usuario (us_dni, us_nombres, us_apellidos, us_email, us_password, us_rol) 
        VALUES(dni, nombres, apellidos, email, pass, rol);
    end if;
END;


-- FUNCION PARA LOGIN
CREATE OR REPLACE FUNCTION login(username usuario.us_email%type)
RETURN SYS_REFCURSOR
AS 
usuario_cr SYS_REFCURSOR;
BEGIN
    OPEN usuario_cr FOR  select us_password, us_id, us_rol from usuario where us_dni = username or us_email = username;
    return usuario_cr;
END;

-- FUNCION PARA OBTENER DATOS DE UN USUARIO
CREATE OR REPLACE FUNCTION obtener_datos_usuario(id_usuario usuario.us_id%type)
RETURN SYS_REFCURSOR
AS 
usuario_cr SYS_REFCURSOR;
BEGIN
    OPEN usuario_cr FOR  select us_dni, us_nombres, us_apellidos, us_email, us_password, us_rol
        from usuario where us_id = id_usuario;
    return usuario_cr;
END;

-- PROCEDIMIENTO PARA ACUALIZAR USUARIO
CREATE OR REPLACE PROCEDURE actualizar_usuario(
id_usuario usuario.us_id%type, nombres usuario.us_nombres%type, 
apellidos usuario.us_apellidos%type, email usuario.us_email%type, pass usuario.us_password%type
)
AS BEGIN
    UPDATE usuario
    SET us_nombres = nombres, us_apellidos = apellidos, us_email = email, us_password = pass
    WHERE us_id = id_usuario;
END;

-----------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------- PRODUCTOS ---------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-- FUNCION PARA OBTENER PRODUCTOS
CREATE OR REPLACE FUNCTION LISTAR_PRODUCTOS RETURN SYS_REFCURSOR AS
  c_productos SYS_REFCURSOR;
BEGIN
  OPEN c_productos FOR SELECT PROD_ID, PROD_DESCRIPCION, PROD_FOTO, PROD_NOMBRE, PROD_PRECIO, PROD_STOCK FROM producto;
  return c_productos;
END;
/

-- PROCEDIMIENTO PARA GUARDAR PRODUCTOS
CREATE OR REPLACE PROCEDURE GUARDAR_PRODUCTO(DESCRIPCION IN PRODUCTO.PROD_DESCRIPCION%TYPE, FOTO IN PRODUCTO.PROD_FOTO%TYPE, NOMBRE IN PRODUCTO.PROD_NOMBRE%TYPE, PRECIO IN PRODUCTO.PROD_PRECIO%TYPE, STOCK IN PRODUCTO.PROD_STOCK%TYPE) AS
BEGIN
  INSERT INTO PRODUCTO (prod_descripcion, prod_foto, prod_nombre, prod_precio, prod_stock) VALUES(DESCRIPCION, FOTO, NOMBRE, PRECIO, STOCK);
END GUARDAR_PRODUCTO;
/

-- PROCEDIMIENTO PARA ELIMINAR PRODUCTO
CREATE OR REPLACE PROCEDURE ELIMINAR_PRODUCTO(ID IN PRODUCTO.PROD_ID%TYPE, FOTO OUT PRODUCTO.PROD_FOTO%TYPE) AS
BEGIN
  IF ID > -1 THEN
    SELECT PROD_FOTO INTO FOTO FROM PRODUCTO WHERE PROD_ID = ID; 
    DELETE FROM PRODUCTO WHERE PROD_ID = ID;
  ELSE
    RAISE_APPLICATION_ERROR(-20256, 'El prod_id no debe ser cero ni negativo');
  END IF;
END ELIMINAR_PRODUCTO;
/

-- PROCEDIMIENTO PARA FOTO PRODUCTO
CREATE OR REPLACE PROCEDURE FOTO_PRODUCTO(ID IN PRODUCTO.PROD_ID%TYPE, FOTO OUT PRODUCTO.PROD_FOTO%TYPE) AS
BEGIN
  IF ID > -1 THEN
    SELECT PROD_FOTO INTO FOTO FROM PRODUCTO WHERE PROD_ID = ID; 
  ELSE
    RAISE_APPLICATION_ERROR(-20256, 'El prod_id no debe ser cero ni negativo');
  END IF;
END FOTO_PRODUCTO;
/
-----------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------- DIRECCION ---------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-- PROCEDIMIENTO PARA REGISTRAR DIRECCION
CREATE OR REPLACE PROCEDURE registrar_direccion(
dir direccion.dir_direccion%type, referencia direccion.dir_referencia%type, 
telefono direccion.dir_telefono%type, usu_id direccion.dir_usu_id%type
)
AS BEGIN
    INSERT INTO direccion (dir_direccion, dir_referencia, dir_telefono, dir_usu_id) 
    VALUES(dir, referencia, telefono, usu_id);
END;

-- PROCEDIMIENTO PARA ACUALIZAR DIRECCION
CREATE OR REPLACE PROCEDURE actualizar_direccion(
id_dir direccion.dir_id%type, dir direccion.dir_direccion%type, 
referencia direccion.dir_referencia%type, telefono direccion.dir_telefono%type
)
AS BEGIN
    UPDATE direccion
    SET dir_direccion = dir, dir_referencia = referencia, dir_telefono = telefono
    WHERE dir_id = id_dir;
END;

-- PROCEDIMIENTO PARA ELIMINAR DIRECCION
CREATE OR REPLACE PROCEDURE eliminar_direccion(id_dir direccion.dir_id%type)
AS BEGIN
    DELETE FROM direccion WHERE dir_id = id_dir;
END;

-- FUNCION PARA OBTENER TODAS LAS DIRECCIONES
CREATE OR REPLACE FUNCTION obtener_direcciones(usuario_id direccion.dir_usu_id%type)
RETURN SYS_REFCURSOR
AS 
direccion_cr SYS_REFCURSOR;
BEGIN
    OPEN direccion_cr FOR  select * 
        from direccion where dir_usu_id = usuario_id;
    return direccion_cr;
END;

-----------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------- PAGOS -------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------
-- PROCEDIMIENTO PARA REGISTRAR PAGO
CREATE OR REPLACE PROCEDURE registrar_pago(
numero pago.pago_numero_tarj%type, nombre pago.pago_nombre_tarj%type, mes pago.pago_mes_venc%type,
anio pago.pago_anio_venc%type, usuario_id pago.pago_usu_id%type
)
AS
    mes_actual pago.pago_mes_venc%type;
    anio_actual pago.pago_anio_venc%type;
BEGIN
    select to_char(sysdate,'mm'), to_char(sysdate,'yyyy') into mes_actual, anio_actual from dual;
    if (anio <= anio_actual) then    
        if (mes <= mes_actual) then    
            RAISE_APPLICATION_ERROR(-20002, 'Fecha caducada');
        else 
            INSERT INTO pago (pago_numero_tarj, pago_nombre_tarj, pago_mes_venc, pago_anio_venc, pago_usu_id) 
                VALUES(numero, nombre, mes, anio, usuario_id);
        end if;
    else 
            INSERT INTO pago (pago_numero_tarj, pago_nombre_tarj, pago_mes_venc, pago_anio_venc, pago_usu_id) 
                VALUES(numero, nombre, mes, anio, usuario_id);
    end if;
END;

-- EXECUTE registrar_pago('2123', 'Alvaro', 6, 2021, 2);

-- PROCEDIMIENTO PARA ACUALIZAR PAGO
CREATE OR REPLACE PROCEDURE actualizar_pago(
id_pago pago.pago_id%type, numero pago.pago_numero_tarj%type, nombre pago.pago_nombre_tarj%type, mes pago.pago_mes_venc%type,
anio pago.pago_anio_venc%type
)
AS BEGIN
    UPDATE pago
    SET pago_numero_tarj = numero, pago_nombre_tarj = nombre, pago_mes_venc = mes, pago_anio_venc = anio
    WHERE pago_id = id_pago;
END;

-- PROCEDIMIENTO PARA ELIMINAR PAGO
CREATE OR REPLACE PROCEDURE eliminar_pago(id_pago pago.pago_id%type)
AS BEGIN
    DELETE FROM pago WHERE pago_id = id_pago;
END;

-- FUNCION PARA OBTENER TODOS LOS PAGOS
CREATE OR REPLACE FUNCTION obtener_pagos(usuario_id pago.pago_usu_id%type)
RETURN SYS_REFCURSOR
AS 
pago_cr SYS_REFCURSOR;
BEGIN
    OPEN pago_cr FOR  select * 
        from pago where pago_usu_id = usuario_id;
    return pago_cr;
END;

