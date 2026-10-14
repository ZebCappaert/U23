
package softball.app.fundraising.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softball.app.fundraising.entity.FundraisingSettings;

public interface FundraisingSettingsRepository
        extends JpaRepository<FundraisingSettings, Long> {
}
