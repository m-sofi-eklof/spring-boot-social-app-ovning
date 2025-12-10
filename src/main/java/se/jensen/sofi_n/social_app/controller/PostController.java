package se.jensen.sofi_n.social_app.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.sofi_n.social_app.dto.PostRequestDTO;
import se.jensen.sofi_n.social_app.model.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private List<Post> posts = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        if(posts.isEmpty()) {
            //returnera ett svar med statuskod 204 No Content + tom body
            return ResponseEntity.noContent().build();
        }
        //returnera status 200 OK + listan posts som JSON array
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{index}")
    public ResponseEntity<Post> getPost(@PathVariable int index) {
        if (index<0 || index >= posts.size()) {
            //returnera status 404 not found + body med felsträng
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //returnera status 200 OK + inlägget av indexet index
        return ResponseEntity.ok(posts.get(index));
    }

    @PostMapping
    public ResponseEntity<String> addPost(@Valid @RequestBody PostRequestDTO dto) {
        Post post = new Post(dto.text());
        post.setCreatedAt(LocalDateTime.now());
        posts.add(post);
        //returnera status 201 Created + infosträng som body
        return ResponseEntity.status(HttpStatus.CREATED).body("Post added: (" + post + ")");
    }

    @PutMapping("/{index}")
    public ResponseEntity<String> updatePost(@PathVariable int index, @Valid @RequestBody PostRequestDTO dto) {
        if (index<0 || index >= posts.size()){
            //returnera status 404 Not Found + felsträng som body
            return ResponseEntity.status(404).body("ogiltigt index: "+index);
        }
        String oldText = posts.get(index).getText();
        posts.get(index).setText(dto.text());
        //returnera status 200 OK + infosträng som body
        return ResponseEntity.ok("Post updated (from:" + oldText + " to:" + posts.get(index).getText() + ")");
    }

    @DeleteMapping("/{index}")
    public ResponseEntity<String> deletePost(@PathVariable int index) {
        if (index<0 || index >= posts.size()){
            //returnera status 404 Not Found + felsträng som body
            return ResponseEntity.status(404).body("ogiltigt index: "+index);
        }
        Post deletedPost = posts.get(index);
        posts.remove(index);
        //Returnera status 200 OK + infosträng som body
        return ResponseEntity.ok("Post deleted (" + deletedPost + ")");
    }
}
