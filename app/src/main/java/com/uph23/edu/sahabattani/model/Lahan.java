package com.uph23.edu.sahabattani.model;


public class Lahan {
        private String namaLahan;
        private String lokasiLahan;
        private int kelembapan;
        private int panen;

        public Lahan(String namaLahan, String lokasiLahan, int kelembapan, int panen) {
            this.namaLahan = namaLahan;
            this.lokasiLahan = lokasiLahan;
            this.kelembapan = kelembapan;
            this.panen = panen;
        }

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
}
