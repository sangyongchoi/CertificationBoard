package com.example.certificationboard.post.application.response;

import com.example.certificationboard.post.domain.Contents;
import com.example.certificationboard.post.domain.Post;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

public class PostInfo {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private Long projectId;
    private Post.Type type;
    private Contents contents;

    public PostInfo() {
    }

    public PostInfo(ObjectId id, Long projectId, Post.Type type, Contents contents) {
        this.id = id;
        this.projectId = projectId;
        this.type = type;
        this.contents = contents;
    }

    public ObjectId getId() {
        return id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Post.Type getType() {
        return type;
    }

    public Contents getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "PostInfo{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", type=" + type +
                ", contents=" + contents +
                '}';
    }
}
