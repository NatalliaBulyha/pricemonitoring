package by.senla.training.bulyha.pricemonitoring.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewRatingDto {

    @Min(1)
    @Max(5)
    private Integer assortment;

    @Min(1)
    @Max(5)
    private Integer qualityOfService;

    @Min(1)
    @Max(5)
    private Integer prices;
    private @Min(1) Long shopId;
}
