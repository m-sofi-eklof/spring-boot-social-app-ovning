package se.jensen.sofi_n.social_app.dto;

import java.util.List;

public record UserWithPostResponseDTO(
        UserResponseDTO user,
        List<PostResponseDTO> posts
) {}
