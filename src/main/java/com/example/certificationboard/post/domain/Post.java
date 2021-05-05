package com.example.certificationboard.post.domain;

import com.example.certificationboard.like.domain.Like;
import com.example.certificationboard.post.exception.NotSupportFunctionException;
import com.example.certificationboard.reply.domain.Reply;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Embedded;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Document("post")
public class Post {

    @Id
    private ObjectId id;
    private Long projectId;
    private String memberId;
    private Type type;
    @DBRef
    private List<Like> likes = new ArrayList<>();
    @DBRef
    private List<Reply> replies = new ArrayList<>();
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

    public List<Like> getLikes() {
        return likes;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void changeTaskContents(Contents contents){
        if (contents instanceof TaskContents && Type.TASK.equals(type)) {
            this.contents = contents;
        } else {
            throw new NotSupportFunctionException("업무기능에만 지원하는 기능입니다.");
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", memberId='" + memberId + '\'' +
                ", type=" + type +
                ", likes=" + likes +
                ", replies=" + replies +
                ", contents=" + contents +
                ", createdAt=" + createdAt +
                '}';
    }

    public enum Type {
        WRITTEN,
        TASK
    }
}
