package com.utmach.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.utmach.entity.Producto;

@Repository
//public interface ProductoRepository extends CrudRepository<Producto, Integer> {
public class ProductoRepository {
	
	private final EntityManager entityManager;
	 
    @Autowired
    public ProductoRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }
   
    // Listar productos
	public List<Producto> listarProcedure() {
		TypedQuery<Producto> q = entityManager.createNamedQuery("LISTAR_PRODUCTOS", Producto.class);
		//q.setParameter(0, 1);
		List<Producto> lista = q.getResultList();
		System.out.print("Valores de la lista: " + lista);
		for(Producto p : lista) {
			System.out.println(p.getNombre());
		}
		return lista;
	}
	
	// Recibe la lista de los productos agregados en el carrito de compras
	// Devuelve una lista con información de los productos buscados
	/*public List<Producto> getProductos4Id_r(Integer id) {
		List<Producto> lista_productos = new ArrayList<>();
		for(Integer index : indices) {
			lista_productos.add(getProducto(index));
		}
		return lista_productos;
	}*/
	
	public Producto getProducto_r(Integer id) {
		
		StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("ECOMMERCE.OBTENER_PRODUCTO");
		storedProcedureQuery.registerStoredProcedureParameter("ID", Integer.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("DESCRIPCION", String.class, ParameterMode.OUT);
        storedProcedureQuery.registerStoredProcedureParameter("FOTO", String.class, ParameterMode.OUT);
        storedProcedureQuery.registerStoredProcedureParameter("NOMBRE", String.class, ParameterMode.OUT);
        storedProcedureQuery.registerStoredProcedureParameter("PRECIO", Double.class, ParameterMode.OUT);
        storedProcedureQuery.registerStoredProcedureParameter("STOCK", Integer.class, ParameterMode.OUT);
		
		storedProcedureQuery.setParameter("ID", id);
		storedProcedureQuery.execute();
		String descripcion = (String) storedProcedureQuery.getOutputParameterValue("DESCRIPCION");
		String foto = (String) storedProcedureQuery.getOutputParameterValue("FOTO");
		String nombre = (String) storedProcedureQuery.getOutputParameterValue("NOMBRE");
		Double precio = (Double) storedProcedureQuery.getOutputParameterValue("PRECIO");
		Integer stock = (Integer) storedProcedureQuery.getOutputParameterValue("STOCK");
		
		Producto p = new Producto();
		p.setId(id);
		p.setDescripcion(descripcion);
		p.setFoto(foto);
		p.setNombres(nombre);
		p.setPrecio(precio);
		p.setStock(stock);
		return p;
		
		
		/*TypedQuery<Producto> q = entityManager.createNamedQuery("OBTENER_PRODUCTO", Producto.class);
		q.setParameter("?", 101);
		
		Producto prod = null;
		List<Producto> lista = q.getResultList();
		System.out.print("Valores de la lista: " + lista);
		for(Producto p : lista) {
			prod = p;
			System.out.println(p.getNombre());
		}*/
	}
	
	// Obtener ruta de foto de un producto
		/*public String foto_r(Integer id) {
			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("ECOMMERCE.FOTO_PRODUCTO");
			storedProcedureQuery.registerStoredProcedureParameter("ID", Integer.class, ParameterMode.IN);
			storedProcedureQuery.registerStoredProcedureParameter("FOTO", String.class, ParameterMode.OUT);
			
			storedProcedureQuery.setParameter("ID", id);
			storedProcedureQuery.execute();
			String foto = (String) storedProcedureQuery.getOutputParameterValue("FOTO");
			return foto;
		}*/
	
	
	// Guardar un producto
	public void guardar_r(Producto p) {
		StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("ecommerce.GUARDAR_PRODUCTO");
        
        storedProcedureQuery.registerStoredProcedureParameter("DESCRIPCION", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("FOTO", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("NOMBRE", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("PRECIO", Double.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("STOCK", Integer.class, ParameterMode.IN);
        //storedProcedureQuery.registerStoredProcedureParameter("OUTPUT_PROCEDURE_PARAMETER_NAME2", Long.class, ParameterMode.OUT);
 
        storedProcedureQuery.setParameter("DESCRIPCION", p.getDescripcion());
        storedProcedureQuery.setParameter("FOTO", p.getFoto());
        storedProcedureQuery.setParameter("NOMBRE", p.getNombre());
        storedProcedureQuery.setParameter("PRECIO", p.getPrecio());
        storedProcedureQuery.setParameter("STOCK", p.getStock());
 
        storedProcedureQuery.execute();
	}
	
	
	
	public void actualizar_r(Producto p) {
		
		StoredProcedureQuery storedProcedureQuery;
		
		if(p.getFoto() != null) {
			storedProcedureQuery = entityManager.createStoredProcedureQuery("ecommerce.ACTUALIZAR_PRODUCTO_CON_FOTO");
			
			storedProcedureQuery.registerStoredProcedureParameter("ID", Integer.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("DESCRIPCION", String.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("FOTO", String.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("NOMBRE", String.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("PRECIO", Double.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("STOCK", Integer.class, ParameterMode.IN);
	        //storedProcedureQuery.registerStoredProcedureParameter("OUTPUT_PROCEDURE_PARAMETER_NAME2", Long.class, ParameterMode.OUT);
	 
	        storedProcedureQuery.setParameter("ID", p.getId());
	        storedProcedureQuery.setParameter("DESCRIPCION", p.getDescripcion());
	        storedProcedureQuery.setParameter("FOTO", p.getFoto());
	        storedProcedureQuery.setParameter("NOMBRE", p.getNombre());
	        storedProcedureQuery.setParameter("PRECIO", p.getPrecio());
	        storedProcedureQuery.setParameter("STOCK", p.getStock());
		}else {
			storedProcedureQuery = entityManager.createStoredProcedureQuery("ecommerce.ACTUALIZAR_PRODUCTO_SIN_FOTO");
	        
			storedProcedureQuery.registerStoredProcedureParameter("ID", Integer.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("DESCRIPCION", String.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("NOMBRE", String.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("PRECIO", Double.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("STOCK", Integer.class, ParameterMode.IN);
	 
	        storedProcedureQuery.setParameter("ID", p.getId());
	        storedProcedureQuery.setParameter("DESCRIPCION", p.getDescripcion());
	        storedProcedureQuery.setParameter("NOMBRE", p.getNombre());
	        storedProcedureQuery.setParameter("PRECIO", p.getPrecio());
	        storedProcedureQuery.setParameter("STOCK", p.getStock());
		}
 
        storedProcedureQuery.execute();
	}

	
	
	// Obtener nombre de foto
	public String getFoto_r(Integer id) {
		StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("ECOMMERCE.FOTO_PRODUCTO");
		storedProcedureQuery.registerStoredProcedureParameter("ID", Integer.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("FOTO", String.class, ParameterMode.OUT);
		
		storedProcedureQuery.setParameter("ID", id);
		storedProcedureQuery.execute();
		String foto = (String) storedProcedureQuery.getOutputParameterValue("FOTO");
		return foto;
	}

	
	
	// Eliminar un producto
	public String eliminar_r(Integer id) {
		StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("ECOMMERCE.ELIMINAR_PRODUCTO");
		storedProcedureQuery.registerStoredProcedureParameter("ID", Integer.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("FOTO", String.class, ParameterMode.OUT);
		
		storedProcedureQuery.setParameter("ID", id);
		storedProcedureQuery.execute();
		String foto = (String) storedProcedureQuery.getOutputParameterValue("FOTO");
		return foto;
	}
}
