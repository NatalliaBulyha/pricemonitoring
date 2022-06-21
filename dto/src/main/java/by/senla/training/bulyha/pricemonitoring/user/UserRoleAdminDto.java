package by.senla.training.bulyha.pricemonitoring.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleAdminDto {

    private Long id;
    private String login;
    private List<String> roles;
}
