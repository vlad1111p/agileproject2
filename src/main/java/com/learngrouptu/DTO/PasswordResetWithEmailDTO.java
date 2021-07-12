package com.learngrouptu.DTO;

public class PasswordResetWithEmailDTO {

    private String neuesPassword;
    private String neuesPassword1;
    private String email;


    public PasswordResetWithEmailDTO() {
        super();
    }

    public PasswordResetWithEmailDTO(String neuesPassword, String neuesPassword1, String email) {
        super();
        this.email = email;
        this.neuesPassword = neuesPassword;
        this.neuesPassword1 = neuesPassword1;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getNeuesPassword() {
        return neuesPassword;
    }

    public void setNeuesPassword(String neuesPassword) {
        this.neuesPassword = neuesPassword;
    }

    public String getNeuesPassword1() {
        return neuesPassword1;
    }

    public void setNeuesPassword1(String neuesPassword1) {
        this.neuesPassword1 = neuesPassword1;
    }


}
