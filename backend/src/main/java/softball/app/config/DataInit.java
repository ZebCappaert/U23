package softball.app.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import softball.app.jpa.Role;
import softball.app.jpa.User;
import softball.app.repository.UserRepository;

@Component
public class DataInit implements CommandLineRunner {
    private final UserRepository userRepository;

    public DataInit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User a = new User("Zeb", "Zeb", "Zeb", "Cappaert", Role.COACH);
            userRepository.save(a);
            System.out.println("Added Zeb as COACH");
        } else {
            List<User> users = userRepository.findAll();
            System.out.println("Users in the database are: " + users);
        }
    }

}
