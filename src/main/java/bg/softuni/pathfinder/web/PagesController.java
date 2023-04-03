package bg.softuni.pathfinder.web;
import bg.softuni.pathfinder.model.AppUser;
import bg.softuni.pathfinder.model.dto.view.UserProfileViewModel;
import bg.softuni.pathfinder.service.RoutesService;
import bg.softuni.pathfinder.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;


@Controller
public class PagesController {

    private final UserService userService;
    private final RoutesService routesService;

    public PagesController(UserService userService, RoutesService routesService) {
        this.userService = userService;
        this.routesService = routesService;
    }

    @GetMapping("/admins")
    public String getAdmins(){
        return "admins";
    }

    @GetMapping("/")
    public String getHome(Model model , @AuthenticationPrincipal AppUser appUser){

        if (appUser!=null){
        model.addAttribute("fullName",appUser.getFullName());
        }
           model.addAttribute("mostCommented" ,routesService.getRouteWithMostComments());
        return "index";
    }

    @GetMapping("/users/profile")
    public String getProfile(Model model , Principal principal){

        UserProfileViewModel profileView = userService.getUserProfileViewByUsername(principal.getName());
        model.addAttribute("user",profileView);
        return "profile";
    }

    @GetMapping("/about")
    public String getAbout(){
        return "about";
    }


    //todo fix map

}
