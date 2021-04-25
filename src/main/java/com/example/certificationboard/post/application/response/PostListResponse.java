package com.example.certificationboard.post.application.response;

import java.util.List;

public class PostListResponse {
    boolean hasNext;
    List<PostInfo> postInfos;

    public PostListResponse(boolean hasNext, List<PostInfo> postInfos) {
        this.hasNext = hasNext;
        this.postInfos = postInfos;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public List<PostInfo> getPostInfos() {
        return postInfos;
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "hasNext=" + hasNext +
                ", postInfos=" + postInfos +
                '}';
    }
}
