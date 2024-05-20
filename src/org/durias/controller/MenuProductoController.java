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
import javax.swing.JOptionPane;
import org.durias.bean.Producto;
import org.durias.bean.Proveedores;
import org.durias.bean.TipoDeProducto;
import org.durias.db.Conexion;
import org.durias.system.Principal;

public class MenuProductoController implements Initializable {

    private ObservableList<Producto> listaProducto;
    private ObservableList<Proveedores> listaProveedor;
    private ObservableList<TipoDeProducto> listaTipoDeProducto;
    private Principal escenarioPrincipal;
    
    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }
    private operaciones tipoDeOperaciones = operaciones.NULL;
    
    @FXML
    private TableView<Producto> tblProductos;
    @FXML
    private TableColumn<Producto, String> colcodigoProducto;
    @FXML
    private TableColumn<Producto, String> coldescripcionProducto;
    @FXML
    private TableColumn<Producto, Double> colprecioUnitario;
    @FXML
    private TableColumn<Producto, Double> colprecioDocena;
    @FXML
    private TableColumn<Producto, Double> colprecioMayor;
    @FXML
    private TableColumn<Producto, Integer> colexistencia;
    @FXML
    private TableColumn<Producto, Integer> colcodigoTipoProducto;
    @FXML
    private TableColumn<Producto, Integer> colcodigoProveedor;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtcodigoProducto;
    @FXML
    private TextField txtprecioUnitario;
    @FXML
    private TextField txtprecioDocena;
    @FXML
    private TextField txtprecioMayor;
    @FXML
    private TextField txtexistencia;
    @FXML
    private TextField txtdescripcionProducto;
    @FXML
    private ComboBox<Proveedores> cmbcodigoProveedor;
    @FXML
    private ComboBox<TipoDeProducto> cmbcodigoTipoProducto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaProducto = FXCollections.observableArrayList();
        listaProveedor = FXCollections.observableArrayList();
        listaTipoDeProducto = FXCollections.observableArrayList();

        // Ahora puedes llamar a los métodos que utilizan estas listas
        cargarDatos();
        cmbcodigoProveedor.setItems(getProveedores());
        cmbcodigoTipoProducto.setItems(getTipoDeProducto());
    }



    public void cargarDatos() {
        if (tblProductos != null && colcodigoProducto != null && coldescripcionProducto != null &&
            colprecioUnitario != null && colprecioDocena != null && colprecioMayor != null &&
            colexistencia != null && colcodigoTipoProducto != null && colcodigoProveedor != null) {

            ObservableList<Producto> productos = getProducto();

            if (productos != null) {
                tblProductos.setItems(productos);

                colcodigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
                coldescripcionProducto.setCellValueFactory(new PropertyValueFactory<>("descripcionProducto"));
                colprecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
                colprecioDocena.setCellValueFactory(new PropertyValueFactory<>("precioDocena"));
                colprecioMayor.setCellValueFactory(new PropertyValueFactory<>("precioMayor"));
                colexistencia.setCellValueFactory(new PropertyValueFactory<>("existencia"));
                colcodigoTipoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoTipoProducto"));
                colcodigoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
          } else {
                System.out.println("La lista de productos es nula.");
            }
        } else {
            System.out.println("Algunos elementos de la tabla no están inicializados correctamente.");
        }
    }



    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarProductos()}");
                     ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Producto(
                                resultado.getString("codigoProducto"),
                                resultado.getString("descripcionProducto"),
                                resultado.getDouble("precioUnitario"),
                                resultado.getDouble("precioDocena"),
                                resultado.getDouble("precioMayor"),
                                resultado.getInt("existencia"),
                                resultado.getInt("codigoTipoProducto"),
                                resultado.getInt("codigoProveedor")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableList(lista);
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listaPro = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaPro.add(new Proveedores(
                        resultado.getInt("codigoProveedor"),
                        resultado.getString("NITProveedor"),
                        resultado.getString("nombresProveedor"),
                        resultado.getString("apellidosProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb"),
                        resultado.getString("telefonoProveedor"),
                        resultado.getString("emailProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FXCollections.observableList(listaPro);
    }
    
    public ObservableList<TipoDeProducto> getTipoDeProducto() {
        ArrayList<TipoDeProducto> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarTipoProducto()}");
                     ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new TipoDeProducto(
                                resultado.getInt("codigoTipoProducto"),
                                resultado.getString("descripcionProducto")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableList(lista);
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
        Producto reg = new Producto();
        TipoDeProducto tipoProductoSeleccionado = cmbcodigoTipoProducto.getSelectionModel().getSelectedItem();
        if (tipoProductoSeleccionado == null) {
            System.err.println("Error: Debe seleccionar un tipo de producto.");
            return;
        }
        Proveedores proveedorSeleccionado = cmbcodigoProveedor.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado == null) {
            System.err.println("Error: Debe seleccionar un proveedor.");
            return;
        }
        reg.setCodigoTipoProducto(tipoProductoSeleccionado.getCodigoTipoProducto());
        reg.setCodigoProveedor(proveedorSeleccionado.getCodigoProveedor());
        reg.setCodigoProducto(txtcodigoProducto.getText());
        reg.setDescripcionProducto(txtdescripcionProducto.getText());
        reg.setPrecioUnitario(Double.parseDouble(txtprecioUnitario.getText()));
        reg.setPrecioDocena(Double.parseDouble(txtprecioDocena.getText()));
        reg.setPrecioMayor(Double.parseDouble(txtprecioMayor.getText()));
        reg.setExistencia(Integer.parseInt(txtexistencia.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_crearProducto(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, reg.getCodigoProducto());
            procedimiento.setString(2, reg.getDescripcionProducto());
            procedimiento.setDouble(3, reg.getPrecioUnitario());
            procedimiento.setDouble(4, reg.getPrecioDocena());
            procedimiento.setDouble(5, reg.getPrecioMayor());
            procedimiento.setInt(6, reg.getExistencia());
            procedimiento.setInt(7, reg.getCodigoTipoProducto());
            procedimiento.setInt(8, reg.getCodigoProveedor());
            procedimiento.execute();
            listaProducto.add(reg);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar el producto: " + e.getMessage());
        }
 }

 public void seleccionar() {
     if (tblProductos.getSelectionModel().getSelectedItem() != null) {
         Producto productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();
         txtcodigoProducto.setText(productoSeleccionado.getCodigoProducto());
         txtdescripcionProducto.setText(productoSeleccionado.getDescripcionProducto());
         txtprecioUnitario.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
         txtprecioDocena.setText(String.valueOf(productoSeleccionado.getPrecioDocena()));
         txtprecioMayor.setText(String.valueOf(productoSeleccionado.getPrecioMayor()));
         txtexistencia.setText(String.valueOf(productoSeleccionado.getExistencia()));
         cmbcodigoTipoProducto.getSelectionModel().select(buscarTipoDeProducto(productoSeleccionado.getCodigoTipoProducto()));
         cmbcodigoProveedor.getSelectionModel().select(buscarProveedor(productoSeleccionado.getCodigoProveedor()));
     }
 }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    Producto productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();
                    txtcodigoProducto.setText(productoSeleccionado.getCodigoProducto());
                    txtdescripcionProducto.setText(productoSeleccionado.getDescripcionProducto());
                    txtprecioUnitario.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
                    txtprecioDocena.setText(String.valueOf(productoSeleccionado.getPrecioDocena()));
                    txtprecioMayor.setText(String.valueOf(productoSeleccionado.getPrecioMayor()));
                    txtexistencia.setText(String.valueOf(productoSeleccionado.getExistencia()));
                    cmbcodigoTipoProducto.getSelectionModel().select(buscarTipoDeProducto(productoSeleccionado.getCodigoTipoProducto()));
                    cmbcodigoProveedor.getSelectionModel().select(buscarProveedor(productoSeleccionado.getCodigoProveedor()));

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
        Producto productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();
        productoSeleccionado.setCodigoProducto(txtcodigoProducto.getText());
        productoSeleccionado.setDescripcionProducto(txtdescripcionProducto.getText());
        productoSeleccionado.setPrecioUnitario(Double.parseDouble(txtprecioUnitario.getText()));
        productoSeleccionado.setPrecioDocena(Double.parseDouble(txtprecioDocena.getText()));
        productoSeleccionado.setPrecioMayor(Double.parseDouble(txtprecioMayor.getText()));
        productoSeleccionado.setExistencia(Integer.parseInt(txtexistencia.getText()));

        TipoDeProducto tipoProductoSeleccionado = cmbcodigoTipoProducto.getSelectionModel().getSelectedItem();
        if (tipoProductoSeleccionado == null) {
            System.err.println("Error: Debe seleccionar un tipo de producto válido.");
            return;
        }
        productoSeleccionado.setCodigoTipoProducto(tipoProductoSeleccionado.getCodigoTipoProducto());

        Proveedores proveedorSeleccionado = cmbcodigoProveedor.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado == null) {
            System.err.println("Error: Debe seleccionar un proveedor válido.");
            return;
        }
        productoSeleccionado.setCodigoProveedor(proveedorSeleccionado.getCodigoProveedor());


        // Intentar ejecutar el procedimiento almacenado
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarProducto(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, productoSeleccionado.getCodigoProducto());
            procedimiento.setString(2, productoSeleccionado.getDescripcionProducto());
            procedimiento.setDouble(3, productoSeleccionado.getPrecioUnitario());
            procedimiento.setDouble(4, productoSeleccionado.getPrecioDocena());
            procedimiento.setDouble(5, productoSeleccionado.getPrecioMayor());
            procedimiento.setInt(6, productoSeleccionado.getExistencia());
            procedimiento.setInt(7, productoSeleccionado.getCodigoTipoProducto());
            procedimiento.setInt(8, productoSeleccionado.getCodigoProveedor());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar el producto: " + e.getMessage());
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
            if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_eliminarProducto(?)}");
                        procedimiento.setString(1, ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                        procedimiento.execute();
                        listaProducto.remove(tblProductos.getSelectionModel().getSelectedItem());
                        limpiarControles();
                        cargarDatos();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error al eliminar el producto: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Debe seleccionar un elemento.");
            }
        }
    }
        
    public void activarControles() {
    txtcodigoProducto.setEditable(true);
    txtdescripcionProducto.setEditable(true);
    txtprecioUnitario.setEditable(true);
    txtprecioDocena.setEditable(true);
    txtprecioMayor.setEditable(true);
    txtexistencia.setEditable(true);
    cmbcodigoTipoProducto.setDisable(false);
    cmbcodigoProveedor.setDisable(false);
    }

    public void desactivarControles() {
        txtcodigoProducto.setEditable(false);
        txtdescripcionProducto.setEditable(false);
        txtprecioUnitario.setEditable(false);
        txtprecioDocena.setEditable(false);
        txtprecioMayor.setEditable(false);
        txtexistencia.setEditable(false);
        cmbcodigoTipoProducto.setDisable(true);
        cmbcodigoProveedor.setDisable(true);
    }

    public void limpiarControles() {
        txtcodigoProducto.clear();
        txtdescripcionProducto.clear();
        txtprecioUnitario.clear();
        txtprecioDocena.clear();
        txtprecioMayor.clear();
        txtexistencia.clear();
        cmbcodigoTipoProducto.getSelectionModel().clearSelection();
        cmbcodigoProveedor.getSelectionModel().clearSelection();
    }
    
    private TipoDeProducto buscarTipoDeProducto(int codigoTipoProducto) {
    for (TipoDeProducto tipo : listaTipoDeProducto) {
        if (tipo.getCodigoTipoProducto() == codigoTipoProducto) {
            return tipo;
        }
    }
    return null;
}

    private Proveedores buscarProveedor(int codigoProveedor) {
        for (Proveedores proveedor : listaProveedor) {
            if (proveedor.getCodigoProveedor() == codigoProveedor) {
                return proveedor;
            }
        }
        return null;
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.menuPrincipalView();
        }
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    


}
