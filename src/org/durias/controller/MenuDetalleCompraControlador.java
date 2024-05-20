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
import org.durias.bean.Compras;
import org.durias.bean.DetalleCompra;
import org.durias.bean.Producto;
import org.durias.db.Conexion;
import org.durias.system.Principal;

public class MenuDetalleCompraControlador implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<DetalleCompra> listaDetalleCompra;
    private ObservableList<Producto> listaProducto;
    private ObservableList<Compras> listaCompra;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }
    private operaciones tipoDeOperaciones = operaciones.NULL;

    @FXML
    private TextField txtCodigoDetalleCompra;
    @FXML
    private TextField txtCostoUnitario;
    @FXML
    private TextField txtCantidad;
    @FXML
    private ComboBox<Producto> cmbCodigoProducto;
    @FXML
    private ComboBox<Compras> cmbnumeroDocumento;
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
    private TableView<DetalleCompra> tblDetalleCompra;
    @FXML
    private TableColumn<DetalleCompra, Integer> colcodigoDetalleCompra;
    @FXML
    private TableColumn<DetalleCompra, Double> colcostoUnitario;
    @FXML
    private TableColumn<DetalleCompra, Integer> colcantidad;
    @FXML
    private TableColumn<DetalleCompra, String> colcodigoProducto;
    @FXML
    private TableColumn<DetalleCompra, Integer> colnumeroDocumento;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        listaCompra = FXCollections.observableArrayList();
        listaDetalleCompra = FXCollections.observableArrayList();
        listaProducto = FXCollections.observableArrayList();
        cmbCodigoProducto.setItems(getProducto());
        cmbnumeroDocumento.setItems(getCompras());
    }

    public void cargarDatos() {
        if (tblDetalleCompra != null && colcodigoDetalleCompra != null && colcostoUnitario != null
                && colcantidad != null && colcodigoProducto != null && colnumeroDocumento != null) {
            ObservableList<DetalleCompra> detalleCompra = getDetalleCompra();

            if (detalleCompra != null) {
                tblDetalleCompra.setItems(detalleCompra);

                colcodigoDetalleCompra.setCellValueFactory(new PropertyValueFactory<>("codigoDetalleCompra"));
                colcostoUnitario.setCellValueFactory(new PropertyValueFactory<>("costoUnitario"));
                colcantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
                colcodigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
                colnumeroDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumento"));

            } else {
                System.out.println("La lista de productos es nula");
            }
        } else {
            System.out.println("Algunos elementos de la tabla no están inicializados correctamente");
        }
    }

    public ObservableList<DetalleCompra> getDetalleCompra() {
        ArrayList<DetalleCompra> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarDetallesCompra()}"); 
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new DetalleCompra(
                                resultado.getInt("codigoDetalleCompra"),
                                resultado.getDouble("costoUnitario"),
                                resultado.getInt("cantidad"),
                                resultado.getString("codigoProducto"),
                                resultado.getInt("numeroDocumento")
                        ));
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(lista);
    }

    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> listaProd = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_listarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaProd.add(new Producto(resultado.getString("codigoProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getDouble("precioDocena"),
                        resultado.getDouble("precioMayor"),
                        resultado.getInt("existencia"),
                        resultado.getInt("CodigoTipoProducto"),
                        resultado.getInt("codigoProveedor")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableList(listaProd);
    }

    public Producto buscarProducto(String codigoProducto) {
        Producto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_buscarProducto(?) }");
            procedimiento.setString(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Producto(registro.getString("codigoProducto"),
                        registro.getString("descripcionProducto"),
                        registro.getDouble("precioUnitario"),
                        registro.getDouble("precioDocena"),
                        registro.getDouble("precioMayor"),
                        registro.getInt("existencia"),
                        registro.getInt("codigoTipoProducto"),
                        registro.getInt("codigoProveedor"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<Compras> getCompras() {
        ArrayList<Compras> listaCompras = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_listarCompras()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaCompras.add(new Compras(resultado.getInt("numeroDocumento"),
                        resultado.getString("fechaDocumento"),
                        resultado.getString("descripcionCompra"),
                        resultado.getDouble("totalDocumento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCompra = FXCollections.observableList(listaCompras);
    }

    public Compras buscarCompra(int compraID) {
        Compras resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_buscarCompra(?)}");
            procedimiento.setInt(1, compraID);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Compras(registro.getInt("numeroDocumento"),
                        registro.getString("fechaDocumento"),
                        registro.getString("descripcionCompra"),
                        registro.getDouble("totalDocumento"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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
        DetalleCompra reg = new DetalleCompra();
        Producto ProductoSeleccionado = cmbCodigoProducto.getSelectionModel().getSelectedItem();
        if (ProductoSeleccionado == null) {
            System.err.println("Error: Debe seleciionar un Producto");
            return;
        }
        Compras CompraSeleccionada = cmbnumeroDocumento.getSelectionModel().getSelectedItem();
        if (CompraSeleccionada == null) {
            System.err.println("Error: Debe seleccionar una compra");
            return;
        }
        reg.setCodigoProducto(ProductoSeleccionado.getCodigoProducto());
        reg.setNumeroDocumento((CompraSeleccionada.getNumeroDocumento()));
        reg.setCodigoDetalleCompra(Integer.parseInt(txtCodigoDetalleCompra.getText()));
        reg.setCostoUnitario(Double.parseDouble(txtCostoUnitario.getText()));
        reg.setCantidad(Integer.parseInt(txtCantidad.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_crearDetalleCompra(?,?,?,?,?)}");
            procedimiento.setInt(1, reg.getCodigoDetalleCompra());
            procedimiento.setDouble(2, reg.getCostoUnitario());
            procedimiento.setInt(3, reg.getCantidad());
            procedimiento.setString(4, reg.getCodigoProducto());
            procedimiento.setInt(5, reg.getNumeroDocumento());
            procedimiento.execute();
            listaDetalleCompra.add(reg);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar el producto: " + e.getMessage());
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tblDetalleCompra.getSelectionModel().getSelectedItem() != null) {
                    DetalleCompra detalleCompraSeleccionado = tblDetalleCompra.getSelectionModel().getSelectedItem();
                    txtCodigoDetalleCompra.setText(String.valueOf(detalleCompraSeleccionado.getCodigoDetalleCompra()));
                    txtCostoUnitario.setText(String.valueOf(detalleCompraSeleccionado.getCostoUnitario()));
                    txtCantidad.setText(String.valueOf(detalleCompraSeleccionado.getCantidad()));
                    cmbCodigoProducto.getSelectionModel().select(buscarProducto(detalleCompraSeleccionado.getCodigoProducto()));
                    cmbnumeroDocumento.getSelectionModel().select(buscarCompra(detalleCompraSeleccionado.getNumeroDocumento()));

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

    public void actualizar() {
        DetalleCompra detalleCompraSeleccionado = tblDetalleCompra.getSelectionModel().getSelectedItem();
        detalleCompraSeleccionado.setCodigoDetalleCompra(Integer.parseInt(txtCodigoDetalleCompra.getText()));
        detalleCompraSeleccionado.setCostoUnitario(Double.parseDouble(txtCostoUnitario.getText()));
        detalleCompraSeleccionado.setCantidad(Integer.parseInt(txtCantidad.getText()));
        Producto ProductoSeleccionado = cmbCodigoProducto.getSelectionModel().getSelectedItem();
        if (ProductoSeleccionado == null) {
            System.err.println("Error: Debe seleciionar un Producto");
            return;
        }
        Compras CompraSeleccionada = cmbnumeroDocumento.getSelectionModel().getSelectedItem();
        if (CompraSeleccionada == null) {
            System.err.println("Error: Debe seleccionar una compra");
            return;
        }
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarDetalleCompra(?,?,?,?,?)}");
            procedimiento.setInt(1, detalleCompraSeleccionado.getCodigoDetalleCompra());
            procedimiento.setDouble(2, detalleCompraSeleccionado.getCostoUnitario());
            procedimiento.setInt(3, detalleCompraSeleccionado.getCantidad());
            procedimiento.setString(4, detalleCompraSeleccionado.getCodigoProducto());
            procedimiento.setInt(5, detalleCompraSeleccionado.getNumeroDocumento());
            procedimiento.execute();
        } catch (SQLException e) {
            System.err.println("Error al actualizar el detalle Compra" + e.getMessage());
            
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
            if (tblDetalleCompra.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Detalle Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_eliminarDetalleCompra(?)}");
                        procedimiento.setInt(1, ((DetalleCompra)tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
                        procedimiento.execute();
                        listaProducto.remove(tblDetalleCompra.getSelectionModel().getSelectedItem());
                        limpiarControles();
                        cargarDatos();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error al eliminar el detalle Compra: " + e.getMessage());
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
        if (tblDetalleCompra.getSelectionModel().getSelectedItem() != null) {
            DetalleCompra detalleCompraSeleccionada = tblDetalleCompra.getSelectionModel().getSelectedItem();
            txtCodigoDetalleCompra.setText(String.valueOf(detalleCompraSeleccionada.getCodigoDetalleCompra()));
            txtCostoUnitario.setText(String.valueOf(detalleCompraSeleccionada.getCostoUnitario()));
            txtCantidad.setText(String.valueOf(detalleCompraSeleccionada.getCantidad()));
            cmbCodigoProducto.getSelectionModel().select(buscarProducto((detalleCompraSeleccionada.getCodigoProducto())));
            cmbnumeroDocumento.getSelectionModel().select(buscarCompra((detalleCompraSeleccionada.getNumeroDocumento())));

        }
    }

    public void desactivarControles() {
        txtCodigoDetalleCompra.setEditable(false);
        txtCostoUnitario.setEditable(false);
        txtCantidad.setEditable(false);
        cmbCodigoProducto.setDisable(true);
        cmbnumeroDocumento.setDisable(true);
    }

    public void activarControles() {
        txtCodigoDetalleCompra.setEditable(true);
        txtCostoUnitario.setEditable(true);
        txtCantidad.setEditable(true);
        cmbCodigoProducto.setDisable(false);
        cmbnumeroDocumento.setDisable(false);

    }

    public void limpiarControles() {
        txtCodigoDetalleCompra.clear();
        txtCostoUnitario.clear();
        txtCantidad.clear();
        cmbCodigoProducto.getSelectionModel().clearSelection();
        cmbnumeroDocumento.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

}
