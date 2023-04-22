package bg.softuni.pathfinder.web;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.model.entities.enums.Level;
import bg.softuni.pathfinder.model.entities.enums.UserRoles;
import bg.softuni.pathfinder.repository.RoleRepository;
import bg.softuni.pathfinder.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;;
import java.util.Set;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PagesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private User user = new User();


    @BeforeAll
    public void setUp() {
        user.setUsername("TestingUser");
        user.setPassword("admin");
        user.setActive(true);
        user.setId(1);
        user.setRoles(Set.of(roleRepository.findAll().stream().filter(role2 -> role2.getName().equals(UserRoles.ADMIN)).findFirst().get()));
        userRepository.save(user);
    }


    @AfterAll
     public void tearDown() {
        userRepository.delete(user);
    }


    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void testAdminsPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/admins"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser(username = "TestingUser")
    public void testUsersProfile() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users/profile"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAboutPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/about"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("about"));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllUsers_withValidAdminUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/all-users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("all-users"))
        ;
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllUsers_withNormalUser_redirects() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/all-users"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testHomePage_withLoggedInUser_valid() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    public void getAllPedestrianRoutesTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/pedestrian"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pedestrian"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allPedestrianRoutes"));
    }

    @Test
    public void getAllCarRoutesTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/car"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("car"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allCarRoutes"));
    }

    @Test
    public void getAllMotorcycleRoutesTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/motorcycle"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("motorcycle"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allMotorcycleRoutes"));
    }

    @Test
    public void getAllBicycleRoutesTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/bicycle"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bicycle"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allBicycleRoutes"));
    }


    @Test
    @WithMockUser(username = "admina", roles = {"ADMIN"})
    public void getAllApprovedUsers_withValidLoggedInAdmin_status200() throws Exception {

        User user = new User();
        user.setFullName("testFullName");
        user.setAge(11);
        user.setId(1);
        user.setUsername("admina");
        user.setPassword("1111111");
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/all-users/approved")
                        .with(csrf())
                        .param("onlyApproved", String.valueOf(true))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("onlyApproved"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("approvedUsers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("principalId"))
                .andExpect(MockMvcResultMatchers.view().name("all-users"));
    }


    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void getAllUsersForApprove_withValidLoggedInAdmin_status200() throws Exception {

        User user = new User();
        user.setFullName("testFullName");
        user.setAge(11);
        user.setId(1);
        user.setUsername("test");
        user.setPassword("1111111");
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/all-users/for-approving")
                        .with(csrf())
                        .param("notApproved", String.valueOf(true))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("notApprovedUsers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("notApproved"))
                .andExpect(MockMvcResultMatchers.view().name("all-users"));
    }


    @Test
    @WithMockUser(username = "TestUsername", roles = {"ADMIN"})
    public void approveUser_withLoggedInAdmin_valid() throws Exception {

        User user = new User();
        user.setUsername("name");
        user.setLevel(Level.BEGINNER);
        user.setRoles(Set.of());
        user.setId(1);
        user.setPassword("eqeda");
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/all-users/1")
                        .param("id", String.valueOf(1))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/all-users"));
    }

    @Test
    @WithMockUser(username = "TestUsername", roles = {"ADMIN"})
    public void deleteUser_withLoggedInAdmin_valid() throws Exception {

        User user = new User();
        user.setUsername("name");
        user.setLevel(Level.BEGINNER);
        user.setRoles(Set.of());
        user.setId(1);
        user.setPassword("eqeda");
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/all-users/1/delete")
                        .param("id", String.valueOf(1))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/all-users"));
    }


}