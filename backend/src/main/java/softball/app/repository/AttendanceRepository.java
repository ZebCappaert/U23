package softball.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import softball.app.jpa.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUserIdAndActivityId(Long userId, Long activityId);

    List<Attendance> findByActivityId(Long activityId);

}
