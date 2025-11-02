package domain.services;

import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.domain.entities.Login;
import com.rununit.rununit.domain.entities.MembershipType;
import com.rununit.rununit.domain.enums.Gender;
import com.rununit.rununit.domain.enums.UserRole;
import com.rununit.rununit.domain.services.UserService;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import com.rununit.rununit.infrastructure.repositories.LoginRepository;
import com.rununit.rununit.infrastructure.repositories.MembershipTypeRepository;
import com.rununit.rununit.web.dto.user.UserCreationRequestDto;
import com.rununit.rununit.web.dto.user.UserUpdateRequestDto;
import com.rununit.rununit.web.dto.user.PasswordUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private MembershipTypeRepository membershipTypeRepository;

    @InjectMocks
    private UserService userService;

    private UserCreationRequestDto createCreationRequest() {

        return new UserCreationRequestDto(
                "Novo",
                "Corredor",
                LocalDate.of(2000,1,1),
                Gender.M,
                "America/Sao_Paulo",
                "pt-BR", // locale
                "novo@teste.com",
                "senha123"
        );
    }

    private MembershipType mockMembership;

    @BeforeEach
    void setup() {
        mockMembership = new MembershipType();

    }

    @Test
    void createUser_shouldCreateUserAndLoginSuccessfully() {
        UserCreationRequestDto requestDto = createCreationRequest();
        Long newUserId = 10L;

        User userToBeSaved = Mockito.mock(User.class);
        when(userToBeSaved.getId()).thenReturn(newUserId);
        when(userToBeSaved.getLogin()).thenReturn(new Login());
        when(userToBeSaved.getUserRole()).thenReturn(UserRole.USER);

        when(loginRepository.existsByEmail(requestDto.email())).thenReturn(false);

        when(membershipTypeRepository.findById(1L)).thenReturn(Optional.of(mockMembership));

        when(userRepository.save(any(User.class))).thenReturn(userToBeSaved);

        when(loginRepository.save(any(Login.class))).thenReturn(new Login());

        userService.createUser(requestDto);

        verify(membershipTypeRepository, times(1)).findById(1L);

        verify(userRepository, times(2)).save(any(User.class));

        verify(loginRepository, times(1)).save(any(Login.class));
    }

    @Test
    void createUser_shouldThrowExceptionIfEmailExists() {
        UserCreationRequestDto requestDto = createCreationRequest();

        when(loginRepository.existsByEmail(requestDto.email())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(requestDto);
        });

        assertEquals("Email already registered", exception.getMessage());

        verify(userRepository, never()).save(any());
        verify(membershipTypeRepository, never()).findById(any());
    }


    @Test
    void updateProfile_shouldUpdateOnlyProvidedFields() {
        Long userId = 1L;
        User existingUser = Mockito.mock(User.class);
        when(existingUser.getId()).thenReturn(userId);
        when(existingUser.getName()).thenReturn("OldName");
        when(existingUser.getLastName()).thenReturn("OldLastName");
        when(existingUser.getTotalRunningDistance()).thenReturn(BigDecimal.valueOf(100.5));
        when(existingUser.getGender()).thenReturn(Gender.F);

        UserUpdateRequestDto updateDto = new UserUpdateRequestDto(
                "NewName",
                null,
                null,
                Gender.M,
                null, null, null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class))).thenAnswer(i -> {

            verify(existingUser).setName("NewName");
            verify(existingUser).setGender(Gender.M);

            return existingUser;
        });

        userService.updateProfile(userId, updateDto);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));

        verify(existingUser, never()).setLastName(anyString());
        verify(existingUser, never()).setBirthDate(any(LocalDate.class));
        verify(existingUser, never()).setTimezone(anyString());
    }

    @Test
    void updatePassword_shouldThrowExceptionIfPasswordsDoNotMatch() {
        Long userId = 1L;
        PasswordUpdateRequestDto requestDto = new PasswordUpdateRequestDto(
                "currentPass",
                "newPass",
                "mismatch"
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(userId, requestDto);
        });

        assertEquals("New password and confirmation do not match.", exception.getMessage());

        verify(userRepository, never()).findById(any());
    }
}
