package se.jensen.sofi_n.social_app.model;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private String text;
    LocalDateTime createdAt;

    public Post(String text) {
        id = 0L;
        this.text = text;
        this.createdAt = null;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getText() {return text;}
    public void setText(String text) {this.text = text;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

}
