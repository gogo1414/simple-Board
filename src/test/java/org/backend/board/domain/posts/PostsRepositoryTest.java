package org.backend.board.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void cleanUp() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {

        //given
        String title = "test";
        String content = "content";
        String author = "test2";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author(author).build());
        //when
        List<Posts> postsList = postsRepository.findAll();

        Posts post = postsList.get(0);
        //then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getAuthor()).isEqualTo(author);
        assertThat(post.getContent()).isEqualTo(content);

    }

    @Test
    public void BaseTimeEntity_등록() {
        //given
        LocalDateTime now =
                LocalDateTime.of(2024,02,11,0,0,0,0);

        postsRepository.save(Posts.builder()
                .author("test")
                .title("test")
                .content("test")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        Posts post = postsList.get(0);

        //then
        assertThat(post.getCreatedTime()).isAfter(now);
        assertThat(post.getContent()).isEqualTo("test");

    }
}
