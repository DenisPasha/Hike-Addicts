package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.AppUser;
import bg.softuni.pathfinder.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.stream.Collectors;

public class AppsCurrentUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppsCurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

   return this.userRepository.findByUsername(username)
                .map(userDB -> new AppUser(userDB.getUsername() ,
                                        userDB.getPassword() ,
                        userDB.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName().name()))
                                .collect(Collectors.toList()),
                        userDB.getFullName()
                )).orElseThrow(() -> new UsernameNotFoundException("User with name "+username + "was not found "));


    }

}
