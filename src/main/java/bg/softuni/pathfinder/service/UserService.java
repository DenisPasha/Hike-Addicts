package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.entities.Route;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.model.dto.service.UserRegisterServiceModel;
import bg.softuni.pathfinder.model.dto.view.UserProfileViewModel;
import bg.softuni.pathfinder.model.entities.enums.Level;
import bg.softuni.pathfinder.repository.UserRepository;
import org.modelmapper.ModelMapper;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;



    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    public UserProfileViewModel getUserProfileViewByUsername(String username) {
        return modelMapper.map(userRepository.findByUsername(username).orElse(null), UserProfileViewModel.class);
    }

    public User getUserByUserName(String username) {
      return userRepository.findByUsername(username).orElse(null);
    }

    public UserProfileViewModel getAuthor(Long authorId) {

        User user = userRepository.findById(authorId).get();
        UserProfileViewModel maps = modelMapper.map(user, UserProfileViewModel.class);

        return maps;
    }


    public List<UserProfileViewModel> getAllUsers() {

        List<UserProfileViewModel> usersToReturn = new ArrayList<>();
        for (User user : userRepository.findAll()) {
           usersToReturn.add( modelMapper.map(user , UserProfileViewModel.class) );
        }
        return usersToReturn;
    }

    public void approveUser(Long id) {
        User user = userRepository.findById(id).get();
                user.setActive(true);
                userRepository.save(user);
    }

    //cron expression every day at midnight 00:00
    @Scheduled(cron = "0 0 0 * * *")
    public void removeNotApprovedUsers(){
        userRepository.findAll().stream().filter(user -> !user.getActive()).forEach(
                userRepository::delete);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id).get();
        user.setActive(false);
        userRepository.save(user);
    }

    public List<UserProfileViewModel> getAllApprovedUsers() {
        List<UserProfileViewModel> toReturn = new ArrayList<>();
         for (User user : this.userRepository.findAllApprovedUsers() ) {
            UserProfileViewModel map = modelMapper.map(user, UserProfileViewModel.class);
            toReturn.add(map);
        }
         return toReturn;

    }

    public List<UserProfileViewModel> getAllNotApprovedUsers() {
        List<UserProfileViewModel> toReturn = new ArrayList<>();
        for (User user : this.userRepository.findAllNotApprovedUsers() ) {
            UserProfileViewModel map = modelMapper.map(user, UserProfileViewModel.class);
            toReturn.add(map);
        }
        return toReturn;

    }
}
