package by.senla.training.bulyha.pricemonitoring.service;

import by.senla.training.bulyha.pricemonitoring.entity.User;
import by.senla.training.bulyha.pricemonitoring.user.NewUserDto;
import by.senla.training.bulyha.pricemonitoring.user.NewUserIdDto;
import by.senla.training.bulyha.pricemonitoring.user.UserAllInfoDto;
import by.senla.training.bulyha.pricemonitoring.user.UserDto;
import by.senla.training.bulyha.pricemonitoring.user.UserResponseUpdateDto;
import by.senla.training.bulyha.pricemonitoring.user.UserRoleAdminDto;
import by.senla.training.bulyha.pricemonitoring.user.UserUpdatePasswordDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserDto updateUserStatus(Long id);

    UserResponseUpdateDto updateUser(Map<String, String> map);

    List<UserAllInfoDto> getAll(String status);

    User findByLogin(String login);

    void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto);

    Boolean existUserByLogin(String userName);

    NewUserIdDto saveUser(NewUserDto newUser);

    UserRoleAdminDto addAdminRole(Long id);
}
