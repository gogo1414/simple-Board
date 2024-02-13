package org.backend.board.config.oauth.dto;

import lombok.Getter;
import org.backend.board.domain.member.Member;

import java.io.Serializable;

@Getter
public class SessionMember implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionMember(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.picture = member.getPicture();
    }
}
