package by.senla.training.bulyha.pricemonitoring.mapper;

import by.senla.training.bulyha.pricemonitoring.BrandDao;
import by.senla.training.bulyha.pricemonitoring.RatingDao;
import by.senla.training.bulyha.pricemonitoring.entity.Rating;
import by.senla.training.bulyha.pricemonitoring.entity.Shop;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.enums.ShopTypeEnum;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.shop.NewShopDto;
import by.senla.training.bulyha.pricemonitoring.shop.ShopAdminDto;
import by.senla.training.bulyha.pricemonitoring.shop.ShopDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ShopMapper {

    private BrandDao brandDao;
    private RatingDao ratingDao;

    @Autowired
    public ShopMapper(BrandDao brandDao, RatingDao ratingDao) {
        this.brandDao = brandDao;
        this.ratingDao = ratingDao;
    }

    public ShopDto getShopToShopDto(Shop shop) {
        Map<String, String> ratingsParametersMap = getMap(ratingDao.findAllByShopIdAndStatus(shop.getId(), EntityStatusEnum.ACTUAL));
        return ShopDto.builder()
                .type(shop.getType().toString().toLowerCase())
                .brand(shop.getBrand().getName())
                .address(shop.getAddress())
                .contactNumber(shop.getContactNumber())
                .openTime(shop.getOpenTime().toString())
                .closeTime(shop.getCloseTime().toString())
                .assortment(getSubstring(ratingsParametersMap.get("assortment")))
                .qualityOfService(getSubstring(ratingsParametersMap.get("qualityOfService")))
                .prices(getSubstring(ratingsParametersMap.get("prices")))
                .build();
    }

    private Map<String, String> getMap(List<Rating> ratings) {
        String assortment = Double.toString(ratings.stream().mapToDouble(Rating::getAssortment).average().orElseThrow(IllegalStateException::new));
        String qualityOfService = Double.toString(ratings.stream().mapToDouble(Rating::getQualityOfService).average().orElseThrow(IllegalStateException::new));
        String prices = Double.toString(ratings.stream().mapToDouble(Rating::getPrices).average().orElseThrow(IllegalStateException::new));

        Map<String, String> ratingsMap = new HashMap<>();
        ratingsMap.put("assortment", assortment);
        ratingsMap.put("qualityOfService", qualityOfService);
        ratingsMap.put("prices", prices);

        return ratingsMap;
    }

    private String getSubstring(String string) {
        int lenght = string.length();
        return lenght >= 4 ? string.substring(0, 4) : string.substring(0, lenght);
    }

    public List<ShopDto> getShopListToShopDtoList(List<Shop> shopList) {
        return shopList.stream().map(this::getShopToShopDto).collect(Collectors.toList());
    }

    public Shop getNewShopDtoToShop(NewShopDto shop) {
        return Shop.builder()
                .address(shop.getAddress())
                .contactNumber(shop.getContactNumber())
                .openTime(shop.getOpenTime())
                .closeTime(shop.getCloseTime())
                .status(EntityStatusEnum.ACTUAL)
                .type(ShopTypeEnum.valueOf(shop.getType().toUpperCase()))
                .brand(brandDao.findById(shop.getBrand()).orElseThrow(() ->
                        new EntityNotFoundException(String.format("Brand with id = %s is not found", shop.getBrand()))))
                .build();
    }

    public ShopAdminDto getShopToShopAdminDto(Shop shop) {
        Map<String, String> ratingsParametersMap = getMap(ratingDao.findAllByShopIdAndStatus(shop.getId(), EntityStatusEnum.ACTUAL));

        return buildShopAdminDto(shop, ratingsParametersMap);
    }

    public List<ShopAdminDto> getShopListToShopAdminDtoList(List<Shop> shopList) {
        return shopList.stream().map(this::getShopToShopAdminDto).collect(Collectors.toList());
    }

    public ShopAdminDto getShopToShopAdminDtoAdmin(Shop shop) {
        Map<String, String> ratingsParametersMap = getMap(ratingDao.findAllByShopId(shop.getId()));

        return buildShopAdminDto(shop, ratingsParametersMap);
    }

    public List<ShopAdminDto> getShopListToShopAdminDtoListAdmin(List<Shop> shopList) {
        return shopList.stream().map(this::getShopToShopAdminDtoAdmin).collect(Collectors.toList());
    }

    private ShopAdminDto buildShopAdminDto(Shop shop, Map<String, String> ratingsParametersMap) {
        return ShopAdminDto.builder()
                .id(shop.getId())
                .type(shop.getType().toString())
                .brand(shop.getBrand().getName())
                .address(shop.getAddress())
                .contactNumber(shop.getContactNumber())
                .openTime(shop.getOpenTime().toString())
                .closeTime(shop.getCloseTime().toString())
                .status(shop.getStatus().toString())
                .assortment(getSubstring(ratingsParametersMap.get("assortment")))
                .qualityOfService(getSubstring(ratingsParametersMap.get("qualityOfService")))
                .prices(getSubstring(ratingsParametersMap.get("prices")))
                .build();
    }
}