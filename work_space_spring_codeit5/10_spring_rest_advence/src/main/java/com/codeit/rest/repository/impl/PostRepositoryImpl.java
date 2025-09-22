package com.codeit.rest.repository.impl;

import com.codeit.rest.entity.Category;
import com.codeit.rest.entity.Post;
import com.codeit.rest.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Repository( "postRepository")
public class PostRepositoryImpl implements PostRepository {
    private final List<Post> postList = new ArrayList<>();
    private long seq = 1L;

    {
        postList.add(Post.builder().id(seq++).title("스프링 부트 REST API 설계").content("Spring Boot와 RESTful API 설계 방법을 다룹니다.").tags(List.of("spring", "rest", "api")).category(Category.TECHNOLOGY).authorId(1L).createdAt(Instant.now().minusSeconds(86400L * 30)).updatedAt(Instant.now().minusSeconds(86400L * 30)).build());
        postList.add(Post.builder().id(seq++).title("오늘의 커피 일기").content("아침에 마신 라떼가 너무 맛있어서 기록합니다.").tags(List.of("daily", "coffee")).category(Category.LIFESTYLE).authorId(2L).createdAt(Instant.now().minusSeconds(86400L * 29)).updatedAt(Instant.now().minusSeconds(86400L * 29)).build());
        postList.add(Post.builder().id(seq++).title("JPA 연관관계 매핑 가이드").content("JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.").tags(List.of("jpa", "hibernate", "mapping")).category(Category.TECHNOLOGY).authorId(3L).createdAt(Instant.now().minusSeconds(86400L * 28)).updatedAt(Instant.now().minusSeconds(86400L * 28)).build());
        postList.add(Post.builder().id(seq++).title("주말 등산 후기").content("북한산에 다녀온 주말 이야기.").tags(List.of("daily", "hiking")).category(Category.LIFESTYLE).authorId(4L).createdAt(Instant.now().minusSeconds(86400L * 27)).updatedAt(Instant.now().minusSeconds(86400L * 27)).build());
        postList.add(Post.builder().id(seq++).title("Spring Security 인증과 인가").content("Spring Security로 인증과 인가 구현하기.").tags(List.of("spring", "security", "auth")).category(Category.TECHNOLOGY).authorId(5L).createdAt(Instant.now().minusSeconds(86400L * 26)).updatedAt(Instant.now().minusSeconds(86400L * 26)).build());
        postList.add(Post.builder().id(seq++).title("동네 맛집 탐방기").content("집 근처 새로 생긴 국밥집에 다녀왔습니다.").tags(List.of("daily", "food")).category(Category.LIFESTYLE).authorId(1L).createdAt(Instant.now().minusSeconds(86400L * 25)).updatedAt(Instant.now().minusSeconds(86400L * 25)).build());
        postList.add(Post.builder().id(seq++).title("Docker 컨테이너 활용").content("Docker로 개발 환경을 표준화하는 방법.").tags(List.of("docker", "devops")).category(Category.TECHNOLOGY).authorId(2L).createdAt(Instant.now().minusSeconds(86400L * 24)).updatedAt(Instant.now().minusSeconds(86400L * 24)).build());
        postList.add(Post.builder().id(seq++).title("가족 나들이").content("주말에 가족과 함께 근교 나들이를 다녀왔습니다.").tags(List.of("daily", "family")).category(Category.LIFESTYLE).authorId(3L).createdAt(Instant.now().minusSeconds(86400L * 23)).updatedAt(Instant.now().minusSeconds(86400L * 23)).build());
        postList.add(Post.builder().id(seq++).title("MySQL 성능 최적화").content("인덱스와 쿼리 최적화를 통한 MySQL 성능 향상.").tags(List.of("mysql", "database", "performance")).category(Category.TECHNOLOGY).authorId(4L).createdAt(Instant.now().minusSeconds(86400L * 22)).updatedAt(Instant.now().minusSeconds(86400L * 22)).build());
        postList.add(Post.builder().id(seq++).title("영화 감상 기록").content("최근 본 영화의 감상평을 남깁니다.").tags(List.of("daily", "movie")).category(Category.LIFESTYLE).authorId(5L).createdAt(Instant.now().minusSeconds(86400L * 21)).updatedAt(Instant.now().minusSeconds(86400L * 21)).build());
        postList.add(Post.builder().id(seq++).title("Kubernetes 배포 전략").content("Blue-Green과 Rolling Update 전략 비교.").tags(List.of("kubernetes", "devops")).category(Category.TECHNOLOGY).authorId(1L).createdAt(Instant.now().minusSeconds(86400L * 20)).updatedAt(Instant.now().minusSeconds(86400L * 20)).build());
        postList.add(Post.builder().id(seq++).title("주말 자전거 여행").content("한강 자전거 코스를 달린 후기.").tags(List.of("daily", "bicycle")).category(Category.LIFESTYLE).authorId(2L).createdAt(Instant.now().minusSeconds(86400L * 19)).updatedAt(Instant.now().minusSeconds(86400L * 19)).build());
        postList.add(Post.builder().id(seq++).title("Redis 캐시 활용법").content("Redis를 활용한 애플리케이션 캐싱 전략.").tags(List.of("redis", "cache")).category(Category.TECHNOLOGY).authorId(3L).createdAt(Instant.now().minusSeconds(86400L * 18)).updatedAt(Instant.now().minusSeconds(86400L * 18)).build());
        postList.add(Post.builder().id(seq++).title("반려동물 이야기").content("우리집 강아지와 보낸 하루.").tags(List.of("daily", "pet")).category(Category.LIFESTYLE).authorId(4L).createdAt(Instant.now().minusSeconds(86400L * 17)).updatedAt(Instant.now().minusSeconds(86400L * 17)).build());
        postList.add(Post.builder().id(seq++).title("Git 브랜치 전략").content("Git Flow와 Trunk Based Development 비교.").tags(List.of("git", "vcs")).category(Category.TECHNOLOGY).authorId(5L).createdAt(Instant.now().minusSeconds(86400L * 16)).updatedAt(Instant.now().minusSeconds(86400L * 16)).build());
        postList.add(Post.builder().id(seq++).title("책 읽는 시간").content("최근 읽은 책에 대한 감상문.").tags(List.of("daily", "book")).category(Category.LIFESTYLE).authorId(1L).createdAt(Instant.now().minusSeconds(86400L * 15)).updatedAt(Instant.now().minusSeconds(86400L * 15)).build());
        postList.add(Post.builder().id(seq++).title("Spring Batch 대용량 처리").content("Spring Batch를 활용한 대용량 데이터 처리 방법.").tags(List.of("spring", "batch")).category(Category.TECHNOLOGY).authorId(2L).createdAt(Instant.now().minusSeconds(86400L * 14)).updatedAt(Instant.now().minusSeconds(86400L * 14)).build());
        postList.add(Post.builder().id(seq++).title("집안 인테리어 DIY").content("방을 새롭게 꾸미는 DIY 프로젝트.").tags(List.of("daily", "diy")).category(Category.LIFESTYLE).authorId(3L).createdAt(Instant.now().minusSeconds(86400L * 13)).updatedAt(Instant.now().minusSeconds(86400L * 13)).build());
        postList.add(Post.builder().id(seq++).title("AWS S3 파일 업로드").content("AWS S3를 활용한 파일 업로드 구현.").tags(List.of("aws", "s3")).category(Category.TECHNOLOGY).authorId(4L).createdAt(Instant.now().minusSeconds(86400L * 12)).updatedAt(Instant.now().minusSeconds(86400L * 12)).build());
        postList.add(Post.builder().id(seq++).title("주말 캠핑 이야기").content("가족과 함께한 캠핑의 즐거움.").tags(List.of("daily", "camping")).category(Category.LIFESTYLE).authorId(5L).createdAt(Instant.now().minusSeconds(86400L * 11)).updatedAt(Instant.now().minusSeconds(86400L * 11)).build());
        postList.add(Post.builder().id(seq++).title("Elasticsearch 검색 구현").content("Spring Data Elasticsearch를 이용한 검색 기능.").tags(List.of("elasticsearch", "search")).category(Category.TECHNOLOGY).authorId(1L).createdAt(Instant.now().minusSeconds(86400L * 10)).updatedAt(Instant.now().minusSeconds(86400L * 10)).build());
        postList.add(Post.builder().id(seq++).title("아침 조깅 루틴").content("매일 아침 5km 조깅을 시작했습니다.").tags(List.of("daily", "jogging")).category(Category.LIFESTYLE).authorId(2L).createdAt(Instant.now().minusSeconds(86400L * 9)).updatedAt(Instant.now().minusSeconds(86400L * 9)).build());
        postList.add(Post.builder().id(seq++).title("Kafka 메시지 처리").content("Apache Kafka를 활용한 실시간 메시징 시스템.").tags(List.of("kafka", "messaging")).category(Category.TECHNOLOGY).authorId(3L).createdAt(Instant.now().minusSeconds(86400L * 8)).updatedAt(Instant.now().minusSeconds(86400L * 8)).build());
        postList.add(Post.builder().id(seq++).title("Spring Framework 도전").content("Spring Framework MVC 공부 했습니다. 생각보다 어렵네요.").tags(List.of("spring", "cloud")).category(Category.TECHNOLOGY).authorId(2L).createdAt(Instant.now().minusSeconds(86400L * 4)).updatedAt(Instant.now().minusSeconds(86400L * 4)).build());
        postList.add(Post.builder().id(seq++).title("주말 요리 도전").content("파스타를 처음으로 만들어본 날.").tags(List.of("daily", "cooking")).category(Category.LIFESTYLE).authorId(4L).createdAt(Instant.now().minusSeconds(86400L * 7)).updatedAt(Instant.now().minusSeconds(86400L * 7)).build());
        postList.add(Post.builder().id(seq++).title("GraphQL API 설계").content("GraphQL을 이용한 유연한 API 설계.").tags(List.of("graphql", "api")).category(Category.TECHNOLOGY).authorId(5L).createdAt(Instant.now().minusSeconds(86400L * 6)).updatedAt(Instant.now().minusSeconds(86400L * 6)).build());
        postList.add(Post.builder().id(seq++).title("친구와의 저녁식사").content("오랜만에 친구와 함께한 즐거운 식사.").tags(List.of("daily", "friends")).category(Category.LIFESTYLE).authorId(1L).createdAt(Instant.now().minusSeconds(86400L * 5)).updatedAt(Instant.now().minusSeconds(86400L * 5)).build());
        postList.add(Post.builder().id(seq++).title("Spring Cloud 마이크로서비스").content("Spring Cloud로 마이크로서비스 아키텍처 구축.").tags(List.of("spring", "cloud")).category(Category.TECHNOLOGY).authorId(2L).createdAt(Instant.now().minusSeconds(86400L * 4)).updatedAt(Instant.now().minusSeconds(86400L * 4)).build());
        postList.add(Post.builder().id(seq++).title("Spring Boot").content("Spring Boot로 Rest 공부 했습니다. 생각보다 어렵네요.").tags(List.of("spring", "cloud")).category(Category.TECHNOLOGY).authorId(2L).createdAt(Instant.now().minusSeconds(86400L * 4)).updatedAt(Instant.now().minusSeconds(86400L * 4)).build());
        postList.add(Post.builder().id(seq++).title("주말 영화관 나들이").content("새로 개봉한 영화를 보러 갔습니다. 재밋었습니다.").tags(List.of("daily", "movie")).category(Category.LIFESTYLE).authorId(3L).createdAt(Instant.now().minusSeconds(86400L * 3)).updatedAt(Instant.now().minusSeconds(86400L * 3)).build());
        postList.add(Post.builder().id(seq++).title("CI/CD 파이프라인 구축").content("Jenkins와 GitHub Actions를 활용한 자동 배포.").tags(List.of("cicd", "jenkins")).category(Category.TECHNOLOGY).authorId(4L).createdAt(Instant.now().minusSeconds(86400L * 2)).updatedAt(Instant.now().minusSeconds(86400L * 2)).build());
        postList.add(Post.builder().id(seq++).title("아침 스트레칭 루틴").content("아침에 하는 스트레칭으로 하루를 시작합니다.").tags(List.of("daily", "stretching")).category(Category.LIFESTYLE).authorId(5L).createdAt(Instant.now().minusSeconds(86400L)).updatedAt(Instant.now().minusSeconds(86400L)).build());
    }

    @Override
    public Post save(Post post) {
        Instant now = Instant.now();
        if (post.getId() == null) {
            post.setId(seq++);
            post.setCreatedAt(now);
        } else {
            deleteById(post.getId());
        }
        post.setUpdatedAt(now);
        postList.add(post);
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postList.stream()
                .filter(x -> Objects.equals(x.getId(), id))
                .findFirst();
    }

    // 정렬 기준 : id의 역순 <- 최신날짜순
    @Override
    public List<Post> findAll() {
        return postList.stream()
                .sorted(Comparator.comparing(Post::getId).reversed())
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        postList.removeIf(x -> Objects.equals(x.getId(), id));
    }

    @Override
    public boolean existsById(Long id) {
        return postList.stream().anyMatch(x -> Objects.equals(x.getId(), id));
    }

    @Override
    public long count() {
        return postList.size();
    }

    @Override
    public List<Post> findByTitleContaining(String keyword) {
        if (keyword == null || keyword.isBlank()) return List.of();
        return postList.stream()
                .filter(p -> p.getTitle().contains(keyword))
                .sorted(Comparator.comparing(Post::getId).reversed())
                .toList();
    }

    @Override
    public List<Post> findByContentContaining(String keyword) {
        if (keyword == null || keyword.isBlank()) return List.of();
        return postList.stream()
                .filter(p -> p.getContent().contains(keyword))
                .sorted(Comparator.comparing(Post::getId).reversed())
                .toList();
    }

    @Override
    public List<Post> findByCategory(Category category) {
        if (category == null) return List.of();
        return postList.stream()
                .filter(p -> p.getCategory() == category)
                .sorted(Comparator.comparing(Post::getId).reversed())
                .toList();
    }

    @Override
    public List<Post> findByTagsIn(List<String> tags) {
        if (tags == null || tags.isEmpty()) return List.of();

        return postList.stream()
                .filter(p -> p.getTags() != null &&
                                p.getTags().stream()
                                .filter(Objects::nonNull)
                                .map(String::toLowerCase)
                                .anyMatch(tags::contains))
                .sorted(Comparator.comparing(Post::getId).reversed())
                .toList();
    }

    @Override
    public List<Post> findByAuthorId(Long authorId) {
        if (authorId == null) return List.of();
        return postList.stream()
                .filter(p -> Objects.equals(p.getAuthorId(), authorId))
                .sorted(Comparator.comparing(Post::getId).reversed())
                .toList();
    }

    @Override
    public Page<Post> findAllByPage(int size, int page) {
        size = Math.max(1, size);
        page = Math.max(0, page);
        int offset = size * page;

        List<Post> ordered = findAll();
        if (offset >= ordered.size()) {
            return Page.empty(PageRequest.of(page, size));
        }

        int to = Math.min(offset + size, ordered.size());
        List<Post> content = ordered.subList(offset, to);

        return new PageImpl<>(content, PageRequest.of(page, size), ordered.size());
    }
}
