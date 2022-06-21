package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.TokenDao;
import by.senla.training.bulyha.pricemonitoring.exception.AuthException;
import by.senla.training.bulyha.pricemonitoring.mapper.TokenMapper;
import by.senla.training.bulyha.pricemonitoring.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TokenServiceImpl implements TokenService {

    private TokenDao tokenDao;
    private TokenMapper mapper;

    @Autowired
    public TokenServiceImpl(TokenDao tokenDao, TokenMapper mapper) {
        this.tokenDao = tokenDao;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public void existTokenByName(String token) {
        if (tokenDao.existsByNumber(token)) {
            throw new AuthException("Access closed. You are logged out and token is not valid.");
        }
    }

    @Transactional
    @Override
    public void addToken(String token, String userName) {
        tokenDao.save(mapper.getStringToToken(token, userName));
    }
}
