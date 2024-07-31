package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.TeamBoardMenu;
import org.example.shallweeatbackend.entity.Menu;
import org.example.shallweeatbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamBoardMenuRepository extends JpaRepository<TeamBoardMenu, Long> {
    List<TeamBoardMenu> findByTeamBoardTeamBoardId(Long teamBoardId);

    Optional<TeamBoardMenu> findByTeamBoardAndMenu(TeamBoard teamBoard, Menu menu);

    boolean existsByTeamBoardAndUser(TeamBoard teamBoard, User user);

    void deleteByTeamBoard(TeamBoard teamBoard);

    @Query("SELECT COUNT(DISTINCT tbm.user) FROM TeamBoardMenu tbm WHERE tbm.teamBoard = :teamBoard")
    int countDistinctUsersByTeamBoard(@Param("teamBoard") TeamBoard teamBoard);
}
