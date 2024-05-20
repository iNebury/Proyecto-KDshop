package org.durias.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private Connection conexion;
    private static Conexion instancia;

    public Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbKDShop?useSSL=false", "root", "admin");

        } catch (ClassNotFoundException e) {
            System.out.println("Error" + e.getMessage());
        } catch (InstantiationException e) {
            System.out.println("Error" + e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("Error" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }

    }

    public static Conexion getInstance() {
        if (instancia == null) {

            instancia = new Conexion();

        }
        return instancia;
    }

    public Conexion(Connection conexion) {
        this.conexion = conexion;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
}
