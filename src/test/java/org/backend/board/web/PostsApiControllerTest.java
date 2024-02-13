package org.backend.board.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.board.domain.posts.Posts;
import org.backend.board.domain.posts.PostsRepository;
import org.backend.board.web.dto.PostsResponseDto;
import org.backend.board.web.dto.PostsSaveRequestDto;
import org.backend.board.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void cleanUp() {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="MEMBER")
    public void Posts_등록된다() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

//        ResponseEntity<Long> responseEntity = restTemplate
//                .withBasicAuth("test","test")
//                .postForEntity(url, requestDto, Long.class);

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @WithMockUser(roles = "MEMBER")
    @Test
    public void posts_수정한다() throws Exception {
        //given
        String title = "test";
        String content = "Test Start";
        String author = "Test@test.com";

        System.out.println("안녕하세요.");

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

//        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

//        ResponseEntity<Long> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.PUT,
//                requestEntity,
//                Long.class
//        );

        //then
        List<Posts> postsList = postsRepository.findAll();
        Posts posts1 = postsList.get(0);

        assertThat(posts1.getTitle()).isEqualTo(updateTitle);
        assertThat(posts1.getContent()).isEqualTo(updateContent);
    }

    @WithMockUser(roles = "MEMBER")
    @Test
    public void posts_조회한다() throws Exception {
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
        mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.author").value(author));



//        ResponseEntity<PostsResponseDto> responseEntity =
//                restTemplate.getForEntity(url, PostsResponseDto.class);

        //then


//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody().getTitle()).isEqualTo(title);
//        assertThat(responseEntity.getBody().getContent()).isEqualTo(content);
//        assertThat(responseEntity.getBody().getAuthor()).isEqualTo(author);
    }
}
