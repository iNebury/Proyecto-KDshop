
package org.durias.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.durias.system.Principal;

public class MenuPrincipalControlador implements Initializable {

    private Principal escenarioPrincipal;
    @FXML
    MenuItem btnMenuClientes;
    @FXML
    MenuItem btnProgramador;
    @FXML
    MenuItem btnTipoDeProducto;
    @FXML
    MenuItem btnCompras;
    @FXML
    MenuItem btnCargoEmpleado;
    @FXML
    MenuItem btnProveedores;
    @FXML
    MenuItem btnProductos;
    @FXML
    MenuItem btnDetalleCompra;
    @FXML
    MenuItem btnEmpleados;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnMenuClientes) {
            escenarioPrincipal.menuClientes();
        } else if (event.getSource() == btnProgramador) {
            escenarioPrincipal.programadorView();
        } else if (event.getSource() == btnTipoDeProducto) {
            escenarioPrincipal.tipoDeProducto();
        } else if (event.getSource() == btnCompras) {
            escenarioPrincipal.Compras();
        } else if (event.getSource() == btnCargoEmpleado) {
            escenarioPrincipal.CargoEmpleados();
        }else if(event.getSource() == btnProveedores ){
            escenarioPrincipal.Proveedores();
        }else if(event.getSource() == btnProductos){
            escenarioPrincipal.Producto();
        }else if(event.getSource() == btnDetalleCompra){
            escenarioPrincipal.DetalleCompras();
        }else if(event.getSource() == btnEmpleados){
            escenarioPrincipal.Empleados();
        }
    }
}
