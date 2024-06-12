package org.durias.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.durias.bean.Clientes;
import org.durias.db.Conexion;
import org.durias.report.GenerarReportes;
import org.durias.system.Principal;

public class MenuClientesController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Clientes> listaClientes;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TableColumn colApeC;
    @FXML
    private TableColumn colCodC;
    @FXML
    private TableColumn colCorreoC;
    @FXML
    private TableColumn colDirC;
    @FXML
    private TableColumn colNit;
    @FXML
    private TableColumn colNomC;
    @FXML
    private TableColumn colTelC;
    @FXML
    private TableView tblClientes;
    @FXML
    private TextField txtApellidoC;
    @FXML
    private TextField txtCodC;
    @FXML
    private TextField txtCorreoC;
    @FXML
    private TextField txtDirC;
    @FXML
    private TextField txtNit;
    @FXML
    private TextField txtNombreC;
    @FXML
    private TextField txtTelC;
    @FXML
    private Button btnRegresar;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }
    private operaciones tipoDeOperaciones = operaciones.NULL;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void seleccionar() {
        txtCodC.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        txtNombreC.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getNombreCliente());
        txtApellidoC.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getApellidoCliente());
        txtDirC.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getDireccionCliente());
        txtNit.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getNitCliente());
        txtTelC.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getTelefonoCliente());
        txtCorreoC.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getCorreoCliente());
    }

    public void desactivarControles() {
        txtCodC.setEditable(false);
        txtNombreC.setEditable(false);
        txtApellidoC.setEditable(false);
        txtDirC.setEditable(false);
        txtNit.setEditable(false);
        txtTelC.setEditable(false);
        txtCorreoC.setEditable(false);
    }

    public void activarControles() {
        txtCodC.setEditable(true);
        txtNombreC.setEditable(true);
        txtApellidoC.setEditable(true);
        txtDirC.setEditable(true);
        txtNit.setEditable(true);
        txtTelC.setEditable(true);
        txtCorreoC.setEditable(true);
    }

    public void limpiarControles() {
        txtCodC.clear();
        txtNombreC.clear();
        txtApellidoC.clear();
        txtDirC.clear();
        txtNit.clear();
        txtTelC.clear();
        txtCorreoC.clear();
    }

    public void cargarDatos() {
        tblClientes.setItems(getCliente());
        colCodC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoCliente"));
        colNit.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nitCliente"));
        colNomC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombreCliente"));
        colApeC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidoCliente"));
        colDirC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionCliente"));
        colTelC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoCliente"));
        colCorreoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("correoCliente"));

    }

    public ObservableList<Clientes> getCliente() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarClientes()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Clientes(
                                resultado.getInt("codigoCliente"),
                                resultado.getString("nitCliente"),
                                resultado.getString("nombreCliente"),
                                resultado.getString("apellidoCliente"),
                                resultado.getString("direccionCliente"),
                                resultado.getString("telefonoCliente"),
                                resultado.getString("correoCliente")
                        )
                        );
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
        return listaClientes = FXCollections.observableList(lista);
    }

    public void Agregar() throws SQLException {
        switch (tipoDeOperaciones) {
            case NULL:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEditar.setText("Cancelar");
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                cargarDatos();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                tipoDeOperaciones = operaciones.NULL;
                break;

        }
    }

    public void guardar() throws SQLException {
        Clientes reg = new Clientes();
        reg.setCodigoCliente(Integer.parseInt(txtCodC.getText()));
        reg.setNombreCliente(txtNombreC.getText());
        reg.setApellidoCliente(txtApellidoC.getText());
        reg.setDireccionCliente(txtDirC.getText());
        reg.setNitCliente(txtNit.getText());
        reg.setTelefonoCliente(txtTelC.getText());
        reg.setCorreoCliente(txtCorreoC.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCliente(?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setInt(1, reg.getCodigoCliente());
        procedimiento.setString(2, reg.getNitCliente());
        procedimiento.setString(3, reg.getNombreCliente());
        procedimiento.setString(4, reg.getApellidoCliente());
        procedimiento.setString(5, reg.getDireccionCliente());
        procedimiento.setString(6, reg.getTelefonoCliente());
        procedimiento.setString(7, reg.getCorreoCliente());
        procedimiento.execute();
        listaClientes.add(reg);
    }

    public void eliminar() throws SQLException {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
                break;
            default:
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    int ans = JOptionPane.showConfirmDialog(null, "Confirma esta Accion", "Verificacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (ans == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarClientes(?)}");
                            procedimiento.setInt(1, ((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            listaClientes.remove(tblClientes.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar Cliente para eliminar");
                }
        }
    }

    public void editar() throws Exception {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnEliminar.setDisable(true);
                    btnAgregar.setDisable(true);
                    tipoDeOperaciones = operaciones.NULL;
                    activarControles();
                    txtCodC.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciones un Cliente");
                }
                break;

            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Cancelar");
                btnEliminar.setDisable(false);
                btnAgregar.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NULL;
                cargarDatos();
                break;
        }
    }

    public void reportes() {
        switch (tipoDeOperaciones) {
            case NULL:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
                cargarDatos();
                break;
        }
    }

    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoCliente", null);
        GenerarReportes.mostrarReportes("reportClientes.jasper", "Reporte de Clientes", parametros);
    }

    public void actualizar() throws Exception {
        Clientes reg = new Clientes();
        reg.setCodigoCliente(Integer.parseInt(txtCodC.getText()));
        reg.setNombreCliente(txtNombreC.getText());
        reg.setApellidoCliente(txtApellidoC.getText());
        reg.setDireccionCliente(txtDirC.getText());
        reg.setNitCliente(txtNit.getText());
        reg.setTelefonoCliente(txtTelC.getText());
        reg.setCorreoCliente(txtCorreoC.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarClientes(?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setInt(1, reg.getCodigoCliente());
        procedimiento.setString(2, reg.getNitCliente());
        procedimiento.setString(3, reg.getNombreCliente());
        procedimiento.setString(4, reg.getApellidoCliente());
        procedimiento.setString(5, reg.getDireccionCliente());
        procedimiento.setString(6, reg.getTelefonoCliente());
        procedimiento.setString(7, reg.getCorreoCliente());
        procedimiento.execute();

    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }
    }

}   