package com.learngrouptu.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Annonce {

    @Id
    @GeneratedValue
    private Integer annonceId;

    private String vorlName;

    private String choice;

    private String nachricht;

    @UpdateTimestamp
    private Date datum;

    @ManyToMany (mappedBy = "userAnnoncen")
    private Set<User> ersteller = new HashSet<User>();

    public Annonce(){

    }

    public Annonce(Integer annonceId, String vorlName, String choice, String nachricht, Date datum){

        super();
        this.annonceId = annonceId;
        this.vorlName = vorlName;
        this.choice = choice;
        this.nachricht = nachricht;
        this.datum = datum;
    }

    //basic getter and setter
    public Integer getAnnonceId(){return annonceId;}
    public String getVorlName(){return vorlName;}
    public String getChoice(){return choice;}
    public String getNachricht(){return nachricht;}
    public Date getDatum(){return datum;}
    public User getErsteller() {
        return ersteller.stream().findFirst().get();
    }


    public void setAnnonceId(Integer annonceId){this.annonceId = annonceId;}
    public void setVorlName(String vorlName){
        this.vorlName = vorlName;
    }
    public void setChoice(String choice){this.choice = choice;}
    public void setNachricht(String nachricht){this.nachricht = nachricht;}
    public void setDatum(Date datum){this.datum = datum;}
    public void setErsteller(Set<User> ersteller) {
        this.ersteller = ersteller;
    }


}
