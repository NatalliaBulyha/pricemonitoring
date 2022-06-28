package by.senla.training.bulyha.pricemonitoring.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {

    private String shopName;
    private String address;
    private String assortment;
    private String qualityOfService;
    private String prices;
}