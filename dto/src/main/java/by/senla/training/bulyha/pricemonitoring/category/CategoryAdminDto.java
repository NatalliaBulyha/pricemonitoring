package by.senla.training.bulyha.pricemonitoring.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAdminDto {

    private Long id;
    private String category;
    private String status;
}
