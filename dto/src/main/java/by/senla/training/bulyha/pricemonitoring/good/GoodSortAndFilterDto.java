package by.senla.training.bulyha.pricemonitoring.good;

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
public class GoodSortAndFilterDto {

    private String subcategoryName;
    private List<GoodShopsDto> goods;
}
