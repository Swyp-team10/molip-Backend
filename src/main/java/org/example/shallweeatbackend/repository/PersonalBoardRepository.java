package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.PersonalBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalBoardRepository extends JpaRepository<PersonalBoard, Long> {
}
