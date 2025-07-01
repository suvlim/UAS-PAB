package com.uph23.edu.sahabattani.model;


import io.realm.RealmObject;

public class Lahan extends RealmObject {
        private String namaLahan;
        private String lokasiLahan;
        private int kelembapan;
        private int panen;
        private String jenisTanaman;

    public String getNamaLahan() {
        return namaLahan;
    }

    @Override
    public String toString() {
        return "Lahan{" +
                "namaLahan='" + namaLahan + '\'' +
                ", lokasiLahan='" + lokasiLahan + '\'' +
                ", kelembapan=" + kelembapan +
                ", panen=" + panen +
                ", jenisTanaman='" + jenisTanaman + '\'' +
                '}';
    }

    public void setNamaLahan(String namaLahan) {
        this.namaLahan = namaLahan;
    }

    public String getLokasiLahan() {
        return lokasiLahan;
    }

    public void setLokasiLahan(String lokasiLahan) {
        this.lokasiLahan = lokasiLahan;
    }

    public int getKelembapan() {
        return kelembapan;
    }

    public void setKelembapan(int kelembapan) {
        this.kelembapan = kelembapan;
    }

    public int getPanen() {
        return panen;
    }

    public void setPanen(int panen) {
        this.panen = panen;
    }

    public String getJenisTanaman() {
        return jenisTanaman;
    }

    public void setJenisTanaman(String jenisTanaman) {
        this.jenisTanaman = jenisTanaman;
    }

    public Lahan(String namaLahan, String lokasiLahan, int kelembapan, int panen, String jenisTanaman) {
        this.namaLahan = namaLahan;
        this.lokasiLahan = lokasiLahan;
        this.kelembapan = kelembapan;
        this.panen = panen;
        this.jenisTanaman = jenisTanaman;
    }
}
