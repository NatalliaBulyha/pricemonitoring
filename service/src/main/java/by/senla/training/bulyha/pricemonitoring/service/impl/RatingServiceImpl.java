package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.RatingDao;
import by.senla.training.bulyha.pricemonitoring.entity.Rating;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.mapper.RatingMapper;
import by.senla.training.bulyha.pricemonitoring.rating.NewRatingDto;
import by.senla.training.bulyha.pricemonitoring.rating.RatingDto;
import by.senla.training.bulyha.pricemonitoring.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class RatingServiceImpl implements RatingService {

    private RatingDao ratingDao;
    private RatingMapper mapper;
    public static final Logger LOG = Logger.getLogger(BrandServiceImpl.class.getName());

    @Autowired
    public RatingServiceImpl(RatingDao ratingDao, RatingMapper mapper) {
        this.ratingDao = ratingDao;
        this.mapper = mapper;
    }

    @Override
    public void save(NewRatingDto newRating) {
        ratingDao.save(mapper.getNewRatingDtoToRating(newRating));
    }

    @Override
    public RatingDto getRatingByShopId(Long id) {
        List<Rating> ratings = ratingDao.findAllByShopIdAndStatus(id, EntityStatusEnum.ACTUAL);
        if (ratings.isEmpty()) {
            LOG.warning(String.format("Rating with shopId = %s is not found", id));
            throw new EntityNotFoundException(String.format("Rating with shopId = %s is not found", id));
        }
        return mapper.getRatingListToRatingDto(ratings);
    }
}
