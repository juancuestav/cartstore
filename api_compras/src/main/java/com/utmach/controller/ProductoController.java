package com.utmach.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.utmach.entity.Producto;
import com.utmach.service.ProductoService;

//Cross origin

@RestController
@RequestMapping("productos")
public class ProductoController {
	
	@Autowired
	ProductoService productoService;
	
	
	
	
	@CrossOrigin(origins = "http://localhost:8090")
	@PostMapping("/guardar")
	public ResponseEntity<?> guardar_c(@RequestParam("foto") MultipartFile foto, @RequestParam("descripcion") String descripcion, @RequestParam("nombre") String nombre, @RequestParam("precio") Double precio, @RequestParam("stock") Integer stock) {
		if(foto.isEmpty()) {
			return new ResponseEntity<Object>("Seleccionar un archivo", HttpStatus.OK);
		}
		try {
			productoService.guardar_s(foto, descripcion, nombre, precio, stock);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return new ResponseEntity<Object>("Archivo subido correctamente", HttpStatus.OK);
	}
	
	
	// ACTUALIZAR
	@CrossOrigin(origins = "http://localhost:8090")
	@PutMapping("/actualizar/{id}")
	//@RequestMapping(value = "/actualizar/{id}", method = RequestMethod.PUT, consumes = "multipart/form-data")
	public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestParam(name="foto" , required = false) MultipartFile foto, @RequestParam("descripcion") String descripcion, @RequestParam("nombre") String nombre, @RequestParam("precio") Double precio, @RequestParam("stock") Integer stock) {
		try {
			productoService.actualizarProducto_s(id, foto, descripcion, nombre, precio, stock);
			//productoService.guardar_s(foto, descripcion, nombre, precio, stock);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return new ResponseEntity<Object>("Actualizado correctamente!", HttpStatus.OK);
		//productoService.save(p);
	}
	
	
	
	@CrossOrigin(origins = "http://localhost:8090")
	@GetMapping("/listar")
	public ResponseEntity<List<Producto>> listar_c() {
		List<Producto> lista = productoService.listar_s();
		return new ResponseEntity(lista, HttpStatus.OK);
	}
	
	
	
	@CrossOrigin(origins = "http://localhost:8090")
	@GetMapping("/getproductscarrito/{ID}")
	public ResponseEntity<Producto> getProducts4Id_c(@PathVariable("ID") Integer id) {
		Producto lista = productoService.getProductos4Id_s(id);
		return new ResponseEntity(lista, HttpStatus.OK);
	}
	
	
	
	
	
	
	@CrossOrigin(origins = "http://localhost:8090")
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> eliminar(@PathVariable("id") Integer id) {
		System.out.println("Valor de id recibido: " + id);
		try {
			productoService.eliminar_s(id);
		}catch (Exception e) {
			//e.printStackTrace();
		}
		return new ResponseEntity<Object>("Producto eliminado!", HttpStatus.OK);
	}
	
	
	// Tabla Compra
	@CrossOrigin(origins = "http://localhost:8090")
	@PostMapping("/comprar")//Tabla Compra
	public ResponseEntity<?> comprar(@RequestParam("USUARIO_ID") Integer usuario_id, @RequestParam("SUBTOTAL") Double subtotal, @RequestParam("IVA") Double iva, @RequestParam("TOTAL") Double total) {
		System.out.println("id: " + usuario_id + " subtotal: " + subtotal + " iva: " + iva + " total: " + total);
		try {
			productoService.comprar_s(usuario_id, subtotal, iva, total);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Object>("Compra registrada!", HttpStatus.OK);
	}
	
	
	// Tabla Detalle Compra
	@CrossOrigin(origins = "http://localhost:8090")
	@PostMapping("/detallecompra")//Tabla Detalle Compra
	public ResponseEntity<?> detalle_compra(@RequestParam("PRODUCT_ID") Integer prod_id, @RequestParam("CANTIDAD") Integer cantidad) {
		try {
			productoService.detalle_compra_s(prod_id, cantidad);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Object>("Detalle compra registrada!", HttpStatus.OK);
	}
}
