package org.backend.board.web;

import lombok.RequiredArgsConstructor;
import org.backend.board.web.dto.PostsResponseDto;
import org.backend.board.web.dto.PostsSaveRequestDto;
import org.backend.board.web.dto.PostsUpdateRequestDto;
import org.backend.board.web.service.PostsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping(value = "/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto.toEntity());
    }

    @PutMapping(value = "/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping(value = "/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }
}
