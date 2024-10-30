package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.SearchWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchWordRepository extends JpaRepository<SearchWord, Long> {
}
