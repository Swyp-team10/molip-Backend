package org.example.shallweeatbackend.repository;

import org.example.shallweeatbackend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    @Test
    public void testSaveAndFindUser() {
        // User 생성
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@email.com");

        // User 저장
        User savedUser = userRepository.save(user);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        // User 조회 (by username)
        User foundUser = userRepository.findByUsername("testuser");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testuser");
        assertThat(foundUser.getEmail()).isEqualTo("testuser@email.com");

        // User 조회 (by Id)
        Optional<User> optionalUser = userRepository.findById(savedUser.getId());
        assertThat(optionalUser).isPresent();
        User userById = optionalUser.get();
        assertThat(userById.getUsername()).isEqualTo("testuser");

        // User 삭제
        userRepository.delete(userById);
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertThat(deletedUser).isNotPresent();
    }
}