package com.learngrouptu.models;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
}
