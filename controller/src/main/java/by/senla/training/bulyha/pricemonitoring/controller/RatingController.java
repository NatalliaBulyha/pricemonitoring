package by.senla.training.bulyha.pricemonitoring.controller;

import by.senla.training.bulyha.pricemonitoring.rating.NewRatingDto;
import by.senla.training.bulyha.pricemonitoring.rating.RatingDto;
import by.senla.training.bulyha.pricemonitoring.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.logging.Logger;

@Validated
@RestController
@RequestMapping("ratings")
@Tag(name = "rating controller", description = "the rating API with description tag annotation.")
public class RatingController {

    private RatingService ratingService;
    public static final Logger LOG = Logger.getLogger(RatingController.class.getName());

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Operation(summary = "add new rating",
            description = "method adds NewRatingDto. parameters assortment, qualityOfService and prices wil be from 1 to 5.")
    @PostMapping
    public void addRating(@RequestBody @Valid NewRatingDto newRating) {
        ratingService.save(newRating);
        LOG.info(String.format("New rating gor shop with id = %s was added.", newRating.getShopId()));
    }

    @Operation(summary = "get rating by shop id",
            description = "method returns rating by 3 parameters")
    @GetMapping
    public RatingDto getRatingByShopId(@RequestParam @Min(1) Long shopId) {
        return ratingService.getRatingByShopId(shopId);
    }
}
