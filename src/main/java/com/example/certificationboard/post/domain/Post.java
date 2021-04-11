package com.example.certificationboard.post.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Embedded;
import javax.persistence.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document("post")
public class Post {

    @Id
    private ObjectId id;
    private Long projectId;
    private Type type;
    @Embedded
    private Contents contents;

    public enum Type {
        WRITTEN
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", contents=" + contents +
                '}';
    }
}
