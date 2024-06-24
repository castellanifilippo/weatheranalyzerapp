/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.weatheranalyzerapp.utility;

import java.io.Serializable;
import java.util.Date;

public class Weather implements Serializable{
    
    public Integer id;
    public Double latitude;
    public Double longitude;
    public Double temperature;
    public Integer humidity;
    public Double precipitation;
    public Boolean snow;
    public Date datetime;
    public String city;

    public Integer getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public Boolean getSnow() {
        return snow;
    }

    public Date getDatetime() {
        return datetime;
    }

    public String getCity() {
        return city;
    }
    
}
