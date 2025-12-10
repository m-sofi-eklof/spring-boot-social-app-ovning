package se.jensen.sofi_n.social_app.dto;

import java.time.LocalDateTime;

public record PostResponseDTO(int id, String text, LocalDateTime createdAt) {}
