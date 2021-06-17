package com.learngrouptu.models;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer userID;

    @NotBlank
    @NotNull
    @Column(unique = true)
    private String username;

    @NotBlank
    @NotNull
    @Column(unique = true)
    private String email;

    @NotBlank
    @NotNull
    private String password;

    @ManyToMany
    @JoinTable
    private Set<Annonce> userAnnoncen = new HashSet<>();

    private String studiengang;

    private String abschluss;

    private String bio;

    public User(){
        super();
    }

    public User(Integer userID, String username, String email, String password){

        super();
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.password = password;
    }


    //basic getter and setter


    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Annonce> getUserAnnonces(){return userAnnoncen;}

    public void addAnnonce(Annonce annonce){userAnnoncen.add(annonce);}

    public String getStudiengang() {
        return studiengang;
    }

    public void setStudiengang(String studiengang) {
        this.studiengang = studiengang;
    }

    public String getAbschluss() {
        return abschluss;
    }

    public void setAbschluss(String abschluss) throws AbschlussNotAllowedException {
        if (!(abschluss.equals("Bachelor") || abschluss.equals("Master"))) {
            throw new AbschlussNotAllowedException();
        }
        this.abschluss = abschluss;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
