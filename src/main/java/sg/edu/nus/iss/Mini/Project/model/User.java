package sg.edu.nus.iss.Mini.Project.model;

// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class User {
    
    @Size(min = 5, max = 20, message = "Username should be at least 5 to 20 characters long")
    @NotBlank(message = "Username is mandatory")
    private String username;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,12}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be between 8 - 12 characters")
    @NotBlank(message = "Password is mandatory")
    private String password;
    @Email(message = "Email input does not comform to email format")
    @NotBlank(message = "Email is mandatory")
    private String email;


    // Create a BCrypt password encoder instance
    // private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        // this.password = passwordEncoder.encode(password);
        this.email = email;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return username + "," + password + "," + email;
    }

    /* public boolean checkPassword(String password) {
        return passwordEncoder.matches(password, this.password);  // Check hashed password during login
    } */
    
}
