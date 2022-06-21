package by.senla.training.bulyha.pricemonitoring;

import by.senla.training.bulyha.pricemonitoring.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDao extends JpaRepository<Token, Long> {

    boolean existsByNumber(String number);
}
