package com.codeit.data.service;


import com.codeit.data.dto.base.SlicePageResponse;
import com.codeit.data.dto.comment.CommentCreateRequest;
import com.codeit.data.dto.comment.CommentResponse;
import com.codeit.data.entity.Comment;
import com.codeit.data.entity.Post;
import com.codeit.data.entity.User;
import com.codeit.data.mapper.CommentMapper;
import com.codeit.data.mapper.PageResponseMapper;
import com.codeit.data.repository.CommentRepository;
import com.codeit.data.repository.PostRepository;
import com.codeit.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceV2 {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final PageResponseMapper pageResponseMapper;

    // mapper를 통한 response 전환 예제

    // 댓글 등록
    @Transactional
    public CommentResponse saveComment(CommentCreateRequest request) {
        Optional<Post> post = postRepository.findById(request.getPostId());
        post.orElseThrow(() ->new NoSuchElementException("post not found : " + request.getPostId()));

        Optional<User> user = userRepository.findById(request.getAuthorId());
        user.orElseThrow(() -> new NoSuchElementException("user not found : " + request.getAuthorId()));

        Comment comment = commentMapper.toEntity(request, user.get(), post.get());
        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponse(saved);
    }


    // 댓글 단건 조회
    @Transactional(readOnly = true) // select절
    public CommentResponse findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() ->new NoSuchElementException("comment not found : " + id));
        return commentMapper.toResponse(comment);
    }


    // 전체 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> findAll() {
        return commentRepository.findAll()
                .stream().map(commentMapper::toResponse)
                .toList();
    }

    // 특정 블로그에 달린 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    // 특정 작성자가 단 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> findByAuthorId(Long authorId) {
        return commentRepository.findByAuthorId(authorId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    // 댓글 삭제
    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }


    // slice 적용 예제
    @Transactional(readOnly = true)
    public SlicePageResponse<CommentResponse> findAllSlice(int size, Long cursor) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));

        Slice<Comment> slice = (cursor == null)
                ? commentRepository.findAllByOrderByIdDesc(pageable)
                : commentRepository.findAllByIdLessThanOrderByIdDesc(cursor, pageable);

        List<CommentResponse> items = slice.stream()
                .map(commentMapper::toResponse)
                .toList();

        Long nextCursor = (slice.hasNext() && !items.isEmpty())
                ? items.get(items.size() - 1).getId()
                : null;

        return pageResponseMapper.toSlice(slice, items, nextCursor);
    }

}
