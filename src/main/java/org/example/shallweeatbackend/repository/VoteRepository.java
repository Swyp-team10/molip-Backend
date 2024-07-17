package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.TeamBoardMenu;
import org.example.shallweeatbackend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByTeamBoardTeamBoardId(Long teamBoardId);

    List<Vote> findByTeamBoardMenuTeamBoardMenuId(Long teamBoardMenuId);

    List<Vote> findByMenuMenuId(Long menuId);

    // 추가: 특정 메뉴에 대한 투표 수를 가져오는 메서드
    long countByMenuMenuId(Long menuId);
}
