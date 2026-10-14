
package softball.app.fundraising.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSupportCardSaleDTO(
        @NotNull Long sellerUserId,
        @Size(max = 150) String buyerName,
        @NotNull @Min(1) Integer quantity,
        @NotNull @DecimalMin("0.00") BigDecimal unitPrice,
        @Size(max = 1000) String notes) {
}
