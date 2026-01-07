package se.jensen.sofi_n.social_app.dto;

import java.time.LocalDateTime;

public record PostResponseDTO(
        Long id,
        String text,
        LocalDateTime createdAt
) {}
