package by.senla.training.bulyha.pricemonitoring.shop;

import by.senla.training.bulyha.pricemonitoring.entity.Shop;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopDao extends JpaRepository<Shop, Long>, JpaSpecificationExecutor<Shop> {

    Shop findShopById(Long id);

    List<Shop> findShopByBrandIdAndStatus(Long brandId, EntityStatusEnum status);

    List<Shop> findShopByBrandId(Long brandId);

    /*@Query(value = "SELECT s FROM Shop s WHERE s.address LIKE %:address% ")*/
    List<Shop> findShopsByAddressContainsAndStatus(String address, EntityStatusEnum status);

    List<Shop> findAllByAddressContains(String address);

    Shop findShopByBrandIdAndAddress(Long brandId, String address);

    List<Shop> findShopsByStatus(EntityStatusEnum status);
}