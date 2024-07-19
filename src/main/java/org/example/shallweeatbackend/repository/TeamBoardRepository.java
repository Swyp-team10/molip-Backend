package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.TeamMember;
import org.example.shallweeatbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamBoardRepository extends JpaRepository<TeamBoard, Long> {
    //List<TeamBoard> findByUser(User user);
    List<TeamBoard> findByUserUserId(Long userId);

    @Query("SELECT COUNT(DISTINCT tbm.user.userId) FROM TeamBoardMenu tbm WHERE tbm.teamBoard.teamBoardId = :teamBoardId")
    Long countDistinctUsersByTeamBoardId(@Param("teamBoardId") Long teamBoardId);

    @Query("SELECT tb.teamMembersNum FROM TeamBoard tb WHERE tb.teamBoardId = :teamBoardId")
    Integer findTeamMembersNumByTeamBoardId(@Param("teamBoardId") Long teamBoardId);

}
