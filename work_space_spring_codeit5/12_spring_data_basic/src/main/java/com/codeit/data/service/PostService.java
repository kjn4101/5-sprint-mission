package com.codeit.data.service;

import com.codeit.data.dto.base.SimplePageResponse;
import com.codeit.data.dto.post.PostCreateRequest;
import com.codeit.data.dto.post.PostPageRequest;
import com.codeit.data.dto.post.PostSearchRequest;
import com.codeit.data.dto.post.PostUpdateRequest;
import com.codeit.data.entity.Category;
import com.codeit.data.entity.Post;
import com.codeit.data.entity.User;
import com.codeit.data.repository.PostRepository;
import com.codeit.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post create(PostCreateRequest req) {
        User author = userRepository.findById(req.getAuthorId())
                .orElseThrow(()->new NoSuchElementException("User not found : " + req.getAuthorId()));

        Post post = req.toPost(author);
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("post not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        // 최신순 정렬해서 내보내기
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }


    @Transactional
    public Post update(Long id, PostUpdateRequest req) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("post not found: " + id));
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setCategory(req.getCategory());
        post.setTags(req.getTags());
        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        if (!postRepository.existsById(id)) return;
        postRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SimplePageResponse<Post> findPage(PostPageRequest pageReq, PostSearchRequest search) {
        // 페이징 처리
        int size = pageReq.getSize() > 0 ? pageReq.getSize() : 10;
        int page =  pageReq.getPage() > 0 ? pageReq.getPage() -1 : 0; // 1base -> 0base(jpa)

        // 페이지 객체 생성
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("id").descending().and(Sort.by("title").descending()));

        Page<Post> result = postRepository.searchPosts(search.getTitle(),
                                            search.getContent(), search.getNickname(), pageable);


        return SimplePageResponse.<Post>builder()
                .page(result.getNumber() + 1)      // 1-based page
                .size(result.getSize())
                .totalPages(result.getTotalPages())
                .totalItems(result.getTotalElements())
                .items(result.getContent())
                .build();
    }

    @Transactional(readOnly = true)
    public List<Post> findByAuthorId(Long authorId) {
        return postRepository.findByAuthor_IdOrderByIdDesc(authorId);
    }

    @Transactional(readOnly = true)
    public long count() {
        return postRepository.count();
    }


    public List<Post> findTitle(String title) {
        return postRepository.findByTitleContaining(title);
    }

    public List<Post> findTag(String tags) {
        return postRepository.findByTagsContainingIgnoreCase(tags);
    }

    public List<Post> findCategory(List<String> category) {
        List<Category> categories = category.stream()
                .map(s -> Category.valueOf(s.trim().toUpperCase())).toList();
      return postRepository.findByCategoryIn(categories);
    }

}
