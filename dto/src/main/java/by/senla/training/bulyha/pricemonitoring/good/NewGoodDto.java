package by.senla.training.bulyha.pricemonitoring.good;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewGoodDto {

    private String name;
    private String producer;
    private String country;
    private String description;
    private Long subcategory;

    @DecimalMin("0.01")
    private BigDecimal price;

    @Min(1)
    private Long shopId;
}
