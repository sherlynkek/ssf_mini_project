package sg.edu.nus.iss.Mini.Project.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.Mini.Project.model.User;

@Service
public class UserService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Hash the password before saving
        users.put(user.getUsername(), user);
    }

    public User findUserByUsername(String username) {
        return users.get(username);  // Retrieve user by username
    }
}
