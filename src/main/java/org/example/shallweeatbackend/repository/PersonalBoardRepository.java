package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.PersonalBoard;
import org.example.shallweeatbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalBoardRepository extends JpaRepository<PersonalBoard, Long> {
    List<PersonalBoard> findByUser(User user);
}
