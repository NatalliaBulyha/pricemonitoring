package by.senla.training.bulyha.pricemonitoring.good;

import by.senla.training.bulyha.pricemonitoring.entity.Good;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodDao extends JpaRepository<Good, Long>, JpaSpecificationExecutor<Good> {

    List<Good> findGoodLikeByName(String name);

    List<Good> findGoodByNameLikeAndStatus(String name, EntityStatusEnum status);

    Good findGoodByNameAndProducer(String name, String producer);

    List<Good> findGoodsBySubcategoryIdAndStatus(Long subcategoryId, EntityStatusEnum statusEnum);

    List<Good> findGoodsByStatus(EntityStatusEnum status);

    Good findGoodByNameAndProducerAndDescription(String name, String producer, String description);
}
