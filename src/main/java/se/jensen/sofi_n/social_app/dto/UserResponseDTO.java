package se.jensen.sofi_n.social_app.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(Long id, String username, String email, String role, String displayName, String bio, String profileImagePath) {}