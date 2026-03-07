package softball.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import softball.app.dto.LoginDTO;
import softball.app.jpa.User;
import softball.app.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder pe) {
        this.userRepository = userRepository;
        this.passwordEncoder = pe;
    }

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/")
    public User createUser(@RequestBody User user) {
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        } else {
            String fallback = (user.getUsername() != null) ? user.getUsername().trim() : "default";
            user.setPassword(passwordEncoder.encode(fallback));
        }

        if (user.getUsername() != null) {
            user.setUsername(user.getUsername().trim());
        }

        System.out.println("New user added: " + user.getFirstName());
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginRequest) {
        return userRepository.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(loginRequest.getPassword().trim(), user.getPassword())) {
                        return ResponseEntity.ok(user);
                    }
                    return ResponseEntity.status(401).body("Wachtwoord onjuist");
                })
                .orElse(ResponseEntity.status(401).body("Gebruiker niet gevonden"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
