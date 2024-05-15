package org.durias.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.durias.bean.Clientes;
import org.durias.bean.Compras;
import org.durias.db.Conexion;
import org.durias.system.Principal;

public class ComprasControlador implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Compras> listaCompras;
    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private DatePicker calFechaDoc;

    @FXML
    private TableColumn colDesC;

    @FXML
    private TableColumn colFechaDoc;

    @FXML
    private TableColumn colNumDoc;

    @FXML
    private TableColumn colTotalDoc;

    @FXML
    private TableView tblClientes;

    @FXML
    private TextField txtDescC;

    @FXML
    private TextField txtNumDoc;

    @FXML
    private TextField txtTotal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }
    private operaciones tipoDeOperaciones = operaciones.NULL;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public void seleccionar() {
        Compras compraSeleccionada = (Compras) tblClientes.getSelectionModel().getSelectedItem();

        if (compraSeleccionada != null) {
            txtNumDoc.setText(String.valueOf(compraSeleccionada.getNumeroDocumento()));
            txtDescC.setText(compraSeleccionada.getDescripcionCompra());
            txtTotal.setText(String.valueOf(compraSeleccionada.getTotalDocumento()));

            LocalDate fechaSeleccionada = calFechaDoc.getValue();
            if (fechaSeleccionada != null) {
                String fechaFormateada = fechaSeleccionada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                calFechaDoc.setValue(LocalDate.parse(fechaFormateada));
            }
        }
    }

    public void desactivarControles() {
        txtNumDoc.setEditable(false);
        txtDescC.setEditable(false);
        txtTotal.setEditable(false);
        calFechaDoc.setEditable(false);
    }

    public void activarControles() {
        txtNumDoc.setEditable(true);
        txtDescC.setEditable(true);
        txtTotal.setEditable(true);
        calFechaDoc.setEditable(true);
    }

    public void limpiarControles() {
        txtNumDoc.clear();
        txtDescC.clear();
        txtTotal.clear();
    }

    public void cargarDatos() {
        tblClientes.setItems(getCompra());
        colNumDoc.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
        colFechaDoc.setCellValueFactory(new PropertyValueFactory<Compras, String>("fechaDocumento"));
        colDesC.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcionCompra"));
        colTotalDoc.setCellValueFactory(new PropertyValueFactory<Compras, Double>("totalDocumento"));
    }

    public ObservableList<Compras> getCompra() {
        ArrayList<Compras> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_mostrarcompras()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Compras(
                                resultado.getInt("numeroDocumento"),
                                resultado.getString("fechaDocumento"),
                                resultado.getString("descripcionCompra"),
                                resultado.getDouble("totalDocumento")
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
        listaCompras = FXCollections.observableList(lista); // Corrección aquí
        return listaCompras;
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
        Compras reg = new Compras();
        reg.setNumeroDocumento(Integer.parseInt(txtNumDoc.getText()));
        reg.setDescripcionCompra(txtDescC.getText());
        reg.setTotalDocumento(Double.parseDouble(txtTotal.getText()));
        LocalDate fechaSeleccionada = calFechaDoc.getValue();

        if (fechaSeleccionada != null) {
            String fechaFormateada = fechaSeleccionada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            reg.setFechaDocumento(fechaFormateada);

            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_agregarcompra(?, ?, ?, ?)}");
            procedimiento.setInt(1, reg.getNumeroDocumento());
            procedimiento.setString(3, reg.getDescripcionCompra());
            procedimiento.setDouble(4, reg.getTotalDocumento());
            procedimiento.setString(2, reg.getFechaDocumento());
            procedimiento.execute();
            listaCompras.add(reg);
        } else {
            // Manejar el caso en el que la fecha seleccionada sea nula
            System.out.println("La fecha seleccionada no es válida.");
        }
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
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_eliminarcompra(?)}");
                            procedimiento.setInt(1, ((Compras) tblClientes.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            listaCompras.remove(tblClientes.getSelectionModel().getSelectedItem());
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
                    txtNumDoc.setEditable(false);
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
        Compras reg = new Compras();
        reg.setNumeroDocumento(Integer.parseInt(txtNumDoc.getText()));
        reg.setDescripcionCompra(txtDescC.getText());
        reg.setTotalDocumento(Double.parseDouble(txtTotal.getText()));
        LocalDate fechaSeleccionada = calFechaDoc.getValue();
        String fechaFormateada = fechaSeleccionada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        reg.setFechaDocumento(fechaFormateada);
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarcompra(?, ?, ?, ?)}");
        procedimiento.setInt(1, reg.getNumeroDocumento());
        procedimiento.setString(3, reg.getDescripcionCompra());
        procedimiento.setDouble(4, reg.getTotalDocumento());
        procedimiento.setString(2, reg.getFechaDocumento());
        procedimiento.execute();
    }

}
