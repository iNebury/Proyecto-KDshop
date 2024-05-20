/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import org.durias.bean.CargoEmpleado;
import org.durias.bean.Empleado;
import org.durias.db.Conexion;
import org.durias.system.Principal;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class MenuEmpleadosControlador implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<CargoEmpleado> listaCargoEmpleado;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }
    private operaciones tipoDeOperaciones = operaciones.NULL;
    @FXML
    private TextField txtcodigoEmpleado;
    @FXML
    private TextField txtnombresEmpleado;
    @FXML
    private TextField txtapellidosEmpleado;
    @FXML
    private ComboBox<CargoEmpleado> cmbcodigoCargoEmpleado;
    @FXML
    private TextField txtsueldo;
    @FXML
    private TextField txtdireccion;
    @FXML
    private TextField txtturno;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<Empleado> tblEmpleado;
    @FXML
    private TableColumn<Empleado, Integer> colcodigoEmpleado;
    @FXML
    private TableColumn<Empleado, String> colnombresEmpleado;
    @FXML
    private TableColumn<Empleado, String> colapellidosEmpleado;
    @FXML
    private TableColumn<Empleado, Double> colsueldo;
    @FXML
    private TableColumn<Empleado, Double> coldireccion;
    @FXML
    private TableColumn<Empleado, String> colturno;
    @FXML
    private TableColumn<Empleado, Integer> colcodigoCargoEmpleado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        listaEmpleado = FXCollections.observableArrayList();
        listaCargoEmpleado = FXCollections.observableArrayList();
        cmbcodigoCargoEmpleado.setItems(getCargoEmpleado());
    }

    public void cargarDatos() {
        if (tblEmpleado != null && colcodigoEmpleado != null && colnombresEmpleado != null && colapellidosEmpleado != null
                && colsueldo != null && coldireccion != null && colturno != null && colcodigoCargoEmpleado != null) {
            ObservableList<Empleado> empleado = getEmpleados();

            if (empleado != null) {
                tblEmpleado.setItems(empleado);

                colcodigoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
                colnombresEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombresEmpleado"));
                colapellidosEmpleado.setCellValueFactory(new PropertyValueFactory<>("apellidosEmpleado"));
                colsueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
                coldireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
                colturno.setCellValueFactory(new PropertyValueFactory<>("turno"));
                colcodigoCargoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoCargoEmpleado"));
            } else {
                System.out.println("La lista de empleados es nula");
            }
        } else {
            System.out.println("Algunos elementos de la tabla no están inicializados correctamente");
        }
    }

    public ObservableList<Empleado> getEmpleados() {
        ArrayList<Empleado> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();

            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarEmpleados()}"); ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Empleado(
                                resultado.getInt("codigoEmpleado"),
                                resultado.getString("nombresEmpleado"),
                                resultado.getString("apellidosEmpleado"),
                                resultado.getDouble("sueldo"),
                                resultado.getString("direccion"),
                                resultado.getString("turno"),
                                resultado.getInt("codigoCargoEmpleado")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableList(lista);
    }

    public ObservableList<CargoEmpleado> getCargoEmpleado() {
        ArrayList<CargoEmpleado> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarCargoEmpleado()}");
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
        return listaCargoEmpleado = FXCollections.observableList(lista);
    }

    public CargoEmpleado buscarCargo(int cargoID) {
        CargoEmpleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_buscarCargoEmpleado(?)}");
            procedimiento.setInt(1, cargoID);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new CargoEmpleado(
                        registro.getInt("codigoCargoEmpleado"),
                        registro.getString("nombreCargo"),
                        registro.getString("descripcionCargo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tblEmpleado.getSelectionModel().getSelectedItem() != null) {
                    Empleado empleadoSeleccionado = tblEmpleado.getSelectionModel().getSelectedItem();
                    txtcodigoEmpleado.setText(String.valueOf(empleadoSeleccionado.getCodigoEmpleado()));
                    txtnombresEmpleado.setText((empleadoSeleccionado.getNombresEmpleado()));
                    txtapellidosEmpleado.setText((empleadoSeleccionado.getApellidosEmpleado()));
                    txtsueldo.setText(String.valueOf(empleadoSeleccionado.getSueldo()));
                    txtdireccion.setText(empleadoSeleccionado.getDireccion());
                    txtturno.setText(empleadoSeleccionado.getTurno());
                    cmbcodigoCargoEmpleado.getSelectionModel().select(buscarCargo(empleadoSeleccionado.getCodigoCargoEmpleado()));
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    System.out.println("Debe seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                cargarDatos();
                limpiarControles();
                tipoDeOperaciones = operaciones.NULL;
                break;
        }
    }
    
    public void actualizar(){
        Empleado empleadoSeleccionado = tblEmpleado.getSelectionModel().getSelectedItem();
        empleadoSeleccionado.setCodigoEmpleado(Integer.parseInt(txtcodigoEmpleado.getText()));
        empleadoSeleccionado.setNombresEmpleado(txtnombresEmpleado.getText());
        empleadoSeleccionado.setApellidosEmpleado(txtapellidosEmpleado.getText());
        empleadoSeleccionado.setSueldo(Double.parseDouble(txtsueldo.getText()));
        empleadoSeleccionado.setDireccion(txtdireccion.getText());
        empleadoSeleccionado.setTurno(txtturno.getText());
        CargoEmpleado cargoSeleccionado = cmbcodigoCargoEmpleado.getSelectionModel().getSelectedItem();
        if (cargoSeleccionado == null) {
            System.err.println("Error: Debe seleciionar un empleado");
            return;
        }
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarEmpleado(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, empleadoSeleccionado.getCodigoEmpleado());
            procedimiento.setString(2, empleadoSeleccionado.getNombresEmpleado());
            procedimiento.setString(3, empleadoSeleccionado.getApellidosEmpleado());
            procedimiento.setDouble(4, empleadoSeleccionado.getSueldo());
            procedimiento.setString(5, empleadoSeleccionado.getDireccion());
            procedimiento.setString(6, empleadoSeleccionado.getTurno());
            procedimiento.setInt(7, empleadoSeleccionado.getCodigoCargoEmpleado());
            procedimiento.execute();
        }catch(SQLException e){
            System.err.println("Error al actualizar al Empleado: " + e.getMessage());
        }
        
    }
    

    public void Agregar() {
        switch (tipoDeOperaciones) {
            case NULL:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operaciones.AGREGAR;
                break;
            case AGREGAR:
                guardar();
                desactivarControles();
                cargarDatos();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
                break;
        }
    }

    public void guardar() {
        Empleado reg = new Empleado();
        CargoEmpleado cargoSeleccionado = cmbcodigoCargoEmpleado.getSelectionModel().getSelectedItem();
        if (cargoSeleccionado == null) {
            System.err.println("Error: Debe seleciionar un empleado");
            return;
        }

        reg.setCodigoCargoEmpleado(cargoSeleccionado.getCodigoCargoEmpleado());
        reg.setCodigoEmpleado(Integer.parseInt(txtcodigoEmpleado.getText()));
        reg.setNombresEmpleado((txtnombresEmpleado.getText()));
        reg.setApellidosEmpleado((txtapellidosEmpleado.getText()));
        reg.setSueldo(Double.parseDouble(txtsueldo.getText()));
        reg.setDireccion((txtdireccion.getText()));
        reg.setTurno((txtturno.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_crearEmpleado(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, reg.getCodigoEmpleado());
            procedimiento.setString(2, reg.getNombresEmpleado());
            procedimiento.setString(3, reg.getApellidosEmpleado());
            procedimiento.setDouble(4, reg.getSueldo());
            procedimiento.setString(5, reg.getDireccion());
            procedimiento.setString(6, reg.getTurno());
            procedimiento.setInt(7, reg.getCodigoCargoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(reg);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar el empleado: " + e.getMessage());
        }
    }

    public void eliminar() {
        if (tipoDeOperaciones == operaciones.AGREGAR) {
            desactivarControles();
            limpiarControles();
            btnAgregar.setText("Agregar");
            btnEliminar.setText("Eliminar");
            btnEditar.setDisable(false);
            btnReporte.setDisable
            (false);
            tipoDeOperaciones = operaciones.NULL;
        } else {
            if (tblEmpleado.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_eliminarEmpleado(?)}");
                        procedimiento.setInt(1, ((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                        procedimiento.execute();
                        listaCargoEmpleado.remove(tblEmpleado.getSelectionModel().getSelectedItem());
                        limpiarControles();
                        cargarDatos();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error al eliminar el Empleado: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Debe seleccionar un elemento.");
            }
        }
    }

    public void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.menuPrincipalView();
        }
    }

    public void seleccionar() {
        if (tblEmpleado.getSelectionModel().getSelectedItem() != null) {
            Empleado empleadoSeleccionado = tblEmpleado.getSelectionModel().getSelectedItem();
            txtcodigoEmpleado.setText(String.valueOf(empleadoSeleccionado.getCodigoEmpleado()));
            txtnombresEmpleado.setText(empleadoSeleccionado.getNombresEmpleado());
            txtapellidosEmpleado.setText(empleadoSeleccionado.getApellidosEmpleado());
            txtsueldo.setText(String.valueOf(empleadoSeleccionado.getSueldo()));
            txtdireccion.setText(empleadoSeleccionado.getDireccion());
            txtturno.setText(empleadoSeleccionado.getTurno());
            cmbcodigoCargoEmpleado.getSelectionModel().select(buscarCargo((empleadoSeleccionado).getCodigoCargoEmpleado()));
        }
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void activarControles() {
        txtcodigoEmpleado.setEditable(true);
        txtnombresEmpleado.setEditable(true);
        txtapellidosEmpleado.setEditable(true);
        txtsueldo.setEditable(true);
        txtdireccion.setEditable(true);
        txtturno.setEditable(true);
        cmbcodigoCargoEmpleado.setDisable(false);
    }

    public void desactivarControles() {
        txtcodigoEmpleado.setEditable(false);
        txtnombresEmpleado.setEditable(false);
        txtapellidosEmpleado.setEditable(false);
        txtsueldo.setEditable(false);
        txtdireccion.setEditable(false);
        txtturno.setEditable(false);
        cmbcodigoCargoEmpleado.setDisable(true);

    }

    public void limpiarControles() {
        txtcodigoEmpleado.clear();
        txtnombresEmpleado.clear();
        txtapellidosEmpleado.clear();
        txtsueldo.clear();
        txtdireccion.clear();
        txtturno.clear();
        cmbcodigoCargoEmpleado.getSelectionModel().clearSelection();
    }

}
