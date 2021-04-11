package com.example.certificationboard.project.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectParticipantsRepository extends JpaRepository<ProjectParticipants, ProjectParticipantsId> {

    Page<ProjectParticipants> findAllByFavorites(Pageable pageable, boolean favorites);
}
