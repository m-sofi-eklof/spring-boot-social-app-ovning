package se.jensen.sofi_n.social_app.mapper;

import org.springframework.stereotype.Component;
import se.jensen.sofi_n.social_app.dto.PostRequestDTO;
import se.jensen.sofi_n.social_app.dto.PostResponseDTO;
import se.jensen.sofi_n.social_app.model.Post;

@Component
public class PostMapper {
    public PostResponseDTO toDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getText(),
                post.getCreatedAt());
    }

    public Post fromDTO(PostRequestDTO dto) {
        Post post = new Post();
        post.setText(dto.text());
        //createdAt sätts i service
        return post;
    }
}
