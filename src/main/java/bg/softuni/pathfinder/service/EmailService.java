package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.entities.EmailConfirmationToken;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.repository.TokenRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalTime.now;

@Service
public class EmailService  {

    private static final String MAIL_SENDER_EMAIL = "hike@addict.com";
    private final JavaMailSender javaMailSender;
    private final TokenRepository tokenRepository;
    private final TemplateEngine templateEngine;

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletContext context;

    public EmailService(JavaMailSender javaMailSender, TokenRepository tokenRepository, TemplateEngine templateEngine, HttpServletRequest request, HttpServletResponse response, ServletContext context) {
        this.javaMailSender = javaMailSender;
        this.tokenRepository = tokenRepository;
        this.templateEngine = templateEngine;
        this.request = request;
        this.response = response;
        this.context = context;
    }


    public void sendSimpleMail(String registeredUserEmail, String fullName, User userEntity
                               ) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        String message = getMessage(fullName , userEntity ,request ,response ,context );

        mimeMessageHelper.setFrom(MAIL_SENDER_EMAIL);
        mimeMessageHelper.setTo(registeredUserEmail);
        mimeMessageHelper.setSubject("Welcome to Hike Addicts!");
        mimeMessageHelper.setText(message,true);


        javaMailSender.send(mimeMessageHelper.getMimeMessage());

    }


    public String getMessage(
            String fullName ,
            User userEntity,
            HttpServletRequest request,
            HttpServletResponse response,
            ServletContext context){

        EmailConfirmationToken token = createConfirmationToken(userEntity);

        WebContext webContext = new WebContext(request, response, context);

        webContext.setVariable("fullName" , fullName);
        webContext.setVariable("token" , token.getToken());
       return templateEngine.process("email-on-register" , webContext);
    }



    public EmailConfirmationToken createConfirmationToken(User user){

        EmailConfirmationToken ect = new EmailConfirmationToken();
        String uuid = UUID.randomUUID().toString().substring(0, 32);
        ect.setUser(user);
        ect.setToken(uuid);
        ect.setExpireAt( LocalDateTime.now().plusHours(24));
        tokenRepository.save(ect);
        return ect;
    }


    public EmailConfirmationToken findToken(String token) {
      return this.tokenRepository.findEmailConfirmationTokenByToken(token).orElse(null);
    }
}
