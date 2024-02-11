package org.backend.board.web.service;

import lombok.RequiredArgsConstructor;
import org.backend.board.domain.posts.Posts;
import org.backend.board.domain.posts.PostsRepository;
import org.backend.board.web.dto.PostsResponseDto;
import org.backend.board.web.dto.PostsSaveRequestDto;
import org.backend.board.web.dto.PostsUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(Posts posts) {
        return postsRepository.save(posts).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());
        return posts.getId();
    }

    @Transactional
    public PostsResponseDto findById(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id));

        return new PostsResponseDto(posts);
    }


}
