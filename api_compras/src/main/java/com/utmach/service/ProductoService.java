package com.utmach.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.utmach.entity.Producto;
import com.utmach.repository.ProductoRepository;

@Service
public class ProductoService {

	@Autowired
    ProductoRepository productoRepository;
	
	private String url_imagenes = ".//src//main//resources//static//";
	
	// Listar productos
	public List<Producto> listar_s(){
        return productoRepository.listarProcedure();
    }
	
	public Producto getProductos4Id_s(Integer id){
        return productoRepository.getProducto_r(id);
    }
	
	/*public byte[] foto_s(Integer id) throws IOException{
		Path path = Paths.get(productoRepository.foto_r(id));
		byte[] foto = Files.readAllBytes(path);
        return foto;
    }*/
	
	// Guardar producto
	public void guardar_s(MultipartFile foto, String descripcion, String nombre, Double precio, Integer stock) throws IOException{
		
		if (!foto.isEmpty()) {
			byte[] bytes = foto.getBytes();
			String name = foto.getOriginalFilename();
			Path path = Paths.get(url_imagenes + name);
			Files.write(path, bytes);	// Guarda en el fs
			
			Producto p = new Producto();
			p.setDescripcion(descripcion);
			p.setFoto(name);
			p.setNombres(nombre);
			p.setPrecio(precio);
			p.setStock(stock);
			
			productoRepository.guardar_r(p);
		}
    }
	
	
	// ACTUALIZAR
	public void actualizarProducto_s(Integer id, MultipartFile foto, String descripcion, String nombre, Double precio, Integer stock) throws IOException{
		
		Producto p = new Producto();
		
		if (!foto.isEmpty()) {
			// Guarda la nueva foto
			byte[] bytes = foto.getBytes();
			String name = foto.getOriginalFilename();
			Path path = Paths.get(url_imagenes + name);
			Files.write(path, bytes);	
			
			// Elimina la antigua foto
			eliminarFotoFS(productoRepository.getFoto_r(id));
			
			p.setId(id);
			p.setDescripcion(descripcion);
			p.setFoto(name);
			p.setNombres(nombre);
			p.setPrecio(precio);
			p.setStock(stock);
			
			
		}else {
			
			p.setId(id);
			p.setDescripcion(descripcion);
			p.setNombres(nombre);
			p.setPrecio(precio);
			p.setStock(stock);
			
			System.out.print("Actualizar sin foto(Valor de foto): " + p.getFoto());
		}
		
		productoRepository.actualizar_r(p);
    }
	
	
	

	// Eliminar un producto
	public void eliminar_s(int id) {
		String foto = productoRepository.eliminar_r(id);
		eliminarFotoFS(foto);
	}

	
	// Elimina la foto del Sistema de Archivos
	private void eliminarFotoFS(String foto) {
		System.out.println(url_imagenes + foto);
		Path path = Paths.get(url_imagenes + foto);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
