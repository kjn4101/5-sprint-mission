package com.codeit.data.service;

import com.codeit.data.dto.comment.CommentCreateRequest;
import com.codeit.data.entity.Comment;
import com.codeit.data.entity.Post;
import com.codeit.data.entity.User;
import com.codeit.data.repository.CommentRepository;
import com.codeit.data.repository.PostRepository;
import com.codeit.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 등록
    @Transactional // 해당 메서드에서 runtime 예외가 발생하는 경우 동작했던 모든 쿼리를 롤백하는 어노테이션
    public Comment saveComment(CommentCreateRequest request) {
        Optional<Post> post = postRepository.findById(request.getPostId());
        post.orElseThrow(() ->new NoSuchElementException("post not found : " + request.getPostId()));

        Optional<User> user = userRepository.findById(request.getAuthorId());
        user.orElseThrow(() -> new NoSuchElementException("user not found : " + request.getAuthorId()));

        Comment comment = request.toComment(user.get(), post.get());
        return  commentRepository.save(comment);
    }

    // 댓글 단건 조회
    @Transactional(readOnly = true) // select절
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() ->new NoSuchElementException("comment not found : " + id));
    }

    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    // 특정 블로그에 달린 댓글 조회
    @Transactional(readOnly = true)
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // 특정 작성자가 단 댓글 조회
    @Transactional(readOnly = true)
    public List<Comment> findByAuthorId(Long authorId) {
        return commentRepository.findByAuthorId(authorId);
    }

    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}