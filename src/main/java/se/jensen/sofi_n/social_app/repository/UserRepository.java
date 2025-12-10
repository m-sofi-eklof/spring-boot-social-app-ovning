package se.jensen.sofi_n.social_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.jensen.sofi_n.social_app.controller.UserController;
import se.jensen.sofi_n.social_app.dto.UserResponseDTO;
import se.jensen.sofi_n.social_app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);


    boolean existsByUsernameIgnoreCaseAndIdNot(String username, Long id);
    boolean existsByEmailIgnoreCaseAndIdNot(String username, Long id);
}
