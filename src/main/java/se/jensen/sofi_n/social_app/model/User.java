package se.jensen.sofi_n.social_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="app_user")
public class User {
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

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    //toString override
    @Override
    public String toString() {
        String maskedPassword = password != null ? "*".repeat(password.length()) : "";//maskerat lösenord **
        return "User{" +
                "id=" + id +
                ", username=" + username +
                ", password='" + maskedPassword +
                ", role=" + role +
                "}";
    }
}
