package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.entities.Role;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.model.entities.enums.Level;
import bg.softuni.pathfinder.model.entities.enums.UserRoles;
import bg.softuni.pathfinder.repository.RoleRepository;
import bg.softuni.pathfinder.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DbInit implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public DbInit(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() == 0){
            initRoles();
            initAdminUser();
        }
    }

    private void initAdminUser() {

        Role adminRole =  roleRepository.findAll().stream()
                .filter(role -> role.getName()
                        .equals(UserRoles.ADMIN))
                          .findFirst().get();

        User admin = new User();
        admin.setUsername("admin");
        admin.setLevel(Level.ADVANCED);
        admin.setEmail("admin@abv.bg");
        admin.setAge(25);
        admin.setFullName("Admin Adminov");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(Set.of(adminRole));
        admin.setActive(true);
        admin.setAccountConfirmed(true);

        userRepository.save(admin);
    }

    private void initRoles() {
        List<Role> roles = new ArrayList<>();

        Role roleUser = new Role();
        Role roleModerator = new Role();
        Role roleAdmin = new Role();
        roleUser.setName(UserRoles.USER);
        roleModerator.setName(UserRoles.MODERATOR);
        roleAdmin.setName(UserRoles.ADMIN);

        roles.add(roleUser);
        roles.add(roleModerator);
        roles.add(roleAdmin);

        roleRepository.saveAll(roles);

    }
}
