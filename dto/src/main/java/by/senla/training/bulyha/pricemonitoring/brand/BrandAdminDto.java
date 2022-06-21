package by.senla.training.bulyha.pricemonitoring.brand;

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
public class BrandAdminDto {

    private Long id;
    private String name;
    private String email;
    private String contactNumber;
    private Integer unp;
    private String status;
}
