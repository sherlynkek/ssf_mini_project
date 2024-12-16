package sg.edu.nus.iss.Mini.Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/login")
public class UserController {
    
    @GetMapping("")
    public String getLogin() {
        return "login";
    }

}
