package com.learngrouptu.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Vorlesung implements Serializable {




    @Id
    @GeneratedValue
    private Integer vorlesungid;
    private String kursnr;

    private String titel;

    private Integer cp;

    private String studiengang;

    public Vorlesung(Integer vorlesungid,String kursnr, String titel, Integer cp, String studiengang){
        super();
        this.vorlesungid=vorlesungid;
        this.kursnr = kursnr;
        this.titel = titel;
        this.cp = cp;
        this.studiengang = studiengang;
    }

    public Vorlesung() {

    }

    //basic getter and setter
    public String getKursnr(){return kursnr;}
    public String getTitel(){return titel;}
    public Integer getCp(){return cp;}
    public String getStudiengang(){return studiengang;}


    public void setKursnr(String kursnr){this.kursnr = kursnr;}
    public void setTitel(String titel){
        this.titel = titel;
    }
    public void setCp(Integer cp){this.cp = cp;}
    public void setStudiengang(String studiengang){this.studiengang = studiengang;}


    public Integer getVorlesungid() {
        return vorlesungid;
    }

    public void setVorlesungid(Integer vorlesungid) {
        this.vorlesungid = vorlesungid;
    }



}



