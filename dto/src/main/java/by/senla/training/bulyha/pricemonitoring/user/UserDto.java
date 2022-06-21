package by.senla.training.bulyha.pricemonitoring.user;

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
public class UserDto {

    private String login;
    private String lastName;
    private String firstName;
    private String status;
}
