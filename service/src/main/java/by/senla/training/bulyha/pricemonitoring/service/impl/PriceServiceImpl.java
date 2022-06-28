package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.enums.PricesStatusEnum;
import by.senla.training.bulyha.pricemonitoring.exception.CsvException;
import by.senla.training.bulyha.pricemonitoring.PriceDao;
import by.senla.training.bulyha.pricemonitoring.exception.InternalException;
import by.senla.training.bulyha.pricemonitoring.price.NewPriceDto;
import by.senla.training.bulyha.pricemonitoring.price.PriceAdminDto;
import by.senla.training.bulyha.pricemonitoring.price.PriceDto;
import by.senla.training.bulyha.pricemonitoring.entity.Price;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.mapper.PriceMapper;
import by.senla.training.bulyha.pricemonitoring.price.PriceImportDto;
import by.senla.training.bulyha.pricemonitoring.price.priceComparison.PriceComparisonDto;
import by.senla.training.bulyha.pricemonitoring.price.priceDynamics.PriceDynamicsForPeriodDao;
import by.senla.training.bulyha.pricemonitoring.service.PriceService;
import by.senla.training.bulyha.pricemonitoring.service.util.CSVHelper;
import by.senla.training.bulyha.pricemonitoring.service.util.TestPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class PriceServiceImpl implements PriceService {

    private PriceDao priceDao;
    private PriceMapper mapper;
    private CSVHelper csvHelper;
    private TestPeriod period;
    public static final Logger LOG = Logger.getLogger(PriceService.class.getName());

    @Autowired
    public PriceServiceImpl(PriceDao priceDao, PriceMapper mapper, CSVHelper csvHelper, TestPeriod period) {
        this.priceDao = priceDao;
        this.mapper = mapper;
        this.csvHelper = csvHelper;
        this.period = period;
    }

    @Transactional
    @Override
    public PriceDto getById(Long priceId) {
        return mapper.getPriceToPriceDto(priceDao.findById(priceId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Price with id = %s is not found", priceId))));
    }

    @Transactional
    @Override
    public List<PriceDto> getAll() {
        List<Price> prices = priceDao.findAll();
        if (prices.isEmpty()) {
            LOG.warning("Prices list is empty");
            throw new EntityNotFoundException("Prices list is empty");
        }
        return mapper.getPriceListToPriceDtoList(prices);
    }

    @Override
    public List<PriceAdminDto> getByShopId(Long shopId) {
        List<Price> price = priceDao.findAllByShopIdAndStatus(shopId, PricesStatusEnum.ACTUAL);
        if (price.isEmpty()) {
            LOG.warning(String.format("Prices with shopId = %s is not found", shopId));
            throw new EntityNotFoundException(String.format("Prices with shopId = %s is not found", shopId));
        }

        return mapper.getPriceListToPriceAdminDtoList(price);
    }

    @Override
    public PriceDto getByPriceAndGood(BigDecimal goodsPrice, String goodName) {
        Price price = priceDao.findPriceByPriceAndGoodName(goodsPrice, goodName);
        if (price == null) {
            LOG.warning(String.format("%s with price = %s is not found", goodName, goodsPrice));
            throw new EntityNotFoundException(String.format("%s with price = %s is not found", goodName, goodsPrice));
        }
        return mapper.getPriceToPriceDto(price);
    }

    @Transactional
    @Override
    public PriceDto addPrice(NewPriceDto newPriceDto) {
        Price price = priceDao.findPriceByGoodIdAndShopIdAndStatus(newPriceDto.getGoodId(), newPriceDto.getShopId(),
                PricesStatusEnum.ACTUAL);
        if (price != null) {
            price.setStatus(PricesStatusEnum.HISTORY);
            LOG.info(String.format("Status of price %s of good %s in shop %s has been changed to history.",
                    price.getPrice(), price.getGood().getName(), price.getShop().getBrand().getName()));
            priceDao.save(price);
        }
        return mapper.getPriceToPriceDto(priceDao.save(mapper.getNewPriceDtoToPrice(newPriceDto)));
    }

    @Transactional
    @Override
    public PriceDynamicsForPeriodDao getPriceDynamicsForPeriod(Long goodId, LocalDate startDate, LocalDate finishDate, Long shopId) {
        if (!period.isRightPeriod(startDate, finishDate)) {
            throw new InternalException("Start date mast be before finish date.");
        }
        List<Price> prices;
        prices = shopId == null ? priceDao.findPricesByGoodIdAndPeriod(goodId, startDate, finishDate) :
                priceDao.findPricesByGoodIdAndPeriodByShop(goodId, startDate, finishDate, shopId);
        if (prices.isEmpty()) {
            LOG.warning(String.format("Prices list dynamics for period with goodId = %s is empty.", goodId));
            throw new EntityNotFoundException("Prices have not changed during this period");
        }
        return mapper.getPriceToPriceDynamicsForPeriodDao(prices);
    }

    @Transactional
    @Override
    public PriceComparisonDto getPriceComparison(Long goodId, Set<Long> shops) {
        List<Price> prices = priceDao.findByGoodIdAndStatusAndShopIdIn(goodId, PricesStatusEnum.ACTUAL, shops);
        if (prices.isEmpty()) {
            LOG.warning(String.format("Prices comparison list with goodId = %s is empty.", goodId));
            throw new EntityNotFoundException("Prices comparison list is empty.");
        }
        return mapper.getPriceListToPriceComparisonDtoList(prices);
    }

    @Transactional
    @Override
    public PriceDynamicsForPeriodDao getListPriceGraphicalChangeOfPriceDynamics(Long goodId) {
        List<Price> prices = priceDao.findPricesByGoodId(goodId);
        if (prices.isEmpty()) {
            LOG.warning(String.format("Prices list graphical change of price dynamics with goodId = %s is empty.", goodId));
            throw new EntityNotFoundException("Prices list graphical change of price dynamics is empty.");
        }
        return mapper.getPriceToPriceDynamicsForPeriodDao(prices);
    }

    public void importFile(MultipartFile file) {
        if (csvHelper.hasCSVFormat(file)) {
            List<Price> priceList = mapper.getPriceImportDtoListToPriceList(csvHelper.loadObjectList(PriceImportDto.class,
                    file.getOriginalFilename()));
            if (priceList.isEmpty()) {
                LOG.warning(String.format("List from file %s is empty.", file.getOriginalFilename()));
                throw new CsvException("List from file is empty.");
            }
            for (Price price : priceList) {
                Price priceCheck = priceDao.findPriceByGoodIdAndShopIdAndStatus(price.getGood().getId(), price.getShop().getId(),
                        PricesStatusEnum.ACTUAL);
                if (priceCheck == null) {
                    price.setStatus(PricesStatusEnum.ACTUAL);
                    priceDao.save(price);
                } else {
                    if (priceCheck.getDate().isBefore(price.getDate())) {
                        price.setStatus((PricesStatusEnum.ACTUAL));
                        LOG.info(String.format("Status of price %s of good %s in shop %s has been changed to actual.",
                                price.getPrice(), price.getGood().getName(), price.getShop().getBrand().getName()));

                        priceCheck.setStatus(PricesStatusEnum.HISTORY);
                        LOG.info(String.format("Status of price %s of good %s in shop %s has been changed to history.",
                                priceCheck.getPrice(), priceCheck.getGood().getName(), priceCheck.getShop().getBrand().getName()));
                        priceDao.save(price);
                        priceDao.save(priceCheck);
                    } else if (priceCheck.getDate().isAfter(price.getDate())) {
                        price.setStatus(PricesStatusEnum.HISTORY);
                        LOG.info(String.format("Status of price %s of good %s in shop %s has been changed to history.",
                                price.getPrice(), price.getGood().getName(), price.getShop().getBrand().getName()));
                        priceDao.save(price);
                    } else {
                        LOG.info(String.format("Price %s of good %s in shop %s is already in database.",
                                price.getPrice(), price.getGood().getName(), price.getShop().getBrand().getName()));
                    }
                }
            }
        } else {
            LOG.warning(String.format("File %s haven't csv format.", file.getOriginalFilename()));
            throw new CsvException("Please import a csv file!");
        }
    }
}
