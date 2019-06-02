
package Vista.Administrador.Experiencia;

import Datos.Bda.actividadesDAO;
import Datos.Bda.GestionBD;
import Datos.Bda.experienciasActividadesDAO;
import Datos.Bda.experienciasDAO;
import Modelo.Actividad;
import Modelo.ActividadExperiencia;
import Modelo.Experiencia;
import Modelo.Notificacion;
import Modelo.ValidarCampos;
import Vista.Administrador.Experiencia.InterfaceOrdenacion.OrdenFechInicioAE;
import Vista.Administrador.Experiencia.InterfaceOrdenacion.OrdenFechaE;
import Vista.Administrador.Experiencia.InterfaceOrdenacion.OrdenNombreE;
import Vista.Administrador.Experiencia.InterfaceOrdenacion.OrdenPrecioAE;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.DateCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.Notifications;



public class ExperienciaAdminController implements Initializable {

    @FXML
    private GridPane menu2;  
    @FXML
    private TextField textExperiencia;   
    @FXML
    private TextField textNombre;
    @FXML
    private TextField textDescripcion;
    @FXML
    private TextField textFecha;
    @FXML
    private TextField textFoto;
    @FXML
    private TextField textUsuario;
    @FXML
    private TableView<Experiencia> tableView;
    @FXML
    private TableColumn<Experiencia, Integer> tb_idExperiencia;
    @FXML
    private TableColumn<Experiencia, String> tb_nombre;
    @FXML
    private TableColumn<Experiencia, String> tb_descripcion;
    @FXML
    private TableColumn<Experiencia, LocalDate> tb_fechaTope;
    @FXML
    private TableColumn<Experiencia, String> tb_foto;
    @FXML
    private TableColumn<Experiencia, Integer> tb_idUsuario;
    @FXML
    private ImageView imageView;
    @FXML
    private TableView<ActividadExperiencia> tableListaExperiencias;
    @FXML
    private TableColumn<ActividadExperiencia, Integer> tb_orden;
    @FXML
    private TableColumn<ActividadExperiencia, Integer> tb_idExperi;
    @FXML
    private TableColumn<ActividadExperiencia, Integer> tb_idActividad;
    @FXML
    private TableColumn<ActividadExperiencia, LocalDateTime> tb_fechaInicio;
    @FXML
    private TableColumn<ActividadExperiencia, LocalDateTime> tb_fechaFinal;
    @FXML
    private TableColumn<ActividadExperiencia, Double> tb_precio;
    @FXML
    private TableColumn<ActividadExperiencia, Integer> tb_numPlazas;
    @FXML
    private TextField textOrden;
    @FXML
    private TextField textIdExperiencia;
    @FXML
    private TextField textIdActividad;
    @FXML
    private TextField textFechaInicio;
    @FXML
    private TextField textFechaFinal;
    @FXML
    private TextField textHoraInicio;
    @FXML
    private TextField textHoraFin;
    @FXML
    private TextField textPrecio;
    @FXML
    private TextField textNumPlazas;
    @FXML
    private JFXTimePicker horaInicio;
    @FXML
    private JFXTimePicker horaFin;
    @FXML
    private JFXDatePicker fechaInicio;
    @FXML
    private JFXDatePicker fechaFin;
    @FXML
    private JFXDatePicker fechaTopeValidez;
    @FXML
    private ToggleGroup grupoEx;    
    @FXML
    private RadioButton radioId;
    @FXML
    private RadioButton radioNombre;
    @FXML
    private RadioButton radioFechaTope;
    @FXML
    private ToggleGroup grupoAcEx;
    @FXML
    private RadioButton radioPrecio;
    @FXML
    private RadioButton radioFechaInicio;
    @FXML
    private RadioButton radioOrden;
    @FXML
    private AnchorPane anchorPane;
    
    
    private ObservableList<Experiencia> obExperiencias;
    private ObservableList<ActividadExperiencia> obActiviEncias;
    
    private static GestionBD gestion;
    private experienciasDAO experienDAO;
    private experienciasActividadesDAO eaDAO;
    private Experiencia experiencia;
    private Notificacion not;
    private ActividadExperiencia actExperiencia;
    private actividadesDAO activiDAO;
    
    boolean delete;
    
    
       
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obExperiencias = FXCollections.observableArrayList();
        obActiviEncias = FXCollections.observableArrayList();
        
        not = new Notificacion();
        anchorPane.getStyleClass().add("fondoExperienciaAdmin");
        
        LocalDate minDate = LocalDate.now();
        fechaTopeValidez.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(minDate));
            }
        });
        fechaInicio.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(minDate));
            }
        });
        fechaFin.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(minDate));
            }
        });
    }
    
       
    public void setGestion(GestionBD gestion){
        ExperienciaAdminController.gestion = gestion;
    }
    
    public void ejecutaAlPrincipio(){
        experienDAO = new experienciasDAO(gestion);
        activiDAO = new actividadesDAO(gestion);
        eaDAO = new experienciasActividadesDAO(gestion);
        
        listar();
       
    }
    
// ----------------------- RADIO BUTTON EXPERIENCIAS ----------------------
    
    private void ordenarId(){
        Collections.sort(obExperiencias);
    }
    
    private void ordenarNombre(){
        Collections.sort(obExperiencias,new OrdenNombreE());
    }
    
    private void ordenarFechaTope(){
        Collections.sort(obExperiencias,new OrdenFechaE());
    }
    
 
    
// -------------------- RADIO BUTTON ACTIVIDAD EXPERIENCIAS ---------------
    
    private void ordenarOrden(){
        Collections.sort(obActiviEncias);
    }
    
    private void ordenarFechaInicio(){
        Collections.sort(obActiviEncias,new OrdenFechInicioAE());
    }
    
    private void ordenarPrecio(){
        Collections.sort(obActiviEncias, new OrdenPrecioAE());
    }
    
    
    
// -------------------------- INSERTAR EXPERIENCIA ----------------------------
    
    private void insertar(){
//     
        int id = 0, idUsuario = 0;
        String nombre,descripcion,foto;
        LocalDate fechaTope = null;
        List<ActividadExperiencia> lista = null;
        boolean ok = false;
        boolean errorNF = false;
               
        try{
           idUsuario = Integer.parseInt(textUsuario.getText()); 
        }catch(NumberFormatException nf){
           System.out.println("Error : " + nf.getMessage());
           errorNF = true;
        }
        if(errorNF != true){
            nombre = textNombre.getText();
            descripcion = textDescripcion.getText();           
            fechaTope = fechaTopeValidez.getValue().plusDays(1);
            System.out.println("fechatp"+fechaTope);
            foto = textFoto.getText();
    //        String idAux = String.valueOf(experiencia.getId());
    //        id = Integer.parseInt(textExperiencia.getText());
            boolean general = validarExperiencia();

            if(general){               
                try {
                    Experiencia nueva = new Experiencia(id, idUsuario, nombre, descripcion, fechaTope, foto,lista);

                    ok = experienDAO.insertarExperiencia(nueva);

                } catch (SQLException ex) {
                    not.error("ERROR", "Error al intentar conectar con la base de datos");
                    if(ex.getErrorCode() == 1452){
                        not.error("ERROR USUARIO DESCONOCIDO", "El usuario no se encuentra registrado en la DB");
                    }
                    if(ex.getErrorCode() == 1062){
                        not.error("ERROR NOMBRE EXPERIENCIA", "El nombre de la experiencia ya existe, elige otro nombre");
                    }
                    System.out.println("ERROR 1: " + ex.getMessage());
                    System.out.println("ERROR 2: " + ex.getErrorCode());
                    System.out.println("ERROR 3: " + ex.getSQLState());
                    ex.getStackTrace();
                } catch (java.time.DateTimeException dt){
                    not.alert("ERROR EN FECHA TOPE", "Formato de fecha invalido");
                }

                if(ok){
                    not.confirm("INSERTAR REGISTRO", "Operación realizada con exito");
                }
                else{
                    not.alert("ERROR AL INSERTAR REGISTRO", "Operación fallida");
                }
            }
            else{
                //not.error("ERROR AL INSERTAR LA TABLA EXPERIENCIAS", "");
            }
        }
        else{
            not.error("ERROR CAMPOS VACIOS EN LA TABLA EXPERIENCIAS",
                    "Rellena los campos obligatorios");
        }
    }


// ------------------------- MODIFICAR EXPERIENCIA ----------------------
    
    private void actualizar(){
        ValidarCampos validar = new ValidarCampos();
        LocalDate minDate = LocalDate.now();
        int id = 0,idUsuario = 0;
        String nombre,descripcion,foto;
        LocalDate fechaTope;
        boolean ok = false;
        
        experiencia = tableView.getSelectionModel().getSelectedItem();
        
        if(experiencia != null){
             
            idUsuario = Integer.parseInt(textUsuario.getText());
            nombre = textNombre.getText();
            descripcion = textDescripcion.getText();                   
            fechaTope = fechaTopeValidez.getValue();
            foto = textFoto.getText();
            id = experiencia.getId();

            boolean general = validarExperiencia();

            if(general){
                try {
                ok = experienDAO.modificarExperiencia(idUsuario,nombre,descripcion,fechaTope,foto,id);

                } catch(SQLException ex){
                    not.error("ERROR", "Error al modificar en tabla Experiencias");
                } catch(DateTimeParseException dt){
                    String valor = "Text '' could not be parsed at index 0";
                    if(dt.equals(valor)){
                        not.alert("FORMATO FECHA INVALIDO", "Debes introducir un formato como este yyyy-MM-dd");
                    }
                }        
                if(ok){
                    not.confirm("MODIFICAR EXPERIENCIA","Operación realizada con éxito");
                }
                else {
                    not.alert("FORMATO FECHA INVALIDO", "Debes introducir un formato de fecha \ncomo este --> yyyy-MM-dd");
                } 
            }
            else{
                not.error("ERROR ", "AL MODIFICAR LA TABLA EXPERIENCIAS");
            }
        }
        else{
            not.error("ERROR", "No se puede modificar\n"
                    + " selecciona una experiencia primero");
        }
        
    }

    
// --------------------------- VALIDAR EXPERIENCIA ----------------------------
    
    private boolean validarExperiencia(){
        ValidarCampos validar = new ValidarCampos();
        int id = 0,idUsuario = 0;
        String nombre,descripcion,foto;
        LocalDate fechaTope;
        boolean general = true;
       
        if(textUsuario.getText().isEmpty()){
            not.error("ERROR ID USUARIO DESCONOCIDO", "El idUsuario no existe, debe ser\n"
                    + " un usuario que exista en la tabla experiencias");
            textUsuario.setStyle("-fx-background-color: tomato");
            general = false;
        }
        else{
            if(validar.validarNumEntero(textUsuario.getText()) == 0){
                general = false;
                not.alert("ERROR CAMPO IDUSUARIO VACIO", "No puede estar vacio");
                textUsuario.setStyle("-fx-background-color: tomato");
            }
            else{
                idUsuario = Integer.parseInt(textUsuario.getText());
                textUsuario.setStyle("-fx-background-color: white");
            }
           
        }
        nombre = textNombre.getText();
        if(nombre.isEmpty()){
            general = false;
            not.alert("ERROR CAMPO NOMBRE VACIO","No puede estar vacio");
            textNombre.setStyle("-fx-background-color: tomato");
        }
        else{
            textNombre.setStyle("-fx-background-color: white");
        }
        
        descripcion = textDescripcion.getText();           
        fechaTope = fechaTopeValidez.getValue();
        if(fechaTope != null){
            fechaTopeValidez.setValue(fechaTope);
            textFecha.setStyle("-fx-background-color: white");
        }
        else{
            general = false;
            not.alert("ERROR CAMPO FECHA VACIO", "No puede estar vacio");
            textFecha.setStyle("-fx-background-color: tomatp");
        }                    
        foto = textFoto.getText();
        if(!foto.isEmpty()){
            if(validar.validarFoto(foto) != true){
                general = false;
                not.alert("ERROR DE FORMATO FOTO","La extensión no es válida");
                textFoto.setText("-fx-background-color: tomato");
            }
            else{
                textFoto.setText("-fx-background-color: white");
            }
        }
        
        return general;
    }

// ------------------- INSERTAR ACTIVIDAD EXPERIENCIA --------------------------    
    
    private boolean insertarActividadExperiencia(){
        int orden = 0, idExperiencia = 0,numPlazas = 0;
        double precio = 0;        
        LocalDateTime fechaIni = null, fechaFinal = null;       
        Actividad actividad = null;
        ActividadExperiencia acEx;
        boolean ok = false;
        boolean general = true;
        
        
        
//        general = this.validarActividadExperiencia();
              
        try{
            
            try{
                orden = Integer.parseInt(textOrden.getText());
            } catch(NumberFormatException nf){
                general = false;
                if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
                        (nf.getMessage().equals("For input string: \" \"")) ||
                        (nf.getMessage().equals("For input string: \"\"")))){
                    not.alert("ERROR EL CAMPO ORDEN ESTA VACIO","Verifica que el campo de texto no este vacio");
                }    
                else if(nf.getMessage().equals("For input string: \""+ textOrden.getText() +"\"")){
                    not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo orden ");    
                }
                else{
                        not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
                          + " \n algo raro ha ocurrido");
                }
            }
            if(general){
                try{
                    idExperiencia = Integer.parseInt(textIdExperiencia.getText());
                } catch(NumberFormatException nf){
                    general = false;
                    if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
                            (nf.getMessage().equals("For input string: \" \"")) ||
                            (nf.getMessage().equals("For input string: \"\"")))){
                        not.alert("ERROR EL CAMPO IDEXPERIENCIA ESTA VACIO","Verifica que el campo de texto no este vacio");
                    }
                    else if(nf.getMessage().equals("For input string: \""+ textIdExperiencia.getText() +"\"")){
                        not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo idExperiencia ");

                    }
                    else{
                            not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
                              + " \n algo raro ha ocurrido");
                    }
                }
            }
            
            if(general){
                try{
                    actividad = activiDAO.consultarActividad(Integer.parseInt(textIdActividad.getText()));
                } catch(NumberFormatException nf){
                    general = false;
                    if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
                            (nf.getMessage().equals("For input string: \" \"")) ||
                            (nf.getMessage().equals("For input string: \"\"")))){
                        not.alert("ERROR EL CAMPO ACTIVIDAD ESTA VACIO","Verifica que el campo de texto no este vacio");
                    }
                    else if(nf.getMessage().equals("For input string: \""+ textIdActividad.getText() +"\"")){
                        not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo actividad ");

                    }
                    else{
                            not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
                              + " \n algo raro ha ocurrido");
                    }
                }
            }
            
            if(general){
                try{
                    numPlazas = Integer.parseInt(textNumPlazas.getText());
                } catch(NumberFormatException nf){
                    general = false;
                    if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
                            (nf.getMessage().equals("For input string: \" \"")) ||
                            (nf.getMessage().equals("For input string: \"\"")))){
                        not.alert("ERROR EL CAMPO NUMPLAZAS ESTA VACIO","Verifica que el campo de texto no este vacio");
                    }
                    else if(nf.getMessage().equals("For input string: \""+ textNumPlazas.getText() +"\"")){
                        not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo numPlazas ");

                    }
                    else{
                            not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
                              + " \n algo raro ha ocurrido");
                    }
                }
            }
            
            if(general){
                if(fechaIni == null && fechaFinal == null){          

                    try{
                        fechaIni = LocalDateTime.of(fechaInicio.getValue(), horaInicio.getValue()); 
                    }catch(Exception es){
                        general = false;
                        if(es.getMessage().equals("date")){
                            not.error("ERROR CAMPO FECHA INICIO VACIO", "Selecciona una fecha");
                        }
                        if(es.getMessage().equals("time")){
                            not.error("ERROR CAMPO HORA INICIO VACIO", "Selecciona una hora");
                        }

                    }

                    try{
                        fechaFinal = LocalDateTime.of(fechaFin.getValue(), horaFin.getValue());  
                    }catch(Exception es){
                        general = false;
                        if(es.getMessage().equals("date")){
                            not.error("ERROR CAMPO FECHA FIN VACIO", "Selecciona una fecha");
                        }
                        if(es.getMessage().equals("time")){
                            not.error("ERROR CAMPO HORA FIN VACIO", "Selecciona una hora");
                        }

                    }
                    if(fechaFinal.isBefore(fechaIni)){
                        general = false;
                        not.error("ERROR AL SELECCIONAR LA FECHA FIN", 
                                "No se puede selccionar una fecha final anterior a la fecha inicial");
                    }

                }
            }
                  
            precio = actividad.getPrecio() * numPlazas;
            
        } catch(Exception ex){
            general = false;

        }    
            
        if(general){
            try {    

                acEx = new ActividadExperiencia(orden,idExperiencia,actividad,fechaIni,fechaFinal,precio,numPlazas);
                ok = eaDAO.insertarActividadExperiencia(acEx);

            } catch (SQLException ex) {
                not.error("ERROR", "El número de orden y la actividad ya estan registrados\n"
                        + " elige otro número de orden");
            } catch (java.time.format.DateTimeParseException dt){           
                not.alert("ERROR EN EL FORMATO FECHA", "El formato de fecha no es correcto");
            } catch(Exception ex){
                System.out.println("ERROR1: "+ ex.getMessage());
                System.out.println("ERROR2: " + ex.getCause());
                System.out.println("ERROR3: " + ex.getStackTrace());
                not.error("ERROR: ", "Mensaje: " + ex.getMessage() + " En insertar Actividad experiencia");
            }      


            if(ok){
                not.confirm("INSERTAR EN LA TABLA EXPERINCIA ACTIVIDAD","Operación realizada con éxito");
            }
        }    
               
        return ok;
    }
              
    
 // ------------------------- MODIFICAR ACTIVIDAD EXPERIENCIA ------------------
    
    private boolean modificarActividadExperiencia(){       
        int orden = 0, idExperiencia = 0,numPlazas = 0;
        double precio = 0;        
        LocalDateTime fechaIni = null, fechaFinal = null;       
        Actividad actividad = null;
//        ActividadExperiencia acEx;
        boolean ok = false;
        boolean general = true;
        
        actExperiencia = tableListaExperiencias.getSelectionModel().getSelectedItem();        
        if(actExperiencia != null){
//            general = this.validarActividadExperiencia();
            try{
            
                try{
                    orden = Integer.parseInt(textOrden.getText());
                } catch(NumberFormatException nf){
                    general = false;
                    if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
                            (nf.getMessage().equals("For input string: \" \"")) ||
                            (nf.getMessage().equals("For input string: \"\"")))){
                        not.alert("ERROR EL CAMPO ORDEN ESTA VACIO","Verifica que el campo de texto no este vacio");
                    }    
                    else if(nf.getMessage().equals("For input string: \""+ textOrden.getText() +"\"")){
                        not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo orden ");    
                    }
                    else{
                            not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
                              + " \n algo raro ha ocurrido");
                    }
                }
                if(general){
                    try{
                        idExperiencia = Integer.parseInt(textIdExperiencia.getText());
                    } catch(NumberFormatException nf){
                        general = false;
                        if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
                                (nf.getMessage().equals("For input string: \" \"")) ||
                                (nf.getMessage().equals("For input string: \"\"")))){
                            not.alert("ERROR EL CAMPO IDEXPERIENCIA ESTA VACIO","Verifica que el campo de texto no este vacio");
                        }
                        else if(nf.getMessage().equals("For input string: \""+ textIdExperiencia.getText() +"\"")){
                            not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo idExperiencia ");

                        }
                        else{
                                not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
                                  + " \n algo raro ha ocurrido");
                        }
                    }
                }

                if(general){
                    try{
                        actividad = activiDAO.consultarActividad(Integer.parseInt(textIdActividad.getText()));
                    } catch(NumberFormatException nf){
                        general = false;
                        if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
                                (nf.getMessage().equals("For input string: \" \"")) ||
                                (nf.getMessage().equals("For input string: \"\"")))){
                            not.alert("ERROR EL CAMPO ACTIVIDAD ESTA VACIO","Verifica que el campo de texto no este vacio");
                        }
                        else if(nf.getMessage().equals("For input string: \""+ textIdActividad.getText() +"\"")){
                            not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo actividad ");

                        }
                        else{
                                not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
                                  + " \n algo raro ha ocurrido");
                        }
                    }
                }

                if(general){
                    try{
                        numPlazas = Integer.parseInt(textNumPlazas.getText());
                    } catch(NumberFormatException nf){
                        general = false;
                        if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
                                (nf.getMessage().equals("For input string: \" \"")) ||
                                (nf.getMessage().equals("For input string: \"\"")))){
                            not.alert("ERROR EL CAMPO NUMPLAZAS ESTA VACIO","Verifica que el campo de texto no este vacio");
                        }
                        else if(nf.getMessage().equals("For input string: \""+ textNumPlazas.getText() +"\"")){
                            not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo numPlazas ");

                        }
                        else{
                                not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
                                  + " \n algo raro ha ocurrido");
                        }
                    }
                }

                if(general){
                    if(fechaIni == null && fechaFinal == null){          

                        try{
                            fechaIni = LocalDateTime.of(fechaInicio.getValue(), horaInicio.getValue()); 
                        }catch(Exception es){
                            general = false;
                            if(es.getMessage().equals("date")){
                                not.error("ERROR CAMPO FECHA INICIO VACIO", "Selecciona una fecha");
                            }
                            if(es.getMessage().equals("time")){
                                not.error("ERROR CAMPO HORA INICIO VACIO", "Selecciona una hora");
                            }

                        }

                        try{
                            fechaFinal = LocalDateTime.of(fechaFin.getValue(), horaFin.getValue());  
                        }catch(Exception es){
                            general = false;
                            if(es.getMessage().equals("date")){
                                not.error("ERROR CAMPO FECHA FIN VACIO", "Selecciona una fecha");
                            }
                            if(es.getMessage().equals("time")){
                                not.error("ERROR CAMPO HORA FIN VACIO", "Selecciona una hora");
                            }

                        }
                        if(fechaFinal.isBefore(fechaIni)){
                            general = false;
                            not.error("ERROR AL SELECCIONAR LA FECHA FIN", 
                                    "No se puede selccionar una fecha final anterior a la fecha inicial");
                        }

                    }
                }

                precio = actividad.getPrecio() * numPlazas;
            
            } catch(Exception ex){
                general = false;

            }    

            if(general){
                try {    

                    ok = eaDAO.modificarActividadExperiencia(orden, idExperiencia, actividad, fechaIni, fechaFinal, precio, numPlazas);

                } catch (SQLException ex) {
                    not.error("ERROR", "El número de orden y la actividad ya estan registrados\n"
                            + " elige otro número de orden");
                } catch (java.time.format.DateTimeParseException dt){           
                    not.alert("ERROR EN EL FORMATO FECHA", "El formato de fecha no es correcto");
                } catch(Exception ex){
                    System.out.println("ERROR1: "+ ex.getMessage());
                    System.out.println("ERROR2: " + ex.getCause());
                    System.out.println("ERROR3: " + ex.getStackTrace());
                    not.error("ERROR: ", "Mensaje: " + ex.getMessage() + " En modificar Actividad experiencia");
                }      


                if(ok){
                    not.confirm("INSERTAR EN LA TABLA EXPERINCIA ACTIVIDAD","Operación realizada con éxito");
                }
            }    
               
        }
        else{
            not.error("ERROR CAMPOS VACIOS", "Selecciona un registro ");
        }
        return ok;
    }
    
// ----------------- VALIDAR EXPERIENCIA ACTIVIDAD -------------------------
    
//    private boolean validarActividadExperiencia(){
//        int orden = 0, idExperiencia = 0,numPlazas = 0;
//        double precio = 0, precioAUX;        
//        LocalDateTime fechaIni = null, fechaFinal = null;       
//        Actividad actividad = null;
//        ActividadExperiencia acEx;
//        boolean ok = false;
//        boolean general = true;
//        
//        try{
//            
//            try{
//                orden = Integer.parseInt(textOrden.getText());
//            } catch(NumberFormatException nf){
//                general = false;
//                if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
//                        (nf.getMessage().equals("For input string: \" \"")) ||
//                        (nf.getMessage().equals("For input string: \"\"")))){
//                    not.alert("ERROR EL CAMPO ORDEN ESTA VACIO","Verifica que el campo de texto no este vacio");
//                }    
//                else if(nf.getMessage().equals("For input string: \""+ textOrden.getText() +"\"")){
//                    not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo orden ");    
//                }
//                else{
//                        not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
//                          + " \n algo raro ha ocurrido");
//                }
//            }
//            if(general){
//                try{
//                    idExperiencia = Integer.parseInt(textIdExperiencia.getText());
//                } catch(NumberFormatException nf){
//                    general = false;
//                    if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
//                            (nf.getMessage().equals("For input string: \" \"")) ||
//                            (nf.getMessage().equals("For input string: \"\"")))){
//                        not.alert("ERROR EL CAMPO IDEXPERIENCIA ESTA VACIO","Verifica que el campo de texto no este vacio");
//                    }
//                    else if(nf.getMessage().equals("For input string: \""+ textIdExperiencia.getText() +"\"")){
//                        not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo idExperiencia ");
//
//                    }
//                    else{
//                            not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
//                              + " \n algo raro ha ocurrido");
//                    }
//                }
//            }
//
//            if(general){
//                try{
//                    actividad = activiDAO.consultarActividad(Integer.parseInt(textIdActividad.getText()));
//                } catch(NumberFormatException nf){
//                    general = false;
//                    if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
//                            (nf.getMessage().equals("For input string: \" \"")) ||
//                            (nf.getMessage().equals("For input string: \"\"")))){
//                        not.alert("ERROR EL CAMPO ACTIVIDAD ESTA VACIO","Verifica que el campo de texto no este vacio");
//                    }
//                    else if(nf.getMessage().equals("For input string: \""+ textIdActividad.getText() +"\"")){
//                        not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo actividad ");
//
//                    }
//                    else{
//                            not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
//                              + " \n algo raro ha ocurrido");
//                    }
//                }
//            }
//
//            if(general){
//                try{
//                    numPlazas = Integer.parseInt(textNumPlazas.getText());
//                } catch(NumberFormatException nf){
//                    general = false;
//                    if(nf.getCause() == null && (nf.getMessage().equals("empty String") ||
//                            (nf.getMessage().equals("For input string: \" \"")) ||
//                            (nf.getMessage().equals("For input string: \"\"")))){
//                        not.alert("ERROR EL CAMPO NUMPLAZAS ESTA VACIO","Verifica que el campo de texto no este vacio");
//                    }
//                    else if(nf.getMessage().equals("For input string: \""+ textNumPlazas.getText() +"\"")){
//                        not.alert("ERROR FORMATO NUMERICO", "No introducir texto en el campo numPlazas ");
//
//                    }
//                    else{
//                            not.alert("ERROR DE OTRO TIPO","Verifica los campos de texto"
//                              + " \n algo raro ha ocurrido");
//                    }
//                }
//            }
//
//            if(general){
//                if(fechaIni == null && fechaFinal == null){          
//
//                    try{
//                        fechaIni = LocalDateTime.of(fechaInicio.getValue(), horaInicio.getValue()); 
//                    }catch(Exception es){
//                        general = false;
//                        if(es.getMessage().equals("date")){
//                            not.error("ERROR CAMPO FECHA INICIO VACIO", "Selecciona una fecha");
//                        }
//                        if(es.getMessage().equals("time")){
//                            not.error("ERROR CAMPO HORA INICIO VACIO", "Selecciona una hora");
//                        }
//
//                    }
//
//                    try{
//                        fechaFinal = LocalDateTime.of(fechaFin.getValue(), horaFin.getValue());  
//                    }catch(Exception es){
//                        general = false;
//                        if(es.getMessage().equals("date")){
//                            not.error("ERROR CAMPO FECHA FIN VACIO", "Selecciona una fecha");
//                        }
//                        if(es.getMessage().equals("time")){
//                            not.error("ERROR CAMPO HORA FIN VACIO", "Selecciona una hora");
//                        }
//
//                    }
//                    if(fechaFinal.isBefore(fechaIni)){
//                        general = false;
//                        not.error("ERROR AL SELECCIONAR LA FECHA FIN", 
//                                "No se puede selccionar una fecha final anterior a la fecha inicial");
//                    }
//
//                }
//            }
//
//            precio = actividad.getPrecio() * numPlazas;
//            
//        } catch(Exception ex){
//            general = false;
//
//        }
//        
//        return general;
//    }
 

// ---------------------------- BORRAR --------------------------------    

    private void eliminar(){
        boolean ok = false;
        int id;
        
        experiencia = tableView.getSelectionModel().getSelectedItem();
        if(experiencia != null){
            id = experiencia.getId();

            ok = not.alertWarningDelete("SE ELIMINARA EL REGISTRO " + id + " EN LA TABLA EXPERIENCIAS",
                    "¿ESTAS SEGURO !!! ?");

            if(ok){
                try {
                    experienDAO.borrarExperiencia(id);
                    if(ok){
                        not.confirm("SE HA ELIMINADO EL REGISTRO " + id + " EN LA TABLA EXPERIENCIAS", 
                        " Operación realizada con éxito");
                    }
                } catch (SQLException ex) {
                    not.error("ERROR", "Error al eliminar el registro "+ id + " de tabla experiencias");
                }
            }
        }
        else{
            not.error("ERROR NO HAY UNA EXPERIENCIA SELECCIONADA",
                    " Selecciona una experiencia primero");
        }
    }
  
    private void eliminarActividadExperiencia(){
        int id, orden;
        boolean ok = false;
        
        actExperiencia = tableListaExperiencias.getSelectionModel().getSelectedItem();
        if(actExperiencia != null){
            id = actExperiencia.getIdExperiencia();
            orden = actExperiencia.getOrden();

                ok = not.alertWarningDelete("SE ELIMINARA EL REGISTRO CON NUMERO DE ORDEN " + orden + "",
                    "¿ESTAS SEGURO !!! ?");

            if(ok){
                try {
                     delete = eaDAO.eliminarActividadExperiencia(orden, id);
                    if(ok){
                        not.confirm("SE HA ELIMINADO EL REGISTRO " + orden + " EN LA TABLA EXPERIENCIA ACTIVIDAD", 
                        " Operación realizada con éxito");
                    }
                } catch (SQLException ex) {
                    not.error("ERROR", "Error al eliminar el registro "+ orden + " de tabla experiencia actividad");
                }
            }
        }
        else{
            not.error("ERROR NO HAY UNA EXPERIENCIA ACTIVIDAD SELECCIONADA", "Selecciona un registro primero");
        }
    }
    
    
// ---------------------------- LISTAR -----------------------------------
    
    
    private void listar(){
        List<Experiencia> lista = new ArrayList<>();
        
        try{
            
            lista = experienDAO.consultarTodasExperiencias();
            
            obExperiencias.clear();
            obExperiencias.addAll(lista);
            
            if(radioId.isSelected()){
               this.ordenarId();
            }
            if(radioNombre.isSelected()){
                this.ordenarNombre();
            }
            if(radioFechaTope.isSelected()){
                this.ordenarFechaTope();
            }
            
            
            tableView.setItems(obExperiencias);
            
            tb_idExperiencia.setCellValueFactory(new PropertyValueFactory<>("id"));
            tb_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tb_descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            tb_fechaTope.setCellValueFactory(new PropertyValueFactory<>("fechaTopeValidez"));
            tb_foto.setCellValueFactory(new PropertyValueFactory<>("foto"));
            tb_idUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
            
        } catch(SQLException es){
            not.error("ERROR SQL", "Error al listar informacion de experiencias");
        }
    }
    
    
    
    private void listarActividadExperiencia(List<ActividadExperiencia> listaDos){
        obActiviEncias.clear();
        obActiviEncias.addAll(listaDos);
        
        if(radioOrden.isSelected()){
            this.ordenarOrden();
        }
        if(radioFechaInicio.isSelected()){
            this.ordenarFechaInicio();
        }
        if(radioPrecio.isSelected()){
            this.ordenarPrecio();
        }
        
        
        tableListaExperiencias.setItems(obActiviEncias);

        tb_orden.setCellValueFactory(new PropertyValueFactory<>("orden"));
        tb_idExperi.setCellValueFactory(new PropertyValueFactory<>("idExperiencia"));
        tb_idActividad.setCellValueFactory(new PropertyValueFactory<>("actividad"));
        tb_fechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        tb_fechaFinal.setCellValueFactory(new PropertyValueFactory<>("fechaFinal"));
        tb_precio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tb_numPlazas.setCellValueFactory(new PropertyValueFactory<>("numPlazas"));        
        
    }
    
    
// -------------------------- SELECCIONAR ITEM -----------------------------

    
    private void seleccionarItem(){
        int id, idUsuario;
        String nombre,descripcion,foto;
        LocalDate fechaTope;
        List<ActividadExperiencia> listaDos = new ArrayList<>();
        
        
        experiencia = tableView.getSelectionModel().getSelectedItem();
        if(experiencia != null){
            id = experiencia.getId();
            nombre = experiencia.getNombre();
            descripcion = experiencia.getDescripcion();
            fechaTope = experiencia.getFechaTopeValidez();
            foto = experiencia.getFoto();
            idUsuario = experiencia.getIdUsuario();

            textNombre.setText(nombre);
            textDescripcion.setText(descripcion);
            fechaTopeValidez.setValue(fechaTope);
            textFoto.setText(foto);
            textUsuario.setText(String.valueOf(idUsuario));

            try {
                if(foto != null){
                    imageView.setVisible(true);
                    imageView.setImage(new Image("Imagenes/" + foto));
                    imageView.setFitWidth(275);
                    imageView.setFitHeight(250);
                    imageView.setPreserveRatio(false);
                }
                else {
                    imageView.setVisible(true);                
                }

                listaDos = eaDAO.consultarActividadesDeExperiencia(id);

            } catch(Exception ex){
                not.error("ERROR LA IMAGEN NO EXISTE EN EL ARCHIVO DE IMAGENES", 
                        "No encuentra la ruta de esa imagen");
            } 
        }
        
        
        listarActividadExperiencia(listaDos);
    }
    
    private void seleccionarItemActividaExperiencia(){
        int numOrden, idExperienciaLista,numPlazas;
        double precio;
        Actividad actividad;
        LocalDateTime fechaInicial, fechaFinal ;
        LocalDate f_ini,f_fin;
        LocalTime h_ini, h_fin;
              
        actExperiencia = tableListaExperiencias.getSelectionModel().getSelectedItem();
        
//        if(actExperiencia != null){
//        double precioAUX = actExperiencia.getPrecio();
//        int numPlazasAUX = actExperiencia.getNumPlazas();
//        double precioUnitario = precioAUX / numPlazasAUX;
        
        if(actExperiencia != null){
            numOrden = actExperiencia.getOrden();
            idExperienciaLista = actExperiencia.getIdExperiencia();
            actividad = actExperiencia.getActividad();
            fechaInicial = actExperiencia.getFechaInicio();
            f_ini = fechaInicial.toLocalDate();
            h_ini = fechaInicial.toLocalTime();
            fechaFinal = actExperiencia.getFechaFinal();
            f_fin = fechaInicial.toLocalDate();
            h_fin = fechaInicial.toLocalTime();
//            precio = actExperiencia.getPrecio();            
            numPlazas = actExperiencia.getNumPlazas();
            precio = actividad.getPrecio() * numPlazas;

            actExperiencia = new ActividadExperiencia(numOrden,idExperienciaLista,actividad,fechaInicial,fechaFinal,precio,numPlazas);

            textOrden.setText(String.valueOf(numOrden));
            textIdExperiencia.setText(String.valueOf(idExperienciaLista));
            textIdActividad.setText(String.valueOf(actividad.getId()));
            fechaInicio.setValue(f_ini);
            horaInicio.setValue(h_ini);
            fechaFin.setValue(f_fin);
            horaFin.setValue(h_fin);
            
            textPrecio.setText(String.valueOf(precio));
            textNumPlazas.setText(String.valueOf(numPlazas)); 
        }
        
    }

    
// ------------------------- LIMPIAR ------------------------------------   
    
    @FXML
    private void limpiar(){
        textExperiencia.clear();
        textNombre.clear();
        textDescripcion.clear();
        fechaTopeValidez.setValue(null);
        textFoto.clear();
        textUsuario.clear();
        textNombre.setStyle("-fx-background-color: white");
        textFecha.setStyle("-fx-background-color: white");
        textFoto.setStyle("-fx-background-color: white");
        textUsuario.setStyle("-fx-background-color: white");
        
        
        textOrden.clear();
        textIdExperiencia.clear();
        textIdActividad.clear();
        textFechaInicio.clear();
        textFechaFinal.clear();
        fechaInicio.setValue(null);
        horaInicio.setValue(null);
        fechaFin.setValue(null);
        horaFin.setValue(null);
        textPrecio.clear();
        textNumPlazas.clear();
        
        imageView.setVisible(false);
    }
    
    private void limpiarActividadExperiencia(){
        textOrden.clear();
        textIdExperiencia.clear();
        textIdActividad.clear();
        textFechaInicio.clear();
        textFechaFinal.clear();
        fechaInicio.setValue(null);
        horaInicio.setValue(null);
        fechaFin.setValue(null);
        horaFin.setValue(null);
        textPrecio.clear();
        textNumPlazas.clear();
    }
    
    
// ------------------------- EVENTOS -------------------------------------   

    @FXML
    private void anadir(ActionEvent event) {
        insertar();
        listar();
    }

    @FXML
    private void modificar(ActionEvent event) {
        actualizar();
        listar();
//        limpiar();
    }

    @FXML
    private void borrar(ActionEvent event) {
        eliminar();
        listar();
    }

    @FXML
    private void ver(ActionEvent event) {
        listar();
    }

    @FXML
    private void mostrar(MouseEvent event) {
        seleccionarItem();
    }
    
    @FXML
    private void mostrarActividadExperiencia(MouseEvent event) {
        seleccionarItemActividaExperiencia();
    }
    

    private void limpiar(ActionEvent event) {
        limpiar();
    }

    @FXML
    private void añadirNueva(ActionEvent event) {
        insertarActividadExperiencia();
        seleccionarItem();

    }

    @FXML
    private void modificarNueva(ActionEvent event) {
        modificarActividadExperiencia();
        seleccionarItem();
        limpiarActividadExperiencia();
    }

    @FXML
    private void borrarNueva(ActionEvent event) {
        eliminarActividadExperiencia();
        seleccionarItem();
        limpiarActividadExperiencia();
    }

    @FXML
    private void ordenarEx(ActionEvent event) {
        listar();
    }

    @FXML
    private void ordenarAcEx(ActionEvent event) {
        seleccionarItem();
    }

    
}
