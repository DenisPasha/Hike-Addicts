package bg.softuni.pathfinder.web;

import bg.softuni.pathfinder.model.dto.binding.ImageAddBinding;
import bg.softuni.pathfinder.model.dto.binding.RouteAddBinding;
import bg.softuni.pathfinder.model.dto.view.RouteDetailsView;
import bg.softuni.pathfinder.model.dto.view.RoutesView;
import bg.softuni.pathfinder.model.entities.Route;
import bg.softuni.pathfinder.service.CommentService;
import bg.softuni.pathfinder.service.PictureService;
import bg.softuni.pathfinder.service.RoutesService;
import bg.softuni.pathfinder.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class RoutesController {


    private final UserService userService;
    private final RoutesService routesService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;
    private final CommentService commentService;

    public RoutesController(UserService userService, RoutesService routesService, PictureService pictureService, ModelMapper modelMapper, CommentService commentService) {
        this.userService = userService;
        this.routesService = routesService;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
        this.commentService = commentService;
    }

    @GetMapping("/add-route")
    public String getAddRoute(Model model , RouteAddBinding routeAddBinding){
        if (!model.containsAttribute("routeAddBinding")){
            model.addAttribute("routeAddBinding",new RouteAddBinding());
        }
        return "add-route";
    }

    @PostMapping("routes/details/{id}/add-comment")
    public String postAComment(@PathVariable Long id , String message , Principal principal ,RedirectAttributes redirectAttributes){

        if (message.isEmpty()){
           redirectAttributes.addFlashAttribute("isCommentEmpty",true);
            return "redirect:";
        }
        commentService.saveComment(id , message ,principal);
        return "redirect:";
    }

    @PostMapping("routes/details/{id}")
    public String saveNewImage(@PathVariable Long id , MultipartFile picture , Principal principal
            , RedirectAttributes redirectAttributes ) throws IOException {

        if (picture.isEmpty() || principal==null){
            redirectAttributes.addFlashAttribute("isRedirected",true);
            return "redirect:{id}";
        }
        ImageAddBinding img = mapToPicture(id, picture, principal);
        pictureService.savePicture(img);
        return "redirect:/";
    }

    @GetMapping("/routes/details/{id}")
    public String getRouteDetails(Model model, @PathVariable Long id){
        RouteDetailsView routeById = routesService.getRouteById(id);
        model.addAttribute("route",routeById);
        model.addAttribute("author", userService.getAuthor(routeById.getAuthorId()));
        model.addAttribute("comments",commentService.getAllCommentsForRoute(id));

        return "route-details";
    }
    @GetMapping("/routes")
    public String getAllRoutes(Model model , Principal principal){

        List<RoutesView> allRoutes = routesService.getAllRoutes();
        model.addAttribute("routes",allRoutes);
        model.addAttribute("user",principal);
        return "routes";
    }
    @PostMapping("/add-route")
    public String postAddRoute(@Valid RouteAddBinding routeAddBinding , BindingResult bindingResult , RedirectAttributes redirectAttributes, Principal principal) throws IOException {

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("routeAddBinding" ,routeAddBinding);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.routeAddBinding",bindingResult);
            return "redirect:/add-route";
        }
        routesService.createRoute(routeAddBinding , principal.getName());
        return "redirect:/routes";
    }

    @PostMapping("/routes/details/{id}/approve-route")
    public String approveRoute(Model model , boolean isRouteActive, @PathVariable Long id){
        if (!model.containsAttribute("isRouteActive")){
            model.addAttribute("isRouteActive",isRouteActive);
        }
        routesService.setRouteApproved(id , isRouteActive );
        // todo make a call to the db to approve the route
        return "index";
    }


    private ImageAddBinding mapToPicture(Long id, MultipartFile picture, Principal principal) throws IOException {
        ImageAddBinding img = new ImageAddBinding();
        img.setImage(picture.getBytes());
        img.setTitle(picture.getOriginalFilename());
        img.setContentType(picture.getContentType());
        img.setAuthor(userService.getUserByUserName(principal.getName()));
        img.setRoute(modelMapper.map(routesService.getRouteById(id), Route.class));
        return img;
    }
}
