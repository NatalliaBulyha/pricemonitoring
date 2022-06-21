package by.senla.training.bulyha.pricemonitoring.good;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodGoodsDto {

    private String name;
    private String producer;
    private String description;
    private BigDecimal price;
}
