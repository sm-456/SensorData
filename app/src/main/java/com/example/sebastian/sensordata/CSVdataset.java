package com.example.sebastian.sensordata;

/**
 * Created by sebas on 14.02.2018.
 */
import com.opencsv.bean.CsvBindByName;

public class CSVdataset {
    @CsvBindByName
    private String time;

    @CsvBindByName
    private String temperature;

    @CsvBindByName
    private String pressure;

    @CsvBindByName
    private String humidity;

    @CsvBindByName
    private String moisture;

    public String getTime(){
        return this.time;
    }


    public int getTemperature(){
        int tmp = Integer.parseInt(this.temperature);
        return tmp;
    }

    public int getPressure(){
        int tmp = Integer.parseInt(this.pressure);
        return tmp;
    }

    public int getHumidity(){
        int tmp = Integer.parseInt(this.humidity);
        return tmp;
    }

    public int getMoisture(){
        int tmp = Integer.parseInt(this.moisture);
        return tmp;
    }

    public void setTemperature(int newTemperature){
        this.temperature = Integer.toString(newTemperature);
    }

    public void setPressure(int newPressure){
        this.pressure = Integer.toString(newPressure);
    }

    public void setHumidity(int newHumidity){
        this.humidity = Integer.toString(newHumidity);
    }

    public void setMoisture(int newMoisture){
        this.moisture = Integer.toString(newMoisture);
    }

    public void setTime(String newTime){
        this.time = newTime;
    }
}
