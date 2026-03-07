package softball.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import softball.app.jpa.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

}