package org.backend.board.config.oauth.dto;

import lombok.Getter;
import org.backend.board.domain.member.Members;

import java.io.Serializable;

@Getter
public class SessionMember implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionMember(Members members) {
        this.name = members.getName();
        this.email = members.getEmail();
        this.picture = members.getPicture();
    }
}
