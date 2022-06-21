package by.senla.training.bulyha.pricemonitoring.subcategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubcategoryAdminDto {

    private Long id;
    private String category;
    private String subcategoryName;
    private String status;
}
