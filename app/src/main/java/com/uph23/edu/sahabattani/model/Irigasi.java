package com.uph23.edu.sahabattani.model;

import java.util.Date;

public class Irigasi {
    private int id;
    private int lahanId;
    private String tanggalIrigasi;
    private int durasiIrigasi;
    private int volumeIrigasi;

    public Irigasi(){

    }

    public Irigasi(int id, int lahanId, String tanggalIrigasi, int durasiIrigasi, int volumeIrigasi) {
        this.id = id;
        this.lahanId = lahanId;
        this.tanggalIrigasi = tanggalIrigasi;
        this.durasiIrigasi = durasiIrigasi;
        this.volumeIrigasi = volumeIrigasi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLahanId() {
        return lahanId;
    }

    public void setLahanId(int lahanId) {
        this.lahanId = lahanId;
    }

    public String getTanggalIrigasi() {
        return tanggalIrigasi;
    }

    public void setTanggalIrigasi(String tanggalIrigasi) {
        this.tanggalIrigasi = tanggalIrigasi;
    }

    public int getDurasiIrigasi() {
        return durasiIrigasi;
    }

    public void setDurasiIrigasi(int durasiIrigasi) {
        this.durasiIrigasi = durasiIrigasi;
    }

    public int getVolumeIrigasi() {
        return volumeIrigasi;
    }

    public void setVolumeIrigasi(int volumeIrigasi) {
        this.volumeIrigasi = volumeIrigasi;
    }
}
