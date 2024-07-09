package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.TeamMember;
import org.example.shallweeatbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamBoardRepository extends JpaRepository<TeamBoard, Long> {
    //List<TeamBoard> findByUser(User user);
    List<TeamBoard> findByUserUserId(Long userId);

}
