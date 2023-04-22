package bg.softuni.pathfinder.web;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getRegisterPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("userRegistrationBinding"))
                .andExpect(MockMvcResultMatchers.view().name("register"));
    }

    @Test
    @WithMockUser(username = "admin" , roles = {"ADMIN"})
    public void emailConfirmation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/register/confirm/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));
    }

    @Test
    public void registerUser_withValidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .with(csrf())
                        .param("email" , "userEmail@abv.bg")
                        .param("username" , "username")
                        .param("fullName" , "name")
                        .param("age" , "25")
                        .param("password" , "secretPassword")
                        .param("confirmPassword" , "secretPassword"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));
    }




}