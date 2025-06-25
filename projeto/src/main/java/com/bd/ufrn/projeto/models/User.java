package com.bd.ufrn.projeto.models;

import com.bd.ufrn.projeto.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private List<UserRole> userRoles;
}
