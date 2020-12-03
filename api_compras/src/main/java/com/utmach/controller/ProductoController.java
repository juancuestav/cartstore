package com.utmach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.utmach.entity.Producto;
import com.utmach.service.ProductoService;

//Cross origin

/*
spring.datasource.url=jdbc:oracle:thin:@centos:1521/orcl
spring.datasource.username=BDCARRITOCOMPRAS
spring.datasource.password=BDCARRITOCOMPRAS
spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
#spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.Oracle10gDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
 * */

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
			e.printStackTrace();
		}
		
		return new ResponseEntity<Object>("Archivo subido correctamente", HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:8090")
	@GetMapping("/listar")
	public ResponseEntity<List<Producto>> listar_c() {
		List<Producto> lista = productoService.listar_s();
		return new ResponseEntity(lista, HttpStatus.OK);
	}
	
	/*@DeleteMapping("/eliminar/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		productoService.deleteById(id);
	}
	
	@PutMapping("/actualizar")
	public void actualizar(@RequestBody Producto p) {
		productoService.save(p);
	}*/
}
