package grupo5.tienda_libros.servicio;

import grupo5.tienda_libros.modelo.Libro;
import grupo5.tienda_libros.repositorio.LibroRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//notacion especifica
@Service
public class LibroServicio implements ILibroServicio{

    @Autowired //para que inyecte automaticamente la instancia libroRepositorio
    private LibroRepositorio libroRepositorio;
    
    @Override
    public List<Libro> listarLibros() {
        return libroRepositorio.findAll();
    }

    @Override
    public Libro buscarLibroPorId(Integer idLibro) {
        Libro libro = libroRepositorio.findById(idLibro).orElse(null); //se agrega el metodo orElse, debido a que findById no es preciso
        return libro;
    }

    @Override
    public void guardarLibro(Libro libro) {
        libroRepositorio.save(libro); //de manera automatica este metodo identifica si se requiere un insert o update
    }

    @Override
    public void eliminarLibro(Libro libro) {
        libroRepositorio.delete(libro);
    }
    
}
