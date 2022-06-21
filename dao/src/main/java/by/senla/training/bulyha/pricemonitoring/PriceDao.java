package by.senla.training.bulyha.pricemonitoring;

import by.senla.training.bulyha.pricemonitoring.entity.Price;
import by.senla.training.bulyha.pricemonitoring.enums.PricesStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface PriceDao extends JpaRepository<Price, Long> {
    Price findPriceByPriceAndGoodName(BigDecimal price, String goodName);

    List<Price> findAllByShopId(Long id);

    Price findPriceByGoodIdAndShopIdAndStatus(Long goodId, Long shopId, PricesStatusEnum status);

    List<Price> findPriceByGoodIdAndStatus(Long goodId, PricesStatusEnum status);

    @Query(value = "SELECT p.* FROM prices p WHERE p.goods_id = ?1 AND DATE BETWEEN ?2 AND ?3 AND (p.status = 'ACTUAL' OR p.status = 'HISTORY')",
            nativeQuery = true)
    List<Price> findPricesByGoodIdAndPeriod(Long goodId, LocalDate startDate, LocalDate finishDate);

    @Query(value = "SELECT p.* FROM prices p WHERE (p.status = 'ACTUAL' OR + p.status = 'HISTORY') AND p.goods_id = ?1 AND p.shops_id = ?4 AND DATE BETWEEN ?2 AND ?3",
            nativeQuery = true)
    List<Price> findPricesByGoodIdAndPeriodByShop(Long goodId, LocalDate startDate, LocalDate finishDate, Long shopId);

    List<Price> findPricesByGoodId(Long goodId);

    List<Price> findAllByShopIdAndStatus(Long shopId, PricesStatusEnum status);

    List<Price> findByGoodIdAndStatusAndShopIdIn(Long goodId, PricesStatusEnum status, Set<Long> shopsId);
}
