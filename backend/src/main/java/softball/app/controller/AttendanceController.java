package softball.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import softball.app.jpa.Attendance;
import softball.app.repository.AttendanceRepository;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;

    public AttendanceController(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/{activityId}")
    public List<Attendance> getAttendanceByActivity(@PathVariable Long activityId) {
        return attendanceRepository.findByActivityId(activityId);
    }

    @PostMapping
    public Attendance saveAttendance(@RequestBody Attendance newAttendance) {
        return attendanceRepository
                .findByUserIdAndActivityId(newAttendance.getUser().getId(), newAttendance.getActivity().getId())
                .map((eAttendance) -> {
                    eAttendance.setIsPresent(newAttendance.getIsPresent());
                    return attendanceRepository.save(eAttendance);
                }).orElseGet(() -> {
                    return attendanceRepository.save(newAttendance);
                });
    }
}
