/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package py.com.scraping.scrapingmaven;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Francisco Riquelme <francisco.riquelme@konecta.com.py at Konecta SA>
 */
public class Scraping {

    private String urlLenguajes = "https://www.tiobe.com/tiobe-index/";
    private String urlGit = "https://github.com/topics/";

    private ArrayList<String> lenguajes = new ArrayList<>();
    private Map diccionario = new HashMap<>();
    private HashMap<String, Integer> topicsObtenidos = new HashMap<String, Integer>();
    private ArrayList<Dato> nroRepositorios = new ArrayList<>();
    private int mayorNroRepositorios;
    private int menorNroRepositorios;

    public Scraping() {
        this.diccionario.put("C++", "cpp");
        this.diccionario.put("C#", "csharp");
        this.diccionario.put("Visual Basic", "vbnet");
        this.diccionario.put("Classic Visual Basic", "Visual Basic");
        this.diccionario.put("Delphi/Object Pascal", "Pascal");
        this.diccionario.put("MATLAB", "matlab");
    }

    public Scraping(String topic) {
        this.urlGit = this.getUrlGit() + topic + "?o=desc&s=updated&page=";
    }

    public String getUrlGit() {
        return urlGit;
    }

    public void setUrlGit(String urlGit) {
        this.urlGit = urlGit;
    }

    public String getUrlLenguajes() {
        return urlLenguajes;
    }

    public void setUrlLenguajes(String urlLenguajes) {
        this.urlLenguajes = urlLenguajes;
    }

    public ArrayList<String> getLenguajes() {
        return lenguajes;
    }

    public void setLenguajes(String lenguajes) {
        this.lenguajes.add(lenguajes);
    }

    public Map getDiccionario() {
        return diccionario;
    }

    public void setDiccionario(Map diccionario) {
        this.diccionario = diccionario;
    }

    public ArrayList<Dato> getNroRepositorios() {
        return nroRepositorios;
    }

    public void setNroRepositorios(ArrayList<Dato> nroRepositorios) {
        this.nroRepositorios = nroRepositorios;
    }

    public int getMayorNroRepositorios() {
        return mayorNroRepositorios;
    }

    public void setMayorNroRepositorios(int mayorNroRepositorios) {
        this.mayorNroRepositorios = mayorNroRepositorios;
    }

    public int getMenorNroRepositorios() {
        return menorNroRepositorios;
    }

    public void setMenorNroRepositorios(int menorNroRepositorios) {
        this.menorNroRepositorios = menorNroRepositorios;
    }

    public void cargarLenguajes(Document document) {

        for (Element row : document.select("table#top20 tr")) {
            if (!row.select("td:nth-of-type(1)").text().isEmpty()) {
                final String name = row.select("td:nth-of-type(5)").text();
                this.setLenguajes(name);
            }
        }
    }

    public String verificarUrl(String lenguaje) {
        if (this.diccionario.get(lenguaje) != null) {
            lenguaje = this.diccionario.get(lenguaje) + "";
        }
        return this.urlGit + lenguaje;
    }

    public void obtenerRepositorios() throws InterruptedException {
        String url;
        int nroRepositorio;
        System.out.println("Obteniendo repositorios ...");
        Dato dato = new Dato();
        boolean flag = true;

        for (int i = 0; i < 20; i++) {
            url = this.verificarUrl(this.lenguajes.get(i));

            nroRepositorio = this.obtenerNroRepositorios(url);
            if (flag) {
                this.menorNroRepositorios = nroRepositorio;
                this.mayorNroRepositorios = nroRepositorio;
                flag = false;
            }

            if (nroRepositorio > this.mayorNroRepositorios) {
                this.mayorNroRepositorios = nroRepositorio;
            }
            if (nroRepositorio < this.menorNroRepositorios) {
                this.menorNroRepositorios = nroRepositorio;
            }

            dato = new Dato(this.lenguajes.get(i), nroRepositorio);
            System.out.println("Accediendo al repositorios de  " + this.lenguajes.get(i) + " - " + nroRepositorio);
            this.nroRepositorios.add(dato);
            Thread.sleep(2000);
        }
        System.out.println("Mayor " + this.mayorNroRepositorios);
        System.out.println("Menor " + this.menorNroRepositorios);
        System.out.println("Los Datos se obtuvieron correctamente ...");
    }

    public int obtenerNroRepositorios(String url) {
        String data = null;
        try {
            Document document = Jsoup.connect(url).get();
            data = document.select("h2.h3.color-fg-muted").text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(data.replaceAll("[^0-9]", ""));
    }

    public void crearArchivo(ArrayList<Dato> datos) {

        System.out.println("Generando archivo ...");
        try {
            String ruta = "./file/resultados.txt";
            String contenido = "Contenido de ejemplo";
            File file = new File(ruta);
            NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < datos.size(); i++) {
                bw.write(datos.get(i).getLenguaje() + "," + formatoNumero.format(datos.get(i).getRespositorio()) + "\n");
            }
            //datos.get(i).getRespositorio()
            bw.close();
            System.out.println("Archivo creado correctamente ruta:" + ruta);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Dato> obtenerRating(int menor, int mayor, ArrayList<Dato> datos) {

        for (int i = 0; i < datos.size(); i++) {

            datos.get(i).setRating(this.calcularRating(datos.get(i).getRespositorio()));
        }
        return datos;
    }

    public float calcularRating(int valor) {
        float x = (float) (valor - this.menorNroRepositorios) / (this.mayorNroRepositorios - this.menorNroRepositorios) * 100;
        return x;
    }

    public void mostrarRatingOrdenados(ArrayList<Dato> datos) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("Cantidad de apariciones por lenguaje");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%20s %20s %20s", "Lenguaje", "Aparicion","Rating");
        System.out.println();
        System.out.println("-------------------------------------------------------------");
        
        
        for (int i = 0; i < datos.size(); i++) {
            System.out.format("%20s %20s %20s",datos.get(i).getLenguaje(),datos.get(i).getRespositorio(),datos.get(i).getRating());
              System.out.println();
        }
        System.out.println("-------------------------------------------------------------"); 
        
    }

    public void sort(ArrayList<Dato> list) {
        list.sort((o1, o2) -> Float.compare(o2.getRating(), o1.getRating()));
    }

    public HashMap<String, Integer> obtenerTopicAsociados() {
        try {
            Document document;
            int paginasRecorridas=0;
            boolean menosDe30 = true;
            while (paginasRecorridas<10 && menosDe30) {
                 System.out.println("Recorriendo pagina " + paginasRecorridas + " ....");
                document = Jsoup.connect(this.getUrlGit() + paginasRecorridas).get();
                for (Element article : document.select("div.col-md-8.col-lg-9 article")) {
                    menosDe30 = diferenciaDias(article.select("relative-time").text());
                  if(menosDe30){
                      for (Element topic : article.select("div.d-flex.flex-wrap a")) {
                        if (topic.text() != "") {
                            menosDe30 = diferenciaDias(article.select("relative-time").text());
                            if (this.topicsObtenidos.containsKey(topic.text())) {

                                this.topicsObtenidos.put(topic.text(), this.topicsObtenidos.get(topic.text()) + 1);
                            } else {
                                this.topicsObtenidos.put(topic.text(), 1);
                            }
                        }
                    } 
                  }      
                }
                Thread.sleep(2000);
                paginasRecorridas++;
            }
            System.out.println("Paginas recorridas " + paginasRecorridas);
            ordenarTopicsValor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.topicsObtenidos;
    }

    public void ordenarTopicsValor() {
        this.topicsObtenidos = this.topicsObtenidos.entrySet().stream().sorted((i1, i2) -> i2.getValue().compareTo(i1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (z1, z2) -> z1, LinkedHashMap::new));
    }

    public void crearArchivoTopics(HashMap<String, Integer> topics) {
        System.out.println("Generando archivo ...");
        try {
            String ruta = "./file/resultadosTopics.txt";
            String contenido = "Contenido de ejemplo";
            File file = new File(ruta);
            NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            // Si el archivo no existe es creado
            File path = new File("./file");
            
            if(!path.exists()){
                path.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            topics.forEach((k, v) -> {
                try {
                    bw.write(k + "," + v + "\n");
                } catch (IOException ex) {
                    Logger.getLogger(Scraping.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            //datos.get(i).getRespositorio()
            bw.close();
            System.out.println("Archivo creado correctamente ruta:" + ruta);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void mostrarNumeroDeTopics(HashMap<String, Integer> topics) {
        System.out.println("-------------------------------------------------");
        System.out.println("Cantidad de apariciones ordenados Descendentemente");
        System.out.println("-------------------------------------------------");
        System.out.printf("%20s %20s", "Palabra", "Aparicion");
        System.out.println();
        System.out.println("-------------------------------------------------");
        int flag = 0;
        for (String key : topics.keySet()) {
            if(flag<=10){
              System.out.format("%20s %20s",key,this.topicsObtenidos.get(key)); 
              System.out.println();
              flag++;
            }else{
                break;
            }     
	}
        System.out.println("-------------------------------------------------"); 
    }

    public boolean diferenciaDias(String fecha) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        Date fechaInicial = formato.parse(fecha);
        Date fechaFinal = formato.parse(formato.format(new Date()));
        int dias = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 86400000);
        return dias <= 30;
    }
}
