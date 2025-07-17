package com.uph23.edu.sahabattani.model;

import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Sensor extends RealmObject {
    @PrimaryKey
    private int sensorID;
    private String longitude;
    private String latitude;
    private String namaSensor;
    private int kelembapan;

    private Lahan lahan;

    public Sensor(){

    }
    public Sensor(int sensorID, String  longitude, String latitude, String namaSensor, int kelembapan, Lahan lahan) {
        this.sensorID = sensorID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.namaSensor = namaSensor;
        this.kelembapan = kelembapan;
        this.lahan = lahan;
    }

    public int getSensorID() {
        return sensorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getNamaSensor() {
        return namaSensor;
    }
    public void setNamaSensor(String namaSensor) {
        this.namaSensor = namaSensor;
    }

    public int getKelembapan() {
        return kelembapan;
    }

    public void setKelembapan(int kelembapan) {
        this.kelembapan = kelembapan;
    }

    public Lahan getLahan() {
        return lahan;
    }

    public void setLahan(Lahan lahan) {
        this.lahan = lahan;
    }

    public int generaterandomHumidity(int min, int max){
        Random random = new Random();
        return random.nextInt(max - min + 1)+min;
}

}
