package com.learngrouptu.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String vorlName;

    private String choice;

    private String kontakt;

    private String nachricht;


    public Annonce(){

    }

    public Annonce(String vorlName, String choice, String kontakt, String nachricht){

        this.vorlName = vorlName;
        this.choice = choice;
        this.kontakt = kontakt;
        this.nachricht = nachricht;
    }

    //basic getter and setter
    public Integer getId(){return id;}
    public String getVorlName(){return vorlName;}

    public void setId(Integer id){this.id = id;}
    public void setVorlName(String vorlName){
        this.vorlName = vorlName;
    }

}
