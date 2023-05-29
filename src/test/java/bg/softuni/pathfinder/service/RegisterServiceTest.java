package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.service.UserRegisterServiceModel;
import bg.softuni.pathfinder.model.entities.EmailConfirmationToken;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.model.entities.enums.Level;
import bg.softuni.pathfinder.repository.TokenRepository;
import bg.softuni.pathfinder.repository.UserRepository;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import javax.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RegisterServiceTest {


    private  TokenRepository tokenRepository;

    private  UserRepository userRepository;
    private  ModelMapper modelMapper;
    private  EmailService emailService;

    private RegisterService registerService;

    @BeforeEach
    public void setUp() throws Exception {
        tokenRepository = Mockito.mock(TokenRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        modelMapper = new ModelMapper();
        emailService = Mockito.mock(EmailService.class);

        registerService = new RegisterService(tokenRepository , userRepository ,modelMapper ,emailService);

    }

    @Test
    public void saveUserTest() throws MessagingException {
        UserRegisterServiceModel registerModel = new UserRegisterServiceModel();
        registerModel.setEmail("sample@mail.com");
        registerModel.setLevel(Level.BEGINNER);
        registerModel.setUsername("test");
        registerModel.setAge(21);
        registerModel.setPassword("password");
        registerModel.setFullName("Test Test");

        registerService.save(registerModel);
        User save = verify(userRepository).save(any(User.class));
        verify(emailService).sendSimpleMail(eq("sample@mail.com"),eq("Test Test") ,any());

    }

    @Test
    public void confirmEmailTest_tokeNotExpired(){

        User user = new User();
        user.setId(1);
        user.setActive(true);
        user.setAccountConfirmed(false);
        user.setUsername("test user");


        EmailConfirmationToken token = new EmailConfirmationToken();
        token.setUser(user);
        token.setExpireAt(LocalDateTime.now().plusDays(0));
        token.setToken("adsadad");
        token.setId(1L);

        when(emailService.findToken(any(String.class))).thenReturn(token);
        registerService.confirmEmail(token.getToken());
        ArgumentCaptor<User>captor = ArgumentCaptor.forClass(User.class);
         verify(userRepository).save(captor.capture());
        User value = captor.getValue();
        assertThat(value.getAccountConfirmed()).isTrue();
        verify(tokenRepository).delete(token);
    }

    @Test
    public void confirmEmailTest_expiredToken(){
        User user = new User();
        user.setId(1);
        user.setActive(true);
        user.setAccountConfirmed(false);
        user.setUsername("test user");


        EmailConfirmationToken token = new EmailConfirmationToken();
        token.setUser(user);
        token.setExpireAt(LocalDateTime.now().plusDays(5));
        token.setToken("adsadad");
        token.setId(1L);


        when(emailService.findToken(any(String.class))).thenReturn(token);
        registerService.confirmEmail(token.getToken());
        ArgumentCaptor<EmailConfirmationToken>captor = ArgumentCaptor.forClass(EmailConfirmationToken.class);
        verify(tokenRepository).delete(captor.capture());
        EmailConfirmationToken value = captor.getValue();
        assertThat(value.getToken()).isEqualTo(token.getToken());
    }

    @Test
    public void removeExpiredTokens_test(){
        EmailConfirmationToken token =
                new EmailConfirmationToken();
        token.setId(1L);
        token.setExpireAt(LocalDateTime.now().plusDays(5));
        token.setToken("tokenName");

        EmailConfirmationToken token2 =
                new EmailConfirmationToken();
        token2.setId(1L);
        token2.setExpireAt(LocalDateTime.now().plusDays(5));
        token2.setToken("tokenName");

        List<EmailConfirmationToken> tokens = new ArrayList<>();
        tokens.add(token);
        tokens.add(token2);
        when(tokenRepository.findAll()).thenReturn(tokens);
        registerService.removeExpiredTokens();
        verify(tokenRepository , times(2)).delete(any(EmailConfirmationToken.class));

    }
}