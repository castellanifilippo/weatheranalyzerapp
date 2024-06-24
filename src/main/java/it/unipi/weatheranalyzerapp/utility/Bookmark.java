/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.weatheranalyzerapp.utility;

import java.io.Serializable;

/**
 *
 * @author Filippo
 */
public class Bookmark implements Serializable{
    
    public Integer id;
    public String username;
    public String name;
    public Double latitude;
    public Double longitude;

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    
}
