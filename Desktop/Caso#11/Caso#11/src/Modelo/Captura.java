/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Captura{
  
  public Captura() {
  }
  
  public ArrayList<String> extractContent(String urlString, int anchura) throws MalformedURLException, IOException {
      ArrayList<String> content = new ArrayList<>();
    try {
      
      URL url = new URL(urlString);
      URLConnection urlConnection = url.openConnection();
      InputStream is = urlConnection.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String linea = br.readLine();
      while (null != linea) {
        content.add(linea);
        linea = br.readLine();
      }
      
    }
    catch(java.io.IOException e){

    }
    return showLinks(content, anchura);
  }
  
  private ArrayList<String> showLinks(ArrayList<String> content, int anchura) {
    int cantURL = 0;
    ArrayList<String> urls = new ArrayList<>();
    Pattern pattern = Pattern.compile("(?i)HREF\\s*=\\s*\"(.*?)\"");
    for (int i=0; i<content.size(); i++) {
      Matcher matcher = pattern.matcher(content.get(i));
      while (matcher.find()) {
          if(cantURL < anchura && matcher.group(1).length()<61 && matcher.group(1).charAt(0) == 'h' && matcher.group(1).charAt(4) == 's') {
            urls.add(matcher.group(1));
            cantURL++;
          }
      }
    }
    return urls;
  }

  public ArrayList<String> palabras(String url) throws IOException {
    ArrayList<String> palabra = new ArrayList<>();
    org.jsoup.nodes.Document doc = Jsoup.connect(url).timeout(6000).get();
    Elements ele= doc.select("p");
    for (Element element : ele) {
      String[] linea = ele.toString().split(" ");
      for (int j=0; j < linea.length; j++) {
        if(linea[j].length() > 4) {
          if(linea[j].chars().allMatch(Character::isLetter)) {
            palabra.add(linea[j]);
          }
        }
      }
    }
    
    return palabra;
  }
  
}
