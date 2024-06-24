/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.weatheranalyzerapp;

import it.unipi.weatheranalyzerapp.utility.Lingua;
import it.unipi.weatheranalyzerapp.utility.Response;
import it.unipi.weatheranalyzerapp.utility.Bookmark;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
public class BookmarkAddController{

    private static final Logger logger = LogManager.getLogger(BookmarkAddController.class);
        
    private Lingua lingua;
    @FXML private TextField nameField;
    @FXML private TextField latField;
    @FXML private TextField lonField;
    @FXML private Text errorText;
    @FXML private Text nameText;
    @FXML private Button addButton;
    @FXML private Button backButton;
    @FXML private Button moreButton;
    @FXML private Button weatherButton;
    @FXML private Menu optionMenu;
    @FXML private Menu languageMenu;
    
    @FXML
    public void initialize(){
        setIt();
    }
    
    //Funzione per aggiunta del bookamrk
    public void add() throws IOException{
         Task task = new Task<Void>() {
            @Override
            public Void call(){
                try {
                    addExecution();
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
                return null;
            }
        };
        
        new Thread(task).start();
    }
    
    //Funzioen che richeide a lseerver l'aggiunta di un bookmark
    private void addExecution() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("token.json"));
        String token = reader.readLine();
        
        Bookmark bookmark = new Bookmark();
        bookmark.username = token;
        bookmark.name = nameField.getText();
        bookmark.latitude = Double.parseDouble(latField.getText());
        bookmark.longitude = Double.parseDouble(lonField.getText());
        //Controllo il formato di lat e lon
        if(Double.isNaN(bookmark.longitude) || Double.isNaN(bookmark.latitude)){
            return;
        }
        Gson gson = new Gson();
        String url = "http://localhost:8080/bookmark/add";
        
        Response response = HttpController.getResponse(gson.toJson(bookmark), url);
        switch (response.code) {
            case 401:
                App.setRoot("login");
                break;
            case 400:
                errorText.setText(lingua.bookmarkMany);
                break;
            default:
                App.setRoot("bookmarks");
                break;
        }
    }
    
    //Funzioni per cambio scena
    public void back(){
        change("bookmarks"); 
    }
    
    public void last10() throws IOException{
        change("more"); 
    }
    
    public void weather() throws IOException{
       change("lastWeather"); 
    }
    
    
    public void logout() throws IOException{
        change("login"); 
    }
    
    //Funzioni per il cambio lingua
    public void setIt(){
        changeLan("it");
    }
    
    public void setEn(){
        changeLan("en");
    }
    
    private void change(String line){
        try {
            App.setRoot(line);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    private void changeLan(String language){
        try {
            String json = LoginController.readFile("lingue/lingua-" + language + ".json");
            Gson gson = new Gson();
            lingua = gson.fromJson(json, Lingua.class);
            nameText.setText(lingua.name);
            addButton.setText(lingua.add);
            backButton.setText(lingua.back);
            weatherButton.setText(lingua.weather);
            moreButton.setText(lingua.more);
            optionMenu.setText(lingua.options);
            languageMenu.setText(lingua.language);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
      
    }
    
}
