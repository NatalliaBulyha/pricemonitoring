package by.senla.training.bulyha.pricemonitoring;

import by.senla.training.bulyha.pricemonitoring.entity.Category;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.enums.GoodsCategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {

    List<Category> findCategoriesByStatus(EntityStatusEnum status);

    Category findCategoryByName(GoodsCategoryEnum name);

    @Query(value = "SELECT c.* FROM categories c JOIN subcategories s ON c.id = s.categories_id WHERE s.id = ?1",
            nativeQuery = true)
    Category findCategoryBySubcategoryId(Long subcategoryId);
}
