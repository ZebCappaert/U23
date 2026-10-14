
package softball.app.fundraising.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FundraisingActivityDTO(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 1000) String description,
        @NotBlank @Size(max = 50) String activityType,
        LocalDate startDate,
        LocalDate endDate,
        @Size(max = 30) String status) {
}
