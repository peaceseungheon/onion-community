package com.onion.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpUser {

    private String name;
    private String password;
    private String email;

}
