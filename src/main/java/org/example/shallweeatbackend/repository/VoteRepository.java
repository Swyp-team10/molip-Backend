package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByTeamBoardTeamBoardId(Long teamBoardId);
    List<Vote> findByMenuMenuId(Long menuId);
    long countByMenuMenuId(Long menuId);
}
