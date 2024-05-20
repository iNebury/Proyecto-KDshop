
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
import org.durias.bean.Clientes;
import org.durias.bean.TipoDeProducto;
import org.durias.db.Conexion;
import org.durias.system.Principal;

public class TipoDeProductoControlador implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<TipoDeProducto> listaTipoDeProducto;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TableColumn colCodP;
    @FXML
    private TableColumn coldesP;
    @FXML
    private TableView tblTipoDeProducto;
    @FXML
    private TextField txtcodTP;
    @FXML
    private TextField txtdesP;
    @FXML
    private Button btnRegresar;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }
    private operaciones tipoDeOperaciones = operaciones.NULL;
    
    
     public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }
    
   

    public void seleccionar() {
        txtcodTP.setText(String.valueOf(((TipoDeProducto) tblTipoDeProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        txtdesP.setText(((TipoDeProducto) tblTipoDeProducto.getSelectionModel().getSelectedItem()).getDescripcionProducto());
    }

    public void desactivarControles() {
        txtcodTP.setEditable(false);
        txtdesP.setEditable(false);
    }

    public void activarControles() {
        txtcodTP.setEditable(true);
        txtdesP.setEditable(true);
    }

    public void limpiarControles() {
        txtcodTP.clear();
        txtdesP.clear();
    }

    public void cargarDatos() {
        tblTipoDeProducto.setItems(getTipoDeProducto());
        colCodP.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoTipoProducto"));
        coldesP.setCellValueFactory(new PropertyValueFactory<Clientes, String>("descripcionProducto"));

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
                                resultado.getString("descripcionProducto")
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
        return listaTipoDeProducto = FXCollections.observableList(lista);
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
        TipoDeProducto reg = new TipoDeProducto();
        reg.setCodigoTipoProducto(Integer.parseInt(txtcodTP.getText()));
        reg.setDescripcionProducto(txtdesP.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_crearTipoProducto(?, ?)}");
        procedimiento.setInt(1, reg.getCodigoTipoProducto());
        procedimiento.setString(2, reg.getDescripcionProducto());
        procedimiento.execute();
        listaTipoDeProducto.add(reg);
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
                if (tblTipoDeProducto.getSelectionModel().getSelectedItem() != null) {
                    int ans = JOptionPane.showConfirmDialog(null, "Confirma esta Accion", "Verificacion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (ans == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_eliminarTipoProducto(?)}");
                            procedimiento.setInt(1, ((TipoDeProducto) tblTipoDeProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            procedimiento.execute();
                            listaTipoDeProducto.remove(tblTipoDeProducto.getSelectionModel().getSelectedItem());
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
                if (tblTipoDeProducto.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnEliminar.setDisable(true);
                    btnAgregar.setDisable(true);
                    tipoDeOperaciones = operaciones.NULL;
                    activarControles();
                    txtcodTP.setEditable(false);
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
        TipoDeProducto reg = new TipoDeProducto();
        reg.setCodigoTipoProducto(Integer.parseInt(txtcodTP.getText()));
        reg.setDescripcionProducto(txtdesP.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarTipoProducto(?,?)}");
        procedimiento.setInt(1, reg.getCodigoTipoProducto());
        procedimiento.setString(2, reg.getDescripcionProducto());
        procedimiento.execute();

    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
       if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }
    }

}
