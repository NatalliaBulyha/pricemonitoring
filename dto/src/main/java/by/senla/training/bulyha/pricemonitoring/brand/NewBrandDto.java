package by.senla.training.bulyha.pricemonitoring.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewBrandDto {


    private String name;
    private String email;

    @Size(min = 17, max = 18, message = "Enter mobile number in the format: +375(XX) XXX-XX-XX")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?(\\(\\d{2}\\))[- ]?\\d{3}([- ]?\\d{2}){2}$", message = "Wrong mobile phone " +
            "format. Enter in the format: +375(XX) XXX-XX-XX")
    private String contactNumber;

    @Size(min = 9, max = 9, message = "Unp must be 9 characters.")
    private String unp;
}
