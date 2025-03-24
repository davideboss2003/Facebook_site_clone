package com.example.demo.repository;

import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        User user = new User("John Doe", "john.doe@example.com", "0743761908",
                Boolean.FALSE, Boolean.FALSE, 0.0F, "abc");

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(savedUser.getIsAdmin()).isEqualTo(user.getIsAdmin());
        assertThat(savedUser.getIsBanned()).isEqualTo(user.getIsBanned());
        assertThat(savedUser.getScore()).isEqualTo(user.getScore());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());

        userRepository.delete(savedUser);
    }
}
