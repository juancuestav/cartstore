-- Funcion que retorna un cursor con todos los productos registrados
CREATE OR REPLACE FUNCTION LISTAR_PRODUCTOS RETURN SYS_REFCURSOR AS
  c_productos SYS_REFCURSOR;
BEGIN
  OPEN c_productos FOR SELECT PROD_ID, PROD_DESCRIPCION, PROD_FOTO, PROD_NOMBRE, PROD_PRECIO, PROD_STOCK FROM producto;
  return c_productos;
END;


-- Procedimiento almacenado que guarda un registro de producto
CREATE OR REPLACE PROCEDURE GUARDAR_PRODUCTO(DESCRIPCION IN PRODUCTO.PROD_DESCRIPCION%TYPE, FOTO IN PRODUCTO.PROD_FOTO%TYPE, NOMBRE IN PRODUCTO.PROD_NOMBRE%TYPE, PRECIO IN PRODUCTO.PROD_PRECIO%TYPE, STOCK IN PRODUCTO.PROD_STOCK%TYPE) AS
BEGIN
  INSERT INTO PRODUCTO (prod_descripcion, prod_foto, prod_nombre, prod_precio, prod_stock) VALUES(DESCRIPCION, FOTO, NOMBRE, PRECIO, STOCK);
END GUARDAR_PRODUCTO;



-- set serveroutput=on
declare
  c_productos SYS_REFCURSOR;
  prod_id NUMBER;
  prod_nombre VARCHAR(255);
  prod_foto VARCHAR(255); 
  prod_descripcion VARCHAR(255);
  prod_precio FLOAT;
  prod_stock NUMBER;
begin
  listar_productos2(c_productos);
  loop
    fetch c_productos into prod_id, prod_nombre, prod_foto, prod_descripcion, prod_precio, prod_stock;
    exit when c_productos%notfound;
    dbms_output.put_line(prod_nombre);
  end loop;
  close c_productos;
    
end;
/



-- INSERT
INSERT INTO PRODUCTO(PROD_DESCRIPCION, PROD_FOTO, PROD_NOMBRE, PROD_PRECIO, PROD_STOCK)VALUES ('Este es un teclado','Esta es la ruta','Teclado', 17, 5);
INSERT INTO PRODUCTO(PROD_DESCRIPCION, PROD_FOTO, PROD_NOMBRE, PROD_PRECIO, PROD_STOCK)VALUES ('Este es un parlante','Esta es la ruta','Parlante', 17, 5);


