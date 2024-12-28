package sg.edu.nus.iss.Mini.Project.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.Mini.Project.model.User;
import sg.edu.nus.iss.Mini.Project.repo.MapRepo;

@Service
public class UserService {

    @Autowired
    MapRepo mapRepo;

    private static final String REDIS_KEY = "user";  // Prefix for Redis user data
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Hash the password before saving
        users.put(user.getUsername(), user);
    }

    public User findUserByUsername(String username) {
        return users.get(username);  // Retrieve user by username
    }    

    // Save or update a user's data in Redis
    public void saveUser(User user) {
        // Save user details in Redis using user ID as the hashKey
        mapRepo.create(REDIS_KEY, user.getUsername(), user.getPassword()); // Store username and password
        mapRepo.create(REDIS_KEY, user.getUsername() + "_email", user.getEmail()); // Store email as a separate key
    }

    // Get a user from Redis by username
    public User getUserByUsername(String username) {
        String password = (String) mapRepo.get(REDIS_KEY, username);  // Retrieve password from Redis
        String email = (String) mapRepo.get(REDIS_KEY, username + "_email");  // Retrieve email

        if(password == null || email == null) {
            return null;  // User not found
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        return user;
    }

    // Check if a user exists by username
    public boolean userExists(String username) {
        return mapRepo.keyExists(REDIS_KEY, username);  // Check if user exists in Redis
    }

    // Update user details
    public void updateUser(User user) {
        saveUser(user);  // Update the user by saving again (overwrite in Redis)
    }

    // Change the user's password
    public void changePassword(User user, String newPassword) {
        mapRepo.create(REDIS_KEY, user.getUsername(), newPassword);  // Update password in Redis
    }

    // Delete a user from Redis
    public void deleteUser(User user) {
        mapRepo.delete(REDIS_KEY, user.getUsername());  // Delete user by username
        mapRepo.delete(REDIS_KEY, user.getUsername() + "_email");  // Delete user's email as well
    }
}
