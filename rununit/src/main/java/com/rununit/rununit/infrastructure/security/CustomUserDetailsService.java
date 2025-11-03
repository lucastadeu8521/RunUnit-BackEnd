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
        // 1. Buscar o Login pelo email
        Login login = loginRepository.findByEmail(email) // O método deve existir no seu LoginRepository
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // 2. Buscar o User associado ao Login (assumindo que Login tem uma FK ou ID de User)
        // Se a entidade Login tem um link direto (como login.getUser()), use-o.
        // Se não, busque o User pelo ID do Login
        User user = userRepository.findByLoginEmail(email) // Método findByLogin deve ser implementado no UserRepository
                .orElseThrow(() -> new UsernameNotFoundException("Dados do usuário não encontrados para o email: " + email));


        // 3. Ponto CRÍTICO: Retornar AuthUser
        return new AuthUser(user);
    }
}
