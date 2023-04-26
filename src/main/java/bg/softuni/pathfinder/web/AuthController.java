package bg.softuni.pathfinder.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {


    @GetMapping("/users/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/users/logout")
    public String getLogout(){
        return "index";
    }
}
