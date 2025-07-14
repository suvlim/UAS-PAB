package com.uph23.edu.sahabattani.model;

import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Sensor extends RealmObject {
    @PrimaryKey
    private int sensorID;
    private int longitude;
    private int latitude;
    private String namaLahan;
    private String namaSensor;
    private int kelembapan;

    public Sensor(){

    }
    public Sensor(int sensorID, int longitude, int latitude, String namaLahan,String namaSensor, int kelembapan) {
        this.sensorID = sensorID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.namaLahan = namaLahan;
        this.namaSensor = namaSensor;
        this.kelembapan = generaterandomHumidity(30,80);
    }

    public int getSensorID() {
        return sensorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public String getNamaLahan() {
        return namaLahan;
    }

    public void setNamaLahan(String namaLahan) {
        this.namaLahan = namaLahan;
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

    public int generaterandomHumidity(int min, int max){
        Random random = new Random();
        return random.nextInt(max - min + 1)+min;
}

}
