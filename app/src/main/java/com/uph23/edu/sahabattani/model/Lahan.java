package com.uph23.edu.sahabattani.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Lahan extends RealmObject {
    @PrimaryKey
    private int id;
    private int userid;
    private String namaLahan;
    private String lokasiLahan;
    private String jenisTanaman;
    private String tanggalTanam;
    private String estimasiPanen;
    private String kelembapanTanah;

    public Lahan() {
    }

    public Lahan(int id, int userid, String namaLahan, String lokasiLahan, String jenisTanaman, String tanggalTanam, String estimasiPanen, String kelembapanTanah) {
        this.id = id;
        this.userid = userid;
        this.namaLahan = namaLahan;
        this.lokasiLahan = lokasiLahan;
        this.jenisTanaman = jenisTanaman;
        this.tanggalTanam = tanggalTanam;
        this.estimasiPanen = estimasiPanen;
        this.kelembapanTanah = kelembapanTanah;
    }

    @Override
    public String toString() {
        return "Lahan{" +
                "id=" + id +
                ", namaLahan='" + namaLahan + '\'' +
                ", lokasiLahan='" + lokasiLahan + '\'' +
                ", jenisTanaman='" + jenisTanaman + '\'' +
                ", tanggalTanam='" + tanggalTanam + '\'' +
                ", estimasiPanen='" + estimasiPanen + '\'' +
                ", kelembapanTanah='" + kelembapanTanah + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public int getUserid() {return userid;}

    public void setUserid(int userid) {this.userid = userid;}

    public String getNamaLahan() {
        return namaLahan;
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

    public String getJenisTanaman() {
        return jenisTanaman;
    }

    public void setJenisTanaman(String jenisTanaman) {
        this.jenisTanaman = jenisTanaman;
    }

    public String getTanggalTanam() {
        return tanggalTanam;
    }

    public void setTanggalTanam(String tanggalTanam) {
        this.tanggalTanam = tanggalTanam;
    }

    public String getEstimasiPanen() {
        return estimasiPanen;
    }

    public void setEstimasiPanen(String estimasiPanen) {
        this.estimasiPanen = estimasiPanen;
    }

    public String getKelembapanTanah() {
        return kelembapanTanah;
    }

    public void setKelembapanTanah(String kelembapanTanah) {
        this.kelembapanTanah = kelembapanTanah;
    }


}


