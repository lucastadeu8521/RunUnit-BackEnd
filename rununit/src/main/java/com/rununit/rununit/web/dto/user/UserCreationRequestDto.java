package com.rununit.rununit.web.dto.user;

import com.rununit.rununit.domain.enums.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserCreationRequestDto(

        @NotBlank(message = "O primeiro nome é obrigatório.")
        @Size(max = 100, message = "O primeiro nome não pode exceder 100 caracteres.")
        String name,

        @NotBlank(message = "O sobrenome é obrigatório.")
        @Size(max = 150, message = "O sobrenome não pode exceder 150 caracteres.")
        String lastName,

        @NotNull(message = "A data de nascimento é obrigatória.")
        @Past(message = "A data de nascimento deve ser no passado.")
        LocalDate birthDate,

        @NotNull(message = "O gênero é obrigatório.")
        Gender gender,

        @Size(max = 50, message = "O timezone não pode exceder 50 caracteres.")
        String timezone,

        @Size(max = 10, message = "O locale não pode exceder 10 caracteres.")
        String locale,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O email deve ser válido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres.")
        String password
) {}
