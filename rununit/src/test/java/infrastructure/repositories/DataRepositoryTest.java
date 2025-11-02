package infrastructure.repositories;

import com.rununit.rununit.domain.entities.Login;
import com.rununit.rununit.domain.entities.MembershipType;
import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.enums.Gender;
import com.rununit.rununit.domain.enums.UserRole;
import com.rununit.rununit.infrastructure.repositories.LoginRepository;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DataRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private TestEntityManager entityManager;

    private MembershipType defaultMembership;

    @BeforeEach
    void setup() {
        defaultMembership = new MembershipType();
        defaultMembership.setId(1L);
        defaultMembership.setName("PADRAO_TESTE");
        entityManager.persistAndFlush(defaultMembership);
    }

    private User buildTestUser(String email, String name) {
        Login login = new Login();
        login.setEmail(email);
        login.setPasswordHash("hashed_password_test");
        login.setUserId(null);

        User user = User.builder()
                .name(name)
                .lastName("Repository")
                .birthDate(LocalDate.of(1995, 1, 1))
                .gender(Gender.M)
                .timezone("America/Sao_Paulo")
                .locale("pt-BR")
                .userRole(UserRole.USER)
                .membershipType(defaultMembership)
                .totalRunningDistance(BigDecimal.ZERO)
                .totalRunningTime(0L)
                .active(true)
                .hasBiometrics(false)
                .build();

        user.setLogin(login);

        return user;
    }

    @Test
    void userRepository_shouldFindUserByLoginEmail_querySuccessful() {
        // 1. Arrange
        User user = buildTestUser("user.repo@test.com", "UserRepo");
        entityManager.persistAndFlush(user);

        // 2. Act
        Optional<User> foundUser = userRepository.findByLoginEmail("user.repo@test.com");

        // 3. Assert
        assertTrue(foundUser.isPresent(), "O usuário deve ser encontrado usando findByLoginEmail.");
        assertEquals("UserRepo", foundUser.get().getName());
        assertEquals("user.repo@test.com", foundUser.get().getLogin().getEmail(), "O email do Login deve ser o esperado.");
    }

    @Test
    void userRepository_shouldReturnEmptyWhenLoginEmailNotFound() {
        User user = buildTestUser("existente@test.com", "Ex");
        entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findByLoginEmail("nao_existe@test.com");


        assertFalse(foundUser.isPresent(), "Não deve encontrar usuário com email de login inexistente.");
    }


    @Test
    void loginRepository_shouldFindLoginByEmail() {
        String email = "login.repo@test.com";
        User user = buildTestUser(email, "LoginRepo");
        entityManager.persistAndFlush(user);

        Optional<Login> foundLogin = loginRepository.findByEmail(email);

        assertTrue(foundLogin.isPresent(), "O Login deve ser encontrado usando findByEmail.");
        assertEquals(email, foundLogin.get().getEmail());
        assertNotNull(foundLogin.get().getUser(), "O Login deve ter o objeto User associado (associação bidirecional).");
    }

    @Test
    void loginRepository_shouldReturnTrueWhenEmailExists() {
        String email = "exists@test.com";
        User user = buildTestUser(email, "Exists");
        entityManager.persistAndFlush(user);

        assertTrue(loginRepository.existsByEmail(email), "O método existsByEmail deve retornar true para um email existente.");
    }

    @Test
    void loginRepository_shouldReturnFalseWhenEmailDoesNotExist() {

        assertFalse(loginRepository.existsByEmail("nao.existe.nao@db.com"), "O método existsByEmail deve retornar false para um email inexistente.");
    }

    @Test
    void shouldPersistLoginWhenSavingUserDueToCascadeAll() {
        User user = buildTestUser("cascade@test.com", "Cascade");

        User savedUser = userRepository.save(user);

        entityManager.flush();

        Optional<Login> foundLogin = loginRepository.findByEmail("cascade@test.com");

        assertTrue(foundLogin.isPresent(), "O Login deve ser salvo automaticamente devido ao CascadeType.ALL no User.");
        assertEquals(savedUser.getId(), foundLogin.get().getUser().getId());
    }
}