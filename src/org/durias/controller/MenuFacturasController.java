/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.durias.bean.Clientes;
import org.durias.bean.Empleado;
import org.durias.bean.Factura;
import org.durias.db.Conexion;
import org.durias.report.GenerarReportes;
import org.durias.system.Principal;

/**
 *
 * @author diego
 */
public class MenuFacturasController implements Initializable{
    private ObservableList<Factura> listaFactura;
    private ObservableList<Clientes> listaClientes;
    private ObservableList<Empleado> listaEmpleados;
    private Principal escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }

    private operaciones tipoDeOperaciones = operaciones.NULL;

    @FXML
    private Button btn_EliminarC;

    @FXML
    private Button btn_Siguiente;

    @FXML
    private Button btn_Volver;

    @FXML
    private Button btn_agregarC;

    @FXML
    private Button btn_editarC;

    @FXML
    private Button btn_reportesC;

    @FXML
    private ComboBox<Clientes> cmb_codigoCliente;

    @FXML
    private ComboBox<Empleado> cmb_codigoEmpleado;

    @FXML
    private TableColumn<Factura, Integer> col_codigoCliente;

    @FXML
    private TableColumn<Factura, Integer> col_codigoEmpleado;

    @FXML
    private TableColumn<Factura, String> col_estado;

    @FXML
    private TableColumn<Factura, String> col_fechaFactura;

    @FXML
    private TableColumn<Factura, Integer> col_numeroDeFactura;

    @FXML
    private TableColumn<Factura, Double> col_totalFactura;

    @FXML
    private TableView<Factura> tv_Producto;

    @FXML
    private TextField txt_estado;

    @FXML
    private TextField txt_fechaFactura;

    @FXML
    private TextField txt_numeroDeFactura;

    @FXML
    private TextField txt_totalFactura;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmb_codigoCliente.setItems(getCliente());
        cmb_codigoEmpleado.setItems(getEmpleado());
    }

    public void cargarDatos() {
        tv_Producto.setItems(getFactura());
        col_codigoCliente.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));
        col_codigoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
        col_estado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        col_fechaFactura.setCellValueFactory(new PropertyValueFactory<>("fechaFactura"));
        col_numeroDeFactura.setCellValueFactory(new PropertyValueFactory<>("numeroDeFactura"));
        col_totalFactura.setCellValueFactory(new PropertyValueFactory<>("totalFactura"));
    }

    public ObservableList<Factura> getFactura() {
        ArrayList<Factura> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarFacturas()}");
                     ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Factura(
                                resultado.getInt("numeroDeFactura"),
                                resultado.getString("estado"),
                                resultado.getDouble("totalFactura"),
                                resultado.getString("fechaFactura"),
                                resultado.getInt("codigoCliente"),
                                resultado.getInt("codigoEmpleado")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaFactura = FXCollections.observableList(lista);
    }

    public ObservableList<Empleado> getEmpleado() {
        ArrayList<Empleado> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarEmpleados()}");
                     ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Empleado(
                                resultado.getInt("codigoEmpleado"),
                                resultado.getString("nombresEmpleado"),
                                resultado.getString("apellidosEmpleado"),
                                resultado.getDouble("sueldo"),
                                resultado.getString("direccion"),
                                resultado.getString("turno"),
                                resultado.getInt("codigoCargoEmpleado")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEmpleados = FXCollections.observableList(lista);
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
                                resultado.getString("correoCliente")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableList(lista);
    }

    public void Agregar() {
        switch (tipoDeOperaciones) {
            case NULL:
                activarControles();
                btn_agregarC.setText("Guardar");
                btn_EliminarC.setText("Cancelar");
                btn_editarC.setDisable(true);
                btn_reportesC.setDisable(true);
                tipoDeOperaciones = operaciones.AGREGAR;
                break;
            case AGREGAR:
                guardar();
                desactivarControles();
                cargarDatos();
                limpiarControles();
                btn_agregarC.setText("Agregar");
                btn_EliminarC.setText("Eliminar");
                btn_editarC.setDisable(false);
                btn_reportesC.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
                break;
        }
    }

    public void guardar() {
        Factura reg = new Factura();
        Object codigoClienteObj = cmb_codigoCliente.getSelectionModel().getSelectedItem();
        if (codigoClienteObj instanceof Clientes) {
            Clientes codigo = (Clientes) codigoClienteObj;
            reg.setCodigoCliente(codigo.getCodigoCliente());
        } else {
            System.err.println("Error: Debe seleccionar un cliente válido.");
            return;
        }
        Object codigoEmpleadoObj = cmb_codigoEmpleado.getSelectionModel().getSelectedItem();
        if (codigoEmpleadoObj instanceof Empleado) {
            Empleado codigoEmpleado = (Empleado) codigoEmpleadoObj;
            reg.setCodigoEmpleado(codigoEmpleado.getCodigoEmpleado());
        } else {
            System.err.println("Error: Debe seleccionar un empleado válido.");
            return;
        }
        reg.setNumeroDeFactura(Integer.parseInt(txt_numeroDeFactura.getText()));
        reg.setEstado(txt_estado.getText());
        reg.setTotalFactura(Double.parseDouble(txt_totalFactura.getText()));
        reg.setFechaFactura(txt_fechaFactura.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_crearFactura(?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, reg.getNumeroDeFactura());
            procedimiento.setString(2, reg.getEstado());
            procedimiento.setDouble(3, reg.getTotalFactura());
            procedimiento.setString(4, reg.getFechaFactura());
            procedimiento.setInt(5, reg.getCodigoCliente());
            procedimiento.setInt(6, reg.getCodigoEmpleado());
            procedimiento.execute();
            listaFactura.add(reg);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar la factura: " + e.getMessage());
        }
    }
    
    public void seleccionar() {
        if (tv_Producto.getSelectionModel().getSelectedItem() != null) {
            Factura FacturaSelecionada = tv_Producto.getSelectionModel().getSelectedItem();
            txt_numeroDeFactura.setText(String.valueOf(FacturaSelecionada.getNumeroDeFactura()));
            txt_estado.setText(FacturaSelecionada.getEstado());
            txt_totalFactura.setText(String.valueOf(FacturaSelecionada.getTotalFactura()));
            txt_fechaFactura.setText(FacturaSelecionada.getFechaFactura());
            cmb_codigoCliente.getSelectionModel().select(buscarCodigoCliente(FacturaSelecionada.getCodigoCliente()));
            cmb_codigoEmpleado.getSelectionModel().select(buscarCodigoEmpleado(FacturaSelecionada.getCodigoEmpleado()));
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tv_Producto.getSelectionModel().getSelectedItem() != null) {
                    Factura productoSeleccionado = tv_Producto.getSelectionModel().getSelectedItem();
                    txt_numeroDeFactura.setText(String.valueOf(productoSeleccionado.getNumeroDeFactura()));
                    txt_estado.setText(productoSeleccionado.getEstado());
                    txt_totalFactura.setText(String.valueOf(productoSeleccionado.getTotalFactura()));
                    txt_fechaFactura.setText(productoSeleccionado.getFechaFactura());
                    cmb_codigoCliente.getSelectionModel().select(buscarCodigoCliente(productoSeleccionado.getCodigoCliente()));
                    cmb_codigoEmpleado.getSelectionModel().select(buscarCodigoEmpleado(productoSeleccionado.getCodigoEmpleado()));

                    btn_editarC.setText("Actualizar");
                    btn_reportesC.setText("Cancelar");
                    btn_agregarC.setDisable(true);
                    btn_EliminarC.setDisable(true);
                    activarControles();
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    System.out.println("Debe seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btn_editarC.setText("Editar");
                btn_reportesC.setText("Reportes");
                btn_agregarC.setDisable(false);
                btn_EliminarC.setDisable(false);
                desactivarControles();
                cargarDatos();
                limpiarControles();
                tipoDeOperaciones = operaciones.NULL;
                break;
        }
    }

    public void actualizar() {
        Factura productoSeleccionado = tv_Producto.getSelectionModel().getSelectedItem();
        productoSeleccionado.setNumeroDeFactura(Integer.parseInt(txt_numeroDeFactura.getText()));
        productoSeleccionado.setEstado(txt_estado.getText());
        productoSeleccionado.setTotalFactura(Double.parseDouble(txt_totalFactura.getText()));
        productoSeleccionado.setFechaFactura(txt_fechaFactura.getText());

        Object codigoClienteObj = cmb_codigoCliente.getSelectionModel().getSelectedItem();
        if (codigoClienteObj instanceof Clientes) {
            Clientes CodigoCliente = (Clientes) codigoClienteObj;
            productoSeleccionado.setCodigoCliente(CodigoCliente.getCodigoCliente());
        } else {
            System.err.println("Error: Debe seleccionar un cliente válido.");
            return;
        }

        Object CodigoEmpleadoObj = cmb_codigoEmpleado.getSelectionModel().getSelectedItem();
        if (CodigoEmpleadoObj instanceof Empleado) {
            Empleado CodigoEmpleado = (Empleado) CodigoEmpleadoObj;
            productoSeleccionado.setCodigoEmpleado(CodigoEmpleado.getCodigoEmpleado());
        } else {
            System.err.println("Error: Debe seleccionar un empleado válido.");
            return;
        }

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarFactura(?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, productoSeleccionado.getNumeroDeFactura());
            procedimiento.setString(2, productoSeleccionado.getEstado());
            procedimiento.setDouble(3, productoSeleccionado.getTotalFactura());
            procedimiento.setString(4, productoSeleccionado.getFechaFactura());
            procedimiento.setInt(5, productoSeleccionado.getCodigoCliente());
            procedimiento.setInt(6, productoSeleccionado.getCodigoEmpleado());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar la factura: " + e.getMessage());
        }
    }

    public void eliminar() {
    if (tipoDeOperaciones == operaciones.AGREGAR) {
        desactivarControles();
        limpiarControles();
        btn_agregarC.setText("Agregar");
        btn_EliminarC.setText("Eliminar");
        btn_editarC.setDisable(false);
        btn_reportesC.setDisable(false);
        tipoDeOperaciones = operaciones.NULL;
    } else {
        if (tv_Producto.getSelectionModel().getSelectedItem() != null) {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    PreparedStatement eliminarDetalleFactura = Conexion.getInstance().getConexion().prepareStatement("DELETE FROM detallefactura WHERE numeroDeFactura = ?");
                    eliminarDetalleFactura.setInt(1, tv_Producto.getSelectionModel().getSelectedItem().getNumeroDeFactura());
                    eliminarDetalleFactura.executeUpdate();
                    
                    PreparedStatement eliminarFactura = Conexion.getInstance().getConexion().prepareStatement("DELETE FROM factura WHERE numeroDeFactura = ?");
                    eliminarFactura.setInt(1, tv_Producto.getSelectionModel().getSelectedItem().getNumeroDeFactura());
                    eliminarFactura.executeUpdate();
                    
                    listaFactura.remove(tv_Producto.getSelectionModel().getSelectedIndex());
                    limpiarControles();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error al eliminar la factura: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Debe seleccionar un elemento.");
        }
    }
}


    public void activarControles() {
        txt_numeroDeFactura.setEditable(true);
        txt_estado.setEditable(true);
        txt_totalFactura.setEditable(true);
        txt_fechaFactura.setEditable(true);
        cmb_codigoCliente.setDisable(false);
        cmb_codigoEmpleado.setDisable(false);
    }

    public void desactivarControles() {
        txt_numeroDeFactura.setEditable(false);
        txt_estado.setEditable(false);
        txt_totalFactura.setEditable(false);
        txt_fechaFactura.setEditable(false);
        cmb_codigoCliente.setDisable(true);
        cmb_codigoEmpleado.setDisable(true);
    }

    public void limpiarControles() {
        txt_numeroDeFactura.clear();
        txt_estado.clear();
        txt_totalFactura.clear();
        txt_fechaFactura.clear();
        cmb_codigoCliente.getSelectionModel().clearSelection();
        cmb_codigoEmpleado.getSelectionModel().clearSelection();
    }

    public void reportes() {
        switch (tipoDeOperaciones) {
            case NULL:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btn_editarC.setText("Editar");
                btn_reportesC.setText("Reportes");
                btn_agregarC.setDisable(false);
                btn_EliminarC.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
                cargarDatos();
                break;
        }
    }

    public void imprimirReporte() {
    Factura facturaSeleccionada = (Factura) tv_Producto.getSelectionModel().getSelectedItem();
    if (facturaSeleccionada != null) {
        int factID = facturaSeleccionada.getNumeroDeFactura();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("factID", factID);
        GenerarReportes.mostrarReportes("reporteFactura.jasper", "Reporte de Factura", parametros);
    } else {
        System.out.println("No se ha seleccionado ninguna factura.");
    }
}


    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btn_Volver) {
            escenarioPrincipal.menuPrincipalView();
        } else if (event.getSource() == btn_Siguiente) {
            escenarioPrincipal.DetalleCompras();
        }
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    private Clientes buscarCodigoCliente(int codigoCliente) {
        if (listaClientes != null) {
            for (Clientes cliente : listaClientes) {
                if (cliente.getCodigoCliente() == codigoCliente) {
                    return cliente;
                }
            }
        }
        return null;
    }

    private Empleado buscarCodigoEmpleado(int codigoEmpleado) {
        if (listaEmpleados != null) {
            for (Empleado empleado : listaEmpleados) {
                if (empleado.getCodigoEmpleado() == codigoEmpleado) {
                    return empleado;
                }
            }
        }
        return null;
    }
}
