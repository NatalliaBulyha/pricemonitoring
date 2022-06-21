package by.senla.training.bulyha.pricemonitoring.controller;

import by.senla.training.bulyha.pricemonitoring.user.UserAllInfoDto;
import by.senla.training.bulyha.pricemonitoring.user.UserDto;
import by.senla.training.bulyha.pricemonitoring.service.UserService;
import by.senla.training.bulyha.pricemonitoring.user.UserResponseUpdateDto;
import by.senla.training.bulyha.pricemonitoring.user.UserRoleAdminDto;
import by.senla.training.bulyha.pricemonitoring.user.UserUpdatePasswordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("users")
@Tag(name = "user controller",
        description = "the user API with description tag annotation.")
public class UserController {

    private UserService userService;
    public static final Logger LOG = Logger.getLogger(UserController.class.getName());

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "deleted user",
            description = "method changes status of user from actual to deleted. " +
            "admin can delete any user and user can delete only himself.")
    @PostMapping("/status")
    public UserDto updateUserStatus(@RequestParam @Min(1) Long id) {
        UserDto userDto = userService.updateUserStatus(id);
        LOG.info(String.format("User with id = %s was deleted", id));
        return userDto;
    }

    @Operation(summary = "update user",
            description = "method updates user. userId is necessarily; lastName, firstName, mobileNumber, email - are " +
                    "no necessarily, only what will be change; mobileNumber mast be in +375(33) 123-45-67")
    @PostMapping("/editing")
    public UserResponseUpdateDto updateUser(@RequestParam @Min(1) Long userId,
                                            @RequestParam(required = false) String lastName,
                                            @RequestParam(required = false) String firstName,
                                            @RequestParam(required = false) @Size(min = 17, max = 18, message = "Enter " +
                                                    "mobile number in the format: +375(XX) XXX-XX-XX")
                                            @Pattern(regexp = "^(\\+\\d{1,3}( )?)?(\\(\\d{2}\\))[- ]?\\d{3}([- ]?\\d{2}){2}$",
                                                    message = "Wrong mobile phone format. Enter in the format: +375(XX) XXX-XX-XX")
                                                        String mobileNumber,
                                            @RequestParam(required = false) String email) {
        Map<String, String> updateUserMap = new HashMap<>();
        updateUserMap.put("id", userId.toString());
        if (lastName != null) {
            updateUserMap.put("last_name", lastName);
        }
        if (firstName != null) {
            updateUserMap.put("first_name", firstName);
        }
        if (mobileNumber != null) {
            updateUserMap.put("mobile_number", mobileNumber);
        }
        if (email != null) {
            updateUserMap.put("email", email);
        }
        UserResponseUpdateDto userResponseUpdateDto = userService.updateUser(updateUserMap);
        LOG.info(String.format("User with login = %s was updated.", userResponseUpdateDto.getLogin()));
        return userResponseUpdateDto;
    }

    @Operation(summary = "get all users",
            description = "get all info about users, only for admins")
    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public List<UserAllInfoDto> getAll(@RequestParam(required = false) String status) {
        return userService.getAll(status);
    }

    @Operation(summary = "update password",
            description = "method updates password; you can add login and new password; you can only change your password.")
    @PostMapping
    public void updatePassword(@RequestBody UserUpdatePasswordDto userUpdatePasswordDto) {
        userService.updatePassword(userUpdatePasswordDto);
        LOG.info(String.format("User with login = %s changed password.", userUpdatePasswordDto.getLogin()));
    }

    @Operation(summary = "add admin role",
            description = "admin adds admin role to the user. user id - 1 anna, 2 lena, 3 genka, 4 egor, 5 natasha, 6 admin")
    @Secured({"ROLE_ADMIN"})
    @PostMapping("/role")
    public UserRoleAdminDto addAdminRole(@RequestParam @Min(1) Long id) {
        UserRoleAdminDto userDto = userService.addAdminRole(id);
        LOG.info(String.format("User with id = %s added role admin.", id));
        return userDto;
    }
}
