package se.jensen.sofi_n.social_app.service;

import org.springframework.stereotype.Service;
import se.jensen.sofi_n.social_app.dto.PostRequestDTO;
import se.jensen.sofi_n.social_app.dto.PostResponseDTO;
import se.jensen.sofi_n.social_app.mapper.PostMapper;
import se.jensen.sofi_n.social_app.model.Post;
import se.jensen.sofi_n.social_app.model.User;
import se.jensen.sofi_n.social_app.repository.PostRepository;
import se.jensen.sofi_n.social_app.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }
    public PostResponseDTO createPost(Long userId, PostRequestDTO dto) {
        //skapa post och sätt värden
        Post post = new Post();
        post.setText(dto.text());
        post.setCreatedAt(LocalDateTime.now());

        User user = userRepository.findById(userId)
                .orElseThrow(()->
                        new NoSuchElementException("Användare med detta id ej hittat" + userId));
        post.setUser(user);

        //spara och returnera
        Post savedPost = postRepository.save(post);

        return PostMapper.toDTO(savedPost);
    }

    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostMapper::toDTO)
                .toList();
    }

    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->
                        new NoSuchElementException("Inlägg med id " + id + "hittades ej"));
        return PostMapper.toDTO(post);
    }

    public PostResponseDTO updatePost(Long id, PostRequestDTO dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->
                        new NoSuchElementException("Inlägg med id " + id + "hittades ej"));
        post.setText(dto.text());

        Post updated = postRepository.save(post);
        return PostMapper.toDTO(updated);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Inlägg med id " + id + " hittades ej"));
        postRepository.delete(post);
    }

}
