package com.ifrn.junitintroductionapp.service;

import com.ifrn.junitintroductionapp.domain.User;
import com.ifrn.junitintroductionapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsersAndReturnAListOfSizeOne() {
        // arrange
        User user = new User(1L, "Karla", "karla@gmail.com", 22);
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Act (test)
        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
    }

    @Test
    void getUserById() {
        User user = new User(1L, "Karla", "karla@gmail.com", 22);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        User userById = userService.getUserById(1L);
        assertEquals(user.getId(), userById.getId());
        assertNotNull(userById);
    }

    @Test
    void createUser() {
        User user1 = new User(1L, "Karla", "karla@gmail.com", 22);
        when(userRepository.save(any(User.class))).thenReturn(user1);

        User createdUser = userService.createUser(user1);
        assertEquals(user1.getId(), createdUser.getId());
        assertNotNull(createdUser);
    }

    @Test
    void updateUser() {
        User user = new User(1L, "Karla", "karla@gmail.com", 22);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        User userWithNewName = new User(1L, "Karla", "Raphael", 22);;

        userService.updateUser(1L, userWithNewName);
        assertEquals(userWithNewName.getName(), user.getName());
        assertEquals(userWithNewName.getEmail(), user.getEmail());
    }

    @Test
    void deleteUser() {
        User userWithNewName = new User(1L, "Karla", "Raphael", 22);;
        when(userRepository.findById(any())).thenReturn(Optional.of(userWithNewName));
    }

    @Test
    void createUserWithDuplicatedEmail() {
        User user1 = new User(1L,"Karla", "karla@gmail.com", 22);
        when(userRepository.existsByEmail(any())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user1)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void returnAMessageThatUserIsTooYoungToVote() {
        User user = new User(1L, "Karla", "karla@gmail.com", 15);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        String msg = userService.canVote(user.getId());
        assertEquals("User is too young to vote.", msg);
    }
}