/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.weatheranalyzerapp;

import it.unipi.weatheranalyzerapp.utility.Lingua;
import it.unipi.weatheranalyzerapp.utility.Weather;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Filippo
 */
public class MoreController{
    
    private static final Logger logger = LogManager.getLogger(MoreController.class);

    @FXML TableView<Weather> table = new TableView<>();
    @FXML private Button weatherButton;
    @FXML private Text cityText;
    @FXML private Button requestButton;
    @FXML private Menu optionMenu;
    @FXML private Menu languageMenu;

    private ObservableList<Weather> ol;
    Lingua lingua;
    @FXML private TextField cityField;
    @FXML private TextField latField;
    @FXML private TextField lonField;
    
    @FXML
    public void initialize(){
        //Inizzializzo la tabella
        table.setPlaceholder(new Label(""));
        TableColumn temCol = new TableColumn("Temperatura");
        temCol.setCellValueFactory(new PropertyValueFactory<Weather, String>("temperature"));
        TableColumn preCol = new TableColumn("Precipitazioni");
        preCol.setCellValueFactory(new PropertyValueFactory<Weather, String>("precipitation"));
        TableColumn humCol = new TableColumn("Umidità");
        humCol.setCellValueFactory(new PropertyValueFactory<Weather, String>("humidity"));
        TableColumn dateCol = new TableColumn("Data");
        dateCol.setCellValueFactory(new PropertyValueFactory<Weather, String>("datetime"));
        //Cambio il formato delal data
        dateCol.setCellFactory(column -> {
            TableCell<Weather, Date> cell = new TableCell<Weather, Date>() {
                @Override
                protected void updateItem(Date date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        String pattern = "dd/MM/yyyy HH:mm";
                        DateFormat dateFormat = new SimpleDateFormat(pattern);
                        String dataString = dateFormat.format(date);
                        setText(dataString);
                    }
                }
            };
            return cell;
        });
        
        table.getColumns().addAll(temCol, preCol, humCol, dateCol);
        
        ol = FXCollections.observableArrayList();
        table.setItems(ol);
        
        setIt();
    } 
    
    //Richiede gli ultimi 10 record disponiboli sul databse
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
    
    //Richiede l'aggiunta dell'ultimo record dal repository
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
   
    //Funzione che esegue le richieste
    private void requestExecution(String baseUrl) throws MalformedURLException, IOException{
        String city = cityField.getText();
        URL url;
        if(city == null || city.equals("")){
            double lat = Double.parseDouble(latField.getText());
            double lon = Double.parseDouble(lonField.getText());
            if(Double.isNaN(lat) || Double.isNaN(lon)){
                return;
            }
            url = new URL(baseUrl + "coord10=" + lat +"&" + lon);
        }else{
            String encoded = URLEncoder.encode(city, "utf-8");
            url = new URL(baseUrl + "city10/" + encoded);
        }
        Weather[] weathers = getWeather(url);
        ol.clear();
        if(weathers == null){
            ol.clear();
            return;
        }
        for (Weather weather : weathers) {
            ol.add(weather);
        }
    }
    
    //Funzione che richiede pù record meteo
    private Weather[] getWeather(URL url) throws IOException{
        String file = HttpController.getRequest(url);
        if(file == null){
            return null;
        }
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(file, JsonArray.class);
            
        return gson.fromJson(jsonArray, Weather[].class);
    }
    
    //Funzioni di cambio scena
    public void weather(){
        change("lastWeather");
    }
    
    public void bookmark(){
        change("bookmarks");
    }
    
    public void logout(){
        change("login");
    }
    
    //Funzioni di cambio lingua
    public void setIt(){
        changeLan("it");
    }
    
    public void setEn() throws Exception{
        changeLan("en");
    }
    
    private void changeLan(String language){
        try {
            String json = LoginController.readFile("lingue/lingua-" + language + ".json");
            Gson gson = new Gson();
            lingua = gson.fromJson(json, Lingua.class);
            cityText.setText(lingua.weatherCity);
            requestButton.setText(lingua.weatherRequest);
            weatherButton.setText(lingua.weather);
            optionMenu.setText(lingua.options);
            languageMenu.setText(lingua.language);
        } catch (Exception ex) {
            logger.error(ex.getMessage());;
        }
    }
    
    private void change(String line){
        try {
            App.setRoot(line);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
    
}
