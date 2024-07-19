package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByTeamBoardTeamBoardId(Long teamBoardId);
    long countByUserUserIdAndTeamBoardTeamBoardId(Long userId, Long teamBoardId);
    boolean existsByUserUserIdAndTeamBoardTeamBoardIdAndMenuMenuId(Long userId, Long teamBoardId, Long menuId);
    List<Vote> findByUserUserIdAndTeamBoardTeamBoardId(Long userId, Long teamBoardId);
    List<Vote> findByUserUserId(Long userId);
}