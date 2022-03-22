package com.educavalieri.dscatolog.dto;

import com.educavalieri.dscatolog.services.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

    private String password;

    private UserInsertDTO(){
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
