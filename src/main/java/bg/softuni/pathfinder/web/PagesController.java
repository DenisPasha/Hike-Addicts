package bg.softuni.pathfinder.web;
import bg.softuni.pathfinder.model.AppUser;
import bg.softuni.pathfinder.model.dto.view.RouteDetailsView;
import bg.softuni.pathfinder.model.dto.view.RoutesView;
import bg.softuni.pathfinder.model.dto.view.UserProfileViewModel;
import bg.softuni.pathfinder.service.RoutesService;
import bg.softuni.pathfinder.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;


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
        RouteDetailsView mostCommentedRoute = routesService.getRouteWithMostComments();

        if (appUser!=null){
        model.addAttribute("fullName",appUser.getFullName());
        }
        if(mostCommentedRoute!=null){
            model.addAttribute("mostCommented" ,mostCommentedRoute);
        }
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

    @GetMapping("/pedestrian")
    public String getPedestrian(Model model){

        List<RoutesView> allPedestrianRoutes = routesService.getRoutesByCategory();
        model.addAttribute("allPedestrianRoutes",allPedestrianRoutes);
        return "pedestrian";
    }

    @GetMapping("/bicycle")
    public String getBicycle(Model model){

        List<RoutesView> allBicycleRoutes = routesService.getAllBicycleRoutes();
        model.addAttribute("allBicycleRoutes",allBicycleRoutes);
        return "bicycle";
    }

    @GetMapping("/motorcycle")
    public String getMotorcycle(Model model){

        List<RoutesView> allMotorcycleRoutes = routesService.getAllMotorcycleRoutes();
        model.addAttribute("allMotorcycleRoutes",allMotorcycleRoutes);
        return "motorcycle";
    }

    @GetMapping("/car")
    public String getCar(Model model){

        List<RoutesView> allCarRoutes = routesService.getAllCarRoutes();
        model.addAttribute("allCarRoutes",allCarRoutes);
        return "car";
    }

    @GetMapping("/all-users")
    public String getAllUsers(){
        return "all-users";
    }

    @PostMapping("/all-users/approved")
    public String getAllApprovedUsers(Model model , Principal principal ,boolean onlyApproved ){

        if (!model.containsAttribute("onlyApproved")){
            model.addAttribute("onlyApproved" , onlyApproved);
        }
        model.addAttribute("principalId" , userService.getUserByUserName(principal.getName()).getId());
        model.addAttribute("approvedUsers" , userService.getAllApprovedUsers() );
        return "all-users";
    }

    @PostMapping("/all-users/for-approving")
    public String getAllNotApprovedUsers(Model model ,boolean notApproved ){

        if (!model.containsAttribute("notApproved")){
            model.addAttribute("notApproved" , notApproved);
        }
        model.addAttribute("notApprovedUsers" , userService.getAllNotApprovedUsers() );
        return "all-users";
    }


    @PostMapping("all-users/{id}")
    public String approveUser(@PathVariable Long id){
        userService.approveUser(id);
        return "redirect:/all-users";
    }

    @PostMapping("all-users/{id}/delete")
    public String deactivateUser(@PathVariable Long id , Model model , Principal principal){
        userService.deactivateUser(id);
        return "redirect:/all-users";
    }


    //todo fix map

}
