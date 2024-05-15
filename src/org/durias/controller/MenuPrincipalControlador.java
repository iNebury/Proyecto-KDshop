
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
            escenarioPrincipal.tipoDeProductoView();
        } else if (event.getSource() == btnCompras) {
            escenarioPrincipal.ComprasView();
        } else if (event.getSource() == btnCargoEmpleado) {
            escenarioPrincipal.CargoEmpleados();
        }
    }
}