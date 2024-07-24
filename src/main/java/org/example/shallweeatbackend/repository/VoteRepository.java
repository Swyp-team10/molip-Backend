package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByTeamBoardTeamBoardId(Long teamBoardId);
    long countByUserUserIdAndTeamBoardTeamBoardId(Long userId, Long teamBoardId);
    boolean existsByUserUserIdAndTeamBoardTeamBoardIdAndMenuMenuId(Long userId, Long teamBoardId, Long menuId);
    List<Vote> findByUserUserIdAndTeamBoardTeamBoardId(Long userId, Long teamBoardId);
    List<Vote> findByUserUserId(Long userId);
    boolean existsByTeamBoardAndUser(TeamBoard teamBoard, User user);

    @Query("SELECT COUNT(DISTINCT v.user.userId) FROM Vote v WHERE v.teamBoard.teamBoardId = :teamBoardId")
    Long countDistinctVotedUsersByTeamBoardId(@Param("teamBoardId") Long teamBoardId);
}