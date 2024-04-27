package grupo5.tienda_libros.vista;

import grupo5.tienda_libros.modelo.Libro;
import grupo5.tienda_libros.servicio.LibroServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//notacion padre
@Component //desplegar aplicacion swing con spring

public class LibroForm extends JFrame {
    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField idTexto;
    private JTextField libroTexto;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField existenciasTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tablaModeloLibros;

    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
        agregarButton.addActionListener(e -> agregarLibro());
        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
        modificarButton.addActionListener(e->modificarLibro());
        eliminarButton.addActionListener(e->eliminarLibro());
    }


    //metodo para mostrar formulario
    private void iniciarForma(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //para cerrar la aplicacion
        setVisible(true);
        setSize(900,700); //dimensiones
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimensionPantalla = toolkit.getScreenSize(); //obtener dimension de pantalla
        int x = (dimensionPantalla.width - getWidth()/2);
        int y = (dimensionPantalla.height - getHeight()/2);
        setLocation(x,y);
    }

    private void agregarLibro(){
        //Leer valores de formulario
        if (libroTexto.getText().equals("")){
            mostraMensaje("Proporciona el nombre del libro");
            libroTexto.requestFocusInWindow();
            return; // para que el usuario capture la informacion
        }
        var nombreLibro = libroTexto.getText();
        var autor = autorTexto.getText();
        var precio = Double.parseDouble(precioTexto.getText()); //para recuperar valor flotante
        var existencias = Integer.parseInt(existenciasTexto.getText()); // recuperar valor entero

        //Crear objeto libro
        var libro = new Libro(null, nombreLibro, autor, precio, existencias);
//        libro.setNombreLibro(nombreLibro);
//        libro.setAutor(autor);
//        libro.setPrecio(precio);
//        libro.setExistencias(existencias);
        this.libroServicio.guardarLibro(libro);
        mostraMensaje("Se agrego el Libro...");
        limpiarFormulario();
        listarLibros(); //Cargar informacion a la bd
    }

    private void cargarLibroSeleccionado(){
        //Iniciando los indices de columnas en 0
        var renglon = tablaLibros.getSelectedRow();
        if(renglon != -1){ // regresa -1 si no se selecciono ningun registro
            String idLibro = tablaLibros.getModel().getValueAt(renglon, 0).toString();
            idTexto.setText(idLibro);
            String nombreLibro = tablaLibros.getModel().getValueAt(renglon,1).toString();
            libroTexto.setText(nombreLibro);
            String autor = tablaLibros.getModel().getValueAt(renglon, 2).toString();
            autorTexto.setText(autor);
            String precio = tablaLibros.getModel().getValueAt(renglon, 3).toString();
            precioTexto.setText(precio);
            String existencias = tablaLibros.getModel().getValueAt(renglon, 4).toString();
            existenciasTexto.setText(existencias);
        }
    }

    private void modificarLibro(){
        if(this.idTexto.getText().equals("")){
            mostraMensaje("Debe seleccionar un registro...");
        }else{
            //Verificando que nombre del libro no sea nulo
            if (libroTexto.getText().equals("")){
                mostraMensaje("Proporciona el nombre del libro...");
                libroTexto.requestFocusInWindow(); //para seleccionar texto y escribir la informacion en el campo requerido
                return; //mostrar formulario
            }
            //Llenamos el objeto del libro a actualizar
            int idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = libroTexto.getText();
            var autor = autorTexto.getText();
            var precio = Double.parseDouble(precioTexto.getText());
            var existencias = Integer.parseInt(existenciasTexto.getText());
            var libro = new Libro(idLibro, nombreLibro, autor, precio, existencias); //actualizacion
            libroServicio.guardarLibro(libro);
            mostraMensaje("Se modifico el libro...");
            limpiarFormulario();
            listarLibros(); //actualizando bd
        }
    }

    private void eliminarLibro(){
        var renglon = tablaLibros.getSelectedRow();
        if(renglon != -1){
            String idLibro = tablaLibros.getModel().getValueAt(renglon, 0).toString();
            var libro = new Libro();
            libro.setIdLibro(Integer.parseInt(idLibro));
            libroServicio.eliminarLibro(libro);
            mostraMensaje("Libro "+idLibro+" eliminado.");
            limpiarFormulario();
            listarLibros();
        }else{
            mostraMensaje("Seleccione un libro a eliminar");
        }
    }

    private void limpiarFormulario(){
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        existenciasTexto.setText("");
    }

    private void mostraMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        // Creando el elemento idTexto oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);

        this.tablaModeloLibros = new DefaultTableModel(0,5){
            //Metodo para impedir la edicion directamente en el registro
            @Override
            public  boolean isCellEditable(int row, int column){
                return false;
            }
        };
        String[] cabeceros = {"Id","Libro","Autor","Precio","Existencias"};
        this.tablaModeloLibros.setColumnIdentifiers(cabeceros);

        // Instanciar obj JTable
        this.tablaLibros = new JTable(tablaModeloLibros);
        //Evitar que la seleccion de varios registros
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listarLibros();
    }

    private void listarLibros(){
        //limpiar tabla
        tablaModeloLibros.setRowCount(0);
        //obtener libro
        var libros = libroServicio.listarLibros();
        libros.forEach((libro)->{
            Object[] rengloLibro = {
                    libro.getIdLibro(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getExistencias()
            };
            this.tablaModeloLibros.addRow(rengloLibro);
        });
    }

}
