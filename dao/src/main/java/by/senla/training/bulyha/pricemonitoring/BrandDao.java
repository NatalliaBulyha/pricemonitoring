package by.senla.training.bulyha.pricemonitoring;

import by.senla.training.bulyha.pricemonitoring.entity.Brand;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandDao extends JpaRepository<Brand, Long> {

    @Query(value = "SELECT b.* FROM brands b WHERE b.name LIKE ?1",
            nativeQuery = true)
    Brand findBrandLikeName(String name);

    Brand findBrandByName(String name);

    Brand findBrandByUnp(Integer unp);

    List<Brand> findBrandsOrderBy(Sort sort);

    List<Brand> findBrandsByStatus(EntityStatusEnum status);

    @Query(value = "SELECT b.* FROM brands b JOIN shops s ON b.id = s.brands_id WHERE s.id = ?1",
            nativeQuery = true)
    Brand findBrandByShopId(Long shopId);
}
