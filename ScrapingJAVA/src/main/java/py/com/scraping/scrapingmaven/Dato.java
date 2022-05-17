/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.scraping.scrapingmaven;

/**
 *
 * @author Francisco Riquelme <francisco.riquelme@konecta.com.py at Konecta SA>
 */
public class Dato {

    private String lenguaje;
    private int respositorio;
    private float rating;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public Dato() {
    }

    public int getRespositorio() {
        return respositorio;
    }

    public Dato(String lenguaje, int respositorio) {
        this.lenguaje = lenguaje;
        this.respositorio = respositorio;
    }

    public void setRespositorio(int respositorio) {
        this.respositorio = respositorio;
    }

  

}
