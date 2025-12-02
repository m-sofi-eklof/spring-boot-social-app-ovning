package se.jensen.sofi_n.social_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private List<String> posts = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<String>> getPosts() {
        if(posts.isEmpty()) {
            //returnera ett svar med statuskod 304 No Content + tom body
            return ResponseEntity.noContent().build();
        }
        //returnera status 200 OK + listan posts som JSON array
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{index}")
    public ResponseEntity<String> getPost(@PathVariable int index) {
        if (index<0 || index >= posts.size()){
            //returnera status 404 not found + body med felsträng
            return ResponseEntity.status(404).body("ogiltigt index: "+index);
        }
        //returnera status 200 OK + inlägget av indexet index
        return ResponseEntity.ok(posts.get(index));
    }

    @PostMapping
    public ResponseEntity<String> addPost(@RequestBody String post) {
        posts.add(post);
        return ResponseEntity.ok("Post added: (" + post + ")");
    }

    @PutMapping("/{index}")
    public ResponseEntity<String> updatePost(@PathVariable int index, @RequestBody String newPost) {
        if (index<0 || index >= posts.size()){
            //returnera status 404 Not Found + felsträng som body
            return ResponseEntity.status(404).body("ogiltigt index: "+index);
        }
        String oldPost = posts.get(index);
        posts.set(index, newPost);
        //returnera status 200 OK + infosträng som body
        return ResponseEntity.ok("Post updated (from:" + oldPost + " to:" + newPost + ")");
    }

    @DeleteMapping("/{index}")
    public ResponseEntity<String> deletePost(@PathVariable int index) {
        if (index<0 || index >= posts.size()){
            //returnera status 404 Not Found + felsträng som body
            return ResponseEntity.status(404).body("ogiltigt index: "+index);
        }
        String deletedPost = posts.get(index);
        posts.remove(index);
        //Returnera status 200 OK + infosträng som body
        return ResponseEntity.ok("Post deleted (" + deletedPost + ")");
    }
}
