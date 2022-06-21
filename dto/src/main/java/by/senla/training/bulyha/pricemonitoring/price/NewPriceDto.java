package by.senla.training.bulyha.pricemonitoring.price;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewPriceDto {

    @Min(1)
    private Long goodId;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 3, fraction = 2)
    private BigDecimal price;

    @Min(1)
    private Long shopId;
}
