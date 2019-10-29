/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author efrai
 */
public class NewMain {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws IOException {
    JsonReaderList reader = new JsonReaderList();
    BPlusTree nuevoArbol = reader.parsearJson();
    System.out.println("Trabajo hecho por: ");
    System.out.println("Efrain Vega Morua. 2017047604. ");
    System.out.println("Debir Granados Solano. 2018185125.");
    System.out.println("Arbol ordenado segun claves: " + nuevoArbol.toString());
    System.out.println("Palabras en el arbol: " + reader.getCantPalabras());
    System.out.println("");
    System.out.println("Cantidad de URL: "+reader.getListaURL().size());
    System.out.println("");
    reader.encontrarPalabra("sitio");
    System.out.println("");
    reader.elementoMasRepetido("https://es.wikipedia.org/wiki/Felis_silvestris_catus");
    
    
    
    
    
  }
  
}
