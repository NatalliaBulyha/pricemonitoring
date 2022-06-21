package by.senla.training.bulyha.pricemonitoring.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {

    private String login;
    private String password;
    private String repeatedPassword;
    private String lastName;
    private String firstName;

    @Size(min = 17, max = 18, message = "Enter mobile number in the format: +375(XX) XXX-XX-XX")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?(\\(\\d{2}\\))[- ]?\\d{3}([- ]?\\d{2}){2}$", message = "Wrong mobile phone " +
            "format. Enter in the format: +375(XX) XXX-XX-XX")
    private String mobileNumber;
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
