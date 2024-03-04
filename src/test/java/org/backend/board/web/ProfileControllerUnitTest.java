package org.backend.board.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileControllerUnitTest {

    @Test
    @DisplayName(value = "real profile 조회")
    public void checkRealProfile() {

        //given
        String expectetdProfile = "real";
        MockEnvironment env = new MockEnvironment();

        env.addActiveProfile(expectetdProfile);
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(env);

        //when
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectetdProfile);
    }

    @Test
    @DisplayName(value = "active profile이 없으면 default가 조회된다.")
    public void checkDefault() {

        //given
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();
        ProfileController controller = new ProfileController(env);

        //when
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}
