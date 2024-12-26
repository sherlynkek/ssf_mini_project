package sg.edu.nus.iss.Mini.Project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.edu.nus.iss.Mini.Project.model.Event;
import sg.edu.nus.iss.Mini.Project.model.User;
import sg.edu.nus.iss.Mini.Project.service.EventService;
import sg.edu.nus.iss.Mini.Project.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    EventService eventService;
    
    @Autowired
    UserService userService;
    
    private List<User> users = new ArrayList<>();

    // registration
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register"; // Return the form if validation fails
        }
        userService.addUser(user);
        model.addAttribute("message", "Registration is successful");
        return "login"; // Redirect to login page
    }

    // login
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "login";
    }

    // if login successful, it will return to event home page
    // if login failed, it will return a message stating "invalid username or password"
    @PostMapping("/login")
    public String loginUser(@RequestParam String username, 
                            @RequestParam String password, 
                            HttpSession session, Model model) {
        User user = userService.findUserByUsername(username);
        if (user != null && user.checkPassword(password)) {
            session.setAttribute("loggedInUser", user); // Make sure this is happening
            model.addAttribute("message", "Login successful");
            return "redirect:/event/home";
        }
        model.addAttribute("message", "Invalid username or password");
        return "login";
    }

    // profile page of each user (need to provide link for user to visit in)
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

    // add interested events into the user profile
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

    // allow user to remove interested events from their profile
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

    // update the user profile 
    @GetMapping("/update-profile")
    public String showUpdateProfilePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";  // Redirect to login if not logged in
        }

        // Add the user object to the model to pre-populate the form fields
        model.addAttribute("user", loggedInUser);
        return "profileSetting";  // This is the page with the form for profile update
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("user") User updatedUser, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";  // Redirect to login if not logged in
        }

        loggedInUser.setUsername(updatedUser.getUsername());
        loggedInUser.setEmail(updatedUser.getEmail());
        session.setAttribute("loggedInUser", loggedInUser);

        model.addAttribute("message", "Profile updated successfully.");
        return "profile";  // Return to the profile page after update
    }


    @GetMapping("/change-password")
    public String showChangePasswordPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";  // Redirect to login if not logged in
        }

        return "profileSetting";  // Page with form to change password
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                @RequestParam("newPassword") String newPassword,
                                HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";  // Redirect to login if not logged in
        }

        if (!loggedInUser.getPassword().equals(currentPassword)) {
            model.addAttribute("message", "Current password is incorrect.");
            return "profileSetting";  // Stay on the change password page if password is incorrect
        }

        loggedInUser.setPassword(newPassword);
        session.setAttribute("loggedInUser", loggedInUser);

        model.addAttribute("message", "Password changed successfully.");
        return "profile";  // Redirect to the profile page after password change
    }


    // user to log out from session and their account
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/event/home";
    }

    // to see the saved list of usernames and passwords
    @GetMapping("/all-users")
    @ResponseBody
    public List<User> getAllUsers() {
        return users; 
    }
}
