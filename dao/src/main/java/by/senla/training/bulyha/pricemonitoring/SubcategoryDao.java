package by.senla.training.bulyha.pricemonitoring;

import by.senla.training.bulyha.pricemonitoring.entity.Subcategory;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.enums.GoodsSubcategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryDao extends JpaRepository<Subcategory, Long> {

    List<Subcategory> findSubcategoriesByCategoryIdAndStatus(Long categoryId, EntityStatusEnum status);

    Subcategory findSubcategoryByName(GoodsSubcategoryEnum name);

    List<Subcategory> findSubcategoriesByStatus(EntityStatusEnum statusEnum);

    Subcategory findSubcategoryById(Long id);
}
