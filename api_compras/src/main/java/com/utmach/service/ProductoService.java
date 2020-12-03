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
	
	private String url_imagenes = ".//src//main//resources//imagenes//";
	
	// Listar productos
	public List<Producto> listar_s(){
        return productoRepository.listarProcedure();
    }
	
	public byte[] foto_s(Integer id) throws IOException{
		Path path = Paths.get(productoRepository.foto_r(id));
		byte[] foto = Files.readAllBytes(path);
        return foto;
    }
	
	// Guardar producto
	public void guardar_s(MultipartFile foto, String descripcion, String nombre, Double precio, Integer stock) throws IOException{
		
		if (!foto.isEmpty()) {
			byte[] bytes = foto.getBytes();
			Path path = Paths.get(url_imagenes + foto.getOriginalFilename());
			Files.write(path, bytes);	// Guarda en el fs
			
			Producto p = new Producto();
			p.setDescripcion(descripcion);
			p.setFoto(path.toString());
			p.setNombres(nombre);
			p.setPrecio(precio);
			p.setStock(stock);
			
			productoRepository.guardar_r(p);
		}
    }
	
	// Eliminar un producto
	public void eliminar_s(int id) {
		String foto = productoRepository.eliminar_r(id);
		System.out.println(foto);
		Path path = Paths.get(foto);
		try {
			Files.delete(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
