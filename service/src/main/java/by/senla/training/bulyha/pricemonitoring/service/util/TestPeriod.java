package by.senla.training.bulyha.pricemonitoring.service.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TestPeriod {

    public boolean isRightPeriod(LocalDate startDate, LocalDate finishDate) {
        return startDate.isBefore(finishDate) && startDate.isBefore(LocalDate.now());
    }
}
