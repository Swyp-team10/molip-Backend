package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByUserUserId(Long userId);
    // 특정 팀 보드의 모든 투표 조회
    List<Vote> findByTeamBoardTeamBoardId(Long teamBoardId);

    // 사용자가 특정 팀 보드에서 투표한 수를 카운트
    long countByUserUserIdAndTeamBoardTeamBoardId(Long userId, Long teamBoardId);

    // 사용자가 특정 팀 보드의 특정 메뉴에 이미 투표했는지 확인
    boolean existsByUserUserIdAndTeamBoardTeamBoardIdAndMenuMenuId(Long userId, Long teamBoardId, Long menuId);

    // 특정 팀 보드에서 사용자의 모든 투표 조회
    List<Vote> findByUserUserIdAndTeamBoardTeamBoardId(Long userId, Long teamBoardId);

    // 팀 보드에 대해 사용자가 투표했는지 여부 확인
    boolean existsByTeamBoardAndUser(TeamBoard teamBoard, User user);

    // 특정 팀 보드에서 투표에 참여한 고유한 사용자 수 조회
    @Query("SELECT COUNT(DISTINCT v.user.userId) FROM Vote v WHERE v.teamBoard.teamBoardId = :teamBoardId")
    Long countDistinctVotedUsersByTeamBoardId(@Param("teamBoardId") Long teamBoardId);
}