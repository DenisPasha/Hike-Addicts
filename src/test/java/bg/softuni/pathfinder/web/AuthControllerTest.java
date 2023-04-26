package bg.softuni.pathfinder.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin" , password = "1111111")
    public void testLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/login"))
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    @Test
    @WithMockUser(username = "admin" , password = "1111111")
    public void testLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/logout"))
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }


}