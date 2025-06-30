package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends AbstractRepository<User> implements StrongEntity<User,Integer> {
    //Que nem findbyid só que com string
    public User findByEmail(String email) {
        return null;
    }
}
