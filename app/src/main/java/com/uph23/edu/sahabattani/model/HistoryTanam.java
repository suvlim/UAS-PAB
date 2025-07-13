package com.uph23.edu.sahabattani.model;

import io.realm.annotations.PrimaryKey;
import io.realm.RealmObject;

    public class HistoryTanam extends RealmObject {

        @PrimaryKey
        private int id;

        private int idLahan;
        private String jenisTanaman;
        private String tanggalTanam;
        private String tanggalPanen;

        public HistoryTanam() {
        }

        public HistoryTanam(int id, int idLahan, String jenisTanaman, String tanggalTanam, String tanggalPanen) {
            this.id = id;
            this.idLahan = idLahan;
            this.jenisTanaman = jenisTanaman;
            this.tanggalTanam = tanggalTanam;
            this.tanggalPanen = tanggalPanen;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIdLahan() {
            return idLahan;
        }

        public void setIdLahan(int idLahan) {
            this.idLahan = idLahan;
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

        public String getTanggalPanen() {
            return tanggalPanen;
        }

        public void setTanggalPanen(String tanggalPanen) {
            this.tanggalPanen = tanggalPanen;
        }

        @Override
        public String toString() {
            return "HistoryTanam{" +
                    "id=" + id +
                    ", idLahan=" + idLahan +
                    ", jenisTanaman='" + jenisTanaman + '\'' +
                    ", tanggalTanam='" + tanggalTanam + '\'' +
                    ", tanggalPanen='" + tanggalPanen + '\'' +
                    '}';
        }
    }
