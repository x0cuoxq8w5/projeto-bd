package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends AbstractRepository<User,Integer>{
    public User findByEmail(String email) {
        return null;
    }
}
