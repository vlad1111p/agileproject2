package com.learngrouptu.DTO;

public class VorlesungDTO {

        private String titel;

        private String kursnummer;

        private String studiengang;

    public VorlesungDTO(String titel, String kursnummer, String studiengang) {
        this.titel = titel;
        this.kursnummer = kursnummer;
        this.studiengang = studiengang;
    }

    public VorlesungDTO(){

        }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getKursnummer() {
        return kursnummer;
    }

    public void setKursnummer(String kursnummer) {
        this.kursnummer = kursnummer;
    }

    public String getStudiengang() {
        return studiengang;
    }

    public void setStudiengang(String studiengang) {
        this.studiengang = studiengang;
    }
}
