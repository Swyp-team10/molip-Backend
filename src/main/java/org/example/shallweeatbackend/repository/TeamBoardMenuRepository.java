package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.TeamBoardMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamBoardMenuRepository extends JpaRepository<TeamBoardMenu, Long> {
    List<TeamBoardMenu> findByTeamBoardTeamBoardId(Long teamBoardId);
}
