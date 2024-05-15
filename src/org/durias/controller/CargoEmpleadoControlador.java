package org.durias.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.durias.bean.CargoEmpleado;
import org.durias.bean.Clientes;
import org.durias.db.Conexion;
import org.durias.system.Principal;

public class CargoEmpleadoControlador implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<CargoEmpleado> listaCargos;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;

    @FXML
    private TableColumn colCodigoC;

    @FXML
    private TableColumn colDescripcionC;

    @FXML
    private TableColumn colNombreC;

    @FXML
    private TableView tblCargo;

    @FXML
    private TextField txtCodE;

    @FXML
    private TextField txtDescripcionC;

    @FXML
    private TextField txtNombreC;
    
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
        txtCodE.setText(String.valueOf(((CargoEmpleado) tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        txtNombreC.setText(((CargoEmpleado) tblCargo.getSelectionModel().getSelectedItem()).getNombreCargo());
        txtDescripcionC.setText(((CargoEmpleado) tblCargo.getSelectionModel().getSelectedItem()).getDescripcionCargo());
    }

    public void desactivarControles() {
        txtCodE.setEditable(false);
        txtNombreC.setEditable(false);
        txtDescripcionC.setEditable(false);
    }

    public void activarControles() {
        txtCodE.setEditable(true);
        txtNombreC.setEditable(true);
        txtDescripcionC.setEditable(true);
    }

    public void limpiarControles() {
        txtCodE.clear();
        txtNombreC.clear();
        txtDescripcionC.clear();
    }

    public void cargarDatos() {
        tblCargo.setItems(getCargoEmpleado());
        colCodigoC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoCargoEmpleado"));
        colNombreC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombreCargo"));
        colDescripcionC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("descripcionCargo"));

    }

    public ObservableList<CargoEmpleado> getCargoEmpleado() {
        ArrayList<CargoEmpleado> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{call sp_mostrarcargoempleados()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new CargoEmpleado(
                                resultado.getInt("codigoCargoEmpleado"),
                                resultado.getString("nombreCargo"),
                                resultado.getString("descripcionCargo")
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
        return listaCargos = FXCollections.observableList(lista);
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
        CargoEmpleado reg = new CargoEmpleado();
        reg.setCodigoCargoEmpleado(Integer.parseInt(txtCodE.getText()));
        reg.setNombreCargo(txtNombreC.getText());
        reg.setDescripcionCargo(txtDescripcionC.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_agregarcargoempleado(?, ?, ?)}");
        procedimiento.setInt(1, reg.getCodigoCargoEmpleado());
        procedimiento.setString(2, reg.getNombreCargo());
        procedimiento.setString(3, reg.getDescripcionCargo());
        procedimiento.execute();
        listaCargos.add(reg);
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
                if (tblCargo.getSelectionModel().getSelectedItem() != null) {
                    int ans = JOptionPane.showConfirmDialog(null, "Confirma esta Accion", "Verificacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (ans == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarcargoempleado(?)}");
                            procedimiento.setInt(1, ((CargoEmpleado) tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                            procedimiento.execute();
                            listaCargos.remove(tblCargo.getSelectionModel().getSelectedItem());
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
                if (tblCargo.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnEliminar.setDisable(true);
                    btnAgregar.setDisable(true);
                    tipoDeOperaciones = operaciones.NULL;
                    activarControles();
                    txtCodE.setEditable(false);
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
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
        }
    }

    public void actualizar() throws Exception {
        CargoEmpleado reg = new CargoEmpleado();
        reg.setCodigoCargoEmpleado(Integer.parseInt(txtCodE.getText()));
        reg.setNombreCargo(txtNombreC.getText());
        reg.setDescripcionCargo(txtDescripcionC.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_actualizarcargoempleado(?, ?, ?)}");
        procedimiento.setInt(1, reg.getCodigoCargoEmpleado());
        procedimiento.setString(2, reg.getNombreCargo());
        procedimiento.setString(3, reg.getDescripcionCargo());
        procedimiento.execute();

    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }

    }

}
