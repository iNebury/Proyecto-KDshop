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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.durias.bean.Proveedores;
import org.durias.db.Conexion;
import org.durias.report.GenerarReportes;
import org.durias.system.Principal;


public class MenuProveedoresControlador implements Initializable {

    @FXML
    private TextField txtPaginaProv;
    @FXML
    private TextField txtTelefonoProv;
    @FXML
    private TextField txtEmailProv;
    @FXML
    private TextField txtCodProv;
    @FXML
    private TextField txtNITProv;
    @FXML
    private TextField txtNombreProv;
    @FXML
    private TextField txtApellidosProv;
    @FXML
    private TextField txtDireccionProv;
    @FXML
    private TextField txtRazonSocialProv;
    @FXML
    private TextField txtContactoProv;
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
    private TableView tblProveedores;
    @FXML
    private TableColumn colcodigoProveedor;
    @FXML
    private TableColumn colNITProveedor;
    @FXML
    private TableColumn colnombresProveedor;
    @FXML
    private TableColumn colapellidosProveedor;
    @FXML
    private TableColumn coldireccionProveedor;
    @FXML
    private TableColumn colrazonSocial;
    @FXML
    private TableColumn colcontactoPrincipal;
    @FXML
    private TableColumn colpaginaWeb;
    @FXML
    private TableColumn coltelefonoProveedor;
    @FXML
    private TableColumn colemailProveedor;

    private ObservableList<Proveedores> listaProv;
    private Principal escenarioPrincipal;

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }
    private operador tipoDeOperador = operador.NULL;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
    }

    public void cargarDatos() {
        tblProveedores.setItems(getProveedores());
        colcodigoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("codigoProveedor"));
        colNITProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("NITProveedor"));
        colnombresProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nombresProveedor"));
        colapellidosProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("apellidosProveedor"));
        coldireccionProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccionProveedor"));
        colrazonSocial.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("razonSocial"));
        colcontactoPrincipal.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("contactoPrincipal"));
        colpaginaWeb.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("paginaWeb"));
        coltelefonoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("telefonoProveedor"));
        colemailProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("emailProveedor"));
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listaProvedores = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaProvedores.add(new Proveedores(resultado.getInt("codigoProveedor"),
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
        return this.listaProv = FXCollections.observableList(listaProvedores);
    }

    public void seleccionar() {
        try {
            txtCodProv.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            txtNITProv.setText((((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNITProveedor()));
            txtNombreProv.setText((((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNombresProveedor()));
            txtApellidosProv.setText((((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getApellidosProveedor()));
            txtDireccionProv.setText((((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor()));
            txtRazonSocialProv.setText((((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial()));
            txtContactoProv.setText((((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal()));
            txtPaginaProv.setText((((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWeb()));
            txtTelefonoProv.setText((((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getTelefonoProveedor()));
            txtEmailProv.setText((((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getEmailProveedor()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void editar() {
        switch (tipoDeOperador) {
            case NULL:
                if (tblProveedores.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnEliminar.setDisable(true);
                    btnAgregar.setDisable(true);
                    activarControles();
                    txtCodProv.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperador = operador.NULL;
                cargarDatos();
                break;
        }
    }

    
    public void Agregar() {
        switch (tipoDeOperador) {
            case NULL:
                limpiarControles();
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperador = operador.NULL;
                cargarDatos();
                break;
        }
    }

    public void guardar() {
        try {
            Proveedores registro = new Proveedores();
            registro.setCodigoProveedor(Integer.parseInt(txtCodProv.getText()));
            registro.setNITProveedor(txtNITProv.getText());
            registro.setNombresProveedor(txtNombreProv.getText());
            registro.setApellidosProveedor(txtApellidosProv.getText());
            registro.setDireccionProveedor(txtDireccionProv.getText());
            registro.setRazonSocial(txtRazonSocialProv.getText());
            registro.setContactoPrincipal(txtContactoProv.getText());
            registro.setPaginaWeb(txtPaginaProv.getText());
            registro.setTelefonoProveedor(txtTelefonoProv.getText());
            registro.setEmailProveedor(txtEmailProv.getText());

            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_crearProveedor(?,?,?,?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getNombresProveedor());
            procedimiento.setString(4, registro.getApellidosProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWeb());
            procedimiento.setString(9, registro.getTelefonoProveedor());
            procedimiento.setString(10, registro.getEmailProveedor());
            procedimiento.execute();

            listaProv.add(registro);
        } catch (SQLException e) {
            // Handle SQL exceptions appropriately
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // Handle parsing exceptions for numeric fields
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }
    }


    public void eliminar() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperador = operador.NULL;
                break;
            default:
                if (tblProveedores.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminacion del registro?", "Eliminar Proveedores", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarproveedor(?)}");
                            procedimiento.setInt(1, ((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProv.remove(tblProveedores.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            cargarDatos();
                            JOptionPane.showMessageDialog(null, "Proveedor eliminado correctamente");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para eliminar");
                }

                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarproveedor(?,?,?,?,?,?,?,?,?,?) }");
            Proveedores registro = (Proveedores) tblProveedores.getSelectionModel().getSelectedItem();
            registro.setCodigoProveedor(Integer.parseInt(txtCodProv.getText()));
            registro.setNITProveedor(txtNITProv.getText());
            registro.setNombresProveedor(txtNombreProv.getText());
            registro.setApellidosProveedor(txtApellidosProv.getText());
            registro.setDireccionProveedor(txtDireccionProv.getText());
            registro.setRazonSocial(txtRazonSocialProv.getText());
            registro.setContactoPrincipal(txtContactoProv.getText());
            registro.setPaginaWeb(txtPaginaProv.getText());
            registro.setTelefonoProveedor(txtTelefonoProv.getText());
            registro.setEmailProveedor(txtEmailProv.getText());
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getNombresProveedor());
            procedimiento.setString(4, registro.getApellidosProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWeb());
            procedimiento.setString(9, registro.getTelefonoProveedor());
            procedimiento.setString(10, registro.getEmailProveedor());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void reportes() {
        switch (tipoDeOperador) {
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
                tipoDeOperador = MenuProveedoresControlador.operador.NULL;
                cargarDatos();
                break;
        }
    }

    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoProveedor", null);
        GenerarReportes.mostrarReportes("reportProveedores.jasper", "Reporte de proveedor", parametros);
    }
    
    public void desactivarControles() {
        txtCodProv.setEditable(false);
        txtNITProv.setEditable(false);
        txtNombreProv.setEditable(false);
        txtApellidosProv.setEditable(false);
        txtDireccionProv.setEditable(false);
        txtRazonSocialProv.setEditable(false);
        txtContactoProv.setEditable(false);
        txtPaginaProv.setEditable(false);
        txtTelefonoProv.setEditable(false);
        txtEmailProv.setEditable(false);
    }

    public void activarControles() {
        txtCodProv.setEditable(true);
        txtNITProv.setEditable(true);
        txtNombreProv.setEditable(true);
        txtApellidosProv.setEditable(true);
        txtDireccionProv.setEditable(true);
        txtRazonSocialProv.setEditable(true);
        txtContactoProv.setEditable(true);
        txtPaginaProv.setEditable(true);
        txtTelefonoProv.setEditable(true);
        txtEmailProv.setEditable(true);
    }

    public void limpiarControles() {
        txtCodProv.clear();
        txtNITProv.clear();
        txtNombreProv.clear();
        txtApellidosProv.clear();
        txtDireccionProv.clear();
        txtRazonSocialProv.clear();
        txtContactoProv.clear();
        txtPaginaProv.clear();
        txtTelefonoProv.clear();
        txtEmailProv.clear();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btnRegresar){
            escenarioPrincipal.menuPrincipalView();
        }
    }

}
