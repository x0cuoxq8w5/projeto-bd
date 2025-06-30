package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.configs.SecurityConfiguration;
import com.bd.ufrn.projeto.dtos.RecoveryJwtTokenDto;
import com.bd.ufrn.projeto.dtos.UserDTO;
import com.bd.ufrn.projeto.models.User;
import com.bd.ufrn.projeto.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    protected SecurityConfiguration securityConfiguration;
    public RecoveryJwtTokenDto authenticateUser(UserDTO userDTO) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.email(), userDTO.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        User user = (User) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(user));
    }

    public User createUser(UserDTO userDTO) {

        User newUser = User.builder()
                .name(userDTO.name())
                .email(userDTO.email())
                .password(securityConfiguration.passwordEncoder().encode(userDTO.password()))
                .build();

        userRepository.save(newUser);
        return newUser;
    }

    public User findUser(Integer id) {
        return userRepository.findById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUserById(Long id, UserDTO userDTO) {
        User user = findUser(id);
        if (userDTO.name() != null) user.setName(userDTO.name());
        if (userDTO.email() != null) user.setEmail(userDTO.email());
        if (userDTO.password() != null) user.setPassword(securityConfiguration.passwordEncoder().encode(userDTO.password()));
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.delete(findUser(id));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
