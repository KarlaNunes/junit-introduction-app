package com.ifrn.junitintroductionapp.service;

import com.ifrn.junitintroductionapp.domain.User;
import com.ifrn.junitintroductionapp.exceptions.UserNotFoundException;
import com.ifrn.junitintroductionapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getUserById_UserNotFound() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void createUserWithValidData() {
        //Arrange
        User user = new User(1L,"Karla", "karla@gmail.com", 22);

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(createdUser.getId(), user.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getAge(), createdUser.getAge());
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
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void testCheckVotingEligibility_Underage() {
        User user1 = new User(1L,"Karla", "karla@gmail.com", 15);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        // Act
        String result = userService.canVote(user1.getId());

        // Assert
        assertEquals("User is too young to vote.", result);
    }

    @Test
    void testCheckVotingEligibility_OptionalVoting() {
        User user1 = new User(1L,"Karla", "karla@gmail.com", 16);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        // Act
        String result= userService.canVote(user1.getId());

        // Assert
        assertEquals("User can vote optionally.", result);
    }

    @Test
    void testCheckVotingEligibility_RequiredVoting() {
        User user1 = new User(1L,"Karla", "karla@gmail.com", 18);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        // Act
        String result18 = userService.canVote(user1.getId());

        // Assert
        assertEquals("User is required to vote.", result18);
    }

    @Test
    void testCheckVotingEligibility_OptionalVotingAfter70() {
        User user1 = new User(1L,"Karla", "karla@gmail.com", 71);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        // Act
        String result71 = userService.canVote(user1.getId());

        // Assert
        assertEquals("User can vote optionally.", result71);
    }
}