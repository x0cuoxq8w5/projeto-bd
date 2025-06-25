package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.RecoveryJwtTokenDto;
import com.bd.ufrn.projeto.dtos.UserDTO;
import com.bd.ufrn.projeto.models.User;
import com.bd.ufrn.projeto.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody UserDTO loginUserDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDto) {
        userService.createUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.findUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/find-by-email")
    public ResponseEntity<User> findUserByEmail(@RequestParam String email){
        User user = userService.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable Long id, @RequestBody UserDTO user){
        userService.updateUserById(id,user);
        return new ResponseEntity<>("User atualizado com sucesso", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);
        return new ResponseEntity<>("User deletado com sucesso", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/test")
    public ResponseEntity<User> getAuthenticationTest(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
}
