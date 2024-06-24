/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.weatheranalyzerapp;

import it.unipi.weatheranalyzerapp.utility.Lingua;
import it.unipi.weatheranalyzerapp.utility.Bookmark;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import it.unipi.weatheranalyzerapp.utility.Response;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Filippo
 */
public class BookmarksController{
    
    private static final Logger logger = LogManager.getLogger(BookmarksController.class);
    
    @FXML TableView<Bookmark> table = new TableView<>();
    @FXML private Button moreButton;
    @FXML private Button weatherButton;
    @FXML private Menu optionMenu;
    @FXML private Menu languageMenu;

    private ObservableList<Bookmark> ol;
    
    private Lingua lingua; 
    
    private String token;
    
    TableColumn nameCol;

    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize(){
        //Inizializza la tabella
        table.setPlaceholder(new Label(""));
        nameCol = new TableColumn("Nome");
        nameCol.setCellValueFactory(new PropertyValueFactory<Bookmark, String>("name"));
        TableColumn latCol = new TableColumn("Lat");
        latCol.setCellValueFactory(new PropertyValueFactory<Bookmark, String>("latitude"));
        TableColumn lonCol = new TableColumn("Lon");
        lonCol.setCellValueFactory(new PropertyValueFactory<Bookmark, String>("longitude"));
        
        nameCol.setPrefWidth(149);
        latCol.setPrefWidth(75);
        lonCol.setPrefWidth(75);
        
        
        
        table.getColumns().addAll(nameCol, latCol, lonCol);
        
        ol = FXCollections.observableArrayList();
        table.setItems(ol);
        
        setIt(); 
        //Richiede i bookamrk
        Task task = new Task<Void>() {
            @Override
            public Void call(){
                try {
                    getBookmark();
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();
    } 
    
    //Funzione che richeide ed aggiunge i bookamrk alla tabella
   private void getBookmark() throws UnsupportedEncodingException, IOException{
       
        BufferedReader reader = new BufferedReader(new FileReader("token.json"));
        token = reader.readLine();
        
        Gson gson = new Gson();
        String json = gson.toJson(token);
        
        String url = "http://localhost:8080/bookmark/get";
            
        String line = HttpController.getGeneric(json, url);
        
        JsonArray jsonArray = gson.fromJson(line, JsonArray.class);
            
        Bookmark[] bookmarks = gson.fromJson(jsonArray, Bookmark[].class);
        
        for (Bookmark bookmark : bookmarks) {
            ol.add(bookmark);
        }
    }
   
   //Funzione che richiede la rimozione di un bookmark
    public void remove() throws IOException{
        Task task = new Task<Void>() {
            @Override
            public Void call(){
                try {
                    removeExecution();
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
                return null;
            }
        };
        
        new Thread(task).start();
    }
   
    //Richiede al server la rimozione del boomark
    private void removeExecution() throws IOException{
        Bookmark bookmark = table.getSelectionModel().getSelectedItem();
        
        BufferedReader reader = new BufferedReader(new FileReader("token.json"));
        token = reader.readLine();
        bookmark.username = token;
        
        Gson gson = new Gson();
        
        String url = "http://localhost:8080/bookmark/remove";
        Response response = HttpController.getResponse(gson.toJson(bookmark), url);
        if(response.code == 200){
            ol.remove(bookmark);
        }else{
            App.setRoot("login");
        }
    }
   
    
    public void last10(){
        change("more"); 
    }
    
    public void add(){
       change("bookmarkAdd"); 
    }
    
    public void weather(){
       change("lastWeather"); 
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
            weatherButton.setText(lingua.weather);
            moreButton.setText(lingua.more);
            nameCol.setText(lingua.name);
            optionMenu.setText(lingua.options);
            languageMenu.setText(lingua.language);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
    
