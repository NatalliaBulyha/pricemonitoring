package by.senla.training.bulyha.pricemonitoring.subcategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewSubcategoryDto {

    private @Min(1) Long categoryId;
    private String subcategoryName;
}
