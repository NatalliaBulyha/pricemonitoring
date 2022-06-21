package by.senla.training.bulyha.pricemonitoring.service;

import by.senla.training.bulyha.pricemonitoring.rating.NewRatingDto;
import by.senla.training.bulyha.pricemonitoring.rating.RatingDto;

public interface RatingService {

    void save(NewRatingDto newRating);

    RatingDto getRatingByShopId(Long id);
}
