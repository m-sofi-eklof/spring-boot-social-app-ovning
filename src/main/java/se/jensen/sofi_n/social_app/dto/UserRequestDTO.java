package se.jensen.sofi_n.social_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/*
* public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;
    @Column(name = "display_name", nullable = false)
    private String displayName;
    @Column(nullable = false)
    private String bio;
    @Column(name = "profile_image_path")
    private String profileImagePath;

*/
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
        @Size(max=40, message = "Lösenord får ej överstiga 40 karaktärer")
        String password
) {}
