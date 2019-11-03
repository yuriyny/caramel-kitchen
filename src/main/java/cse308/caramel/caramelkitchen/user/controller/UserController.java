package cse308.caramel.caramelkitchen.user.controller;

import cse308.caramel.caramelkitchen.user.User;
import cse308.caramel.caramelkitchen.user.service.UserDomainService;
import cse308.caramel.caramelkitchen.user.storage.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@CrossOrigin
public class UserController {

    @Autowired
    UserDomainService userDomainService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public UserController(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    @PostMapping(path="/register")
    public String addNewUser(@ModelAttribute("user") @Valid User newUser, BindingResult bindingResult, RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirect.addFlashAttribute("user", newUser);
            redirect.addFlashAttribute("pageType", "register");
            return "redirect:login";
        }

        if(userDomainService.userExists(newUser)) {
            return "redirect:login?usernameError";
        } else {
            userDomainService.saveUser(newUser);
            return "login";
        }
    }

    @GetMapping(path={"/login", "/logout"})
    public String getLoginPage() {
        return "login";
    }

    @PostMapping(path="/login")
    public String loginUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirect.addFlashAttribute("user", user);
            redirect.addFlashAttribute("pageType", "login");
            return "redirect:login";
        }
        redirect.addFlashAttribute("userId", user.getUsername());
        return "redirect:home";
    }

}
