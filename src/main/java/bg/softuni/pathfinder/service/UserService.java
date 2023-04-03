package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.entities.Route;
import bg.softuni.pathfinder.model.entities.User;
import bg.softuni.pathfinder.model.dto.service.UserRegisterServiceModel;
import bg.softuni.pathfinder.model.dto.view.UserProfileViewModel;
import bg.softuni.pathfinder.model.entities.enums.Level;
import bg.softuni.pathfinder.repository.UserRepository;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
 

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;



    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;

    }

    public void save(UserRegisterServiceModel userServiceModel) {
        userServiceModel.setLevel(Level.BEGINNER);
        User userEntity = modelMapper.map(userServiceModel, User.class);
        userRepository.save(userEntity);
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


}
