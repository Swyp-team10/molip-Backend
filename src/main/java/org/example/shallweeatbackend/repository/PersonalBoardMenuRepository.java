package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.Menu;
import org.example.shallweeatbackend.entity.PersonalBoard;
import org.example.shallweeatbackend.entity.PersonalBoardMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalBoardMenuRepository extends JpaRepository<PersonalBoardMenu, Long> {
    void deleteAllByPersonalBoard(PersonalBoard personalBoard);

    List<PersonalBoardMenu> findAllByPersonalBoard(PersonalBoard personalBoard);

    PersonalBoardMenu findByPersonalBoardAndMenu(PersonalBoard personalBoard, Menu menu);
}
