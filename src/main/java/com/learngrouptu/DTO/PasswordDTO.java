package com.learngrouptu.DTO;


public class PasswordDTO {
    private String altesPassword;
    private String altesPassword1;
    private String neuesPassword;
    private String neuesPassword1;

    public PasswordDTO() {super();
    }

    public PasswordDTO(String altesPassword, String altesPassword1, String neuesPassword, String neuesPassword1) {
        super();
        this.altesPassword = altesPassword;
        this.altesPassword1 = altesPassword1;
        this.neuesPassword = neuesPassword;
        this.neuesPassword1 = neuesPassword1;
    }

    public String getAltesPassword() {
        return altesPassword;
    }

    public void setAltesPassword(String altesPassword) {
        this.altesPassword = altesPassword;
    }

    public String getAltesPassword1() {
        return altesPassword1;
    }

    public void setAltesPassword1(String altesPassword1) {
        this.altesPassword1 = altesPassword1;
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
