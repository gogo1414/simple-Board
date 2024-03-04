package org.backend.board.web;

import org.aspectj.lang.annotation.RequiredTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName(value = "profile은 인증 없이 호출 가능하다.")
    public void checkProfileWithoutAuth() {

        //when
        String expectedProfile = "default";

        //given
        ResponseEntity<String> response = testRestTemplate.getForEntity("/profile", String.class);

        //then
        assertThat(response.getBody()).isEqualTo(expectedProfile);

    }

}
