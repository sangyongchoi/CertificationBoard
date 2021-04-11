package com.example.certificationboard.post.application;

import com.example.certificationboard.common.util.PageUtil;
import com.example.certificationboard.post.domain.Post;
import com.example.certificationboard.post.domain.PostRepository;
import com.example.certificationboard.project.application.ProjectService;
import com.example.certificationboard.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ProjectService projectService;

    public PostService(PostRepository postRepository, ProjectService projectService) {
        this.postRepository = postRepository;
        this.projectService = projectService;
    }

    public PostResponse findAll(Pageable pageable, Long id) {
        final Project project = projectService.findById(id);
        final Page<Post> posts = postRepository.findAllByProjectId(pageable, project.getId());
        final List<PostInfo> postInfos = posts.stream()
                .map(post -> new PostInfo(post.getId(), post.getProjectId(), post.getType(), post.getContents()))
                .collect(Collectors.toList());
        boolean hasNext = PageUtil.hasNext(posts, pageable);

        return new PostResponse(hasNext, postInfos);
    }
}
