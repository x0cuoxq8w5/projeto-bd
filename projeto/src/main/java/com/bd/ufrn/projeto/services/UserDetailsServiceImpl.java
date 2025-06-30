package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.models.User;
import com.bd.ufrn.projeto.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email);
    }
}

