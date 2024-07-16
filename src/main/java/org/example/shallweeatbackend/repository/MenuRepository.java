package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query(value = "select m from Menu m " +
            "LEFT JOIN FETCH m.menuTags mt " +
            "LEFT JOIN FETCH mt.tag " +
            "where m.menuId = :menuId")
    Optional<Menu> findByMenuIdWithTags(@Param("menuId") Long menuId);

    @Query("SELECT m FROM Menu m " +
            "LEFT JOIN FETCH m.menuTags mt " +
            "LEFT JOIN FETCH mt.tag")
    List<Menu> findAllWithTags();
}
