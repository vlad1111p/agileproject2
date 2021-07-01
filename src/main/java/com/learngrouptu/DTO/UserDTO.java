package com.learngrouptu.DTO;

import com.learngrouptu.Exceptions.AbschlussNotAllowedException;
import com.learngrouptu.models.Annonce;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {
    private Integer userID;

    private String username;

    private String email;

    private String password;

    private Set<Annonce> userAnnoncen = new HashSet<>();

    private String studiengang;

    private String abschluss;

    private String altesPassword;

    private String bio;

    public UserDTO(){
        super();
    }

    public UserDTO(Integer userID, String username, String email, String password){

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
        if (!(abschluss.equals("Bachelor") || abschluss.equals("Master") || abschluss.equals("Keine Angabe") || abschluss.equals("Diplom"))) {
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
