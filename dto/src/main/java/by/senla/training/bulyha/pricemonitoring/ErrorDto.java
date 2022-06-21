package by.senla.training.bulyha.pricemonitoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {

    private HttpStatus httpStatus;
    private String errorMessage;
}
