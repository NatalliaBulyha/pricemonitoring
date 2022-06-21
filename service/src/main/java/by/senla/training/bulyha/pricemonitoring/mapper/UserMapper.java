package by.senla.training.bulyha.pricemonitoring.mapper;

import by.senla.training.bulyha.pricemonitoring.entity.Role;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.user.NewUserDto;
import by.senla.training.bulyha.pricemonitoring.user.NewUserIdDto;
import by.senla.training.bulyha.pricemonitoring.user.UserAllInfoDto;
import by.senla.training.bulyha.pricemonitoring.user.UserDto;
import by.senla.training.bulyha.pricemonitoring.entity.User;
import by.senla.training.bulyha.pricemonitoring.user.UserResponseUpdateDto;
import by.senla.training.bulyha.pricemonitoring.user.UserRoleAdminDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    @Lazy
    public UserMapper(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDto getUserToUserDto(User user) {
        return UserDto.builder()
                .login(user.getLogin())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .status(user.getStatus().toString())
                .build();
    }

    public List<UserDto> getUserListToUserDtoList(List<User> userList) {
        return userList.stream().map(this::getUserToUserDto).collect(Collectors.toList());
    }

    public UserResponseUpdateDto getUserToUserUpdateDto(User user) {
        return UserResponseUpdateDto.builder()
                .login(user.getLogin())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .mobileNumber(user.getMobileNumber())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();
    }

    public User getNewUserDtoToUser(NewUserDto newUser) {
        return User.builder()
                .login(newUser.getLogin())
                .password(bCryptPasswordEncoder.encode(newUser.getPassword()))
                .lastName(newUser.getLastName())
                .firstName(newUser.getFirstName())
                .registrationDate(LocalDate.now())
                .mobileNumber(newUser.getMobileNumber())
                .email(newUser.getEmail())
                .birthday(newUser.getBirthday())
                .status(EntityStatusEnum.ACTUAL)
                .build();
    }

    public NewUserIdDto getUserToNewUserIdDto(User user) {
        return NewUserIdDto.builder()
                .id(user.getId())
                .build();
    }

    public UserAllInfoDto getUserToUserAllInfoDto(User user) {
        return UserAllInfoDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .mobileNumber(user.getMobileNumber())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .registrationDate(user.getRegistrationDate())
                .status(user.getStatus().toString())
                .role(getRoleList(user.getRoleList()))
                .build();
    }

    private List<String> getRoleList(Set<Role> roles) {
        List<String> roleList = new ArrayList<>();
        for (Role role : roles) {
            roleList.add(role.getName());
        }
        return roleList;
    }

    public List<UserAllInfoDto> getUserListToUserAllInfoDtoList(List<User> users) {
        List<UserAllInfoDto> userAllInfoDtoList = new ArrayList<>();
        for (User user : users) {
            userAllInfoDtoList.add(getUserToUserAllInfoDto(user));
        }
        return users.stream().map(this::getUserToUserAllInfoDto).collect(Collectors.toList());
    }

    public UserRoleAdminDto getUserToUserRoleAdminDto(User user) {
        return UserRoleAdminDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .roles(getRoleList(user.getRoleList()))
                .build();
    }
}
