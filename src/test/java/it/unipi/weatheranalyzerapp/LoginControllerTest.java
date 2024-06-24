/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package it.unipi.weatheranalyzerapp;

import it.unipi.weatheranalyzerapp.utility.Lingua;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Filippo
 */
public class LoginControllerTest {
    
    public LoginControllerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    
    @Test
    public void testChangeLan() throws Exception{
        System.out.println("Test changeLan");
        LoginController instance = new LoginController();
        instance.changeLan("it",true);
        Lingua lingua = instance.getLingua();
        if(lingua.weather.equals("Meteo")){
            System.out.println("OK");
        }else{
            System.out.println("Fallito");
        }
    }
}
