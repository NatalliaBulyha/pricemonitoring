package by.senla.training.bulyha.pricemonitoring.service.util;

import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import org.springframework.stereotype.Component;

@Component
public class Control {

    public static boolean deletedOrNot(EntityStatusEnum status) {
        if (status.equals(EntityStatusEnum.DELETED)) {
            return true;
        }
        return false;
    }
}
