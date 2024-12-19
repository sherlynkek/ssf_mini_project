package sg.edu.nus.iss.Mini.Project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sg.edu.nus.iss.Mini.Project.model.User;

@Controller
@RequestMapping("/user")
public class UserController {
    
    private List<User> users = new ArrayList<>();

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        users.add(user);
        model.addAttribute("message", "Registration is successful");
        return "login";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, 
                            @RequestParam String password, Model model) {
        
        for(User user : users) {
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                model.addAttribute("message", "Login successful");
                return "welcome";
            }
        }
        model.addAttribute("message", "Invalid username or password");
        return "login";
    }

    // to see the saved list of usernames and passwords
    @GetMapping("/all-users")
    @ResponseBody
    public List<User> getAllUsers() {
        return users; 
    }
}
