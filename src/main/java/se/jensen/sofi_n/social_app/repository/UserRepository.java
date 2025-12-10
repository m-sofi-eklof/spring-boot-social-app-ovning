package se.jensen.sofi_n.social_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.sofi_n.social_app.controller.UserController;
import se.jensen.sofi_n.social_app.dto.UserResponseDTO;
import se.jensen.sofi_n.social_app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {}
