/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.durias.report;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.durias.db.Conexion;

/**
 *
 * @author andre
 */
public class GenerarReportes {
    public static void mostrarReportes(String nombreReporte, String titulo, Map parametros){
        InputStream reporte  = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport reporteCliente2 = (JasperReport)JRLoader.loadObject(reporte);
            JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteCliente2, parametros,Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteImpreso,false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
