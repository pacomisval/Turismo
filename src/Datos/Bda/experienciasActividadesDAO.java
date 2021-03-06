/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos.Bda;

import Modelo.Actividad;
import Modelo.ActividadExperiencia;
import Modelo.Subtipo;
import Modelo.Tipo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class experienciasActividadesDAO {

    private GestionBD gestion;
    private Connection conn;

    public experienciasActividadesDAO(GestionBD gestion) {
        this.gestion = gestion;
        this.conn = gestion.getConn();
    }

    public boolean insertarActividadExperiencia(ActividadExperiencia actExp) throws SQLException {
        boolean insertado;
        String consulta = "INSERT INTO experiencia_actividad (orden, idexperiencia, idactividad, fechaInicio, fechaFinal, precio, numPlazas) VALUES(?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(consulta);
        ps.setInt(1, actExp.getOrden());
        ps.setInt(2, actExp.getIdExperiencia());
        ps.setInt(3, actExp.getActividad().getId());
        ps.setTimestamp(4, Timestamp.valueOf(actExp.getFechaInicio()));
        ps.setTimestamp(5, Timestamp.valueOf(actExp.getFechaFinal()));
        ps.setDouble(6, actExp.getPrecio());
        ps.setInt(7, actExp.getNumPlazas());
        ps.executeUpdate();
        insertado = true;
        return insertado;
    }

    public List<ActividadExperiencia> consultarActividadesDeExperiencia(int idExperiencia) throws SQLException {
        List<ActividadExperiencia> listaActividadesExperiencia = new ArrayList<>();
        String sql = "SELECT orden, idExperiencia, idActividad,fechaInicio, fechaFinal, precio, numPlazas FROM experiencia_actividad WHERE idExperiencia = ? ORDER BY idExperiencia, Orden;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idExperiencia);
        ResultSet rs = ps.executeQuery();
        actividadesDAO actividadesDAO = new actividadesDAO(gestion);
        while (rs.next()) {
            listaActividadesExperiencia.add(
                    new ActividadExperiencia(rs.getInt("orden"),
                            rs.getInt("idExperiencia"),
                            actividadesDAO.consultarActividad(rs.getInt("idActividad")),
                            rs.getTimestamp("fechaInicio").toLocalDateTime(),
                            rs.getTimestamp("fechaFinal").toLocalDateTime(),
                            rs.getDouble("precio"),
                            rs.getInt("numPlazas")));
        }
        return listaActividadesExperiencia;
    }

    public boolean modificarActividadExperiencia(int orden, int idExperiencia, Actividad idActividad, LocalDateTime fechaInicio, LocalDateTime fechaFinal, Double precio, int numPlazas) throws SQLException {
        boolean modificado = false;

        if (conn != null) {
            String consulta = "UPDATE experiencia_actividad SET orden = ?, idExperiencia = ?, idActividad = ?,fechaInicio = ?, fechaFinal = ?, precio = ?, numPlazas = ?  WHERE idExperiencia = ? AND orden = ?";
            PreparedStatement ps = conn.prepareStatement(consulta);
            ps.setInt(1, orden);
            ps.setInt(2, idExperiencia);
            ps.setInt(3, idActividad.getId());
            ps.setTimestamp(4, Timestamp.valueOf(fechaInicio));
            ps.setTimestamp(5, Timestamp.valueOf(fechaFinal));
            ps.setDouble(6, precio);
            ps.setInt(7, numPlazas);
            ps.setInt(8, idExperiencia);
            ps.setInt(9, orden);
            ps.executeUpdate();

            modificado = true;
        }

        return modificado;
    }

    public boolean eliminarActividadExperiencia(int orden, int idExperiencia) throws SQLException {
        boolean eliminado = false;

        if (conn != null) {
            String consulta = "DELETE FROM experiencia_actividad WHERE orden = ? AND idExperiencia = ?;";

            PreparedStatement ps = conn.prepareStatement(consulta);
            ps.setInt(1, orden);
            ps.setInt(2, idExperiencia);
            ps.executeUpdate();

            eliminado = true;
        }

        return eliminado;
    }

    public List<ActividadExperiencia> consultarAgenda(LocalDate fechaInicial, LocalDate fechaFinal, Tipo tipo, Subtipo subtipo) throws SQLException {
        Date inicioPS = null;
        Date finalPS = null;

        if (fechaInicial == null) {
            inicioPS = null;
        } else {
            inicioPS = Date.valueOf(fechaInicial);
        }
        if (fechaFinal == null) {
            finalPS = null;
        } else {
            finalPS = Date.valueOf(fechaFinal);
        }

        String tipoInsert;
        String subTipoInsert;
        if (tipo == null) {
            tipoInsert = null;
        } else {
            tipoInsert = tipo.getNombre();
        }
        if (subtipo == null) {
            subTipoInsert = null;
        } else {
            subTipoInsert = subtipo.getNombre();
        }
        System.out.println("el insert");
        System.out.println(inicioPS + " - " + finalPS + " - " + tipoInsert + " - " + subTipoInsert);
        List<ActividadExperiencia> lista = new ArrayList<>();
        String sql = "call agendaDelDia(?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDate(1, inicioPS);
        ps.setDate(2, finalPS);
        ps.setString(3, tipoInsert);
        ps.setString(4, subTipoInsert);
        ResultSet rs = ps.executeQuery();
        actividadesDAO actividadesDAO = new actividadesDAO(gestion);

        while (rs.next()) {
            lista.add(
                    new ActividadExperiencia(rs.getInt("orden"),
                            rs.getInt("idExperiencia"),
                            actividadesDAO.consultarActividad(rs.getInt("idActividad")),
                            rs.getTimestamp("fechaInicio").toLocalDateTime(),
                            rs.getTimestamp("fechaFinal").toLocalDateTime(),
                            rs.getDouble("precio"),
                            rs.getInt("numPlazas")));
        }
        return lista;
    }
}
