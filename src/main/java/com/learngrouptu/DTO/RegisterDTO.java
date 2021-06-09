package com.learngrouptu.DTO;

public class RegisterDTO {


    private boolean userError;

    private boolean emailError;

    private boolean passwordError;

    public RegisterDTO(boolean userError, boolean emailError, boolean passswordError) {
        this.userError = userError;
        this.emailError = emailError;
        this.passwordError = passswordError;
    }

    public boolean isUserError() { return userError; }

    public void setUserError(boolean userError) {
        this.userError = userError;
    }

    public boolean isEmailError() {
        return emailError;
    }

    public void setEmailError(boolean emailError) {
        this.emailError = emailError;
    }

    public boolean isPasswordError() { return passwordError; }

    public void setPasswordError(boolean passwordError){this.passwordError = passwordError;}
}
