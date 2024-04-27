package grupo5.tienda_libros.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

//clase entidad
@Entity 
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //notacion para generar llave primaria automaticamente
    Integer idLibro;
    String nombreLibro;
    String autor;
    Double precio;
    Integer existencias;
}
