package by.senla.training.bulyha.pricemonitoring;

import by.senla.training.bulyha.pricemonitoring.entity.Rating;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingDao extends JpaRepository<Rating, Long> {

    List<Rating> findAllByShopIdAndStatus(Long shopId, EntityStatusEnum status);
}
