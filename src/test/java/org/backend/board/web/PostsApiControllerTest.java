package org.backend.board.web;

import org.backend.board.domain.posts.Posts;
import org.backend.board.domain.posts.PostsRepository;
import org.backend.board.web.dto.PostsResponseDto;
import org.backend.board.web.dto.PostsSaveRequestDto;
import org.backend.board.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void cleanUp() {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() throws Exception {
        //given
        String title = "test";
        String content = "Test Start";
        String author = "Test@test.com";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .author(author)
                .title(title)
                .content(content)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> postsList = postsRepository.findAll();
        Posts post = postsList.get(0);

        assertThat(post.getAuthor()).isEqualTo(author);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);

    }

    @Test
    public void posts_수정한다() {
        //given
        String title = "test";
        String content = "Test Start";
        String author = "Test@test.com";

        Posts posts = postsRepository.save(Posts.builder()
                .author(author)
                .title(title)
                .content(content)
                .build());

        String updateTitle = "test2";
        String updateContent = "test2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(updateTitle)
                .content(updateContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + posts.getId();

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                Long.class
        );

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> postsList = postsRepository.findAll();
        Posts posts1 = postsList.get(0);

        assertThat(posts1.getTitle()).isEqualTo(updateTitle);
        assertThat(posts1.getContent()).isEqualTo(updateContent);
    }

    @Test
    public void posts_조회한다() {
        //given
        String title = "test";
        String content = "Test Start";
        String author = "Test@test.com";

        Posts posts = postsRepository.save(Posts.builder()
                .author(author)
                .title(title)
                .content(content)
                .build());

        String url = "http://localhost:" + port + "/api/v1/posts/" + posts.getId();

        //when
        ResponseEntity<PostsResponseDto> responseEntity =
                restTemplate.getForEntity(url, PostsResponseDto.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(title);
        assertThat(responseEntity.getBody().getContent()).isEqualTo(content);
        assertThat(responseEntity.getBody().getAuthor()).isEqualTo(author);
    }
}
