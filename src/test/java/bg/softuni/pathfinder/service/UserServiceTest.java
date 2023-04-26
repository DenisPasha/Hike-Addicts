package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.view.UserProfileViewModel;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.model.entities.enums.Level;
import bg.softuni.pathfinder.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        saveTestUser();
    }

    @Test
    public void getUserByUsername(){
         saveTestUser();

        User retrievedUser = userService.getUserByUserName("TestUser");

        assertEquals(user.getUsername(), retrievedUser.getUsername());
        assertEquals(user.getFullName(), retrievedUser.getFullName());
    }



    @Test
    public void getAuthorTest(){
        saveTestUser();
        UserProfileViewModel author = userService.getAuthor(1L);
        assertEquals(author.getUsername() , user.getUsername());
    }

    @Test
    public void getAllUsersTest(){

        User user1 = new User();
        user1.setId(2);
        user1.setLevel(Level.BEGINNER);
        user1.setRoles(Set.of());
        user1.setAge(21);
        user1.setUsername("username");
        user1.setAccountConfirmed(false);
        user1.setFullName("fullName");
        user1.setPassword("21212");

        List<UserProfileViewModel> allUsers = userService.getAllUsers();
        assertEquals(1 , allUsers.size());
        userRepository.save(user1);
        List<UserProfileViewModel> allUsers1 = userService.getAllUsers();
        assertEquals(2 , allUsers1.size());

    }



    private void saveTestUser() {
        user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setPassword("1111111");
        user.setActive(true);
        user.setAge(21);
        user.setEmail("testUser@abv.bg");
        user.setRoles(Set.of());
        user.setFullName("user");
        user.setAccountConfirmed(false);

        user = userRepository.save(user);
    }
}