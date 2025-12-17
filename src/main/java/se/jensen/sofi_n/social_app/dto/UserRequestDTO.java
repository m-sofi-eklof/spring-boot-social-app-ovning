package se.jensen.sofi_n.social_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Användarnamn får ej vara tomt")
        @Size(min=3, max = 50, message = "Användarnamn måste vra 3-30 tecken")
        String username,

        @NotBlank(message = "E-post får ej vara tom")
        @Email(message = "Ogiltig e-postadress")
        @Size(max = 60, message = "E-post får ej överstiga 60 tecken")
        String email,

        @NotBlank
        @Pattern(
                regexp = "^(?=.*\\d).{8,25}$",
                message = "Lösenordmåste vara 8-25 tecken, minst 1 siffra"
        )
        @Size(max=255, message = "Lösenord får ej överstiga 255 karaktärer")
        String password,

        @NotBlank(message = "Roll får ej vara tom")
        String role,

        @NotBlank(message = "Display name får ej vara tom")
        @Size(max = 30, message = "Display name får ej överstiga 30 tecken")
        String displayName,

        @NotBlank(message = "Bio får ej vara tom")
        @Size(max = 200, message = "Bio får ej överstiga 200 tecken")
        String bio,

        @Size(max = 100, message = "Bildsökväg får ej överstiga 100 tecken")
        String profileImagePath

) {}
