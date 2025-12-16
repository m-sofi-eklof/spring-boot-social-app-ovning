package se.jensen.sofi_n.social_app.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.sofi_n.social_app.dto.*;
import se.jensen.sofi_n.social_app.model.User;
import se.jensen.sofi_n.social_app.repository.UserRepository;
import se.jensen.sofi_n.social_app.service.PostService;
import se.jensen.sofi_n.social_app.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService, PostService postService){
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> dbUsers = userService.getAllUsers();

        //returnera 200 ok + lista eller 204 No Content
        return !dbUsers.isEmpty() ? ResponseEntity.ok(dbUsers) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        //returnera användare + 200 OK
        return ResponseEntity.ok(user);
    }

    @GetMapping("{id}/with-posts")
    public ResponseEntity<UserWithPostResponseDTO> getUsersWithPosts(@PathVariable Long id) {
        UserWithPostResponseDTO response = userService.getUserWithPosts(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO userResponseDTO = userService.addUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<PostResponseDTO> createPostForUser(
            @PathVariable Long userId,
            @Valid @RequestBody PostRequestDTO request) {
        //skapa post från dto med userId
        PostResponseDTO response = postService.createPost(userId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO responseDTO = userService.updateUser(id, dto);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
