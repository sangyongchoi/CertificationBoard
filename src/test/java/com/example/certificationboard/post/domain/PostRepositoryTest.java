package com.example.certificationboard.post.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@Rollback(value = false)
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("MongoRepository 기능 테스트")
    public void mongoRepository_function_test() {
        Post post = Post.builder()
                .type(Post.Type.WRITTEN)
                .projectId(1L)
                .contents(new TextContents("test", "test"))
                .build();

        final Post save = postRepository.save(post);

        System.out.println(save);
    }

    @Test
    @DisplayName("MongoRepository 기능 테스트2")
    public void mongoRepository_function_test2() {
        Post post = Post.builder()
                .type(Post.Type.WRITTEN)
                .projectId(1L)
                .contents(new TaskContents("test"
                        , LocalDateTime.of(2021, 4, 10, 0, 0)
                        , LocalDateTime.of(2021, 4, 11, 23,59)))
                .build();

        final Post save = postRepository.save(post);

        System.out.println(save);
    }

    @Test
    @DisplayName("MongoRepository 가져오기 기능")
    public void mongoRepository_find_function() throws Exception{
        Pageable pageable = PageRequest.of(0, 20);
        final Page<Post> all = postRepository.findAllByProjectId(pageable, 1L);

        System.out.println(all.getTotalPages());
        all.forEach(System.out::println);
    }

}