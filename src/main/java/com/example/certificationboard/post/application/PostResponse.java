package com.example.certificationboard.post.application;

import java.util.List;

public class PostResponse {
    boolean hasNext;
    List<PostInfo> postInfos;

    public PostResponse(boolean hasNext, List<PostInfo> postInfos) {
        this.hasNext = hasNext;
        this.postInfos = postInfos;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public List<PostInfo> getPostInfos() {
        return postInfos;
    }
}
