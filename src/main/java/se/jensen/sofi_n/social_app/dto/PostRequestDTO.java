package se.jensen.sofi_n.social_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequestDTO(
        @NotBlank(message = "Text får ej vara tom")
        @Size(min = 3, max = 200, message = "Text måste vara minst 3 och max 200 tecken")
        String text
){}
