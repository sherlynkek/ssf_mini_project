package sg.edu.nus.iss.Mini.Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/event")
public class ConcertController {
    
    @GetMapping("")
    public String getAllEvent(Model model) {
        return "event";
    }
}
