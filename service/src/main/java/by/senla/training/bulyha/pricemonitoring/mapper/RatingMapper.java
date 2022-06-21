package by.senla.training.bulyha.pricemonitoring.mapper;

import by.senla.training.bulyha.pricemonitoring.UserDao;
import by.senla.training.bulyha.pricemonitoring.entity.Rating;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.rating.NewRatingDto;
import by.senla.training.bulyha.pricemonitoring.rating.RatingDto;
import by.senla.training.bulyha.pricemonitoring.service.util.UserName;
import by.senla.training.bulyha.pricemonitoring.shop.ShopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RatingMapper {

    private ShopDao shopDao;
    private UserDao userDao;
    private UserName userName;

    @Autowired
    public RatingMapper(ShopDao shopDao, UserDao userDao, UserName userName) {
        this.shopDao = shopDao;
        this.userDao = userDao;
        this.userName = userName;
    }

    public Rating getNewRatingDtoToRating(NewRatingDto newRating) {
        return Rating.builder()
                .assortment(newRating.getAssortment())
                .qualityOfService(newRating.getQualityOfService())
                .prices(newRating.getPrices())
                .date(LocalDate.now())
                .status(EntityStatusEnum.ACTUAL)
                .shop(shopDao.findById(newRating.getShopId()).orElseThrow(() ->
                        new EntityNotFoundException(String.format("Shop with id = %s is not found", newRating.getShopId()))))
                .user(userDao.findByLogin(userName.getUserName()))
                .build();
    }

    public RatingDto getRatingListToRatingDto(List<Rating> ratings) {
        String assortment = Double.toString(ratings.stream().mapToDouble(Rating::getAssortment).average().orElseThrow(IllegalStateException::new));
        String qualityOfService = Double.toString(ratings.stream().mapToDouble(Rating::getQualityOfService).average().orElseThrow(IllegalStateException::new));
        String prices = Double.toString(ratings.stream().mapToDouble(Rating::getPrices).average().orElseThrow(IllegalStateException::new));

        return RatingDto.builder()
                .shopName(ratings.get(0).getShop().getBrand().getName())
                .assortment(getSubstring(assortment))
                .qualityOfService(getSubstring(qualityOfService))
                .prices(getSubstring(prices))
                .build();
    }

    private String getSubstring(String string) {
        int lenght = string.length();
        return lenght >= 4 ? string.substring(0, 4) : string.substring(0, lenght);
    }
}