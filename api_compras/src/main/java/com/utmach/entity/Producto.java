package com.utmach.entity;

//import java.sql.Blob;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQuery;

/*@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
        name = "GUARDAR_PRODUCTO",
        procedureName = "BDCARRITOCOMPRAS.GUARDAR_PRODUCTO",
        parameters = {
          @StoredProcedureParameter(mode=ParameterMode.REF_CURSOR, name="c_productos", type=String.class),
          @StoredProcedureParameter(mode=ParameterMode.IN, name="inputParam1", type=Integer.class)
    })
})*/

@NamedNativeQuery (
        name = "LISTAR_PRODUCTOS", 
        callable = true, 
        query = "{? = call LISTAR_PRODUCTOS}",
        resultClass = Producto.class)

@NamedNativeQuery (
        name = "OBTENER_PRODUCTO", 
        callable = true, 
        query = "{? = call OBTENER_PRODUCTO(?)}",
        resultClass = Producto.class)

@Entity
@Table(name="PRODUCTO")
public class Producto{
	
	@Id
	@Column(name="prod_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="prod_nombre")
    private String nombre;
	
	@Column(name="prod_foto")
	private String foto;
	
	@Column(name="prod_descripcion")
	private String descripcion;
	
	@Column(name="prod_precio")
	private Double precio;
	
	@Column(name="prod_stock")
	private Integer stock;
	
	
	//Getters and setters
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombres(String nombre) {
		this.nombre = nombre;
	}
	
	
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
}
