package com.example.certificationboard.post.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "post")
public class Post {

    @Id
    private String id;

    public String getId() {
        return id;
    }
}
