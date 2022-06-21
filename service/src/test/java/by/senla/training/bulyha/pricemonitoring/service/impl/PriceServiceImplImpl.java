package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.PriceDao;
import by.senla.training.bulyha.pricemonitoring.entity.Brand;
import by.senla.training.bulyha.pricemonitoring.entity.Good;
import by.senla.training.bulyha.pricemonitoring.entity.Price;
import by.senla.training.bulyha.pricemonitoring.entity.Shop;
import by.senla.training.bulyha.pricemonitoring.enums.PricesStatusEnum;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.exception.InternalException;
import by.senla.training.bulyha.pricemonitoring.mapper.PriceMapper;
import by.senla.training.bulyha.pricemonitoring.price.NewPriceDto;
import by.senla.training.bulyha.pricemonitoring.price.PriceAdminDto;
import by.senla.training.bulyha.pricemonitoring.price.PriceDto;
import by.senla.training.bulyha.pricemonitoring.price.priceComparison.PriceComparisonDto;
import by.senla.training.bulyha.pricemonitoring.price.priceDynamics.PriceDynamicsForPeriodDao;
import by.senla.training.bulyha.pricemonitoring.service.PriceService;
import by.senla.training.bulyha.pricemonitoring.service.util.CSVHelper;
import by.senla.training.bulyha.pricemonitoring.service.util.TestPeriod;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriceServiceImplImpl {

    private PriceService priceService;

    @Mock
    private PriceDao priceDao;

    @Mock
    private PriceMapper mapper;

    @Mock
    private CSVHelper csvHelper;

    @Mock
    private TestPeriod period;

    public static final Logger LOG = Logger.getLogger(PriceServiceImplImpl.class.getName());

    @BeforeEach
    public void setUp() {
        priceService = new PriceServiceImpl(priceDao, mapper, csvHelper, period);
    }

    @BeforeAll
    static void start() {
        LOG.info("Testing in PriceServiceImplImpl started.");
    }

    @AfterAll
    static void done() {
        LOG.info("Testing in PriceServiceImplImpl is over.");
    }

    @Test
    public void priceServiceImpl_getById() {
        Long id = 1L;
        Price price = new Price();
        price.setId(id);
        PriceDto priceDto = new PriceDto();

        when(priceDao.findById(id)).thenReturn(Optional.of(price));
        when(mapper.getPriceToPriceDto(price)).thenReturn(priceDto);

        PriceDto returnPriceDto = priceService.getById(id);
        Assertions.assertEquals(priceDto, returnPriceDto);
    }

    @Test
    public void priceServiceImpl_getById_EntityNotFoundException() {
        Long id = 1L;
        Price price = new Price();

        when(priceDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            priceService.getById(id);
        });
    }

    @Test
    public void priceServiceImpl_getAll() {
        List<Price> prices = new ArrayList<>();
        prices.add(new Price());
        prices.add(new Price());
        prices.add(new Price());

        List<PriceDto> priceDto = new ArrayList<>();
        priceDto.add(new PriceDto());
        priceDto.add(new PriceDto());
        priceDto.add(new PriceDto());

        when(priceDao.findAll()).thenReturn(prices);
        when(mapper.getPriceListToPriceDtoList(prices)).thenReturn(priceDto);

        List<PriceDto> returnPricesDto = priceService.getAll();
        Assertions.assertEquals(priceDto, returnPricesDto);
    }

    @Test
    public void priceServiceImpl_getAll_EntityNotFoundException() {
        List<Price> prices = new ArrayList<>();

        when(priceDao.findAll()).thenReturn(prices);

        assertThrows(EntityNotFoundException.class, () -> {
            priceService.getAll();
        });
    }

    @Test
    public void priceServiceImpl_getByShopId() {
        Long id = 1L;
        List<Price> prices = new ArrayList<>();
        prices.add(new Price());
        prices.add(new Price());
        prices.add(new Price());

        List<PriceAdminDto> priceAdminDtos = new ArrayList<>();
        priceAdminDtos.add(new PriceAdminDto());
        priceAdminDtos.add(new PriceAdminDto());
        priceAdminDtos.add(new PriceAdminDto());

        when(priceDao.findAllByShopId(id)).thenReturn(prices);
        when(mapper.getPriceListToPriceAdminDtoList(prices)).thenReturn(priceAdminDtos);

        List<PriceAdminDto> returnPrices = priceService.getByShopId(id);
        Assertions.assertEquals(priceAdminDtos, returnPrices);
    }

    @Test
    public void priceServiceImpl_getByShopId_EntityNotFoundException() {
        Long id = 1L;
        List<Price> prices = new ArrayList<>();

        when(priceDao.findAllByShopId(id)).thenReturn(prices);

        assertThrows(EntityNotFoundException.class, () -> {
            priceService.getByShopId(id);
        });
    }

    @Test
    public void priceServiceImpl_getByPriceAndGood() {
        String name = "A";
        BigDecimal priceGood = BigDecimal.valueOf(1.23);
        Price price = new Price();
        price.setPrice(priceGood);
        PriceDto priceDto = new PriceDto();
        priceDto.setPrice(priceGood);


        when(priceDao.findPriceByPriceAndGoodName(priceGood, name)).thenReturn(price);
        when(mapper.getPriceToPriceDto(price)).thenReturn(priceDto);

        PriceDto returnPrice = priceService.getByPriceAndGood(priceGood, name);
        Assertions.assertEquals(priceDto, returnPrice);
    }

    @Test
    public void priceServiceImpl_getByPriceAndGood_EntityNotFoundException() {
        String name = "A";
        BigDecimal priceGood = BigDecimal.valueOf(1.23);

        when(priceDao.findPriceByPriceAndGoodName(priceGood, name)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            priceService.getByPriceAndGood(priceGood, name);
        });
    }

    @Test
    public void priceServiceImpl_addPrice() {
        Long goodId = 1L;
        Long shopId = 1L;
        Good good = new Good();
        good.setId(goodId);
        good.setName("A");
        Shop shop = new Shop();
        shop.setId(shopId);
        Brand brand = new Brand();
        brand.setName("B");
        shop.setBrand(brand);

        NewPriceDto newPriceDto = new NewPriceDto();
        newPriceDto.setGoodId(goodId);
        newPriceDto.setShopId(shopId);

        Price price = new Price();
        price.setPrice(BigDecimal.valueOf(0.55));
        price.setGood(good);
        price.setShop(shop);
        price.setStatus(PricesStatusEnum.ACTUAL);

        Price actualPrice = new Price();
        actualPrice.setGood(good);
        actualPrice.setShop(shop);

        PriceDto priceDto = new PriceDto();

        when(priceDao.findPriceByGoodIdAndShopIdAndStatus(goodId, shopId, PricesStatusEnum.ACTUAL)).thenReturn(price);
        when(priceDao.save(price)).thenReturn(null);
        when(mapper.getNewPriceDtoToPrice(newPriceDto)).thenReturn(actualPrice);
        when(priceDao.save(actualPrice)).thenReturn(actualPrice);
        when(mapper.getPriceToPriceDto(actualPrice)).thenReturn(priceDto);

        PriceDto returnPrice = priceService.addPrice(newPriceDto);
        Assertions.assertEquals(priceDto, returnPrice);
    }

    @Test
    public void priceServiceImpl_addPrice_EntityNotFoundException() {
        Long goodId = 122L;
        Long shopId = 132L;

        NewPriceDto newPriceDto = new NewPriceDto();
        newPriceDto.setGoodId(goodId);
        newPriceDto.setShopId(shopId);

        when(priceDao.findPriceByGoodIdAndShopIdAndStatus(goodId, shopId, PricesStatusEnum.ACTUAL)).thenReturn(null);
        when(mapper.getNewPriceDtoToPrice(newPriceDto)).thenThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> {
            priceService.addPrice(newPriceDto);
        });
    }

    @Test
    public void priceServiceImpl_getPriceDynamicsForPeriod_InternalException() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate finishDate = startDate.plusDays(1);

        when(period.isRightPeriod(startDate, finishDate)).thenReturn(false);

        assertThrows(InternalException.class, () -> {
            priceService.getPriceDynamicsForPeriod(1L, startDate, finishDate, null);
        });
    }

    @Test
    public void priceServiceImpl_getPriceDynamicsForPeriod_EntityNotFoundException() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate finishDate = startDate.plusDays(5);
        Long id = 1L;
        List<Price> prices = new ArrayList<>();

        when(period.isRightPeriod(startDate, finishDate)).thenReturn(true);
        when(priceDao.findPricesByGoodIdAndPeriod(id, startDate, finishDate)).thenReturn(prices);

        assertThrows(EntityNotFoundException.class, () -> {
            priceService.getPriceDynamicsForPeriod(id, startDate, finishDate, null);
        });
    }

    @Test
    public void priceServiceImpl_getPriceComparison() {
        Long goodId = 1L;
        Long shopId1 = 1L;
        Long shopId2 = 2L;
        Set<Long> shops = new HashSet<>();
        shops.add(shopId1);
        shops.add(shopId2);

        List<Price> prices = new ArrayList<>();
        prices.add(new Price());
        prices.add(new Price());
        prices.add(new Price());

        PriceComparisonDto priceComparisonDtos = new PriceComparisonDto();

        when(priceDao.findByGoodIdAndStatusAndShopIdIn(goodId, PricesStatusEnum.ACTUAL, shops)).thenReturn(prices);
        when(mapper.getPriceListToPriceComparisonDtoList(prices)).thenReturn(priceComparisonDtos);

        PriceComparisonDto returnPriceComparisonDto = priceService.getPriceComparison(goodId, shops);
        Assertions.assertEquals(priceComparisonDtos, returnPriceComparisonDto);
    }

    @Test
    public void priceServiceImpl_getPriceComparison_EntityNotFoundException() {
        Long goodId = 1L;
        Long shopId1 = 1L;
        Long shopId2 = 2L;
        Set<Long> shops = new HashSet<>();
        shops.add(shopId1);
        shops.add(shopId2);

        List<Price> prices = new ArrayList<>();


        when(priceDao.findByGoodIdAndStatusAndShopIdIn(goodId, PricesStatusEnum.ACTUAL, shops)).thenReturn(prices);

        assertThrows(EntityNotFoundException.class, () -> {
            priceService.getPriceComparison(goodId, shops);
        });
    }

    @Test
    public void priceServiceImpl_getListPriceGraphicalChangeOfPriceDynamics() {
        Long goodId = 1L;

        List<Price> prices = new ArrayList<>();
        prices.add(new Price());
        prices.add(new Price());
        prices.add(new Price());

        PriceDynamicsForPeriodDao priceDynamicsForPeriodDao = new PriceDynamicsForPeriodDao();

        when(priceDao.findPricesByGoodId(goodId)).thenReturn(prices);
        when(mapper.getPriceToPriceDynamicsForPeriodDao(prices)).thenReturn(priceDynamicsForPeriodDao);

        PriceDynamicsForPeriodDao returnPriceDynamicsForPeriodDao = priceService.getListPriceGraphicalChangeOfPriceDynamics(goodId);
        Assertions.assertEquals(priceDynamicsForPeriodDao, returnPriceDynamicsForPeriodDao);
    }

    @Test
    public void priceServiceImpl_getListPriceGraphicalChangeOfPriceDynamics_EntityNotFoundException() {
        Long goodId = 1L;

        List<Price> prices = new ArrayList<>();

        when(priceDao.findPricesByGoodId(goodId)).thenReturn(prices);

        assertThrows(EntityNotFoundException.class, () -> {
            priceService.getListPriceGraphicalChangeOfPriceDynamics(goodId);
        });
    }
}
