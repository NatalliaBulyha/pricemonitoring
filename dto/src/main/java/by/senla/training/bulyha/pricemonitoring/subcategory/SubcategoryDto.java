package by.senla.training.bulyha.pricemonitoring.subcategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubcategoryDto {

    private String categoryName;
    private List<String> subcategories;
}
