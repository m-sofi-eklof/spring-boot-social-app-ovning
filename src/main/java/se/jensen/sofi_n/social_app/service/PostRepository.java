package se.jensen.sofi_n.social_app.service;

import org.springframework.data.repository.Repository;
import se.jensen.sofi_n.social_app.model.Post;

interface PostRepository extends Repository<Post, Long> {
}
