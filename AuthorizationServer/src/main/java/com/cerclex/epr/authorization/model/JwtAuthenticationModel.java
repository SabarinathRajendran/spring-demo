package com.cerclex.epr.authorization.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationModel {

    private String username;
    private String password;
}
