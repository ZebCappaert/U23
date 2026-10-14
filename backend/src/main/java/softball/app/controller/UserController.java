
package softball.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import softball.app.dto.LoginDTO;
import softball.app.dto.LoginResponseDTO;
import softball.app.jpa.Role;
import softball.app.jpa.User;
import softball.app.repository.AttendanceRepository;
import softball.app.repository.UserRepository;
import softball.app.service.AuthTokenService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService tokenService;

    public UserController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AttendanceRepository attendanceRepository,
            AuthTokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.attendanceRepository = attendanceRepository;
        this.tokenService = tokenService;
    }

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/")
    public User createUser(@RequestBody User user) {
        if (user.getUsername() == null
                || user.getUsername().isBlank()) {
            throw new IllegalArgumentException(
                    "Een gebruikersnaam is verplicht.");
        }

        if (user.getPassword() == null
                || user.getPassword().isBlank()) {
            throw new IllegalArgumentException(
                    "Een wachtwoord is verplicht.");
        }

        user.setUsername(user.getUsername().trim());
        user.setPassword(
                passwordEncoder.encode(user.getPassword().trim()));

        if (user.getRole() == Role.COACH) {
            String enteredCode = user.getCoachCode();
            String expectedCode = System.getenv("COACH_REGISTRATION_CODE");

            if (expectedCode == null
                    || expectedCode.isBlank()
                    || !expectedCode.equals(enteredCode)) {
                user.setRole(Role.PLAYER);
            }
        }

        return userRepository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDTO loginRequest) {

        if (loginRequest.getUsername() == null
                || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body("Gebruikersnaam en wachtwoord zijn verplicht.");
        }

        return userRepository
                .findByUsername(loginRequest.getUsername().trim())
                .filter(user -> passwordEncoder.matches(
                        loginRequest.getPassword(),
                        user.getPassword()))
                .map(user -> {
                    String token = tokenService.createToken(user.getId());
                    return ResponseEntity.ok(
                            LoginResponseDTO.from(user, token));
                })
                .orElseGet(() -> ResponseEntity.status(401)
                        .body(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).orElseThrow();
            attendanceRepository.deleteByUser(user);
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
