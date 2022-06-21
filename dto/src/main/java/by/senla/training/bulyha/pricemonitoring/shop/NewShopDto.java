package by.senla.training.bulyha.pricemonitoring.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewShopDto {

    private String type;
    private Long brand;
    private String address;

    @Size(min = 17, max = 18, message = "Enter mobile number in the format: +375(XX) XXX-XX-XX")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?(\\(\\d{2}\\))[- ]?\\d{3}([- ]?\\d{2}){2}$", message = "Wrong mobile phone " +
            "format. Enter in the format: +375(XX) XXX-XX-XX")
    private String contactNumber;

    @DateTimeFormat(pattern = "HH-mm")
    private LocalTime openTime;

    @DateTimeFormat(pattern = "HH-mm")
    private LocalTime closeTime;
}
