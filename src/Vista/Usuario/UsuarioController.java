package Vista.Usuario;

import Datos.Bda.GestionBD;
import Datos.Bda.usuariosDAO;
import Modelo.Correo;
import Modelo.Notificacion;
import Modelo.Usuario;
import Vista.Administrador.Principal.PrincipalAdminController;
import Vista.Principal.PrincipalController;
import Vista.Registrar.RegistrarController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import javax.mail.MessagingException;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;

/**
 * FXML Controller class
 *
 * @author Grupo4
 */
public class UsuarioController implements Initializable {

    private Label label;
    private Label nombreET;
    private Stage escenario;
    private TranslateTransition translatePrincipal;
    private TranslateTransition translateAgencia;
    private GestionBD gestion;
    private usuariosDAO usuarioDAO;
    private Notificacion not;
    private Usuario usuario;

    @FXML
    private PasswordField contraTF;
    @FXML
    private TextField nickTF;
    @FXML
    private AnchorPane fondoUsuario;
    @FXML
    private Label agencia;
    @FXML
    private Pane paneagencia;
    @FXML
    private Button botonLog;
    @FXML
    private Button botonReg;
    @FXML
    private Polygon triangle;
    @FXML
    private Pane paneInicio;
    @FXML
    private Pane paneCapaTriangulo;
    @FXML
    private Label olvidar;

//INICIO--------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gestion = new GestionBD();
        gestion.conectar();
        usuarioDAO = new usuariosDAO(gestion);
        usuario = new Usuario();
        not = new Notificacion();
        styleInicio();
    }

    private void styleInicio() {

        //Imagen fondo
        Image img = new Image("Imagenes/inicioprueba.jpg");
        ImageView imagev = new ImageView(img);
        fondoUsuario.getChildren().add(imagev);
        imagev.setFitHeight(800);
        imagev.setFitWidth(1300);

        //panes
        triangle.toFront();
        triangle.setOpacity(0.85);
        paneInicio.toFront();
        paneagencia.toFront();
        paneCapaTriangulo.toFront();

        //Estilos
        paneagencia.getStyleClass().add("paneAgencia");
        botonLog.getStyleClass().add("botoninicio");
        botonReg.getStyleClass().add("botoninicio");
        olvidar.getStyleClass().add("recordarpassword");
    }

//ACCIONES------------------------------------------------------------------
    @FXML
    private void logearse(ActionEvent event) throws InterruptedException {
        // Utilizar uno de estos tres metodos
        logearse();
        //logearseComoCliente();
        //logearseComoAdministrador();
    }

    /**
     * Abre ventana Registrarse al darle al boton registrarse
     *
     * @param event
     *
     */
    @FXML
    private void registrarse(ActionEvent event) {
        Parent root;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Vista/Registrar/Registrar.fxml"));
            root = loader.load();  // el meotodo initialize() se ejecuta

            //OBTENER EL CONTROLADOR DE LA VENTANA
            RegistrarController registrarController = loader.getController();
            registrarController.setUsuarioDAO(usuarioDAO);

            Stage escena = new Stage();    //En Stage nuevo.                  
            escena.setTitle("Registro");

            // NO PERMITE ACCESO A LA VENTANA PRINCIPAL
            escena.initModality(Modality.APPLICATION_MODAL);
            escena.getIcons().add(new Image("/Imagenes/iconos/note.png"));
            escena.setScene(new Scene(root));
            escena.showAndWait();
        } catch (IOException ex) {
            not.error("ERROR", "Error al intentar cargar ventana registro");
        }
    }

//VENTANAS------------------------------------------------------------------
    public void cargarVentanaPrincipal() {

//        escenario = (Stage) this.nickTF.getParent().getScene().getWindow();
        String nombrefichero = "/Vista/Principal/Principal.fxml";
        PrincipalController principalController;
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(nombrefichero));
            root = loader.load(); // el metodo initialize() se ejecuta
            principalController = loader.getController();

            //Pasamos informacion a la clase siguiente
            principalController.setGestion(gestion);
            principalController.setUsuario(usuario);
            principalController.setController(principalController);

            //Damos valores a los nodos antes de mostrarlos
            principalController.calcularNodos();

            escenario.setScene(new Scene(root));
            escenario.show();

        } catch (IOException ex) {
            not.error("ERROR", "Error al intentar cargar ventana Principal");
        }
    }

    private void cargarVentanaPrincipalAdmin() {

//        escenario = (Stage) this.nickTF.getParent().getScene().getWindow();
        String nombrefichero = "/Vista/Administrador/Principal/PrincipalAdmin.fxml";
        PrincipalAdminController principalAdminController;
        Parent root;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(nombrefichero));
            root = loader.load(); // el metodo initialize() se ejecuta
            principalAdminController = loader.getController();

            //Pasamos informacion a la clase siguiente
            principalAdminController.setGestion(gestion);
            principalAdminController.setParametroUsuario(usuario);

            //principalController.setUsuarioDAO(usuario, bda, cambiador);
            //Damos valores a los nodos antes de mostrarlos
            principalAdminController.calcularnodos();
            escenario.setScene(new Scene(root));
            escenario.show();

        } catch (IOException ex) {
            not.error("ERROR", "Error al intentar cargar ventana Principal Administrador");

        }
    }
//CONTROL-------------------------------------------------------------------

    /**
     * Comprueba si la contraseña introducida coincide con la de la BD
     *
     * @return
     */
    public boolean verificaUsuario() {
        PasswordEncryptor encryptor = new BasicPasswordEncryptor();
        boolean existe = false;
        boolean checkPassword;
        String nick = nickTF.getText();
        String contrasena = contraTF.getText();
        String contrasenaBD = null;
        try {
            contrasenaBD = usuarioDAO.obtenerContra(nick);
        } catch (SQLException ex) {
            not.alert("SQL", "No hemos podido acceder a tu contraseña");
        }
        checkPassword = encryptor.checkPassword(contrasena, contrasenaBD);
        return checkPassword;
    }

    /**
     * Da valor a los atributos del Usuario logeado abre la Ventana Principal
     * del rol del usuario
     */
    private void logearse() {
        escenario = (Stage) this.nickTF.getParent().getScene().getWindow();
        boolean logeado = verificaUsuario();        //Verifica que existe y contraseña correcta

        if (logeado) {
            try {
                usuario = usuarioDAO.cargarUsuario(nickTF.getText());

                // segun el roll ejecutará uno de los dos metodos
                if ("CLIENTE".equalsIgnoreCase(usuario.getPerfilString())) {

                    String rol = "cliente";
                    transicionPrincipal(rol);

                } else {

                    if ("ADMINISTRADOR".equalsIgnoreCase(usuario.getPerfilString())) {
                        String rol = "admin";
                        transicionPrincipal(rol);
                    } else {
                        not.error("Segun lorenzo soy tonto",
                                "en logearseBueno() --- UsuarioController");
                    }
                }
            } catch (SQLException ex) {
                not.error("ERROR SQL",
                        "en logearseBueno() --- UsuarioController");
            }
        } else {
            not.error("ERROR", "Usuario o contraseña incorrectos");
            nickTF.setText("");
            contraTF.setText("");
        }
    }

    @FXML
    private void habilitarBT(MouseEvent event) {
        //Hasta que pulsas para escribir los campos no se habilita el boton de login
        if (botonLog.isDisable()) {
            FadeTransition ft = new FadeTransition(Duration.millis(500), botonLog);
            ft.setFromValue(0.6);
            ft.setToValue(1);
            ft.play();

            botonLog.setDisable(false);
        }
    }

    /**
     * Efecto grafico antes de cargar la ventana principal acorde al rol
     *
     * @param rol
     */
    public void transicionPrincipal(String rol) {

        translatePrincipal = new TranslateTransition(Duration.seconds(1), paneCapaTriangulo);
        translateAgencia = new TranslateTransition(Duration.seconds(1), paneagencia);

        translateAgencia.setFromX(0);
        translateAgencia.setToX(600);

        translatePrincipal.setFromX(0);
        translatePrincipal.setToX(-1350);

        translatePrincipal.setInterpolator(Interpolator.LINEAR);
        translatePrincipal.play();
        translateAgencia.setInterpolator(Interpolator.LINEAR);
        translateAgencia.play();

        translatePrincipal.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (rol.equalsIgnoreCase("cliente")) {
                    cargarVentanaPrincipal();
                } else if (rol.equalsIgnoreCase("admin")) {
                    cargarVentanaPrincipalAdmin();
                }
            }
        });

    }

    /**
     * Si el correo introducido corresponde con el de la BD genera una
     * contraseña aleatoria y la manda por email.
     *
     * @param event
     *
     */
    @FXML
    private void recordarPass(MouseEvent event) {
        //Enviar un correo con una nueva contraseña
        not = new Notificacion();
        Pair<String, String> pareja = not.recordar();
        if ((!"".equals(pareja.getKey()))) {
            try {
                String email = usuarioDAO.DarCorreo(pareja.getKey());
                if (email != null) {
                    if (email.equals(pareja.getValue())) {
                        String newPass = (int) (Math.random() * 1000) + "";
                        String contra = usuario.encriptar(newPass);
                        usuarioDAO.introducirContra(contra, pareja.getKey());
                        Correo correo = new Correo();
                        correo.setparametros(pareja, newPass);
                        correo.mandarcorreo();
                        not.info("Email", "Revisa tu correo, te hemos enviado un mensaje");
                    } else {
                        not.alert("Email", "No coincide con el correo que tenemos de ti");
                    }
                }
            } catch (SQLException ex) {
                not.error("SQL", "Hemos tenido un problema al recuperar tu correo");
            } catch (MessagingException ex) {
                not.error("EMAIL", "Hemos tenido un problema al mandar tu email");
            }
        }
    }

//ATAJOS DE PROGRAMADOR-----------------------------------------------------
    private void logearseComoCliente() {
        cargarVentanaPrincipal();
    }

    private void logearseComoAdministrador() {
        cargarVentanaPrincipalAdmin();
    }
}
