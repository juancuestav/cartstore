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
	private static Integer id_compra;
	 
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
		
		
	}

	
	
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
	
	
	
	// Tabla Compra
	public void comprar_r(Integer usuario_id, Double subtotal, Double iva, Double total) {
		StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("ecommerce.COMPRAR_PRODUCTOS");
        
        storedProcedureQuery.registerStoredProcedureParameter("USUARIO_ID", Integer.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("SUBTOTAL", Double.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("IVA", Double.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("TOTAL", Double.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("ID_COMPRA", Integer.class, ParameterMode.OUT);
 
        storedProcedureQuery.setParameter("USUARIO_ID", usuario_id);
        storedProcedureQuery.setParameter("SUBTOTAL", subtotal);
        storedProcedureQuery.setParameter("IVA", iva);
        storedProcedureQuery.setParameter("TOTAL", total);
        
        storedProcedureQuery.execute();
        
        id_compra = (Integer) storedProcedureQuery.getOutputParameterValue("ID_COMPRA");
        System.out.println("Valor de id compra(1): " + id_compra);

	}
	
	
	// Tabla Detalle Compra
		public void detalle_compra_r(Integer prod_id, Integer cantidad) {
			System.out.println("Valor de id compra: " + id_compra);
			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("ecommerce.AGREGAR_DETALLE_COMPRA");
	        
	        storedProcedureQuery.registerStoredProcedureParameter("PRODUCT_ID", Integer.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("COMP_ID", Integer.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("CANTIDAD", Integer.class, ParameterMode.IN);
	 
	        storedProcedureQuery.setParameter("PRODUCT_ID", prod_id);
	        storedProcedureQuery.setParameter("COMP_ID", id_compra);
	        storedProcedureQuery.setParameter("CANTIDAD", cantidad);
	        
	        storedProcedureQuery.execute();
	        
	        minimizar_stock_r(prod_id, cantidad);
		}
		
		
		// Minimizar cantidad en stock
		private void minimizar_stock_r(Integer prod_id, Integer cantidad) {
			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("ecommerce.MINIMIZAR_STOCK_PRODUCTO");
	        
	        storedProcedureQuery.registerStoredProcedureParameter("ID", Integer.class, ParameterMode.IN);
	        storedProcedureQuery.registerStoredProcedureParameter("CANTIDAD", Integer.class, ParameterMode.IN);
	 
	        storedProcedureQuery.setParameter("ID", prod_id);
	        storedProcedureQuery.setParameter("CANTIDAD", cantidad);
	        
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
