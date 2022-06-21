package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.UserDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public JwtUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }


    /**
     * Метод получения UserDetail по username
     *
     * @param username - username
     * @return - UserDetails
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userDao.findByLogin(username);
    }
}

