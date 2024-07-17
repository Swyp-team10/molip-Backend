package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.PersonalBoard;
import org.example.shallweeatbackend.entity.PersonalBoardMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PersonalBoardMenuRepository extends JpaRepository<PersonalBoardMenu, Long> {
    @Query("SELECT pbm FROM PersonalBoardMenu pbm "
            + "JOIN FETCH pbm.menu m "
            + "LEFT JOIN FETCH m.menuTags mt "
            + "LEFT JOIN FETCH mt.tag "
            + "WHERE pbm.personalBoard.personalBoardId = :personalBoardId")
    List<PersonalBoardMenu> findAllByPersonalBoardId(@Param("personalBoardId") Long personalBoardId);

    @Query("SELECT CASE WHEN COUNT(pbm) > 0 THEN true ELSE false END " +
            "FROM PersonalBoardMenu pbm " +
            "WHERE pbm.personalBoard.personalBoardId = :personalBoardId AND pbm.menu.menuId = :menuId")
    boolean existsByPersonalBoardIdAndMenuId(@Param("personalBoardId") Long personalBoardId, @Param("menuId") Long menuId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PersonalBoardMenu pbm WHERE pbm.personalBoard = :personalBoard")
    void deleteAllByPersonalBoard(@Param("personalBoard") PersonalBoard personalBoard);

    @Query("SELECT pbm FROM PersonalBoardMenu pbm "
            + "JOIN FETCH pbm.menu m "
            + "LEFT JOIN FETCH m.menuTags mt "
            + "LEFT JOIN FETCH mt.tag "
            + "WHERE pbm.personalBoard.personalBoardId = :personalBoardId "
            + "AND m.categoryOptions LIKE %:category%")
    List<PersonalBoardMenu> findAllByPersonalBoardIdAndCategory(@Param("personalBoardId") Long personalBoardId, @Param("category") String category);

}
