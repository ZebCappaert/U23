package softball.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import softball.app.jpa.Activity;
import softball.app.jpa.Attendance;
import softball.app.jpa.User;
import softball.app.repository.ActivityRepository;
import softball.app.repository.AttendanceRepository;
import softball.app.repository.UserRepository;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    public ActivityController(ActivityRepository activityRepository, UserRepository userRepository,
            AttendanceRepository attendanceRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping
    public List<Activity> getAllActivities() {
        return this.activityRepository.findAll();
    }

    @PostMapping
    @Transactional
    public Activity createActivity(@RequestBody Activity activity) {
        System.out.println("New activity added: " + activity);
        Activity savedActivity = activityRepository.save(activity);

        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            Attendance defaultAttendance = new Attendance(user, savedActivity, true);
            attendanceRepository.save(defaultAttendance);
        }

        return savedActivity;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        if (activityRepository.existsById(id)) {
            activityRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
