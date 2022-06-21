package by.senla.training.bulyha.pricemonitoring.mapper;

import by.senla.training.bulyha.pricemonitoring.CategoryDao;
import by.senla.training.bulyha.pricemonitoring.UserDao;
import by.senla.training.bulyha.pricemonitoring.exception.CsvException;
import by.senla.training.bulyha.pricemonitoring.good.GoodListBySubcategoryDto;
import by.senla.training.bulyha.pricemonitoring.PriceDao;
import by.senla.training.bulyha.pricemonitoring.SubcategoryDao;
import by.senla.training.bulyha.pricemonitoring.entity.Price;
import by.senla.training.bulyha.pricemonitoring.entity.Shop;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.enums.PricesStatusEnum;
import by.senla.training.bulyha.pricemonitoring.good.GoodAdminDto;
import by.senla.training.bulyha.pricemonitoring.good.GoodUpdateDto;
import by.senla.training.bulyha.pricemonitoring.good.NewGoodDto;
import by.senla.training.bulyha.pricemonitoring.entity.Good;
import by.senla.training.bulyha.pricemonitoring.entity.Subcategory;
import by.senla.training.bulyha.pricemonitoring.good.GoodSortAndFilterDto;
import by.senla.training.bulyha.pricemonitoring.good.GoodGoodsDto;
import by.senla.training.bulyha.pricemonitoring.good.GoodShopsDto;
import by.senla.training.bulyha.pricemonitoring.service.util.UserName;
import by.senla.training.bulyha.pricemonitoring.shop.ShopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoodMapper {

    private SubcategoryDao subcategoryDao;
    private PriceDao priceDao;
    private ShopDao shopDao;
    private UserDao userDao;
    private CategoryDao categoryDao;
    private UserName userName;

    @Autowired
    public GoodMapper(SubcategoryDao subcategoryDao, PriceDao priceDao, ShopDao shopDao, UserDao userDao, CategoryDao categoryDao, UserName userName) {
        this.subcategoryDao = subcategoryDao;
        this.priceDao = priceDao;
        this.shopDao = shopDao;
        this.userDao = userDao;
        this.categoryDao = categoryDao;
        this.userName = userName;
    }

    public GoodListBySubcategoryDto getGoodListToGoodListBySubcategoryDto(Long subcategoryId, List<Good> goods) {
        return GoodListBySubcategoryDto.builder()
                .category(categoryDao.findCategoryBySubcategoryId(subcategoryId).getName().toString())
                .subcategory(subcategoryDao.findSubcategoryById(subcategoryId).getName().toString())
                .goodsName(goods.stream().map(Good::getName).distinct().collect(Collectors.toList()))
                .build();
    }

    public GoodUpdateDto getGoodToGoodUpdateDto(Good good) {
        return GoodUpdateDto.builder()
                .id(good.getId())
                .name(good.getName())
                .producer(good.getProducer())
                .country(good.getCountry())
                .description(good.getDescription())
                .subcategoryId(good.getSubcategory().getId())
                .build();
    }

    public GoodAdminDto getGoodToGoodAdminDto(Good good) {
        return GoodAdminDto.builder()
                .id(good.getId())
                .name(good.getName())
                .producer(good.getProducer())
                .country(good.getCountry())
                .description(good.getDescription())
                .subcategories_id(good.getSubcategory().getId())
                .status(good.getStatus().toString())
                .build();
    }

    public List<GoodAdminDto> getGoodListToGoodAdminDtoList(List<Good> goods) {
        return goods.stream().map(this::getGoodToGoodAdminDto).collect(Collectors.toList());
    }

    public Good getGoodAdminDtoToGood(GoodAdminDto goodAdminDto) {
        return Good.builder()
                .name(goodAdminDto.getName())
                .producer(goodAdminDto.getProducer())
                .country(goodAdminDto.getCountry())
                .description(goodAdminDto.getDescription())
                .status(EntityStatusEnum.valueOf(goodAdminDto.getStatus().toUpperCase()))
                .subcategory(subcategoryDao.findById(goodAdminDto.getSubcategories_id()).orElseThrow(() ->
                        new CsvException(String.format("Problems are with import subcategories_id in Good id = %s", goodAdminDto.getId()))))
                .build();
    }

    public List<Good> getGoodAdminDtoListToGoodList(List<GoodAdminDto> goodAdminDtoList) {
        return goodAdminDtoList.stream().map(this::getGoodAdminDtoToGood).collect(Collectors.toList());
    }

    // Список товаров по подкатегориям

    public List<GoodSortAndFilterDto> getGoodListToGoodSortAndFilterDtoList(List<Subcategory> subcategories, List<Shop> shops,
                                                                            List<Good> goods, Map<String, String> map) {
        List<GoodSortAndFilterDto> goodSortAndFilterDtoList = new ArrayList<>();
        for (Subcategory subcategory : subcategories) {
            goodSortAndFilterDtoList.add(getGoodGoodToGoodShop(subcategory.toString(), shops, goods, map));
        }
        return goodSortAndFilterDtoList;
    }

    // список товаров по заданой подкатегории

    public GoodSortAndFilterDto getGoodGoodToGoodShop(String subcategory, List<Shop> shops, List<Good> goods, Map<String, String> map) {
        List<GoodShopsDto> goodGoods = new ArrayList<>();
        for (Shop shop : shops) {
            GoodShopsDto goodShopsDto = getGoodToGoodShopsDto(shop, goods, map);
            if (!goodShopsDto.getGoods().isEmpty()) {
                goodGoods.add(goodShopsDto);
            }
        }
        return GoodSortAndFilterDto.builder()
                .subcategoryName(subcategory)
                .goods(goodGoods)
                .build();
    }

    public GoodShopsDto getGoodToGoodShopsDto(Shop shop, List<Good> goods, Map<String, String> map) {
        List<GoodGoodsDto> goodGoods = new ArrayList<>();
        for (Good good : goods) {
            Price price = priceDao.findPriceByGoodIdAndShopIdAndStatus(good.getId(), shop.getId(), PricesStatusEnum.ACTUAL);
            if (price != null) {
                goodGoods.add(getGoodGoodsDto(good, price));
            }
        }

        if (map.containsKey("sortFirstMinPrice")) {
            List<GoodGoodsDto> sortList = new ArrayList<>();
            if (map.get("sortFirstMinPrice").equals("true")) {
                sortList = goodGoods.stream()
                        .sorted(Comparator.comparing(GoodGoodsDto::getPrice))
                        .collect(Collectors.toList());
            } else {
                sortList = goodGoods.stream()
                        .sorted(Comparator.comparing(GoodGoodsDto::getPrice).reversed())
                        .collect(Collectors.toList());
            }
            return GoodShopsDto.builder()
                    .shopName(shop.getBrand().getName())
                    .address(shop.getAddress())
                    .goods(sortList)
                    .build();
        }

        return GoodShopsDto.builder()
                .shopName(shop.getBrand().getName())
                .address(shop.getAddress())
                .goods(goodGoods)
                .build();
    }

    public GoodGoodsDto getGoodGoodsDto(Good good, Price price) {
        return GoodGoodsDto.builder()
                .name(good.getName())
                .producer(good.getProducer())
                .description(good.getDescription())
                .price((price).getPrice())
                .build();
    }

    public Good getNewGoodDtoToGood(NewGoodDto newGoodDto) {
        return Good.builder()
                .name(newGoodDto.getName())
                .producer(newGoodDto.getProducer())
                .country(newGoodDto.getCountry())
                .description(newGoodDto.getDescription())
                .status(EntityStatusEnum.ACTUAL)
                .subcategory(subcategoryDao.findSubcategoryById(newGoodDto.getSubcategory()))
                .build();
    }

    public Price getNewGoodDtoToPrice(NewGoodDto newGoodDto, Good good) {
        return Price.builder()
                .date(LocalDate.now())
                .price(newGoodDto.getPrice())
                .status(PricesStatusEnum.ACTUAL)
                .good(good)
                .shop(shopDao.findShopById(newGoodDto.getShopId()))
                .user(userDao.findByLogin(userName.getUserName()))
                .build();
    }
}
