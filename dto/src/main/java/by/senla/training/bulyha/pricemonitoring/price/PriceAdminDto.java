package by.senla.training.bulyha.pricemonitoring.price;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceAdminDto {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private BigDecimal price;
    private String status;
    private Long goodId;
    private String goodName;
    private Long shopId;
    private String shopName;
    private Long userId;
    private String userLastName;
    private String userFirstName;
}
