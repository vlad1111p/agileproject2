package com.learngrouptu.DTO;

public class PasswordResetDTO {
    private String usermail;

    public PasswordResetDTO(String usermail) {
        super();
        this.usermail = usermail;
    }

    public PasswordResetDTO() {
        super();
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }
}
