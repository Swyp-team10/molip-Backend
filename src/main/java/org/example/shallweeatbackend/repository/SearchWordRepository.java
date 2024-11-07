package org.example.shallweeatbackend.repository;

import java.util.List;

import org.example.shallweeatbackend.entity.SearchWord;
import org.example.shallweeatbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchWordRepository extends JpaRepository<SearchWord, Long> {
    List<SearchWord> findByUser(User user);

    List<SearchWord> findByUserOrderByCreatedAtDesc(User user);
}
