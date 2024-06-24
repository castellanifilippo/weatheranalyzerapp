/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.weatheranalyzerapp;

import it.unipi.weatheranalyzerapp.utility.Lingua;
import it.unipi.weatheranalyzerapp.utility.Weather;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * FXML Controller class
 *
 * @author Filippo
 */
public class LastWeatherController{
    
    private static final Logger logger = LogManager.getLogger(MoreController.class);
    
    @FXML private TextField cityField;
    @FXML private TextField latField;
    @FXML private TextField lonField;
    @FXML private Text temperature;
    @FXML private Text precipitation;
    @FXML private Text humidity;
    @FXML private Text date;
    
    @FXML private Text precipitationText;
    @FXML private Text cityText;
    @FXML private Text temperatureText;
    @FXML private Text humidityText;
    @FXML private Text dateText;
    @FXML private Button requestButton;
    @FXML private Button updateButton;
    @FXML private Button moreButton;
    @FXML private Menu optionMenu;
    @FXML private Menu languageMenu;

    private Lingua lingua;
    
    public void initialize(){
        setIt();
    }   
    
    //Funzioni per il cambio della lingua
    public void setIt(){
        changeLan("it");
    }
    
    public void setEn(){
        changeLan("en");
    }
    
    private void changeLan(String language){
        try {
            String json = LoginController.readFile("lingue/lingua-" + language + ".json");
            Gson gson = new Gson();
            lingua = gson.fromJson(json, Lingua.class);
            precipitationText.setText(lingua.weatherPrecipitation);
            cityText.setText(lingua.weatherCity);
            temperatureText.setText(lingua.weatherTemperature);
            humidityText.setText(lingua.weatherHumidity);
            dateText.setText(lingua.weatherData);
            requestButton.setText(lingua.weatherRequest);
            updateButton.setText(lingua.weatherUpdate);
            moreButton.setText(lingua.more);
            optionMenu.setText(lingua.options);
            languageMenu.setText(lingua.language);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
    
    //Funzione che richiede l'ultimo record meteo sul databse
    public void request(){
        Task task = new Task<Void>() {
            @Override
            public Void call(){
                try {
                    requestExecution("http://localhost:8080/weather/");
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();
    }
    
    //Funzione che richiede l'ultimo record meteo sul repository
    public void update(){
        Task task = new Task<Void>() {
            @Override
            public Void call(){
                try {
                    requestExecution("http://localhost:8080/weather/update/");
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();
    }
   
    //Funzione che esegue la richiesta
    private void requestExecution(String baseUrl) throws MalformedURLException, IOException{
        //resetto i risultati a schermo
        temperature.setText("");
        precipitation.setText("");
        humidity.setText("");
        date.setText("");
        String city = cityField.getText();
        URL url;
        //In base a quale field sono compialti quale rihiesta eseguire
        if(city == null || city.equals("")){
            double lat = Double.parseDouble(latField.getText());
            double lon = Double.parseDouble(lonField.getText());
            if(Double.isNaN(lat) || Double.isNaN(lon)){
                return;
            }
            url = new URL(baseUrl + "coord=" + lat +"&" + lon);
        }else{
            String encoded = URLEncoder.encode(city, "utf-8");
            url = new URL(baseUrl + "city/" + encoded);
        }
        Weather weather = getWeather(url);
        //imposto i parametri
        temperature.setText(weather.temperature.toString());
        String pre = weather.precipitation.toString();
        if(weather.snow){
            pre = pre + " " + lingua.snow;
        }
        precipitation.setText(pre);
        humidity.setText(weather.humidity.toString());
        
        String pattern = "dd/MM/yyyy HH:mm";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String dataString = dateFormat.format(weather.datetime);
        date.setText(dataString);
    }
    
    //Funzione che elabora la richeista get
    private Weather getWeather(URL url) throws IOException{
        String file = HttpController.getRequest(url);
        if(file == null){
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(file, Weather.class);
    }
    
    //Funzioni per i bottoni di cambio scena
    public void last10(){
        change("more"); 
    }
    
    public void bookmark(){
      change("bookmarks");  
    }
    
    public void logout(){
        change("login");
    }
    
    private void change(String line){
        try {
            App.setRoot(line);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
    
}
