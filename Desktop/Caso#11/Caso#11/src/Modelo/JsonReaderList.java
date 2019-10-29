/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
 
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
  
public class JsonReaderList { 
  private ArrayList<ArrayList<String>> listaURL = new ArrayList<>();
  private ArrayList<ArrayList<String>> listaPAL = new ArrayList<>();
  private Hashtable<String, ArrayList> diccionario = new Hashtable<String, ArrayList>();
  private Captura captura = new Captura();
  private BPlusTree nuevoArbol = new BPlusTree();
  private int cantPalabras = 0;
  
  public JsonReaderList() {
  }

  public int getCantPalabras() {
    return cantPalabras;
  }

  public void setCantPalabras(int cantPalabras) {
    this.cantPalabras = cantPalabras;
  }
  
  public ArrayList<ArrayList<String>> getListaURL() {
    return listaURL;
  }

  public void setListaURL(ArrayList<ArrayList<String>> listaURL) {
    this.listaURL = listaURL;
  }

  public ArrayList<ArrayList<String>> getListaPAL() {
    return listaPAL;
  }

  public void setListaPAL(ArrayList<ArrayList<String>> listaPAL) {
    this.listaPAL = listaPAL;
  }
  
  public BPlusTree parsearJson() throws IOException{
    File jsonInputFile = new File("JSONExample.txt");
    InputStream is;
    try {
      is = new FileInputStream(jsonInputFile);
      JsonReader reader = Json.createReader(is);
      JsonObject empObj = reader.readObject();
      reader.close();
      
      int anchura = empObj.getInt("anchura");
      int profundidad = empObj.getInt("profundidad");

      JsonArray arrURLs = empObj.getJsonArray("URLs");
      for(int url = 0; url < arrURLs.size(); url++) {
        JsonObject lista = arrURLs.getJsonObject(url);
          String url1ista = lista.getString("url");
          try{
            ArrayList<String> ulr1 = captura.extractContent(url1ista, anchura);
            ArrayList<String> pal1 = captura.palabras(url1ista);
            //System.out.println(pal1.toString());
            listaURL.add(ulr1);
            listaPAL.add(pal1);
            cantPalabras += cantidadPalabras(pal1);
            diccionario.put(url1ista, pal1);
            nuevoArbol.add(pal1, url1ista);
          }
          catch(java.lang.StringIndexOutOfBoundsException e) {}
      }
      int sizeURL = listaURL.size();
      for(int i=0; i < sizeURL; i++) {
        for(int j=0; j < listaURL.get(i).size(); j++) {
          ArrayList<String> ulr2 = captura.extractContent(listaURL.get(i).get(j), profundidad);
          ArrayList<String> pal2 = captura.palabras(listaURL.get(i).get(j));
            listaURL.add(ulr2);
            cantPalabras += cantidadPalabras(pal2); 
        }
      }
      
      int sizeURL2 = listaURL.size();
      for(int i=sizeURL; i < sizeURL2; i++) {
        try{
        for(int j=0; j < listaURL.get(i).size(); j++) {
          ArrayList<String> ulr3 = captura.extractContent(listaURL.get(i).get(j), profundidad);
          ArrayList<String> pal3 = captura.palabras(listaURL.get(i).get(j));
          
            listaURL.add(ulr3);
            cantPalabras += cantidadPalabras(pal3); 
          }
      }
      catch(org.jsoup.UnsupportedMimeTypeException e) {}
    }
      //impimirURLs();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    return nuevoArbol;
  }
  
  public void impimirURLs() {
    for(int i=0; i < listaURL.size(); i++) {
      for(int j=0; j < listaURL.get(i).size(); j++) {
        System.out.println(listaURL.get(i).get(j));
      }
      System.out.println("");
    }
  }
  
  public void elementoMasRepetido(String dominio) {
    System.out.println("Esta parte imprime todas la palabras asi que por eso puse solo 5 en desorden.");
    ArrayList<String> lista = diccionario.get(dominio);
    ArrayList datos = new ArrayList<>();
    int printer = 0;
    for (String palabra : lista) {
      String minus = palabra.toLowerCase();
      int contador = 0;
      if(!datos.contains(minus)){
        for (String demas : lista) {
          if(demas.toLowerCase().equals(minus)){
            contador++;
          }
        }
        datos.add(minus);datos.add(contador);
        if(printer<4) {
          System.out.println(minus+": "+contador+((contador == 1)?" vez":" veces"));
          printer++;
        }
      }

    }
  }
  
  public ArrayList<String> encontrarPalabra(String encontrarPalabra) {
    System.out.println("Estos son los URL relacionados");
    ArrayList array = new ArrayList<>();
    for(int i=0; i < listaPAL.size(); i++) {
      for (int j=0; j < listaPAL.get(i).size(); j++) {
        if(listaPAL.get(i).get(j).equals(encontrarPalabra)) {
          array = listaPAL.get(i);
          System.out.println("URL: " + nuevoArbol.search(listaPAL.get(i)));
          break;
        }
      }
    }
    return array;
  }
  
  public int cantidadPalabras(ArrayList<String> arraycar) {
    Set<String> hs = new HashSet<>();
    hs.addAll(arraycar);
    arraycar.clear();
    arraycar.addAll(hs);
    return arraycar.size();
  }
}

