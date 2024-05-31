package grupo5.tienda_libros.servicio;

import grupo5.tienda_libros.repositorio.LibroRepositorio;
import grupo5.tienda_libros.modelo.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroServicio {
    private final LibroRepositorio libroRepositorio;

    @Autowired
    public LibroServicio(LibroRepositorio libroRepositorio) {
        this.libroRepositorio = libroRepositorio;
    }

    public List<Libro> listarLibros() {
        return libroRepositorio.findAll();
    }

    public void guardarLibro(Libro libro) {
        libroRepositorio.save(libro);
    }

    public void eliminarLibro(Libro libro) {
        libroRepositorio.delete(libro);
    }
}