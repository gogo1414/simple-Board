package org.backend.board.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.board.config.oauth.LoginUser;
import org.backend.board.config.oauth.dto.SessionMember;
import org.backend.board.web.dto.PostsResponseDto;
import org.backend.board.web.service.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping(value = "/")
    public String index(Model model, @LoginUser SessionMember member) {
        model.addAttribute("posts", postsService.findAllDesc());

        if(member != null) {
            model.addAttribute("memberName", member.getName());
        }

        return "index";
    }

    @GetMapping(value = "/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
