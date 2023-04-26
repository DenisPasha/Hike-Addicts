package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.view.UserProfileViewModel;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.repository.UserRepository;
import jdk.dynalink.linker.LinkerServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private  UserRepository userRepository;
    private  ModelMapper modelMapper;

    private UserService userService ;
    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        modelMapper = new ModelMapper();
        userService = new UserService(userRepository , modelMapper);
    }

    @Test
    public void getUserProfileViewByUsernameTest(){

        User user = new User();
        user.setUsername("TestUser");

        when(userRepository.findByUsername("TestUser")).thenReturn(Optional.of(user));
        UserProfileViewModel testUser = userService.getUserProfileViewByUsername("TestUser");

        assertEquals(user.getUsername() , testUser.getUsername());
    }

    @Test
    public void getUserByUserNameTest(){
        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        User testUser = userService.getUserByUserName("testUser");
        assertEquals(user , testUser);
    }


    @Test
    public void getAllUsersTest(){
        User user = new User();
        user.setId(1);
        user.setUsername("user 1");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user 2");
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);
        List<UserProfileViewModel> allUsers = userService.getAllUsers();

        assertEquals(users.size() , allUsers.size());
        assertEquals(users.get(0).getUsername() , allUsers.get(0).getUsername());
        assertEquals(users.get(1).getUsername() , allUsers.get(1).getUsername());
    }


    @Test
    public void approveUserTest(){
        User user = new User();
        user.setActive(false);
        user.setUsername("test");
        user.setId(1L);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        assertFalse(user.getActive());
        userService.approveUser(1L);
        assertTrue(user.getActive());
    }

    @Test
    public void removeNotApprovedUsersTest(){
        User notApproved1 = new User();
        notApproved1.setActive(false);

        User notApproved2 = new User();
        notApproved2.setActive(false);

        User approved1 = new User();
        approved1.setActive(true);

        List<User>users = new ArrayList<>();
        users.add(notApproved1);
        users.add(notApproved2);
        users.add(approved1);

        when(userRepository.findAll()).thenReturn(users);

        userService.removeNotApprovedUsers();
        verify(userRepository, times(2)).delete(any(User.class));

    }

    @Test
    public void deactivateUserTest(){
        User approved = new User();
        approved.setActive(true);

        when(userRepository.findById(any())).thenReturn(Optional.of(approved));

        userService.deactivateUser(1L);
        verify(userRepository).save(approved);
        assertFalse(approved.getActive());
    }

    @Test
    public void getAllApprovedUsersTest(){
        User approved1 = new User();
        approved1.setActive(true);

        User approved2 = new User();
        approved2.setActive(true);

        User notApproved = new User();
        notApproved.setActive(false);

        List<User>users = new ArrayList<>();
        users.add(approved1);
        users.add(approved2);
        users.add(notApproved);

        List<User> approvedUsers = users.stream().filter(user -> user.getActive()).collect(Collectors.toList());
        when(userRepository.findAllApprovedUsers()).thenReturn(approvedUsers);
        List<UserProfileViewModel> allApprovedUsers = userService.getAllApprovedUsers();

        assertEquals(2 ,allApprovedUsers.size());

    }

}