package bg.softuni.pathfinder.web;
import bg.softuni.pathfinder.model.entities.Picture;
import bg.softuni.pathfinder.model.entities.Route;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.model.entities.enums.Level;
import bg.softuni.pathfinder.model.entities.enums.RouteCategory;
import bg.softuni.pathfinder.model.entities.enums.UserRoles;
import bg.softuni.pathfinder.repository.PictureRepository;
import bg.softuni.pathfinder.repository.RoleRepository;
import bg.softuni.pathfinder.repository.RouteRepository;
import bg.softuni.pathfinder.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoutesControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PictureRepository pictureRepository;

    private User user = new User();
    private Route route = new Route();
    private Picture picture = new Picture();

    @BeforeAll
    public void setUp() {
        user.setUsername("TestingUser");
        user.setPassword("admin");
        user.setActive(true);
        user.setId(1L);
        user.setRoles(Set.of(roleRepository.findAll().stream().filter(role2 -> role2.getName().equals(UserRoles.ADMIN)).findFirst().get()));
        userRepository.save(user);


        route.setName("routename");
        route.setVideoUrl("videoURL");
        route.setPictures(Set.of(picture));
        route.setDescription("desc");
        route.setCategorie(RouteCategory.PEDESTRIAN);
        route.setAuthorId(1L);
        route.setLevel(Level.BEGINNER);
        routeRepository.save(route);


        picture.setRoute(route);
        picture.setAuthor(user);
        picture.setId(2L);
        picture.setImage( new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        picture.setContentType("jpeg");
        pictureRepository.save(picture);


    }

    @AfterAll
    public void afterAll() {
        pictureRepository.delete(picture);
        routeRepository.delete(route);
        userRepository.delete(user);
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void testGetAllRoutes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/add-route"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("routeAddBinding"))
                .andExpect(MockMvcResultMatchers.view().name("add-route"));
    }


    @Test
    @WithMockUser(username = "TestUser")
    public void postCommentForSpecificRoute_withLoggedInUser_statusOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/routes/details/1/add-comment")
                .with(csrf())
                        .param("id" , String.valueOf(1))
                        .param("message" , "Comment "))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:"));
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void postCommentForRoute_withEmptyComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/routes/details/1/add-comment")
                .with(csrf())
                .param("id" , String.valueOf(1))
                .param("message" ,""))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("isCommentEmpty"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:"));
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void saveNewImageToRoute_withEmptyImage_statusRedirection() throws Exception {

        Picture mock = mock(Picture.class);
        MultipartFile mock1 = mock(MultipartFile.class);
        mock.setImage(mock1.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/routes/details/1")
                        .file("picture" ,mock1.getBytes() )
                .with(csrf())
                .param("id" , String.valueOf(1)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:{id}"));
    }


    @Test
    @WithMockUser(username = "Test")
    public void getSpecificRouteDetails_statusOk() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.get("/routes/details/1")
                        .param("id" , String.valueOf(1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("route"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("author"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("comments"))
                .andExpect(MockMvcResultMatchers.view().name("route-details"));

    }

    @Test
    @WithMockUser(username = "TestUser")
    public void getAllRoutes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/routes"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("routes"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.view().name("routes"));
    }


    @Test
    @WithMockUser(username = "Admin" , roles = {"ADMIN"})
    public void getAllRoutesToBeApprovedByAdmin_withLoggedInAdmin_statusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/routes-to-approve"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("routes"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.view().name("routes"));
    }


    @Test
    @WithMockUser("TestingUser")
    public void addRoute_withLoggedInUser_validRouteToSave_statusOk() throws Exception {

        MultipartFile multipartMock = mock(MultipartFile.class);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/add-route")
                        .file("image" ,multipartMock.getBytes() )
                .with(csrf())
                        .param("name" ,"routeName")
                        .param("description" ,"desc")
                        .param("description" ,"desc")
                        .param("id" ,"1")
                        .param("level" , String.valueOf(Level.BEGINNER))
                        .param("videoUrl" , "dadadda")

                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/routes"));
    }



    @Test
    @WithMockUser("TestingUser")
    public void addRoute_withLoggedInUser_withEmptyRoute_statusOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/add-route")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/add-route"));
    }


    @Test
    @WithMockUser(username = "TestingUser" , roles = {"ADMIN"})
    public void approveRoute_withLoggedInAdmin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/routes/details/1/approve-route")
                .with(csrf())
                .param("isRouteActive" , String.valueOf(false)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:"));
    }

    @Test
    @WithMockUser(username = "TestingUser" , roles = {"ADMIN"})
    public void deleteRoute_withLoggedInAdminUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/routes/details/1/delete")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/routes"));
    }


    @Test
    @WithMockUser(username = "TestingUser")
    public void saveNewImage_withValidImage() throws Exception {

        byte[] bytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

        mockMvc.perform(MockMvcRequestBuilders.multipart("/routes/details/1")
                .file("picture" , bytes)
                .with(csrf())
                        .param("id", String.valueOf(1)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));

    }





}