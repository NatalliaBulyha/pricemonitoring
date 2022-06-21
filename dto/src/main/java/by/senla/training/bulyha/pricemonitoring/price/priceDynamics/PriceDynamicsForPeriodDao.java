package by.senla.training.bulyha.pricemonitoring.price.priceDynamics;

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
public class PriceDynamicsForPeriodDao {

    private String goodName;
    private List<PriceListDynamicsDto> priceDynamics;
}
