/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista.Administrador.Agenda;

import Datos.Bda.GestionBD;
import Datos.Bda.experienciasActividadesDAO;
import Datos.Bda.experienciasDAO;
import Datos.Bda.subtiposDAO;
import Datos.Bda.tiposDAO;
import Modelo.ActividadExperiencia;
import Modelo.Notificacion;
import Modelo.Subtipo;
import Modelo.Tipo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author 20857464y
 */
public class AgendaController implements Initializable {

    private GestionBD gestion;
    private Notificacion not;
    experienciasActividadesDAO actexpDAO;
    tiposDAO tipDAO;
    subtiposDAO subDAO;
    List<ActividadExperiencia> listaActividades;

    @FXML
    private AnchorPane anchorP;
    @FXML
    private JFXDatePicker datePickerInicio;
    @FXML
    private JFXDatePicker datePickerFinal;
    @FXML
    private JFXButton botonComprobarAgenda;
    @FXML
    private TableView<ActividadExperiencia> tablaActividadesAgenda;
    @FXML
    private JFXComboBox<Tipo> comboTipo;
    @FXML
    private JFXComboBox<Subtipo> comboSubtipo;
    @FXML
    private TableColumn<ActividadExperiencia, Integer> columnaIdExperiencia;
    @FXML
    private TableColumn<ActividadExperiencia, String> columnaActividad;
    @FXML
    private TableColumn<ActividadExperiencia, LocalDateTime> columnaInicio;
    @FXML
    private TableColumn<ActividadExperiencia, LocalDateTime> columnaFinal;
    @FXML
    private TableColumn<ActividadExperiencia, Integer> columnaPlazas;
    @FXML
    private TableColumn<ActividadExperiencia, Double> columnaPrecio;
    @FXML
    private Pane paneAgenda;
    @FXML
    private Label diaInicioL;
    @FXML
    private Label diaFinalL;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stylesInicio();
    }

    private void stylesInicio() {
        diaInicioL.getStyleClass().add("labelAgenda");
        diaFinalL.getStyleClass().add("labelAgenda");
        paneAgenda.getStyleClass().add("paneAgenda");
        datePickerFinal.getStyleClass().add("dateAgenda");
        datePickerInicio.getStyleClass().add("dateAgenda");
        comboSubtipo.getStyleClass().add("combo");
        comboTipo.getStyleClass().add("combo");
        botonComprobarAgenda.getStyleClass().add("botonAgenda");
    }

    public void setGestion(GestionBD gestion) {
        this.gestion = gestion;
        inicio();
    }

    private void inicio() {
        not = new Notificacion();
        anchorP.getStyleClass().add("fondoAdminAgenda");
        listaActividades = new ArrayList<>();

        actexpDAO = new experienciasActividadesDAO(gestion);
        tipDAO = new tiposDAO(gestion);
        subDAO = new subtiposDAO(gestion);

        LocalDate minDate = LocalDate.now();
        datePickerInicio.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(minDate));
            }
        });
        datePickerFinal.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(minDate));
            }
        });
        cargarTipos();
    }

    @FXML
    private void cargarActividades(ActionEvent event) {
        LocalDate fechaInicio = datePickerInicio.getValue();
        LocalDate fechaFinal = datePickerFinal.getValue();

        Tipo tipo = null;
        Subtipo subtipo = null;
        if ((fechaInicio != null) && (fechaFinal != null))  {
            int compararFechas = fechaFinal.compareTo(fechaInicio);
            if(compararFechas > 0){
            System.out.println("Efectivamente ha entrado aqui");
            try {
                tipo = comboTipo.getValue();
            } catch (Exception e) {
                not.error("Error", "No ha podido coger el tipo");
            }
            try {
                subtipo = comboSubtipo.getValue();
            } catch (Exception e) {
                not.error("Error", "No ha podido coger el subtipo");
            }
            listaActividades.clear();
            try {
                for (ActividadExperiencia act : actexpDAO.consultarAgenda(fechaInicio, fechaFinal, tipo, subtipo)) {
                    listaActividades.add(act);
                }
            } catch (SQLException e) {
                not.error("Error", "No se han podido cargar la actividades de la agenda");
            }
            cargarTabla();
            }else{
                not.alert("Error", "La fecha final debe ser posterior a la inicial");
            }
        }else{
            not.alert("Error", "Debes introducir una fecha de inicio y una fecha final");
        }
    }

    private void cargarTabla() {
        tablaActividadesAgenda.getItems().clear();
        for (ActividadExperiencia act : listaActividades) {
            tablaActividadesAgenda.getItems().add(act);
        }
        columnaIdExperiencia.setCellValueFactory(new PropertyValueFactory<>("idExperiencia"));
        columnaActividad.setCellValueFactory(new PropertyValueFactory<>("Actividad"));
        columnaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        columnaFinal.setCellValueFactory(new PropertyValueFactory<>("fechaFinal"));
        columnaPlazas.setCellValueFactory(new PropertyValueFactory<>("numPlazas"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
    }

    private void cargarTipos() {
        try {
            for (Tipo tipo : tipDAO.consultarTipos()) {
                comboTipo.getItems().add(tipo);
            }
        } catch (SQLException e) {
            not.error("Error", "No se han podido cargar los tipos");
        }
    }

    @FXML
    private void cargarActividadesTipo(ActionEvent event) {
        comboSubtipo.getItems().clear();
        try {
            for (Subtipo subtipo : subDAO.consultarSubtiposporTipo(comboTipo.getSelectionModel().getSelectedItem())) {
                comboSubtipo.getItems().add(subtipo);
            }
        } catch (SQLException ex) {
            not.error("Error", "No se han podido cargar los subtipos del tipo seleccionado");
        }
    }

}
