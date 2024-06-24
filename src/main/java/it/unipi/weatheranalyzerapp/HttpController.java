/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.weatheranalyzerapp;

import com.google.gson.Gson;
import it.unipi.weatheranalyzerapp.utility.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Filippo
 */
public class HttpController {
    
    //Collezione di metodi per le richieste http
    
     public static Response getResponse(String content, String url) throws IOException{
        Gson gson = new Gson();
            
        String line = getGeneric(content, url);
            
        return gson.fromJson(line, Response.class);
    }
     
     public static String getRequest(URL url) throws IOException{
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        StringBuffer content;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        }
        
        return content.toString();
    }
     
     public static String getGeneric(String content, String url) throws IOException{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
            
        StringEntity postingString = new StringEntity(content);
        httppost.setEntity(postingString);
        httppost.setHeader("Content-type", "application/json");
        HttpResponse  httpResponse = httpClient.execute(httppost);
        HttpEntity httpEntity = httpResponse.getEntity();
            
        return  EntityUtils.toString(httpEntity);
     }
}
