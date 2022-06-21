package by.senla.training.bulyha.pricemonitoring.mapper;

import by.senla.training.bulyha.pricemonitoring.UserDao;
import by.senla.training.bulyha.pricemonitoring.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {

    private UserDao userDao;

    @Autowired
    public TokenMapper(UserDao userDao) {
        this.userDao = userDao;
    }

    public Token getStringToToken(String token, String userName) {
        return Token.builder()
                .number(token)
                .user(userDao.findByLogin(userName))
                .build();
    }
}
