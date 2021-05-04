package com.example.certificationboard.post.query;

import com.example.certificationboard.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class PostCustomRepository {

    private final MongoTemplate mongoTemplate;

    public PostCustomRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Page<Post> findByProjectId(Long projectId, Pageable pageable) {
        final LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("like")
                .localField("_id")
                .foreignField("postId")
                .as("likes");

        final Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("projectId").is(projectId)),
                lookupOperation,
                Aggregation.skip((long) pageable.getPageSize() * pageable.getPageNumber()),
                Aggregation.limit(pageable.getPageSize()),
                Aggregation.sort(Sort.by("_id").descending())
        );

        return PageableExecutionUtils.getPage(
                mongoTemplate.aggregate(aggregation, "post", Post.class).getMappedResults()
                , pageable
                ,() -> mongoTemplate.count(getCountQuery(projectId), Post.class)
        );
    }

    private Query getCountQuery(Long projectId) {
        return new Query(Criteria.where("projectId").is(projectId));
    }
}
