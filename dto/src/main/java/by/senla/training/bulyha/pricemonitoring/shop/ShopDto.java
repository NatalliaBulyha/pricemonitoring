package by.senla.training.bulyha.pricemonitoring.shop;

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
public class ShopDto {

    private String type;
    private String brand;
    private String address;
    private String contactNumber;
    private String openTime;
    private String closeTime;
    private String assortment;
    private String qualityOfService;
    private String prices;
}
