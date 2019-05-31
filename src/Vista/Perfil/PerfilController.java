package Vista.Perfil;

import Datos.Bda.GestionBD;
import Datos.Bda.usuariosDAO;
import Modelo.Notificacion;
import Modelo.Usuario;
import Modelo.ValidarCampos;
import Vista.Administrador.Principal.PrincipalAdminController;
import Vista.CambiarContra.CambiarContraController;
import Vista.Principal.PrincipalController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joser
 */
public class PerfilController implements Initializable {

    private Notificacion not;
    private GestionBD gestion;
    private usuariosDAO usuarioDAO;
    private PrincipalController principalController;
    private File fotoFile;
    private JFXPasswordField contraPF;
    private String foto;
    private boolean selecionarFoto;
    private final String MAL = " -fx-text-fill:red";
    private final String CORRECTO = " -fx-text-fill: rgb(56, 175, 88)"; //Verde
    private String nick, nombre, apellidos, dni, telefono, direccion, email;
    private LocalDate fecNac;
    private PrincipalAdminController controlador;
    private ValidarCampos validarCampos;

    @FXML
    private AnchorPane Ventana;
    @FXML
    private PasswordField repContraPF;
    @FXML
    private RadioButton clienteRB;
    @FXML
    private ToggleGroup rolUsuRB;
    @FXML
    private TextField nickTF;
    @FXML
    private TextField nombreTF;
    @FXML
    private TextField apellidosTF;
    @FXML
    private TextField dniTF;
    @FXML
    private TextField telefonoTF;
    @FXML
    private TextField direccionTF;
    @FXML
    private TextField emailTF;
    @FXML
    private JFXDatePicker fecNacTF;
    private Usuario usuario;
    @FXML
    private Button modificarBT;
    @FXML
    private Label labelUser;
    @FXML
    private ImageView caraIV;
    @FXML
    private Pane alFrenteAP;
    @FXML
    private Pane barra;
    @FXML
    private Pane paneBienvenido;
    @FXML
    private Button botonGuardar;
    @FXML
    private JFXButton cancelarBT;
    @FXML
    private Label labelPW;
    @FXML
    private Label nickL;
    @FXML
    private Label dniL;
    @FXML
    private Label telefL;
    @FXML
    private Label emailL;
    @FXML
    private Label nombreL;
    @FXML
    private Label apelliL;
    @FXML
    private Label fecNacL;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        not = new Notificacion();
        validarCampos = new ValidarCampos();
        usuarioDAO = new usuariosDAO(gestion);
        Image img = new Image("Imagenes/banner2.jpg");
        ImageView imagev = new ImageView(img);

        imagev.setFitHeight(230);
        imagev.setFitWidth(1300);
        Ventana.getChildren().add(imagev);
        Ventana.toBack();
        alFrenteAP.getStyleClass().add("panePerfilPersonal");
        barra.getStyleClass().add("barraPerfil");
        paneBienvenido.getStyleClass().add("paneBienv");
        cancelarBT.getStyleClass().add("botonCerrarInformacion");
        botonGuardar.getStyleClass().add("botonGuardarModificacion");
        modificarBT.getStyleClass().add("botonEditar");
        labelPW.getStyleClass().add("recordarpassword");
        alFrenteAP.toFront();
        /// lo pasamos a cuando este desabilitado editar
        ///caraIV.setOnMouseClicked(event -> seleccionFoto());

        botonGuardar.setVisible(false);
        cancelarBT.setVisible(false);
        labelPW.setVisible(false);

        botonGuardar.setVisible(false);
        cancelarBT.setVisible(false);

    }

    /**
     * Muestra la foto que seleccionamos del explorador de archivos
     */
    private void selectFoto() {
        selecionarFoto = true;
        fotoFile = usuario.selectFile();
        if (fotoFile != null) {
            Image image = new Image(fotoFile.toURI().toString());
            caraIV.setImage(image);
            caraIV.setPreserveRatio(false);
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Muestra los valores del usuario en las etiquetas y foto
     */
    public void calcularnodos() {
//        usuarioDAO = new usuariosDAO(gestion);
        nickTF.setText(usuario.getNick());
        nombreTF.setText(usuario.getNombre());
        apellidosTF.setText(usuario.getApellidos());
        dniTF.setText(usuario.getDni());
        telefonoTF.setText(usuario.getTelefono());
        direccionTF.setText(usuario.getDireccion());
        emailTF.setText(usuario.getEmail());
        fecNacTF.setValue(usuario.getFecNac());
        labelUser.setText(usuario.getNick().toUpperCase());
        cargaFoto();
    }

    /**
     * Modifica los valores de los atributos del usuario, tambien en la BD
     * Comprueba que los valores introducidos puedan ser modificados en la BD
     *
     * @param event
     *
     */
    @FXML
    private void modificar(ActionEvent event) {
        boolean modiNick = false;
        boolean modiFoto = false;
        boolean todoCorrecto = true;
        boolean correctoSQL = true;
        nick = nickTF.getText();
        nombre = nombreTF.getText();
        apellidos = apellidosTF.getText();
        dni = dniTF.getText();
        fecNac = fecNacTF.getValue();
        telefono = telefonoTF.getText();
        direccion = direccionTF.getText();
        email = emailTF.getText();

        int id = usuario.getId();

        /////COMPARAMOS SI HAY CAMBIOS, cambiamos en BD y cambiamos en USUARIO
        try {
//---NICK
            if (!usuario.getNick().equals(nick)) {

                //Miramos que sea valido
                if ((usuarioDAO.clienteExiste(nick) == true) || nickTF.getText().equals("")) {
                    todoCorrecto = false;
                    nickTF.setText("");
                    if (nickTF.getText().equals("")) {
                        nickTF.setPromptText("*  ESTE CAMPO ES OBLIGATORIO");
                    } else {
                        nickTF.setPromptText("Ese usuario ya existe");
                    }
                    nickL.setStyle(MAL);
                } else {
                    nickL.setStyle(CORRECTO);
                    modiNick = usuarioDAO.modificarNick(nick, id);
                    if (!modiNick) {
                        correctoSQL = false;
                    }
                }

            }
            //// Otras consecuencias del cambios de Nick
            if (modiNick) {

                //**cambiar nombre foto    
                String oldFile = usuario.getFoto();
                String ruta = System.getProperty("user.dir");
                File viejoFile = new File(ruta + oldFile);
                usuario.setNick(nick);

                String[] ext = oldFile.split("\\.");
                String newFile = nick + "." + (ext[ext.length - 1]);
                File nuevoFile = new File(ruta + newFile);
                viejoFile.renameTo(nuevoFile);

                //**cambios en vista    
                labelUser.setText(nick.toUpperCase());
                if (principalController != null) {
                    principalController.cargaNick();
                    principalController.cargaFoto();
                }
                if (controlador != null) {
                    controlador.cargaNick();
                    controlador.cargaFoto();
                }

            }
//fin NICK  ------------

//---NOMBRE
            if (!usuario.getNombre().equals(nombre)) {
                if (nombre.equals("")) {
                    todoCorrecto = false;
                    nombreL.setStyle(MAL);
                    nombreTF.setPromptText("*  ESTE CAMPO ES OBLIGATORIO");
                } else {
                    boolean modiNombre = usuarioDAO.modificarNombre(nombre, id);
                    if (modiNombre) {
                        usuario.setNombre(nombre);
                    } else {
                        correctoSQL = false;
                    }
                }
            }

//---APELLIDOS
            if (!usuario.getApellidos().equals(apellidos)) {
                if (apellidos.equals("")) {
                    todoCorrecto = false;
                    apelliL.setStyle(MAL);
                    apellidosTF.setPromptText("*  ESTE CAMPO ES OBLIGATORIO");
                } else {
                    boolean modificado = usuarioDAO.modificarApellidosTF(apellidos, id);
                    if (modificado) {
                        usuario.setApellidos(apellidos);
                    } else {
                        correctoSQL = false;
                    }
                }
            }
//---DNI
            if (!usuario.getDni().equals(dni)) {
                if (validarCampos.comprobardni(dni) == false || dni.equals("")) {
                    todoCorrecto = false;
                    dniTF.setText("");
                    dniTF.setPromptText("Intoduce un DNI valido");
                    dniL.setStyle(MAL);
                } else {
                    dniL.setStyle(CORRECTO);
                    boolean modificado = usuarioDAO.modificarDni(dni, id);
                    if (modificado) {
                        usuario.setDni(dni);
                    } else {
                        correctoSQL = false;
                    }
                }
            }
//---FECHA NACIMIENTO
            if (!usuario.getFecNac().equals(fecNac) || fecNac == null) {
                todoCorrecto = false;
                fecNacL.setStyle(MAL);
                fecNacTF.setPromptText("*  ESTE CAMPO ES OBLIGATORIO");
            } else {
                boolean modificado = usuarioDAO.modificarFecNac(fecNac, id);
                if (modificado) {
                    usuario.setFecNac(fecNac);
                } else {
                    correctoSQL = false;
                }
            }

//---TELEFONO
            if (!usuario.getTelefono().equals(telefono)) {
                if (validarCampos.comprobarTelefono(telefono) == false || telefono.equals("")) {
                    todoCorrecto = false;
                    telefonoTF.setText("");
                    telefonoTF.setPromptText("Intoduce un Número valido");
                    telefL.setStyle(MAL);
                } else {
                    telefL.setStyle(CORRECTO);
                    boolean modificado = usuarioDAO.modificarTelefono(telefono, id);
                    if (modificado) {
                        usuario.setTelefono(telefono);
                    } else {
                        correctoSQL = false;
                    }
                }
            }
//---DIRECCION
            if (!usuario.getDireccion().equals(direccion)) {
                boolean modificado = usuarioDAO.modificarDireccion(direccion, id);
                if (modificado) {
                    usuario.setDireccion(direccion);
                } else {
                    correctoSQL = false;
                }
            }
//---EMAIL
            if (!usuario.getEmail().equals(email)) {
                if (validarCampos.comprobarEmail(email) == false || email.equals("")) {
                    todoCorrecto = false;
                    emailTF.setText("");
                    emailTF.setPromptText("Intoduce un Email valido");
                    emailL.setStyle(MAL);
                } else {
                    emailL.setStyle(CORRECTO);
                    boolean modificado = usuarioDAO.modificarEmail(email, id);
                    if (modificado) {
                        usuario.setEmail(email);
                    } else {
                        correctoSQL = false;
                    }
                }
            }
//---FOTO   
            if (selecionarFoto) {
                usuario.setFotoFile(fotoFile);
                foto = usuario.renombrarFoto();  ///pasar fotofile a string
                usuario.setFoto(foto);
                modiFoto = usuarioDAO.modificarFoto(foto, id);
                if (!modiFoto) {
                    correctoSQL = false;
                }
            }
//// Otras consecuencias del cambios de Foto para que se actualice la imagen del menu
            if (modiFoto) {
                // guardar foto nueva
                usuario.guardarFoto();
                //cambios en vista        
                if (principalController != null) {
                    principalController.cargaFoto();
                } else {
                    controlador.cargaFoto();
                }
            }
//////INFORMAR DEL RESULTADO  
            if (todoCorrecto && correctoSQL) {
                not.confirm("Modificar", "Ha sido modificado con exito");
            } else {
                not.error("Modificar", "NO ha sido posible modificar");
            }
        } catch (SQLException ex) {
            not.error("SQL", "NO ha sido posible modificar los datos");
        } catch (IOException ex) {

            not.error("Imagen", "No hemos  podido cargar su foto");
        }

    }

    public void setGestion(GestionBD gestion) {
        this.gestion = gestion;
    }

    /**
     * Muestra la foto del usuario La busca en el disco por si es de nueva
     * creacion y sino en el package
     */
    private void cargaFoto() {
        try {
            caraIV.setImage(new Image("file:///C:/tempTurismo/" + usuario.getFoto()));

        } catch (Exception e) {
            try {
                caraIV.setImage(new Image("Imagenes/usuarios/" + usuario.getFoto()));

            } catch (Exception ex) {
                caraIV.setImage(new Image("Imagenes/usuarios/avatar.png"));
            }
        }
    }

    public void setcontroler(PrincipalController principalController) {
        this.principalController = principalController;
    }

    public void setcontrolador(PrincipalAdminController controlador) {
        this.controlador = controlador;
    }

    @FXML
    private void editarPerfil(ActionEvent event) {
        alFrenteAP.getStyleClass().clear();
        botonGuardar.setVisible(true);
        cancelarBT.setVisible(true);
        labelPW.setVisible(true);
        nickTF.setEditable(true);
        nombreTF.setEditable(true);
        apellidosTF.setEditable(true);
        dniTF.setEditable(true);
//        fecNacTF.setEditable(true);
        direccionTF.setEditable(true);
        emailTF.setEditable(true);
        alFrenteAP.getStyleClass().add("panePerfilPersonalEditable");
        editarFoto();
    }

    private void editarFoto() {
        caraIV.setOnMouseClicked(event -> selectFoto());
    }
/**
 * Cancela la edicion; recarga los valores y no permite la edicion
 * @param event 
 */
    @FXML
    private void cancelar(ActionEvent event) {
        alFrenteAP.getStyleClass().clear();
        alFrenteAP.getStyleClass().add("panePerfilPersonal");
        botonGuardar.setVisible(false);
        cancelarBT.setVisible(false);
        labelPW.setVisible(false);

        nickTF.setEditable(false);
        nombreTF.setEditable(false);
        apellidosTF.setEditable(false);
        dniTF.setEditable(false);
        fecNacTF.setEditable(false);
        direccionTF.setEditable(false);
        emailTF.setEditable(false);

        nickTF.setStyle("");
        nombreTF.setStyle("");
        apellidosTF.setStyle("");
        dniTF.setStyle("");
        fecNacTF.setStyle("");
        emailTF.setStyle("");
        direccionTF.setStyle("");
        telefonoTF.setStyle("");
        calcularnodos();

    }

    @FXML
    private void cambiarPassword(MouseEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Vista/CambiarContra/CambiarContra.fxml"));
            root = loader.load();  // el metodo initialize() se ejecuta

            //OBTENER EL CONTROLADOR DE LA VENTANA
            CambiarContraController cambiarcontracontroller = loader.getController();
            cambiarcontracontroller.setParametros(usuarioDAO, usuario);

            Stage escena = new Stage();    //En Stage nuevo.                  
            escena.setTitle("Cambiar Contraseña");

            // NO PERMITE ACCESO A LA VENTANA PRINCIPAL
            escena.initModality(Modality.APPLICATION_MODAL);
            escena.getIcons().add(new Image("/Imagenes/iconos/lock.png"));
            escena.setScene(new Scene(root));
            escena.setResizable(false);
            escena.showAndWait();

            if (cambiarcontracontroller.conseguido()) {
                not.confirm("Enhorabuena", "La contraseña se ha guardado");
            }
        } catch (IOException ex) {
            not.error("ERROR", "Error al cargar la ventana");
        }
    }

}
