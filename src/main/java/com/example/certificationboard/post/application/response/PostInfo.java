package com.example.certificationboard.post.application.response;

import com.example.certificationboard.post.domain.Contents;
import com.example.certificationboard.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class PostInfo {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private Long projectId;
    private String writerId;
    private String writerName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private Post.Type type;
    private Contents contents;

    public PostInfo() {
    }

    public PostInfo(ObjectId id, Long projectId, String writerId, String writerName, Post.Type type, Contents contents, LocalDateTime createdAt) {
        this.id = id;
        this.projectId = projectId;
        this.writerId = writerId;
        this.writerName = writerName;
        this.type = type;
        this.contents = contents;
        this.createdAt = createdAt;
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

    public String getWriterId() {
        return writerId;
    }

    public String getWriterName() {
        return writerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "PostInfo{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", writerId='" + writerId + '\'' +
                ", writerName='" + writerName + '\'' +
                ", createdAt=" + createdAt +
                ", type=" + type +
                ", contents=" + contents +
                '}';
    }
}
