package com.example.certificationboard.project.query;

import com.example.certificationboard.project.application.ProjectInfo;
import com.example.certificationboard.project.domain.QProject;
import com.example.certificationboard.projectparticipants.domain.QProjectParticipants;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectQueryRepository {

    private final JPAQueryFactory queryFactory;
    QProject qProject = QProject.project;
    QProjectParticipants qProjectParticipants = QProjectParticipants.projectParticipants;

    public ProjectQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Page<ProjectInfo> findLists(Pageable pageable, String memberId, boolean favorites) {
        final List<ProjectInfo> content = findListsQuery(pageable, memberId, favorites)
                .fetch();

        final JPAQuery<ProjectInfo> countQuery = findListsQuery(pageable, memberId, favorites);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private JPAQuery<ProjectInfo> findListsQuery(Pageable pageable, String memberId, boolean favorites) {
        return queryFactory
                .select(Projections.constructor(ProjectInfo.class
                        , qProject.id, qProject.title, qProject.explain
                ))
                .from(qProject)
                .innerJoin(qProjectParticipants)
                .on(qProject.id.eq(qProjectParticipants.projectParticipantsId.project.id))
                .where(qProjectParticipants.projectParticipantsId.member.id.eq(memberId)
                        .and(qProjectParticipants.favorites.eq(favorites)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProject.id.desc());
    }
}
