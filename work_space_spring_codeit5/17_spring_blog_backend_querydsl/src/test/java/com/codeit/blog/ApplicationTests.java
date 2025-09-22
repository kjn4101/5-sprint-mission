package com.codeit.blog;

import com.codeit.blog.dto.post.PostFlatDto;
import com.codeit.blog.entity.Category;
import com.codeit.blog.entity.Post;
import com.codeit.blog.entity.User;
import com.codeit.blog.repository.PostRepository;
import com.codeit.blog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void UserRepositoryTest() {
        Optional<User> user1 = userRepository.findByIdQ(1L);
        System.out.println("@@findByIdQ : " + user1.get());
        assert user1.isPresent();

        Optional<User> user2 = userRepository.findByLoginId("test01"); // test01@email.com
        System.out.println("@@findByLoginId : " + user2.get());
        assert user2.isPresent();

        List<User> users = userRepository.findAllOrderByCreatedDesc();
        System.out.println("@@findAllOrderByCreatedDesc : " + users.size());
        users.forEach(System.out::println);
        assert !users.isEmpty();
    }

    @Test
    void PostRepositoryTest(){
        Optional<Post> post1 = postRepository.findByIdQ(1L);
        System.out.println("@@findByIdQ : " + post1.get());
        assert post1.isPresent();

        List<PostFlatDto> flats = postRepository.findAllFlat();
        System.out.println("@@findAllFlat 결과 개수: " + flats.size());
        flats.forEach(System.out::println);
        assert !flats.isEmpty();

        List<Post> posts = postRepository.search("", "", null, null, null, "성능", null);
        System.out.println("@@ search : " + posts.size());
        posts.forEach(System.out::println);
        assert !posts.isEmpty();


        Pageable pageable = PageRequest.of(0, 5);
        Page<Post> page = postRepository.searchPage("스프링", null, null, Category.TECHNOLOGY, null, null, null, pageable);
        System.out.println("@@ searchPage 결과 개수: " + page.getTotalElements());
        page.forEach(System.out::println);
        assert page.getTotalElements() > 0;

        Page<Post>  pageSorted = postRepository.searchPageSorted(null, null, null, Category.TECHNOLOGY, null, null, null, pageable);
        System.out.println("@@searchPageSorted 결과 개수: " + pageSorted.getTotalElements());
        pageSorted.forEach(System.out::println);
        assert pageSorted.getTotalElements() > 0;


    }

}
