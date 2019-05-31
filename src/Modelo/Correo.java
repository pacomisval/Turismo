package Modelo;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Pair;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author joser
 */
public class Correo {

    private final Properties properties = new Properties();

    private Session session;
    private Pair<String, String> pareja;
    private String newPass; //por si variamos el metodo send email
   
    private final String password = "dawGIAT4";

    public Correo() {
        
    }

    public void mandarcorreo() throws MessagingException {
      String emisor = "grupo4amsterdam@gmail.com";  //correo de la agencia
      String receptor=pareja.getValue();      /// pasar correo del usuario
      String contrasena="dawGIAT4";             // contraseña gmail
      String asunto="Agencia Turistica JOHANSA";
      String mensaje="Hola " + pareja.getKey() + " Tu nueva contraseña es "
              + newPass + " te recomendamos la cambies nada mas entrar en la aplicacion";
      
     sendEmail(emisor,receptor,contrasena,asunto,mensaje);
    }

    private void prepararEmision(String emisor) {
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.mail.sender", emisor);
        properties.put("mail.smtp.user", emisor);
        properties.put("mail.smtp.auth", "false");
        session = Session.getDefaultInstance(properties);
    }


    public void setparametros(Pair<String, String> pareja, String numero) {
        this.pareja = pareja;
        this.newPass = numero;
     
    }

    public void sendEmail(String emisor, String receptor, String contrasena, String asunto, String mensaje) throws AddressException, MessagingException {
        prepararEmision(emisor);
       
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor));
        message.setSubject(asunto);
        message.setText(mensaje);
        Transport transport = session.getTransport("smtp");
        transport.connect((String) properties.get("mail.smtp.user"),contrasena );
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
