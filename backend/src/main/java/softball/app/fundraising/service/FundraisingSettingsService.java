
package softball.app.fundraising.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import softball.app.fundraising.dto.UpdateFundraisingSettingsDTO;
import softball.app.fundraising.entity.FundraisingSettings;
import softball.app.fundraising.repository.FundraisingSettingsRepository;

@Service
public class FundraisingSettingsService {

    private static final Long SETTINGS_ID = 1L;

    private final FundraisingSettingsRepository repository;

    public FundraisingSettingsService(
            FundraisingSettingsRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public FundraisingSettings getSettings() {
        return repository.findById(SETTINGS_ID)
                .orElseThrow(() -> new IllegalStateException(
                        "Fundraisinginstellingen ontbreken. "
                                + "Voer het SQL-initialisatiescript uit."));
    }

    @Transactional
    public FundraisingSettings updateSettings(
            UpdateFundraisingSettingsDTO dto) {
        FundraisingSettings settings = getSettings();
        BigDecimal amount = dto.targetAmount();

        if (amount.scale() > 2) {
            amount = amount.setScale(2, java.math.RoundingMode.UNNECESSARY);
        }

        settings.setTargetAmount(amount);
        return repository.save(settings);
    }
}
