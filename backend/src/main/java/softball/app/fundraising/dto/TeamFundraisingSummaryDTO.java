
package softball.app.fundraising.dto;

import java.math.BigDecimal;

public record TeamFundraisingSummaryDTO(
        Long sellerUserId,
        String firstName,
        String lastName,
        Long cardsSold,
        BigDecimal revenue) {
}
