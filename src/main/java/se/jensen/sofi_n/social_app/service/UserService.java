package se.jensen.sofi_n.social_app.service;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import se.jensen.sofi_n.social_app.dto.PostResponseDTO;
import se.jensen.sofi_n.social_app.dto.UserRequestDTO;
import se.jensen.sofi_n.social_app.dto.UserResponseDTO;
import se.jensen.sofi_n.social_app.dto.UserWithPostResponseDTO;
import se.jensen.sofi_n.social_app.exception.EmailExistsException;
import se.jensen.sofi_n.social_app.exception.UserNotFoundException;
import se.jensen.sofi_n.social_app.exception.UsernameExistsException;
import se.jensen.sofi_n.social_app.mapper.UserMapper;
import se.jensen.sofi_n.social_app.model.User;
import se.jensen.sofi_n.social_app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserWithPostResponseDTO getUserWithPosts(Long id) {
        User user = userRepository.findUserWithPosts(id)
                .orElseThrow(()->
                        new UserNotFoundException("Användare med id " + id + " hittades ej"));

        //konverter posts till dto
        List<PostResponseDTO> posts = user.getPosts().stream()
                .map(p-> new PostResponseDTO(
                        p.getId(),
                        p.getText(),
                        p.getCreatedAt()
                )).toList();
        //konverter user till dto
        UserResponseDTO userDto = UserMapper.toDTO(user);

        //slå ihop och returnera
        return new UserWithPostResponseDTO(userDto, posts);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDTO).toList();
    }

    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO).orElseThrow(()->new UserNotFoundException("Användaren hittades inte"));
    }

    public UserResponseDTO addUser(UserRequestDTO dto) {
        //kontrollera unik nvändarnamn o email
        if(userRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new EmailExistsException("En användare med denna e-post finns redan i databasen");
        }
        if(userRepository.existsByUsernameIgnoreCase(dto.username())) {
            throw new UsernameExistsException("En användare med detta användarnamn finns redan i databasen");
        }
        //konvertera
        User user = UserMapper.fromDTO(dto);
        //hasha lösen
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        //spara
        User savedUser = userRepository.save(user);
        //returnera som dto
        return UserMapper.toDTO(savedUser);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        //kontrollera om användare finns
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("Användare med detta id finns inte i databasen"));

        //förhindra försök att ändra till upptaget användarnamn eller email
        if(userRepository.existsByEmailIgnoreCaseAndIdNot(dto.email(), id)) {
            throw new EmailExistsException("E-posten är upptagen");
        }
        if(userRepository.existsByUsernameIgnoreCaseAndIdNot(dto.username(), id)) {
            throw new UsernameExistsException("Användarnamnet är upptaget");
        }

        //uppdatera användare
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setRole(dto.role());
        user.setDisplayName(dto.displayName());
        user.setBio(dto.bio());
        user.setProfileImagePath(dto.profileImagePath());

        // spara och returnera
        User updated = userRepository.save(user);
        return UserMapper.toDTO(updated);
    }

    public UserResponseDTO deleteUser(Long id) {
        //leta efter användare
        Optional<User> optinalUser = userRepository.findById(id);
        if (optinalUser.isEmpty()) {
            throw new UserNotFoundException("Ingen användare med detta id finns i databasen");
        }
        //hämta, radera från db, returnera som dto
        User user = optinalUser.get();
        userRepository.delete(user);
        return UserMapper.toDTO(user);
    }
}
