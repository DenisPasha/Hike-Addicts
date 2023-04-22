package bg.softuni.pathfinder.config;

import bg.softuni.pathfinder.model.entities.enums.UserRoles;
import bg.softuni.pathfinder.repository.UserRepository;
import bg.softuni.pathfinder.service.AppsCurrentUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    private final UserRepository userRepository;

    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new Pbkdf2PasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()
                .antMatchers("/admins").hasRole(UserRoles.ADMIN.name())
                .antMatchers("/all-users").hasRole(UserRoles.ADMIN.name())
                .antMatchers("/routes-to-approve").hasRole(UserRoles.ADMIN.name())
                .antMatchers("/users/logout" , "/users/profile").authenticated()
                .antMatchers("/add-route").authenticated()
                .antMatchers("/routes/details/**/approve-route").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureUrl("/users/login?error=true")
                .and()
                .logout()
                .logoutUrl("/users/logout")
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/");
      return  httpSecurity.build();
    }


    @Bean
    public UserDetailsService userDetailsService(){
        return new AppsCurrentUserService(userRepository);
    }
}
