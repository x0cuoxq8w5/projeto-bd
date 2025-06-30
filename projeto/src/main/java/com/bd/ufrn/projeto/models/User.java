package com.bd.ufrn.projeto.models;

import com.bd.ufrn.projeto.enums.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {
    private int id;
    private String name;
    private String email;
    private String password;
    private List<UserRole> userRoles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of("USER").map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}
