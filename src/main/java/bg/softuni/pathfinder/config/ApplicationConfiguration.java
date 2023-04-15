package bg.softuni.pathfinder.config;

import bg.softuni.pathfinder.model.dto.view.RouteDetailsView;
import bg.softuni.pathfinder.model.entities.Route;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.RouteMatcher;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Properties;

@Configuration
public class ApplicationConfiguration {

    private static final String MAIL_AUTH_PROPERTY = "mail.smpt.auth";
    private static final String MAIL_PROTOCOL = "mail.transport.protocol";

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        Converter<byte[] , String> toString = new Converter<byte[], String>() {
            @Override
            public String convert(MappingContext<byte[], String> context) {
              return Base64.getMimeEncoder().encodeToString(context.getSource());
            }

        };

        Converter<LocalDateTime,String > toStringDate = new AbstractConverter<LocalDateTime, String>() {
            @Override
            protected String convert(LocalDateTime source) {
                return source.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
            }
        };
        return modelMapper;

    }

    @Bean
    public JavaMailSender javaMailSender(
            @Value("${mail.host}" ) String host ,
            @Value("${mail.port}" ) int port ,
            @Value("${mail.username}" ) String username ,
            @Value("${mail.password}" ) String password

    ) throws MessagingException {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setPassword(password);
        javaMailSender.setUsername(username);

        getProperties(javaMailSender);
        return javaMailSender;
    }

    private static void getProperties(JavaMailSenderImpl javaMailSender) {
        Properties properties = new Properties();
        properties.setProperty(MAIL_AUTH_PROPERTY , "true");
        properties.setProperty( MAIL_PROTOCOL, "smtp");
        javaMailSender.setJavaMailProperties(properties);
    }

}
