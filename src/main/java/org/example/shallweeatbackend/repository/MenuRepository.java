package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
