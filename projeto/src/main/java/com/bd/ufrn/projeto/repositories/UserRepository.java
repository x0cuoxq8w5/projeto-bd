package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    public void save(User newUser) {
        
    }

    public User findById(Long id) {
        return null;
    }

    public User findByEmail(String email) {
        return null;
    }

    public void delete(User user) {

    }

    public List<User> findAll() {
        return null;
    }
}
