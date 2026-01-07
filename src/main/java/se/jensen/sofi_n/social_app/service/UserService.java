package se.jensen.sofi_n.social_app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserWithPostResponseDTO getUserWithPosts(Long id) {
        User user = userRepository.findUserWithPosts(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Användare med id " + id + " hittades ej"));

        List<PostResponseDTO> posts = user.getPosts().stream()
                .map(p -> new PostResponseDTO(
                        p.getId(),
                        p.getText(),
                        p.getCreatedAt()
                ))
                .toList();

        UserResponseDTO userDto = userMapper.toDTO(user);
        return new UserWithPostResponseDTO(userDto, posts);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() ->
                        new UserNotFoundException("Användare med id " + id + " hittades ej"));
    }

    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() ->
                        new UserNotFoundException("Användaren hittades inte"));
        return userMapper.toDTO(user);
    }

    public UserResponseDTO addUser(UserRequestDTO dto) {
        if (userRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new EmailExistsException("En användare med denna e-post finns redan i databasen");
        }
        if (userRepository.existsByUsernameIgnoreCase(dto.username())) {
            throw new UsernameExistsException("En användare med detta användarnamn finns redan i databasen");
        }

        User user = userMapper.fromDTO(dto);

        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword)); // hashar lösenordet före save [web:170][web:175]

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Användare med id " + id + " finns inte i databasen"));

        if (userRepository.existsByEmailIgnoreCaseAndIdNot(dto.email(), id)) {
            throw new EmailExistsException("E-posten är upptagen");
        }
        if (userRepository.existsByUsernameIgnoreCaseAndIdNot(dto.username(), id)) {
            throw new UsernameExistsException("Användarnamnet är upptaget");
        }

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        //todo encoda
        user.setPassword(dto.password());
        user.setRole(dto.role());
        user.setDisplayName(dto.displayName());
        user.setBio(dto.bio());
        user.setProfileImagePath(dto.profileImagePath());

        User updated = userRepository.save(user);
        return userMapper.toDTO(updated);
    }

    public UserResponseDTO deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Ingen användare med detta id finns i databasen");
        }

        User user = optionalUser.get();
        userRepository.delete(user);
        return userMapper.toDTO(user);
    }
}
