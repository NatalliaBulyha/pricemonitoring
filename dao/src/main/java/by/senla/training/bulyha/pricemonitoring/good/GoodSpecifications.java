package by.senla.training.bulyha.pricemonitoring.good;

import by.senla.training.bulyha.pricemonitoring.entity.Good;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class GoodSpecifications {

    public static Specification<Good> withSubcategory(Long id) {
        if (id == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("subcategory"), id);
        }
    }

    public static Specification<Good> withStatus(EntityStatusEnum status) {
        if (status == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("status"), status);
        }
    }

    public static Specification<Good> withGoodsName(Map<String, String> map) {
        if (map.get("name") == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.like(root.get("name"), "%" + map.get("name") + "%");
        }
    }

    public static Specification<Good> withProducer(Map<String, String> map) {
        if (map.get("producer") == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.like(root.get("producer"), "%" + map.get("producer") + "%");
        }
    }

    public static Specification<Good> withDescription(Map<String, String> map) {
        if (map.get("description") == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.like(root.get("description"), "%" + map.get("description") + "%");
        }
    }
}
