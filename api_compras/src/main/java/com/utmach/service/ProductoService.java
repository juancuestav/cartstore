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
	
	public List<Producto> listar_s(){
		//return productoRepository.findAll();
		/*List<Object[]> results = productoRepository.listarProcedure();
		System.out.print(results);*/
	    /*storedProcedureResponse.stream().map(result -> new BusinessObject(
	        (String) result[0],
		    (Long) result[1]
	    )).collect(Collectors.toList());*/
        return productoRepository.listarProcedure();
    }
	
	public void guardar_s(MultipartFile foto, String descripcion, String nombre, Double precio, Integer stock) throws IOException{
		
		if (!foto.isEmpty()) {
			byte[] bytes = foto.getBytes();
			Path path = Paths.get(url_imagenes + foto.getOriginalFilename());
			Files.write(path, bytes);
			
			Producto p = new Producto();
			p.setDescripcion(descripcion);
			p.setFoto(path.toString());
			p.setNombres(nombre);
			p.setPrecio(precio);
			p.setStock(stock);
			
			productoRepository.guardar_r(p);
		}
		
    }

}
