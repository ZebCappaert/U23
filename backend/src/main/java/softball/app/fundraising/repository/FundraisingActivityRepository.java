
package softball.app.fundraising.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import softball.app.fundraising.entity.FundraisingActivity;

public interface FundraisingActivityRepository
                extends JpaRepository<FundraisingActivity, Long> {

        List<FundraisingActivity> findAllByOrderByStartDateAscIdAsc();

        List<FundraisingActivity> findByStatusOrderByStartDateAscIdAsc(
                        String status);
}
