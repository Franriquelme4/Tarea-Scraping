/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.scraping.scrapingmaven;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Francisco Riquelme <francisco.riquelme@konecta.com.py at Konecta SA>
 */
public class Grafica extends JFrame{
    
    private ArrayList<Dato> datosAMostrar = new ArrayList<>();
    private HashMap<String, Integer> topicsObtenidos = new HashMap<String, Integer>();

    
    public Grafica(ArrayList<Dato> datosAMostrar) {
        this.datosAMostrar = datosAMostrar;
        CategoryDataset dataSet = createDataSet();
        JFreeChart chart = ChartFactory.createBarChart3D("Grafica de barra TEMA 1",
                "Lenguajes",
                "Rating",
                dataSet,
                PlotOrientation.VERTICAL, true, true, false);
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }
    
    public Grafica(HashMap<String, Integer> topicsObtenidos){
        this.topicsObtenidos=topicsObtenidos;
         CategoryDataset dataSet = createDataSetTopics();
        JFreeChart chart = ChartFactory.createBarChart3D("Grafica de barra TEMA 2",
                "Palabras",
                "Apariciones",
                dataSet,
                PlotOrientation.VERTICAL, true, true, false);
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
        
    }
    
     private CategoryDataset createDataSet() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
         for (int i = 0; i < 10; i++) {
            dataset.addValue(datosAMostrar.get(i).getRespositorio(), "Lenguajes", datosAMostrar.get(i).getLenguaje()); 
         }
        return dataset;
    }
      private CategoryDataset createDataSetTopics() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        int flag = 0;
        for (String key : this.topicsObtenidos.keySet()) {
            if(flag<=10){
              dataset.addValue(this.topicsObtenidos.get(key), "Palabras Asociadas", key);   
              flag++;
            }else{
                break;
            }     
	}
   
        return dataset;
    }
  
}
