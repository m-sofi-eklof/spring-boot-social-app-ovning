package se.jensen.sofi_n.social_app.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.sofi_n.social_app.dto.UserRequestDTO;
import se.jensen.sofi_n.social_app.dto.UserResponseDTO;
import se.jensen.sofi_n.social_app.model.User;
import se.jensen.sofi_n.social_app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){this.userRepository = userRepository;}

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<User> dbUsers = userRepository.findAll();

        if(dbUsers.isEmpty()) {
            //returnera ett svar med statuskod 204 No Content + tom body
            return ResponseEntity.noContent().build();
        }
        //returnera status 200 OK + listan users som UserResponseDTO -lista
        List<UserResponseDTO> results = dbUsers.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getDisplayName(),
                        user.getBio(),
                        user.getProfileImagePath()
                )).toList();
        return ResponseEntity.ok(results);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        //returnera användare och 200OK eller 404 not found
        return userRepository.findById(id)
                .map(user->new ResponseEntity<>(
                        new UserResponseDTO(
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                user.getRole(),
                                user.getDisplayName(),
                                user.getBio(),
                                user.getProfileImagePath()
                        ),
                        HttpStatus.OK
                )).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO dto) {
        //validera unikt användarnamn och email
        boolean usernameExists = userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(dto.username()));
        boolean emailExists = userRepository.findAll().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.email()));

        if (usernameExists) {
            //returnera 409 Conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (emailExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setRole("USER");
        user.setDisplayName(dto.username());//username som default
        user.setBio("");
        user.setProfileImagePath(null);

        User savedUser = userRepository.save(user);

        UserResponseDTO userResponseDTO = new UserResponseDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getDisplayName(),
                savedUser.getBio(),
                savedUser.getProfileImagePath()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        //kolla att användren finns
        Optional<User> optinalUser = userRepository.findById(id);
        if (optinalUser.isEmpty()) {
            //returnera 404 not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optinalUser.get();

        //validera unikt användarnamn och email (om ändrat)
        boolean usernameExists = userRepository.findAll().stream()
                .filter(u-> !u.getId().equals(id))//ignorera användaren som ska updateras
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(dto.username()));
        if (usernameExists) {
            //returnera 409 Conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        boolean emailExists = userRepository.findAll().stream()
                .filter(u-> !u.getId().equals(id))//ignorera användaren som skickas in vid jämföselse
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.email()));
        if (emailExists) {
            //returnera 409 Conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());

        User updatedUser = userRepository.save(user);
        UserResponseDTO userResponseDTO = new UserResponseDTO(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getRole(),
                updatedUser.getDisplayName(),
                updatedUser.getBio(),
                updatedUser.getProfileImagePath()
        );
        //returnera user och 200 ok
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable Long id) {
        Optional<User> optinalUser = userRepository.findById(id);
        if (optinalUser.isEmpty()) {
            //returnera 404 not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userRepository.deleteById(id);
        //returnera 204 No Content
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
