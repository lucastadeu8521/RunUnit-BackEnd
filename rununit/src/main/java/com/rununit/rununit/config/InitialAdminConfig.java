package com.rununit.rununit.config;

import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.entities.Login;
import com.rununit.rununit.domain.entities.MembershipType; // 👈 Import necessário
import com.rununit.rununit.domain.enums.Gender;
import com.rununit.rununit.domain.enums.UserRole;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.infrastructure.repositories.MembershipTypeRepository; // 👈 Import necessário
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class InitialAdminConfig {

    private static final String DEFAULT_MEMBERSHIP_NAME = "BASIC";

    @Bean
    public CommandLineRunner createInitialAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            MembershipTypeRepository membershipTypeRepository
    ) {
        return args -> {
            final String ADMIN_EMAIL = "admin@rununit.com";

            MembershipType defaultMembership = membershipTypeRepository.findByNameIgnoreCase(DEFAULT_MEMBERSHIP_NAME)
                    .orElseGet(() -> {
                        MembershipType basic = new MembershipType(
                                DEFAULT_MEMBERSHIP_NAME,
                                BigDecimal.ZERO,
                                "Default free membership level for all new users."
                        );
                        System.out.println("⚠Criando MembershipType padrão (" + DEFAULT_MEMBERSHIP_NAME + ") antes do Admin.");
                        return membershipTypeRepository.save(basic);
                    });


            if (userRepository.findByLoginEmail(ADMIN_EMAIL).isEmpty()) {

                User adminUser = new User();

                adminUser.setLogin(new Login());
                adminUser.getLogin().setEmail(ADMIN_EMAIL);

                adminUser.setName("Admin");
                adminUser.setLastName("Master");

                adminUser.setUserRole(UserRole.ADMIN);

                adminUser.setPassword(passwordEncoder.encode("RunUnitAdmin123"));

                adminUser.setBirthDate(LocalDate.of(1990, 1, 1));
                adminUser.setGender(Gender.O);

                adminUser.setMembershipType(defaultMembership);

                adminUser.setTotalRunningDistance(BigDecimal.ZERO);
                adminUser.setTotalRunningTime(0L);
                adminUser.setActive(true);
                adminUser.setTimezone("America/Sao_Paulo");
                adminUser.setLocale("pt-BR");

                userRepository.save(adminUser);
                System.out.println(" Usuário ADMIN inicial criado: " + ADMIN_EMAIL);
            }
        };
    }
}
