/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.scraping.scrapingmaven;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Francisco Riquelme <francisco.riquelme@konecta.com.py at Konecta SA>
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {

        Scraping conexion = new Scraping();

// Tema 1
//
        try {
            Document document = Jsoup.connect(conexion.getUrlLenguajes()).get();
            conexion.cargarLenguajes(document);
            conexion.obtenerRepositorios();

            ArrayList<Dato> datos = conexion.getNroRepositorios();
            conexion.crearArchivo(datos);

            ArrayList<Dato> datosRating = conexion.obtenerRating(conexion.getMenorNroRepositorios(), conexion.getMayorNroRepositorios(), datos);
            conexion.sort(datosRating);
            conexion.mostrarRatingOrdenados(datosRating);
            SwingUtilities.invokeAndWait(() -> {
                Grafica ejemplo = new Grafica(datosRating);
                ejemplo.setSize(1300, 800);
                ejemplo.setLocationRelativeTo(null);
                ejemplo.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                ejemplo.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        
// Tema 2
        Scraping conexionGit = new Scraping("pascal");

        HashMap<String, Integer> topicsOrdenados = conexionGit.obtenerTopicAsociados();

        conexionGit.crearArchivoTopics(topicsOrdenados);
        conexionGit.mostrarNumeroDeTopics(topicsOrdenados);
        SwingUtilities.invokeAndWait(() -> {
            Grafica ejemplo = new Grafica(topicsOrdenados);
            ejemplo.setSize(1300, 800);
            ejemplo.setLocationRelativeTo(null);
            ejemplo.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            ejemplo.setVisible(true);
        });
    }
}
