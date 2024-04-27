package grupo5.tienda_libros.repositorio;

//Realizar consultas

import grupo5.tienda_libros.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepositorio extends JpaRepository<Libro, Integer>{
    
}
