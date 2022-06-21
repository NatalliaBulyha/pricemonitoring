package by.senla.training.bulyha.pricemonitoring.service;

public interface TokenService {

    void existTokenByName(String token);

    void addToken(String token, String userName);
}