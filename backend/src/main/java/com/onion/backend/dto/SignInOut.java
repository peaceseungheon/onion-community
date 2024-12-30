package com.onion.backend.dto;

import lombok.Getter;

@Getter
public class SignInOut {

    private String jwt;
    private Long userNo;
    private String userName;

    public SignInOut(String jwt, Long userNo, String userName){
        this.jwt = jwt;
        this.userNo = userNo;
        this.userName = userName;
    }
}
