/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.weatheranalyzerapp;

import it.unipi.weatheranalyzerapp.utility.Lingua;
import it.unipi.weatheranalyzerapp.utility.LoginResponse;
import it.unipi.weatheranalyzerapp.utility.RequestUser;
import it.unipi.weatheranalyzerapp.utility.Response;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.http.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Filippo
 */
public class LoginController{
    
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Text errorText;
    @FXML private Menu optionMenu;
    @FXML private Menu languageMenu;
    private Lingua lingua;
    
    @FXML
    public void initialize() throws Exception {
        setIt();
    }   
    
    //Funzioni di cambio lingua
    public void setIt(){
        changeLan("it", false);
    }
    
    public void setEn(){
        changeLan("en", false);
    }
    
    public void changeLan(String language, boolean test){
        try {
            String json = LoginController.readFile("lingue/lingua-" + language + ".json");
            Gson gson = new Gson();
            lingua = gson.fromJson(json, Lingua.class);
            if(!test){
                optionMenu.setText(lingua.options);
                languageMenu.setText(lingua.language);
            } } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
    
    //Funzione per leggere file (static così da riutilizzarla i naltre classi)
    public static String readFile(String file) throws Exception {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        StringBuffer buffer = new StringBuffer();
        int read;
        char[] chars = new char[1024];
        while ((read = reader.read(chars)) != -1)
            buffer.append(chars, 0, read); 

        return buffer.toString();
    }
}
    
    public void login(){
        //Reset del messaggio di errore
        errorText.setText("");
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(!controllaParametri(username, password)){
            return;
        }
        
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                auth(username, password);
                return null;
            }
        };
        
        new Thread(task).start();
 
    } 
    
    public void sign(){
        //Reset del messaggio di errore
        errorText.setText("");
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(!controllaParametri(username, password)){
            return;
        }
        
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                register(username, password);
                return null;
            }
        };
        
        new Thread(task).start();
    }
    
    //Funziona che controlla il formato dei credenziali
    private boolean controllaParametri(String username, String password){
        if(!username.matches("^[a-zA-Z0-9]*$")){
            errorText.setText(lingua.loginErrorAlphanumeric);
            return false;
        }else if(password.contains(" ")){
            errorText.setText(lingua.loginErrorSpaces);
            return false;
        }else if(username.length() > 10){
            errorText.setText(lingua.loginErrorLongUser);
            return false;
        }else if(password.length() > 72){
            errorText.setText(lingua.loginErrorLongPassword);
            return false;
        }
        return true;
    }
    
    //Funzione messa nel task che effettua l'autenticazione
    private void auth(String username, String password){
        try {
            Response response = getResponse(username, password, "http://localhost:8080/login");
            
            switch (response.code) {
                case 401:
                    errorText.setText(lingua.loginCredentials);
                    break;
                case 200:
                    Gson gson = new Gson();
                    LoginResponse loginResponse = gson.fromJson(response.text, LoginResponse.class);
                    //Salvo il token così da utilizzarlo in seguito
                    BufferedWriter writer = new BufferedWriter(new FileWriter("token.json"));
                    writer.write(loginResponse.token);
                    writer.close();
                    App.setRoot("lastWeather"); 
                    break;
                default:
                    errorText.setText(lingua.clientError);
                    break;
            }
        } catch (JsonSyntaxException | IOException ex) {
            errorText.setText(lingua.clientError);
            logger.error(ex.getMessage());
        }
        
    }
    
    private void register(String username, String password){
        try {
            Response response = getResponse(username, password, "http://localhost:8080/sign-in");
            
            switch (response.code) {
                case 403:
                    errorText.setText(lingua.signCredentials);
                    break;
                case 200:
                    errorText.setText(lingua.signSuccess);
                    break;
                default:
                    errorText.setText(lingua.clientError);
                    break;
            }
        } catch (JsonSyntaxException | IOException | ParseException ex) {
            errorText.setText(lingua.clientError); 
            logger.error(ex.getMessage());
        }
    }
    
    private Response getResponse(String username, String password, String url) throws IOException{
        
        Gson gson = new Gson();
        RequestUser requestUser = new RequestUser(username, password);
        return HttpController.getResponse(gson.toJson(requestUser), url);
    }

    public Lingua getLingua() {
        return lingua;
    }
    
    
}
