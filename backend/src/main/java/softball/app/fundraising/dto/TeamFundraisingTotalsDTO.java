
package softball.app.fundraising.dto;

import java.math.BigDecimal;

public record TeamFundraisingTotalsDTO(
        Long totalCardsSold,
        BigDecimal totalRevenue,
        Long activeSellers) {
}
