package bg.softuni.pathfinder.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class PagesControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getHomePageTestWithoutUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("mostCommented"))
        ;
    }


    @Test
    @WithMockUser(username = "pesho",password = "1111111" )
    public void getProfileTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/profile"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    public void getAboutPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/about"))
                .andExpect(MockMvcResultMatchers.view().name("about"));
    }

    @Test
    @WithMockUser(username = "Admin" ,password = "1111111",roles = "ADMIN")
    public void getAdminsPageValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admins"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admins"));
    }

}
