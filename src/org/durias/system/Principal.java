/**
 *
 * Diego Andree Urias Rivas - 2020292
 * IN5BM
 * Creacion 5/8/2024
 */
package org.durias.system;


import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage; 
import org.durias.bean.Compras;
import org.durias.controller.TipoDeProductoControlador;
import org.durias.controller.CargoEmpleadoControlador;
import org.durias.controller.MenuClientesController;
import org.durias.controller.MenuPrincipalControlador;
import org.durias.controller.MenuProveedoresControlador;
import org.durias.controller.ProgramadorController;


public class Principal extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    


        
    public static void main(String[] args) {
        launch(args);
    }
        @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Fresco Market");
        menuPrincipalView();

        escenarioPrincipal.show();
    }
    
    public Initializable cambiarEscena(String fxmlname, int width, int heigth) throws Exception{
        Initializable resultado;
        FXMLLoader loader = new FXMLLoader();
        InputStream file = Principal.class.getResourceAsStream("/org/durias/view/" + fxmlname);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Principal.class.getResource("/org/durias/view/" + fxmlname));
        
        escena = new Scene((AnchorPane)loader.load(file), width, heigth);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        
        resultado = (Initializable)loader.getController();
        
        return resultado;
    }
    public void menuPrincipalView(){
        try{
            MenuPrincipalControlador menuPrincipalView = (MenuPrincipalControlador)cambiarEscena
         ("MenuPrincipalView.fxml" , 1020, 580);
            menuPrincipalView.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void menuClientes(){
        try{
            MenuClientesController menuClientes = (MenuClientesController)cambiarEscena("MenuClientes.fxml", 1175, 597);
            menuClientes.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage());

        }
    }
    
    public void programadorView(){
        try{
            ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 600, 400);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void tipoDeProductoView(){
        try{
            TipoDeProductoControlador tipoDeProducto = (TipoDeProductoControlador)cambiarEscena("TipoDeProducto.fxml", 1175, 597);
            tipoDeProducto.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void ComprasView(){
        try{
            Compras compra = (Compras)cambiarEscena("MenuCompras.fxml", 1175, 597);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void CargoEmpleados(){
        try{
            CargoEmpleadoControlador cargos = (CargoEmpleadoControlador)cambiarEscena("CargoEmpleadosView.fxml", 1175, 597);
            cargos.setEscenarioPrincipal(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void ProveedoresView(){
        try{
        MenuProveedoresControlador proveedores = (MenuProveedoresControlador)cambiarEscena("MenuProveedores.fxml", 1175, 597);
        proveedores.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

