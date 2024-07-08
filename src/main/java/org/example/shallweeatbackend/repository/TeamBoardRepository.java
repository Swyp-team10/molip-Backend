package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.dto.TeamBoardDTO;
import org.example.shallweeatbackend.entity.PersonalBoard;
import org.example.shallweeatbackend.entity.TeamBoard;
import org.example.shallweeatbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamBoardRepository extends JpaRepository<TeamBoard, Long> {
    List<TeamBoard> findByUser(User user);

    //Optional<TeamBoard> findByIdAndUser(User user, Long teamBoardId);
}
