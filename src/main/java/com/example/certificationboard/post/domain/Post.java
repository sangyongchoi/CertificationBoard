package com.example.certificationboard.post.domain;

import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Embedded;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Document("post")
public class Post {

    @Id
    private ObjectId id;
    private Long projectId;
    private String memberId;
    private Type type;
    @Embedded
    private Contents contents;
    @CreatedDate
    private LocalDateTime createdAt;

    public Post(Long projectId, String memberId, Type type, Contents contents){
        this.projectId = projectId;
        this.memberId = memberId;
        this.type = type;
        this.contents = contents;
    }

    public ObjectId getId() {
        return id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getMemberId() {
        return memberId;
    }

    public Type getType() {
        return type;
    }

    public Contents getContents() {
        return contents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", contents=" + contents +
                '}';
    }

    public enum Type {
        WRITTEN,
        TASK
    }
}
