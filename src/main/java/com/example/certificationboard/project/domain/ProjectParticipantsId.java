package com.example.certificationboard.project.domain;

import com.example.certificationboard.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class ProjectParticipantsId implements Serializable {

    @ManyToOne(targetEntity = Project.class, fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;
}
