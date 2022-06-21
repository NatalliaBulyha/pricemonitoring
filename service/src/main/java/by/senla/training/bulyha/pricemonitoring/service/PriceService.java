package by.senla.training.bulyha.pricemonitoring.service;

import by.senla.training.bulyha.pricemonitoring.price.NewPriceDto;
import by.senla.training.bulyha.pricemonitoring.price.PriceAdminDto;
import by.senla.training.bulyha.pricemonitoring.price.priceComparison.PriceComparisonDto;
import by.senla.training.bulyha.pricemonitoring.price.PriceDto;
import by.senla.training.bulyha.pricemonitoring.price.priceDynamics.PriceDynamicsForPeriodDao;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PriceService {
    PriceDto getById(Long priceId);

    List<PriceDto> getAll();

    List<PriceAdminDto> getByShopId(Long shopId);

    PriceDto getByPriceAndGood(BigDecimal price, String goodName);

    PriceDto addPrice(NewPriceDto newPriceDto);

    PriceDynamicsForPeriodDao getPriceDynamicsForPeriod(Long goodId, LocalDate startDate, LocalDate finishDate, Long shopId);

    PriceComparisonDto getPriceComparison(Long goodId, Set<Long> shops);

    PriceDynamicsForPeriodDao getListPriceGraphicalChangeOfPriceDynamics(Long goodId);

    void importFile(MultipartFile file);
}
