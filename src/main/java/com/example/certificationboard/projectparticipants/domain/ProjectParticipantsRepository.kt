package com.example.certificationboard.projectparticipants.domain

import com.example.certificationboard.member.domain.QMember
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsDto
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

interface ProjectParticipantsRepository: JpaRepository<ProjectParticipants, ProjectParticipantsId>, ProjectParticipantsRepositoryCustom{
    fun findAllByFavorites(pageable: Pageable, favorites: Boolean): Page<ProjectParticipants>
}

interface ProjectParticipantsRepositoryCustom {
    fun findParticipantList(projectId: Long): List<ProjectParticipantsDto>
}

@Repository
open class ProjectParticipantsRepositoryCustomImpl(
        private val jpaQueryFactory: JPAQueryFactory
) : ProjectParticipantsRepositoryCustom, QuerydslRepositorySupport(ProjectParticipants::class.java){

    override fun findParticipantList(projectId: Long): List<ProjectParticipantsDto> {
        val qProjectParticipants = QProjectParticipants.projectParticipants
        val qMember = QMember.member

        return jpaQueryFactory
                .select(Projections.constructor(ProjectParticipantsDto::class.java,
                    qMember.id, qMember.name
                ))
                .from(qProjectParticipants)
                    .innerJoin(qMember).on(qProjectParticipants.projectParticipantsId.member.id.eq(qMember.id))//.fetchJoin()
                .where(qProjectParticipants.projectParticipantsId.project.id.eq(projectId))
                .fetch()
    }

}