package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByUserUserId(Long userId);

}
