package bg.softuni.pathfinder.web;

import bg.softuni.pathfinder.model.dto.binding.UserRegistrationBindingModel;
import bg.softuni.pathfinder.model.dto.service.UserRegisterServiceModel;
import bg.softuni.pathfinder.service.RegisterService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegisterController {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RegisterService registerService;
    private final HttpServletRequest request;

    public RegisterController(PasswordEncoder passwordEncoder, ModelMapper modelMapper, RegisterService registerService, HttpServletRequest request) {
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.registerService = registerService;
        this.request = request;
    }

    @GetMapping("/users/register")
    public String getRegister(Model model){
        if (!model.containsAttribute("userRegistrationBinding")){
            model.addAttribute("userRegistrationBinding",new UserRegistrationBindingModel());
        }

        return "register";
    }

    @PostMapping("/users/register")
    public String postRegister(@Valid UserRegistrationBindingModel userRegistrationBinding ,
                               BindingResult bindingResult ,
                               RedirectAttributes redirectAttributes ) throws ServletException, MessagingException {

        if (bindingResult.hasErrors() || !userRegistrationBinding.getPassword().equals(userRegistrationBinding.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("userRegistrationBinding",userRegistrationBinding)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationBinding",bindingResult);
            return "redirect:/users/register";
        }
        String raw = userRegistrationBinding.getPassword();
        userRegistrationBinding.setPassword(passwordEncoder.encode(userRegistrationBinding.getPassword()));
        UserRegisterServiceModel userServiceModel = modelMapper.map(userRegistrationBinding, UserRegisterServiceModel.class);
         registerService.save(userServiceModel);

        authWithHttpServletRequest(request ,userRegistrationBinding.getUsername() ,raw);
        return "redirect:/";
    }

    @GetMapping("/users/register/confirm/{token}")
    public String confirmEmail(@PathVariable String token){
        registerService.confirmEmail(token);


        return "redirect:/";

    }

    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password)
            throws ServletException {
            request.login(username, password);
    }
}
