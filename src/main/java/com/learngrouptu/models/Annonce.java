package com.learngrouptu.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Annonce {

    @Id
    @GeneratedValue
    private Integer annonceId;

    private String vorlName;

    private String choice;

    private String kontakt;

    private String nachricht;


    public Annonce(){

    }

    public Annonce(Integer annonceId, String vorlName, String choice, String kontakt, String nachricht){

        super();
        this.annonceId = annonceId;
        this.vorlName = vorlName;
        this.choice = choice;
        this.kontakt = kontakt;
        this.nachricht = nachricht;
    }

    //basic getter and setter
    public Integer getAnnonceId(){return annonceId;}
    public String getVorlName(){return vorlName;}
    public String getChoice(){return choice;}
    public String getKontakt(){return kontakt;}
    public String getNachricht(){return nachricht;}

    public void setAnnonceId(Integer annonceId){this.annonceId = annonceId;}
    public void setVorlName(String vorlName){
        this.vorlName = vorlName;
    }
    public void setChoice(String choice){this.choice = choice;}
    public void setKontakt(String kontakt){this.kontakt = kontakt;}
    public void setNachricht(String nachricht){this.nachricht = nachricht;}

}
