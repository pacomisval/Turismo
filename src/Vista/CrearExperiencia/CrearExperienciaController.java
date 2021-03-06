/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista.CrearExperiencia;

import Datos.Bda.GestionBD;
import Datos.Bda.actividadesDAO;
import Datos.Bda.experienciasActividadesDAO;
import Datos.Bda.experienciasDAO;
import Datos.Bda.subtiposDAO;
import Datos.Bda.tiposDAO;
import Modelo.Actividad;
import Modelo.ActividadExperiencia;
import Modelo.Experiencia;
import Modelo.Notificacion;
import Modelo.Subtipo;
import Modelo.Tipo;
import Modelo.Usuario;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author 20857464y
 */
public class CrearExperienciaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXDatePicker datePickerFechaInicio;
    @FXML
    private JFXListView<ActividadExperiencia> listaActividadesCarrito;
    @FXML
    private JFXListView<Actividad> listaActividadesElegir;
    @FXML
    private Label etiquetaActividades;
    @FXML
    private Label etiquetaCarrito;
    @FXML
    private JFXButton botonActividades;
    @FXML
    private JFXButton botonEliminar;
    @FXML
    private Label etiquetaFechaInicio;
    @FXML
    private Label etiquetaFechaFinal;
    @FXML
    private JFXDatePicker datePickerFechaFinal;
    @FXML
    private Label etiquetaHoraInicio;
    @FXML
    private Label etiquetaHoraFinal;
    @FXML
    private JFXTimePicker timePickerHoraInicio;
    @FXML
    private JFXTimePicker timePickerHoraFinal;
    @FXML
    private Label etiquetaNumPlazas;
    @FXML
    private JFXTextField textNumPlazas;
    @FXML
    private JFXTextField textNombreExperiencia;
    @FXML
    private Label etiquetaPrecioTotal;
    @FXML
    private JFXTextField salidaPrecio;
    @FXML
    private JFXTextArea textDescripcion;
    @FXML
    private JFXButton botonAñadirExperiencia;
    @FXML
    private AnchorPane Ventana;
    @FXML
    private JFXComboBox<Tipo> comboBoxTipos;
    @FXML
    private JFXComboBox<Subtipo> comboBoxSubTipos;

    private GestionBD gestion;
    private Usuario usuario;
    private Experiencia experiencia;
    private Notificacion not = new Notificacion();
    private experienciasDAO expDAO;
    private experienciasActividadesDAO expActDAO;
    private String nombreExp;
    @FXML
    private Pane paneExperiencia;
    @FXML
    private Pane paneTituloExperiencia;
    @FXML
    private Pane paneTituloExperiencia1;
    @FXML
    private JFXTextField textPrecioPorPersona;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image img = new Image("Imagenes/fondoExperiencia.jpg");
        ImageView imagev = new ImageView(img);

        imagev.setFitHeight(730);
        imagev.setFitWidth(1300);

        this.Ventana.getChildren().add(imagev);
        imagev.toBack();

        not = new Notificacion();

//        LIMITE LAS FECHAS ANTERIORES AL MOMENTO ACTUAL EN LOS DATE PICKER
        LocalDate minDate = LocalDate.now();
        datePickerFechaInicio.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(minDate));
            }
        });
        datePickerFechaFinal.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(minDate));
            }
        });
        botonActividades.getStyleClass().add("botonAnyadir");
        botonEliminar.getStyleClass().add("botonEliminar");
        botonAñadirExperiencia.getStyleClass().add("botonAnyadirExperiencia");
        paneExperiencia.getStyleClass().add("paneexperiencia");
        paneTituloExperiencia.getStyleClass().add("paneExperienciaTitulo");
        paneTituloExperiencia1.getStyleClass().add("paneExperienciaTitulo");
        comboBoxTipos.getStyleClass().add("combo");
        comboBoxSubTipos.getStyleClass().add("combo");

        listaActividadesCarrito.setOpacity(0.9);
        listaActividadesElegir.setOpacity(0.9);
    }

    public void setGestion(GestionBD gestion) {
        this.gestion = gestion;
        expDAO = new experienciasDAO(gestion);
        expActDAO = new experienciasActividadesDAO(gestion);
        actualizarTipos();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        experiencia = new Experiencia();
        experiencia.setIdUsuario(usuario.getId());
    }

    public void setExperiencia(Experiencia experiencia) {
        this.experiencia = experiencia;
        textNombreExperiencia.setText(experiencia.getNombre());
        cargarActividadesExperiencia();
    }

    @FXML
    private void AñadirActividad(ActionEvent event) {
        int orden = 1;
        boolean actividadValida = false;
        double precioActividad = 0;
        int numPlazas = 0;
        LocalDateTime fechaInicio = null;
        LocalDate diaInicio = null;
        LocalTime horaInicio = null;

        LocalDateTime fechaFinal = null;
        LocalDate diaFinal = null;
        LocalTime horaFinal = null;

        Actividad actividad = null;
//        VALIDACIONES
        try {
            //        VALIDA FECHA INICIO
            try {
                diaInicio = datePickerFechaInicio.getValue();
                actividadValida = true;
            } catch (Exception e) {
                actividadValida = false;
                not.error("Fecha Inicio", "Debes incluir una fecha de inicio para la actividad.");
                throw new Exception();
            }

//        VALIDA HORA INICIO
            try {
                horaInicio = timePickerHoraInicio.getValue();
                actividadValida = true;
            } catch (Exception e) {
                actividadValida = false;
                not.error("Hora Inicio", "Debes incluir una hora de inicio para la actividad.");
                throw new Exception();
            }

//        VALIDA FECHA FINAL
            try {
                diaFinal = datePickerFechaFinal.getValue();
                actividadValida = true;
            } catch (Exception e) {
                actividadValida = false;
                not.error("Fecha Final", "Debes incluir una fecha final para la actividad.");
                throw new Exception();
            }

//        VALIDA HORA FINAL
            try {
                horaFinal = timePickerHoraFinal.getValue();
                actividadValida = true;
            } catch (Exception e) {
                actividadValida = false;
                not.error("Hora Final", "Debes incluir una hora final para la actividad.");
                throw new Exception();
            }

//        VALIDA ACTIVIDAD ELEGIDA
            try {
                actividad = listaActividadesElegir.getSelectionModel().getSelectedItem();
                actividadValida = true;
            } catch (Exception e) {
                actividadValida = false;
                not.error("Elige una actividad", "Debes elegir una actividad de la lista.");
                throw new Exception();
            }

//        COMPROBAR LA COMPOSICION DE LA FECHA DE INICIO
            try {
                fechaInicio = LocalDateTime.of(diaInicio, horaInicio);
                if (fechaInicio.isBefore(LocalDateTime.now())) {
                    throw new Exception();
                }
                actividadValida = true;
            } catch (Exception e) {
                actividadValida = false;
                not.error("Error", "Debes introducir una fecha y hora válida.");
                throw new Exception();
            }
            //        COMPROBAR LA COMPOSICION DE LA FECHA FINAL
            try {
                fechaFinal = LocalDateTime.of(diaFinal, horaFinal);
                if (fechaInicio.isBefore(LocalDateTime.now())) {
                    throw new Exception();
                }
                actividadValida = true;
            } catch (Exception e) {
                actividadValida = false;
                not.error("Error", "Debes introducir una fecha y hora válida.");
                throw new Exception();
            }
//        COMPROBAR DISPONIBILIDAD
            if (actividadValida) {
                for (ActividadExperiencia actExp : listaActividadesCarrito.getItems()) {
                    if (fechaInicio.isAfter(actExp.getFechaInicio()) && fechaInicio.isBefore(actExp.getFechaFinal())) {
                        actividadValida = false;
                        not.alert("Error", "La fecha de inicio se solapa con otra actividad de tu lista.");
                        throw new Exception();
                    }
                    if (fechaFinal.isAfter(actExp.getFechaInicio()) && fechaFinal.isBefore(actExp.getFechaFinal())) {
                        actividadValida = false;
                        not.alert("Error", "La fecha de inicio se solapa con otra actividad de tu lista.");
                        throw new Exception();
                    }
                }
            }
//        COMPROBAR PRECIO
            try {
                precioActividad = actividad.getPrecio();
                actividadValida = true;
            } catch (Exception e) {
                actividadValida = false;
                not.error("Error", "No se ha podido conseguir el precio de la actividad.");
                throw new Exception();
            }

//            COMPROBAR NUMERO DE PLAZAS
            try {
                numPlazas = Integer.parseInt(textNumPlazas.getText());
            } catch (Exception e) {
                actividadValida = false;
                throw new Exception();
            }
        } catch (Exception e) {
            not.error("Error", "No hemos podido añadir su actividad.\nVuelva a intentarlo.");
        }

        if (actividadValida) {
            ActividadExperiencia nueva = new ActividadExperiencia(
                    orden,
                    experiencia.getId(),
                    actividad,
                    fechaInicio,
                    fechaFinal,
                    precioActividad,
                    numPlazas);
            listaActividadesCarrito.getItems().add(nueva);
            experiencia.getListaActividades().add(nueva);
            calcularPrecio();
        }
        actualizarOrden();
    }

    private void actualizarOrden() {
        if (!listaActividadesCarrito.getItems().isEmpty()) {
            int contador = 1;
            for (ActividadExperiencia act : listaActividadesCarrito.getItems()) {
                act.setOrden(contador);
                contador++;
            }
            cargarActividadesExperiencia();
        }
    }

    @FXML
    private void EliminarActividad(ActionEvent event) {
        if (!listaActividadesCarrito.getSelectionModel().isEmpty()) {
            listaActividadesCarrito.getItems().removeAll(listaActividadesCarrito.getSelectionModel().getSelectedItems());
            experiencia.getListaActividades().removeAll(listaActividadesCarrito.getSelectionModel().getSelectedItems());
            actualizarOrden();
            calcularPrecio();
            
            not.confirm("Enhorabuena", "La actividad ha sido borrada con éxito");
        } else {
            not.alert("ERROR", "Debe seleccionar una actividad para poder borrarla");
        }

    }

    private void cargarActividadesExperiencia() {
        listaActividadesCarrito.getItems().clear();
        for (ActividadExperiencia act : experiencia.getListaActividades()) {
            listaActividadesCarrito.getItems().add(act);
        }
        calcularPrecio();
    }

    private void cargarTodasActividades() {
        listaActividadesElegir.getItems().clear();
        actividadesDAO actividadesDAO = new actividadesDAO(gestion);
        Tipo tipo = comboBoxTipos.getSelectionModel().getSelectedItem();
        Subtipo subtipo = comboBoxSubTipos.getSelectionModel().getSelectedItem();
        List<Actividad> lista;
        try {
            if (tipo != null && subtipo == null) {
                lista = actividadesDAO.consultarActividadesPorTipo(tipo);
                for (Actividad act : lista) {
                    listaActividadesElegir.getItems().add(act);
                }
            }
            if (subtipo != null) {
                lista = actividadesDAO.consultarActividadesPorTipoYSubTipo(subtipo);
                for (Actividad act : lista) {
                    listaActividadesElegir.getItems().add(act);
                }
            }
        } catch (SQLException e) {
            not.error("ERROR", "No se ha podido conectar con la base de datos");
        }

    }

    private void calcularPrecio() {
        double precio = 0;
        for (ActividadExperiencia act : listaActividadesCarrito.getItems()) {
            precio += act.getPrecio() * act.getNumPlazas();
        }
        salidaPrecio.setText(String.valueOf(precio));
    }

    @FXML
    private void ActualizarDatosActividad(MouseEvent event) {
        ActividadExperiencia actExp = listaActividadesCarrito.getSelectionModel().getSelectedItem();
        textNumPlazas.setText(String.valueOf(actExp.getNumPlazas()));
//        ACTUALIZAR PRECIO POR PERSONA
        textPrecioPorPersona.setText(String.valueOf(actExp.getPrecio()));
//        ACTUALIZAR FECHA INICIO
        datePickerFechaInicio.setValue(actExp.getFechaInicio().toLocalDate());
        timePickerHoraInicio.setValue(actExp.getFechaInicio().toLocalTime());
//        ACTUALIZAR FECHA FINAL
        datePickerFechaFinal.setValue(actExp.getFechaFinal().toLocalDate());
        timePickerHoraFinal.setValue(actExp.getFechaFinal().toLocalTime());
    }

    private void actualizarTipos() {
        if (!comboBoxSubTipos.getSelectionModel().isEmpty()) {
            comboBoxSubTipos.getItems().clear();
        }

        tiposDAO tiposDAO = new tiposDAO(gestion);
        try {
            for (Tipo tipo : tiposDAO.consultarTipos()) {
                comboBoxTipos.getItems().add(tipo);
            }
        } catch (SQLException e) {
//            EXCEPCION SQL
            not.error("ERROR", "No se ha podido conectar con la base de datos");
        }
        cargarTodasActividades();
    }

    @FXML
    private void actualizarSubtipos(ActionEvent event) {
        if (!comboBoxSubTipos.getSelectionModel().isEmpty()) {
            comboBoxSubTipos.getItems().clear();
        }
        if (!comboBoxTipos.getSelectionModel().isEmpty()) {
            comboBoxSubTipos.getItems().clear();
        }

        subtiposDAO subtiposDAO = new subtiposDAO(gestion);
        try {
            for (Subtipo subtipo : subtiposDAO.consultarSubtiposporTipo(comboBoxTipos.getSelectionModel().getSelectedItem())) {
                comboBoxSubTipos.getItems().add(subtipo);
            }
        } catch (SQLException e) {
            not.error("ERROR", "No se ha podido conectar con la base de datos");
        }
        cargarTodasActividades();
    }

    @FXML
    private void cargarActividades(ActionEvent event) {
        cargarTodasActividades();
    }

    @FXML
    private void AñadirExperiencia(ActionEvent event) throws SQLException {
        experiencia.setIdUsuario(usuario.getId());
        experiencia.setFechaTopeValidez(LocalDate.now().plusYears(1));
        experiencia.setNombre(textNombreExperiencia.getText());
        experiencia.setDescripcion(textDescripcion.getText());
        if (comprobarNombre()) {
            try {
                experiencia.setId(expDAO.idExperienciaSiguiente());
                expDAO.insertarExperiencia(experiencia);
                for (ActividadExperiencia actExp : experiencia.getListaActividades()) {
                    try {
                        actExp.setIdExperiencia(experiencia.getId());
                        expActDAO.insertarActividadExperiencia(actExp);
                    } catch (Exception e) {
                        not.error("Error", "Error al insertar actividades a experiencia");
                        throw new Exception();
                    }
                }
                not.confirm("Enhorabuena", "Se ha creado su experiencia \nde forma satisfactoria");
            } catch (Exception e) {
                e.printStackTrace();
                not.error("Error", "No ha podido insertarse la experiencia en la BD");
            }
        } else {
            not.alert("Error", "Ese nombre de experiencia ya existe");
        }
    }

    private void limpiar() {
        datePickerFechaFinal.setValue(null);
        datePickerFechaInicio.setValue(null);
        timePickerHoraFinal.setValue(null);
        timePickerHoraInicio.setValue(null);
        etiquetaNumPlazas.setText("");
    }

    @FXML
    private void ActualizarPrecioActividad(MouseEvent event) {
        Actividad act = listaActividadesElegir.getSelectionModel().getSelectedItem();
        if (act.getPrecio() == 0) {
            textPrecioPorPersona.setText("Gratis");
        } else {
            textPrecioPorPersona.setText(String.valueOf(act.getPrecio()) + "€ por persona");
        }

    }

    private boolean comprobarNombre() throws SQLException {
        boolean nombreDisponible = true;
        List<String> NombreExperiencias = new ArrayList<>();
        nombreExp = textNombreExperiencia.getText();

        NombreExperiencias = expDAO.consultarNombre();

        for (String nom : NombreExperiencias) {
            if (nom.equalsIgnoreCase(nombreExp)) {
                nombreDisponible = false;
            }
        }

        return nombreDisponible;
    }
}
