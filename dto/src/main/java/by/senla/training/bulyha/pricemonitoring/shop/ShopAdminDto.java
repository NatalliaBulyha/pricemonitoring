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
public class ShopAdminDto {

    private Long id;
    private String type;
    private String brand;
    private String address;
    private String contactNumber;
    private String openTime;
    private String closeTime;
    private String status;
    private String assortment;
    private String qualityOfService;
    private String prices;
}
