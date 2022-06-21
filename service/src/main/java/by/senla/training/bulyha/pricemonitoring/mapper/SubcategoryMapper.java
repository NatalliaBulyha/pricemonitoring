package by.senla.training.bulyha.pricemonitoring.mapper;

import by.senla.training.bulyha.pricemonitoring.CategoryDao;
import by.senla.training.bulyha.pricemonitoring.SubcategoryDao;
import by.senla.training.bulyha.pricemonitoring.entity.Category;
import by.senla.training.bulyha.pricemonitoring.entity.Subcategory;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.enums.GoodsSubcategoryEnum;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.subcategory.NewSubcategoryDto;
import by.senla.training.bulyha.pricemonitoring.subcategory.SubcategoryAdminDto;
import by.senla.training.bulyha.pricemonitoring.subcategory.SubcategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubcategoryMapper {

    private SubcategoryDao subcategoryDao;
    private CategoryDao categoryDao;

    @Autowired
    public SubcategoryMapper(SubcategoryDao subcategoryDao, CategoryDao categoryDao) {
        this.subcategoryDao = subcategoryDao;
        this.categoryDao = categoryDao;
    }

    public SubcategoryDto getSubcategoryToSubcategoryDto(Category category) {
        List<Subcategory> subcategoryList = subcategoryDao.findSubcategoriesByCategoryIdAndStatus(category.getId(), EntityStatusEnum.ACTUAL);
        return SubcategoryDto.builder()
                .categoryName(category.getName().toString())
                .subcategories(subcategoryList
                        .stream()
                        .map(s -> s.getName().toString())
                        .filter(s -> !s.equals(""))
                        .collect(Collectors.toList()))
                .build();
    }

    public List<SubcategoryDto> getSubcategoryListToSubcategoryDtoList(List<Category> categoryList) {
        List<SubcategoryDto> subcategoryDtoList = new ArrayList<>();
        for (Category category : categoryList) {
            SubcategoryDto subcategoryDto = getSubcategoryToSubcategoryDto(category);
            if (!subcategoryDto.getSubcategories().isEmpty()) {
                subcategoryDtoList.add(subcategoryDto);
            }
        }
        return subcategoryDtoList;
    }

    public Subcategory getNewSubcategoryDtoToSubcategory(NewSubcategoryDto newSubcategoryDto) {
        return Subcategory.builder()
                .name(GoodsSubcategoryEnum.valueOf(newSubcategoryDto.getSubcategoryName().toUpperCase()))
                .category(categoryDao.findById(newSubcategoryDto.getCategoryId()).orElseThrow(() ->
                        new EntityNotFoundException(String.format("Category with id = %s is not found", newSubcategoryDto.getCategoryId()))))
                .status(EntityStatusEnum.ACTUAL)
                .build();
    }

    public SubcategoryAdminDto getSubcategoryToSubcategoryAdminDto(Subcategory subcategory) {
        return SubcategoryAdminDto.builder()
                .id(subcategory.getId())
                .category(subcategory.getCategory().getName().toString())
                .subcategoryName(subcategory.getName().toString())
                .status(subcategory.getStatus().toString())
                .build();
    }
}