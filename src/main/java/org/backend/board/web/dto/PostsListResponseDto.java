package org.backend.board.web.dto;

import lombok.Getter;
import org.backend.board.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;

    public PostsListResponseDto(Posts posts) {
        this.id = posts.getId();
        this.author = posts.getAuthor();
        this.title = posts.getTitle();
        this.modifiedDate = posts.getModifiedDate();
    }
}
