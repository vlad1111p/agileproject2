package com.learngrouptu.DTO;

public class RegisterDTO {


    private boolean userError;

    private boolean emailError;

    public RegisterDTO(boolean userError, boolean emailError) {
        this.userError = userError;
        this.emailError = emailError;
    }

    public boolean isUserError() {
        return userError;
    }

    public void setUserError(boolean userError) {
        this.userError = userError;
    }

    public boolean isEmailError() {
        return emailError;
    }

    public void setEmailError(boolean emailError) {
        this.emailError = emailError;
    }
}
