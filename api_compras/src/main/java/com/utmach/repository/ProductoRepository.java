package com.utmach.repository;

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
   
	
    // Listar los productos
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
	
	
	// Guardar un producto
	public void guardar_r(Producto p) {
		StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("BDCARRITOCOMPRAS.GUARDAR_PRODUCTO");
        
        // Registrar los parámetros de entrada y salida
        storedProcedureQuery.registerStoredProcedureParameter("DESCRIPCION", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("FOTO", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("NOMBRE", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("PRECIO", Double.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("STOCK", Integer.class, ParameterMode.IN);
        //storedProcedureQuery.registerStoredProcedureParameter("OUTPUT_PROCEDURE_PARAMETER_NAME2", Long.class, ParameterMode.OUT);
 
        // Configuramos el valor de entrada
        storedProcedureQuery.setParameter("DESCRIPCION", p.getDescripcion());
        storedProcedureQuery.setParameter("FOTO", p.getFoto());
        storedProcedureQuery.setParameter("NOMBRE", p.getNombre());
        storedProcedureQuery.setParameter("PRECIO", p.getPrecio());
        storedProcedureQuery.setParameter("STOCK", p.getStock());
 
        // Realizamos la llamada al procedimiento
        storedProcedureQuery.execute();
		
	}
}
