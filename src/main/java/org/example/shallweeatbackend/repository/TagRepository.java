package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
