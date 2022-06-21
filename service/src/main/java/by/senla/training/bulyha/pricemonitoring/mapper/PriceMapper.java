package by.senla.training.bulyha.pricemonitoring.mapper;

import by.senla.training.bulyha.pricemonitoring.BrandDao;
import by.senla.training.bulyha.pricemonitoring.UserDao;
import by.senla.training.bulyha.pricemonitoring.entity.Good;
import by.senla.training.bulyha.pricemonitoring.entity.Brand;
import by.senla.training.bulyha.pricemonitoring.enums.PricesStatusEnum;
import by.senla.training.bulyha.pricemonitoring.exception.CsvException;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.good.GoodDao;
import by.senla.training.bulyha.pricemonitoring.price.NewPriceDto;
import by.senla.training.bulyha.pricemonitoring.price.PriceAdminDto;
import by.senla.training.bulyha.pricemonitoring.price.PriceImportDto;
import by.senla.training.bulyha.pricemonitoring.price.priceComparison.PriceComparisonDto;
import by.senla.training.bulyha.pricemonitoring.price.priceComparison.PriceComparisonListDto;
import by.senla.training.bulyha.pricemonitoring.price.priceDynamics.PriceListDynamicsDto;
import by.senla.training.bulyha.pricemonitoring.price.PriceDto;
import by.senla.training.bulyha.pricemonitoring.entity.Price;
import by.senla.training.bulyha.pricemonitoring.price.priceDynamics.PriceDynamicsForPeriodDao;
import by.senla.training.bulyha.pricemonitoring.service.util.UserName;
import by.senla.training.bulyha.pricemonitoring.shop.ShopDao;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriceMapper {

    private GoodDao goodDao;
    private BrandDao brandDao;
    private ShopDao shopDao;
    private UserDao userDao;
    private UserName userName;

    public PriceMapper(GoodDao goodDao, BrandDao brandDao, ShopDao shopDao, UserDao userDao, UserName userName) {
        this.goodDao = goodDao;
        this.brandDao = brandDao;
        this.shopDao = shopDao;
        this.userDao = userDao;
        this.userName = userName;
    }

    public Price getNewPriceDtoToPrice(NewPriceDto newPriceDto) {
        return Price.builder()
                .date(LocalDate.now())
                .price(newPriceDto.getPrice())
                .status(PricesStatusEnum.ACTUAL)
                .good(goodDao.findById(newPriceDto.getGoodId()).orElseThrow(() ->
                        new EntityNotFoundException(String.format("Good with id = %s is not found", newPriceDto.getGoodId()))))
                .shop(shopDao.findById(newPriceDto.getShopId()).orElseThrow(() ->
                        new EntityNotFoundException(String.format("Shop with id = %s is not found", newPriceDto.getShopId()))))
                .user(userDao.findByLogin(userName.getUserName()))
                .build();
    }

    public PriceDto getPriceToPriceDto(Price price) {
        return PriceDto.builder()
                .goodName(price.getGood().getName())
                .price(price.getPrice())
                .shopName(price.getShop().getBrand().getName())
                .date(price.getDate())
                .profileId(price.getUser().getId())
                .build();
    }

    public List<PriceDto> getPriceListToPriceDtoList(List<Price> priceList) {
        return priceList.stream().map(this::getPriceToPriceDto).collect(Collectors.toList());
    }

    public PriceDynamicsForPeriodDao getPriceToPriceDynamicsForPeriodDao(List<Price> prices) {
        List<PriceListDynamicsDto> priceListDynamicsDtoList = new ArrayList<>();
        for (Price price : prices) {
            Brand brand = brandDao.findBrandByShopId(price.getShop().getId());
            priceListDynamicsDtoList.add(new PriceListDynamicsDto(price.getDate(), price.getPrice(), brand.getName()));
        }
        List<PriceListDynamicsDto> sortList = priceListDynamicsDtoList.stream()
                .sorted(Comparator.comparing(PriceListDynamicsDto::getDate))
                .collect(Collectors.toList());
        Good good = goodDao.findById(prices.get(0).getGood().getId()).orElseThrow(() ->
                new EntityNotFoundException(String.format("Good with id = %s is not found", prices.get(0).getGood().getId())));
        return PriceDynamicsForPeriodDao.builder()
                .goodName(good.getName())
                .priceDynamics(sortList)
                .build();
    }

    public PriceComparisonDto getPriceListToPriceComparisonDtoList(List<Price> prices) {
        List<PriceComparisonListDto> priceComparisonDtoList = new ArrayList<>();
        for (Price price : prices) {
            priceComparisonDtoList.add(getPriceToPriceComparisonListDto(price));
        }
        return PriceComparisonDto.builder()
                .goodName(prices.get(0).getGood().getName())
                .priceComparisonListDtoList(priceComparisonDtoList)
                .build();
    }

    public PriceComparisonListDto getPriceToPriceComparisonListDto(Price price) {
        return PriceComparisonListDto.builder()
                .price(price.getPrice())
                .date(price.getDate())
                .description(price.getGood().getDescription())
                .shopName(price.getShop().getBrand().getName())
                .address(price.getShop().getAddress())
                .build();
    }

    public List<Price> getPriceImportDtoListToPriceList(List<PriceImportDto> priceImportDtoList) {
        return priceImportDtoList.stream().map(this::getPriceImportDtoToPrice).collect(Collectors.toList());
    }

    public Price getPriceImportDtoToPrice(PriceImportDto priceImportDto) {
        return Price.builder()
                .date(priceImportDto.getDate())
                .price(priceImportDto.getPrice())
                .status(PricesStatusEnum.valueOf(priceImportDto.getStatus().toUpperCase()))
                .good(goodDao.findById(priceImportDto.getGoods_id()).orElseThrow(() ->
                        new CsvException(String.format("Problems are with import goods_id in Price id = %s", priceImportDto.getId()))))
                .shop(shopDao.findById(priceImportDto.getGoods_id()).orElseThrow(() ->
                        new CsvException(String.format("Problems are with import shops_id in Price id = %s", priceImportDto.getId()))))
                .user(userDao.findById(priceImportDto.getUsers_id()).orElseThrow(() ->
                        new CsvException(String.format("Problems are with import users_id in Price id = %s", priceImportDto.getId()))))
                .build();
    }

    public PriceAdminDto getPriceToPriceAdminDto(Price price) {
        return PriceAdminDto.builder()
                .id(price.getId())
                .date(price.getDate())
                .price(price.getPrice())
                .status(price.getStatus().toString())
                .goodId(price.getGood().getId())
                .goodName(price.getGood().getName())
                .shopId(price.getShop().getId())
                .shopName(price.getShop().getBrand().getName())
                .userId(price.getId())
                .userLastName(price.getUser().getLastName())
                .userFirstName(price.getUser().getFirstName())
                .build();
    }

    public List<PriceAdminDto> getPriceListToPriceAdminDtoList(List<Price> prices) {
        return prices.stream().map(this::getPriceToPriceAdminDto).collect(Collectors.toList());
    }
}
