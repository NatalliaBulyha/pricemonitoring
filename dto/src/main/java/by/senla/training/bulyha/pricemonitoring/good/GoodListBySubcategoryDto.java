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
public class GoodListBySubcategoryDto {

    private String category;
    private String subcategory;
    private List<String> goodsName;
}
