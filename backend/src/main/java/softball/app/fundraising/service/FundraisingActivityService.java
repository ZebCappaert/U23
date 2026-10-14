
package softball.app.fundraising.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import softball.app.fundraising.dto.FundraisingActivityDTO;
import softball.app.fundraising.entity.FundraisingActivity;
import softball.app.fundraising.repository.FundraisingActivityRepository;

@Service
public class FundraisingActivityService {

    private static final List<String> VALID_STATUSES = List.of("PLANNED", "ACTIVE", "COMPLETED", "CANCELLED");

    private final FundraisingActivityRepository repository;

    public FundraisingActivityService(
            FundraisingActivityRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<FundraisingActivity> findAll() {
        return repository.findAllByOrderByStartDateAscIdAsc();
    }

    @Transactional(readOnly = true)
    public FundraisingActivity findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Fundraisingactie niet gevonden."));
    }

    @Transactional
    public FundraisingActivity create(FundraisingActivityDTO dto) {
        validate(dto);

        FundraisingActivity activity = new FundraisingActivity();
        applyDto(activity, dto, true);

        return repository.save(activity);
    }

    @Transactional
    public FundraisingActivity update(
            Long id,
            FundraisingActivityDTO dto) {
        validate(dto);

        FundraisingActivity activity = findById(id);
        applyDto(activity, dto, false);

        return repository.save(activity);
    }

    @Transactional
    public void delete(Long id) {
        FundraisingActivity activity = findById(id);
        repository.delete(activity);
    }

    private void validate(FundraisingActivityDTO dto) {
        if (dto.startDate() != null
                && dto.endDate() != null
                && dto.endDate().isBefore(dto.startDate())) {
            throw new IllegalArgumentException(
                    "De einddatum mag niet vóór de startdatum liggen.");
        }

        if (dto.status() != null
                && !dto.status().isBlank()
                && !VALID_STATUSES.contains(dto.status().toUpperCase())) {
            throw new IllegalArgumentException(
                    "Ongeldige status. Gebruik PLANNED, ACTIVE, COMPLETED of CANCELLED.");
        }
    }

    private void applyDto(
            FundraisingActivity activity,
            FundraisingActivityDTO dto,
            boolean creating) {
        activity.setName(dto.name().trim());
        activity.setDescription(dto.description());
        activity.setActivityType(dto.activityType().trim().toUpperCase());
        activity.setStartDate(dto.startDate());
        activity.setEndDate(dto.endDate());

        if (creating) {
            activity.setStatus(
                    dto.status() == null || dto.status().isBlank()
                            ? "PLANNED"
                            : dto.status().toUpperCase());
        } else if (dto.status() != null && !dto.status().isBlank()) {
            activity.setStatus(dto.status().toUpperCase());
        }
    }
}
