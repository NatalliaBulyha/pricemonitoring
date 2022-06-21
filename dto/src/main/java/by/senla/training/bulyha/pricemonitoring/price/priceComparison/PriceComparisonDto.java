package by.senla.training.bulyha.pricemonitoring.price.priceComparison;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceComparisonDto {

    private String goodName;
    private List<PriceComparisonListDto> priceComparisonListDtoList;
}
