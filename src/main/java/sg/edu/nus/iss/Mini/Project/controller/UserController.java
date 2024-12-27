package sg.edu.nus.iss.Mini.Project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.edu.nus.iss.Mini.Project.model.Event;
import sg.edu.nus.iss.Mini.Project.model.User;
import sg.edu.nus.iss.Mini.Project.repo.MapRepo;
import sg.edu.nus.iss.Mini.Project.service.EventService;
import sg.edu.nus.iss.Mini.Project.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    EventService eventService;
    
    @Autowired
    UserService userService;

    @Autowired
    MapRepo mapRepo;
    
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
    
        // Create Redis key for the user
        String redisKey = "user:" + user.getUsername();
        
        // Check if username or email already exists
        if (mapRepo.keyExists(redisKey, "username")) {
            model.addAttribute("message", "Username already exists. Please choose a different one.");
            return "register";
        }

        if (mapRepo.isEmailTaken("email", user.getEmail())) {
            model.addAttribute("message", "Email already exists. Please choose a different one.");
            return "register";
        }

        // Store all user details in one hash
        mapRepo.create(redisKey, "username", user.getUsername());
        mapRepo.create(redisKey, "password", user.getPassword());
        mapRepo.create(redisKey, "email", user.getEmail());
    
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
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpSession session, Model model) {

        // Query Redis to check if the username exists
        String redisKey = "user:" + username;
        String storedPassword = (String) mapRepo.get(redisKey, "password");

        if (storedPassword == null || !storedPassword.equals(password)) {
            model.addAttribute("message", "Invalid username or password.");
            return "login";  // Return to login page if authentication fails
        }

        // User authentication successful, create session
        String email = (String) mapRepo.get(redisKey, "email");
        User loggedInUser = new User();
        loggedInUser.setUsername(username);
        loggedInUser.setPassword(storedPassword);
        loggedInUser.setEmail(email);
        session.setAttribute("loggedInUser", loggedInUser);

        return "redirect:/event/home";  // Redirect to profile page after successful login
    }

    // profile page of each user (need to provide link for user to visit in)
    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";  // Redirect to login if not logged in
        }

        model.addAttribute("user", loggedInUser);

        // Retrieve preferred events from MapRepo
        String redisKey = loggedInUser.getUsername() + "_preferredEvents";
        List<Object> preferredEventObjects = mapRepo.getValues(redisKey); // Get as List<Object>
        List<String> preferredEventIds = preferredEventObjects.stream()
        .map(Object::toString) // Convert each Object to String
        .collect(Collectors.toList()); 
        
        List<Event> preferredEvents = new ArrayList<>();
        if (preferredEventIds != null && !preferredEventIds.isEmpty()) {
            preferredEvents = eventService.getEventsByIds(preferredEventIds);
        }

        model.addAttribute("preferredEvents", preferredEvents);
        return "profile";
    }

    // Add event to favorites
    @PostMapping("/interest/{eventId}")
    public String addEventToFavorites(@PathVariable("eventId") String eventId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";  // Redirect to login if not logged in
        }

        String redisKey = loggedInUser.getUsername() + "_preferredEvents";
        if (!mapRepo.keyExists(redisKey, eventId)) {
            mapRepo.create(redisKey, eventId, eventId); // Use the eventId as both key and value for simplicity
        }

        model.addAttribute("message", "Event added to your preferences!");
        return "redirect:/user/profile";  // Redirect to profile after adding
    }

    // Remove event from favorites
    @GetMapping("/remove-interest/{eventId}")
    public String removeEventFromFavorites(@PathVariable("eventId") String eventId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";  // Redirect to login if not logged in
        }

        String redisKey = loggedInUser.getUsername() + "_preferredEvents";
        if (mapRepo.keyExists(redisKey, eventId)) {
            mapRepo.delete(redisKey, eventId);
            model.addAttribute("message", "Event removed from your preferences.");
        } else {
            model.addAttribute("message", "Event not found in your preferences.");
        }

        return "redirect:/user/profile";  // Redirect to profile after removal
    }

    // Update the user profile
    @GetMapping("/update-profile")
    public String showUpdateProfilePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";  // Redirect to login if not logged in
        }

        model.addAttribute("user", loggedInUser);
        return "profileSetting";  // Page with form to update user profile
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("user") User updatedUser, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";  // Redirect to login if not logged in
        }

        String oldUsername = loggedInUser.getUsername();
        String newUsername = updatedUser.getUsername();

        // If the username has changed, update Redis keys
        if (!oldUsername.equals(newUsername)) {
            // Transfer preferred events to the new username
            String oldPreferredEventsKey = oldUsername + "_preferredEvents";
            String newPreferredEventsKey = newUsername + "_preferredEvents";

            Map<Object, Object> oldEvents = mapRepo.getAllEvents(oldPreferredEventsKey);
            if (oldEvents != null && !oldEvents.isEmpty()) {
                List<String> eventIds = new ArrayList<>();
                List<String> eventDetails = new ArrayList<>();
                oldEvents.forEach((eventId, eventDetail) -> {
                    eventIds.add((String) eventId);
                    eventDetails.add((String) eventDetail);
                });

                // Add events to the new username's key
                mapRepo.addEvents(newPreferredEventsKey, eventIds, eventDetails);
                mapRepo.deleteKey(oldPreferredEventsKey);  // Remove old events key
            }

            // Transfer user details, including password, to the new username key
            String oldRedisKey = "user:" + oldUsername;
            String newRedisKey = "user:" + newUsername;

            String storedPassword = (String) mapRepo.get(oldRedisKey, "password");
            mapRepo.create(newRedisKey, "username", updatedUser.getUsername());
            mapRepo.create(newRedisKey, "email", updatedUser.getEmail());
            mapRepo.create(newRedisKey, "password", storedPassword); // Include password in the new key

            // Delete the old Redis key
            mapRepo.deleteKey(oldRedisKey);

            // Update the session with the new user object
            loggedInUser.setUsername(updatedUser.getUsername());
            loggedInUser.setEmail(updatedUser.getEmail());
            session.setAttribute("loggedInUser", loggedInUser);

            model.addAttribute("message", "Profile updated successfully.");
        }

        // Invalidate the current session and force re-login
        session.invalidate();  // Invalidate the session to reset any stored user info

        // Redirect to the login page for re-authentication
        return "redirect:/user/login";
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

        // Change the password in MapRepo
        String redisKey = "user:" + loggedInUser.getUsername();
        mapRepo.create(redisKey, "password", newPassword);

        loggedInUser.setPassword(newPassword);  // Update the session with the new password
        session.setAttribute("loggedInUser", loggedInUser);
        model.addAttribute("message", "Password changed successfully.");
        return "redirect:/user/profile";  // Redirect to the profile page after password change
    }

    // user to log out from session and their account
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/event/home";
    }

}
