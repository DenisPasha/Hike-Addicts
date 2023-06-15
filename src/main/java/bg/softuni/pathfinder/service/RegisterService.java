package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.service.UserRegisterServiceModel;
import bg.softuni.pathfinder.model.entities.EmailConfirmationToken;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.model.entities.enums.Level;
import bg.softuni.pathfinder.repository.TokenRepository;
import bg.softuni.pathfinder.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Service
public class RegisterService {



    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    Boolean tokenExpired ;

    public RegisterService(TokenRepository tokenRepository, UserRepository userRepository, ModelMapper modelMapper, EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }



    public void save(UserRegisterServiceModel userServiceModel) throws MessagingException {
        userServiceModel.setLevel(Level.BEGINNER);
        User userEntity = modelMapper.map(userServiceModel, User.class);
        userEntity.setActive(false);
        userEntity.setAccountConfirmed(false);
        userRepository.save(userEntity);

        emailService.sendSimpleMail(userEntity.getEmail() , userEntity.getFullName() , userEntity);

    }


    public void confirmEmail(String token) {

        EmailConfirmationToken dbToken = emailService.findToken(token);


        if (dbToken != null ) {

            if (!isTokenExpired(dbToken)){
                User user = dbToken.getUser();
                user.setAccountConfirmed(true);
                userRepository.save(user);
                tokenRepository.delete(dbToken);
            }else {
                this.tokenRepository.delete(dbToken);
            }
        }

    }

    private Boolean isTokenExpired(EmailConfirmationToken dbToken) {
        //for some reason may be sql server is writing the dates 2 days forward
        // thats way i set plus 3 days so the is one day expiration
        if (dbToken.getExpireAt().isAfter(LocalDateTime.now().plusDays(3))){
             return true;
        }
        return false;
    }

    // remove expired token every second day at midnight

    @Scheduled(cron = "0 0 0 */2 * ?")
    public void removeExpiredTokens(){

        for (EmailConfirmationToken token : tokenRepository.findAll()) {
            if(isTokenExpired(token)){
                tokenRepository.delete(token);
            }
        }
    }


}
