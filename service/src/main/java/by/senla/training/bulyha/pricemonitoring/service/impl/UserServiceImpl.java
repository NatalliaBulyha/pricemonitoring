package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.RoleDao;
import by.senla.training.bulyha.pricemonitoring.UserDao;
import by.senla.training.bulyha.pricemonitoring.entity.Role;
import by.senla.training.bulyha.pricemonitoring.exception.AuthException;
import by.senla.training.bulyha.pricemonitoring.exception.InternalException;
import by.senla.training.bulyha.pricemonitoring.service.util.UserName;
import by.senla.training.bulyha.pricemonitoring.user.NewUserDto;
import by.senla.training.bulyha.pricemonitoring.user.NewUserIdDto;
import by.senla.training.bulyha.pricemonitoring.user.UserAllInfoDto;
import by.senla.training.bulyha.pricemonitoring.user.UserDto;
import by.senla.training.bulyha.pricemonitoring.entity.User;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.mapper.UserMapper;
import by.senla.training.bulyha.pricemonitoring.service.UserService;
import by.senla.training.bulyha.pricemonitoring.user.UserResponseUpdateDto;
import by.senla.training.bulyha.pricemonitoring.user.UserRoleAdminDto;
import by.senla.training.bulyha.pricemonitoring.user.UserUpdatePasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private UserMapper mapper;
    private RoleDao roleDao;
    private UserName userName;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public static final Logger LOG = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    public UserServiceImpl(UserDao userDao, UserMapper mapper, RoleDao roleDao, UserName userName, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.userDao = userDao;
        this.mapper = mapper;
        this.roleDao = roleDao;
        this.userName = userName;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @Override
    public UserDto updateUserStatus(Long id) {
        String login = userName.getUserName();
        User user = userDao.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with id = %s is not found", id)));
        User userByName = userDao.findByLogin(login);
        Role adminRole = roleDao.findByName("ROLE_ADMIN");
        if (user.getLogin().equals(login) || userByName.getRoleList().contains(adminRole)) {
            user.setStatus(EntityStatusEnum.DELETED);
            LOG.info(String.format("Users status %s has been changed to deleted", login));
        } else {
            LOG.info(String.format("You are not authorized to correct data for user id = %s.", id));
            throw new AuthException(String.format("You are not authorized to correct data for user id = %s.", id));
        }
        return mapper.getUserToUserDto(userDao.save(user));
    }

    @Transactional
    @Override
    public UserResponseUpdateDto updateUser(Map<String, String> map) {
        User user = userDao.findById(Long.parseLong(map.get("id"))).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with id = %s is not found", map.get("id"))));
        String login = userName.getUserName();
        if (!user.getLogin().equals(login)) {
            LOG.info(String.format("You are not authorized to correct data for user id = %s.", user.getId()));
            throw new AuthException(String.format("You are not authorized to correct data for user id = %s.", user.getId()));
        }
        if (map.containsKey("last_name")) {
            user.setLastName(map.get("last_name"));
            LOG.info(String.format("Last name of user with login = %s has been changed to %s.", user.getLogin(), map.get("last_name")));
        }
        if (map.containsKey("first_name")) {
            user.setFirstName(map.get("first_name"));
            LOG.info(String.format("First name of user with login = %s has been changed to %s.", user.getLogin(), map.get("first_name")));
        }
        if (map.containsKey("mobile_number")) {
            user.setMobileNumber(map.get("mobile_number"));
            LOG.info(String.format("Mobile number of user with login = %s has been changed to %s.", user.getLogin(), map.get("mobile_number")));
        }
        if (map.containsKey("email")) {
            user.setEmail(map.get("email"));
            LOG.info(String.format("Email of user with login = %s has been changed to %s.", user.getLogin(), map.get("email")));
        }
        return mapper.getUserToUserUpdateDto(userDao.save(user));
    }

    @Transactional
    @Override
    public List<UserAllInfoDto> getAll(String status) {
        return (status == null ? mapper.getUserListToUserAllInfoDtoList(userDao.findAll()) :
                mapper.getUserListToUserAllInfoDtoList(userDao.findAllByStatus(EntityStatusEnum.valueOf(status.toUpperCase()))));
    }

    public NewUserIdDto saveUser(NewUserDto newUser) {
        if (!newUser.getPassword().equals(newUser.getRepeatedPassword())) {
            throw new InternalException("Passwords must match!");
        }

        Role userRole = roleDao.findByName("ROLE_USER");
        User user = mapper.getNewUserDtoToUser(newUser);
        user.setRoleList(Collections.singleton(userRole));
        User userFromDB = userDao.save(user);
        return mapper.getUserToNewUserIdDto(userFromDB);
    }

    @Transactional
    @Override
    public User findByLogin(String login) {
        return userDao.findByLogin(login);
    }

    @Transactional
    @Override
    public void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto) {
        String loginFromContext = userName.getUserName();
        User userByLoginFromDto = userDao.findByLogin(userUpdatePasswordDto.getLogin());
        if (loginFromContext.equals(userByLoginFromDto.getLogin())) {
            userByLoginFromDto.setPassword(bCryptPasswordEncoder.encode(userUpdatePasswordDto.getNewPassword()));
            userDao.save(userByLoginFromDto);
        } else {
            LOG.info(String.format("You are not authorized to correct password for user login = %s.", userUpdatePasswordDto.getLogin()));
            throw new AuthException(String.format("You are not authorized to correct password for user login = %s.", userUpdatePasswordDto.getLogin()));
        }
    }

    @Transactional
    @Override
    public Boolean existUserByLogin(String login) {
        return userDao.existsByLogin(login);
    }

    @Transactional
    @Override
    public UserRoleAdminDto addAdminRole(Long id) {
        String name = "ROLE_ADMIN";
        User user = userDao.findByIdAndStatus(id, EntityStatusEnum.ACTUAL);
        if (user == null) {
            LOG.info(String.format("User with id = %s is not found", id));
            throw new EntityNotFoundException(String.format("User with id = %s is not found", id));
        }

        Set<Role> roles = user.getRoleList();

        if (roles.stream().anyMatch(p -> p.getName().equals(name))) {
            LOG.info(String.format("User with id = %s already has role admin.", id));
            throw new InternalException(String.format("User with id = %s already has role admin.", id));
        }
        Role role = roleDao.findByName(name);
        roles.add(role);
        user.setRoleList(roles);
        return mapper.getUserToUserRoleAdminDto(userDao.save(user));
    }
}
