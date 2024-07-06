package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.MenuTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuTagRepository extends JpaRepository<MenuTag, Long> {
    @Query("SELECT mt.tag.name FROM MenuTag mt WHERE mt.menu.menuId = :menuId")
    List<String> findTagNamesByMenuId(@Param("menuId") Long menuId);
}
