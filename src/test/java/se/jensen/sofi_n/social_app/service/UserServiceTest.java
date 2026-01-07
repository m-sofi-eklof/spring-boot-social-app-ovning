package se.jensen.sofi_n.social_app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.jensen.sofi_n.social_app.dto.UserRequestDTO;
import se.jensen.sofi_n.social_app.dto.UserResponseDTO;
import se.jensen.sofi_n.social_app.model.User;
import se.jensen.sofi_n.social_app.repository.UserRepository;
import se.jensen.sofi_n.social_app.service.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateUserFromUserRequestDTO() {
        // arrange
        UserRequestDTO request = new UserRequestDTO(
                "testuser",                 // username
                "test@example.com",         // email
                "pass1234",               // password (minst 1 siffra, 8+ tecken)
                "USER",                     // role
                "Test Test",                // displayName
                "Test bio",                 // bio
                null                        // profileImagePath
        );

        // act
        UserResponseDTO response = userService.addUser(request);

        // assert på response‑DTO
        assertThat(response.id()).isNotNull();
        assertThat(response.username()).isEqualTo("testuser");
        assertThat(response.email()).isEqualTo("test@example.com");

        // verifiera mot databas
        User user = userRepository.findByUsernameIgnoreCase("testuser")
                .orElseThrow(() -> new IllegalStateException("User should exist in DB after addUser"));
        ;

        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isNotEqualTo("secret1234");
    }
}

