package by.senla.training.bulyha.pricemonitoring.shop;

import by.senla.training.bulyha.pricemonitoring.entity.Shop;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class ShopSpecifications {

    public static Specification<Shop> withShopsName(Map<String, String> map) {
        if (map.get("brand") == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("brand"), Long.parseLong(map.get("brand")));
        }
    }

    public static Specification<Shop> withAddress(Map<String, String> map) {
        if (map.get("address") == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.like(root.get("address"), "%" + map.get("address") + "%");
        }
    }
}
