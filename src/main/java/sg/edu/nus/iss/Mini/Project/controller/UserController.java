package sg.edu.nus.iss.Mini.Project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.Mini.Project.model.Event;
import sg.edu.nus.iss.Mini.Project.model.User;
import sg.edu.nus.iss.Mini.Project.service.EventService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    EventService eventService;
    
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
                            @RequestParam String password, 
                            HttpSession session, Model model) {
        
        for(User user : users) {
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                session.setAttribute("loggedInUser", user);  // Store user in session
                model.addAttribute("message", "Login successful");
                return "welcome";
            }
        }
        model.addAttribute("message", "Invalid username or password");
        return "login";
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";  // Redirect to login if not logged in
        }
        
        model.addAttribute("user", loggedInUser);

        // Retrieve preferred events
        List<String> preferredEventIds = (List<String>) session.getAttribute("preferredEvents");
        List<Event> preferredEvents = new ArrayList<>();
        if (preferredEventIds != null && !preferredEventIds.isEmpty()) {
            preferredEvents = eventService.getEventsByIds(preferredEventIds);
        }

        model.addAttribute("preferredEvents", preferredEvents);
        return "profile";
    }

    @PostMapping("/interest/{eventId}")
    public String addEventToFavorites(@PathVariable("eventId") String eventId, HttpSession session, Model model) {
        List<String> preferredEvents = (List<String>) session.getAttribute("preferredEvents");
        if (preferredEvents == null) {
            preferredEvents = new ArrayList<>();
        }

        if (!preferredEvents.contains(eventId)) {
            preferredEvents.add(eventId);
            session.setAttribute("preferredEvents", preferredEvents);
        }

        model.addAttribute("message", "Event added to your preferences!");
        return "redirect:/user/profile";  // Redirect to profile after adding
    }

    @PostMapping("/remove-interest/{eventId}")
    public String removeEventFromFavorites(@PathVariable("eventId") String eventId, HttpSession session, Model model) {
        List<String> preferredEvents = (List<String>) session.getAttribute("preferredEvents");

        if (preferredEvents != null && preferredEvents.contains(eventId)) {
            preferredEvents.remove(eventId);
            session.setAttribute("preferredEvents", preferredEvents);
            model.addAttribute("message", "Event removed from your preferences.");
        } else {
            model.addAttribute("message", "Event not found in your preferences.");
        }

        return "redirect:/user/profile";  // Redirect to profile after removal
    }
    
    // to see the saved list of usernames and passwords
    @GetMapping("/all-users")
    @ResponseBody
    public List<User> getAllUsers() {
        return users; 
    }
}
