package by.senla.training.bulyha.pricemonitoring.controller;

import by.senla.training.bulyha.pricemonitoring.entity.User;
import by.senla.training.bulyha.pricemonitoring.security.JwtProvider;
import by.senla.training.bulyha.pricemonitoring.service.TokenService;
import by.senla.training.bulyha.pricemonitoring.service.UserService;
import by.senla.training.bulyha.pricemonitoring.user.NewUserDto;
import by.senla.training.bulyha.pricemonitoring.user.NewUserIdDto;
import by.senla.training.bulyha.pricemonitoring.user.UserLoginDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.logging.Logger;

@Validated
@RestController
@RequestMapping("auth")
@Tag(name = "login controller", description = "the login API with description tag annotation.")
public class LoginController {

    private UserService userService;
    private TokenService tokenService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    public static final Logger LOG = Logger.getLogger(LoginController.class.getName());

    public LoginController(UserService userService, TokenService tokenService, BCryptPasswordEncoder bCryptPasswordEncoder,
                           AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Operation(summary = "login",
            description = "method returns token if login and password are ok.")
            @PostMapping("/login")
    public ResponseEntity<String> loginIntoApp(@RequestBody UserLoginDto userLoginDto) {
        if (userService.existUserByLogin(userLoginDto.getLogin())) {

            User user = userService.findByLogin(userLoginDto.getLogin());
            if (bCryptPasswordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
                final Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userLoginDto.getLogin(),
                                userLoginDto.getPassword()
                        )
                );
                return ResponseEntity
                        .ok(jwtProvider
                                .generateJwt(authentication));
            } else {
                LOG.info("Incorrect password!");
                return ResponseEntity.badRequest().body("Incorrect password!");
            }
        } else {
            LOG.info(String.format("User with login = %s is not found!", userLoginDto.getLogin()));
            return ResponseEntity.badRequest().body(String.format("User with login = %s is not found!", userLoginDto.getLogin()));
        }
    }

    @Operation(summary = "logout",
            description = "method adds token to database.")
    @PostMapping("/exit")
    public ResponseEntity<String> logout(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = jwtProvider.resolveToken(request);
        String username;
        if (token != null) {
            username = jwtProvider.getUsernameFromToken(token);
            tokenService.addToken(token, username);
            LOG.info(username + " is logged out");
        }
        return ResponseEntity.ok()
                .body("You are logged out");
    }

    @Operation(summary = "register",
            description = "method adds new user. mobile number is like +375(33) 123-45-67; birthday is like yyyy-MM-dd.")
    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody @Valid NewUserDto newUserDto) {
        NewUserIdDto userId;
        if (!userService.existUserByLogin(newUserDto.getLogin())) {
            userId = userService.saveUser(newUserDto);
            LOG.info(String.format("User with login = %s is registered!", newUserDto.getLogin()));
        } else {
            LOG.info(String.format("New user not registered. User with login = %s already exists.", newUserDto.getLogin()));
            return ResponseEntity.badRequest().body(String.format("User with login = %s already exists!", newUserDto.getLogin()));
        }
        LOG.info(String.format("New user registered with id = %s.", userId.getId()));
        return ResponseEntity.ok().body("You have successfully registered!");
    }
}
