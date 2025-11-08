package com.rununit.rununit.infrastructure.security;

import com.rununit.rununit.domain.entities.Login;
import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.infrastructure.repositories.LoginRepository;
import com.rununit.rununit.infrastructure.security.AuthUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginRepository loginRepository;
    private final UserRepository userRepository;

    public CustomUserDetailsService(LoginRepository loginRepository, UserRepository userRepository) {
        this.loginRepository = loginRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Login login = loginRepository.findByEmail(email) // O método deve existir no seu LoginRepository
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        User user = userRepository.findByLoginEmail(email) // Método findByLogin deve ser implementado no UserRepository
                .orElseThrow(() -> new UsernameNotFoundException("Dados do usuário não encontrados para o email: " + email));

        return new AuthUser(user);
    }
}
