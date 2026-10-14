
package softball.app.fundraising.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record UpdateFundraisingSettingsDTO(
        @NotNull @DecimalMin("0.00") BigDecimal targetAmount) {
}
