package sg.edu.nus.iss.Mini.Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.Mini.Project.model.User;



@Controller
@RequestMapping("/user")
public class UserController {
    
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        
        return "register";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

}
