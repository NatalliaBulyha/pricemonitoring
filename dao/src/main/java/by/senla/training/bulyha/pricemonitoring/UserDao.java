package by.senla.training.bulyha.pricemonitoring;

import by.senla.training.bulyha.pricemonitoring.entity.User;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findByLogin(String login);

    User findByLoginAndPassword(String login, String password);

    Boolean existsByLogin(String login);

    List<User> findAllByStatus(EntityStatusEnum status);

    User findByIdAndStatus(Long id, EntityStatusEnum status);
}
