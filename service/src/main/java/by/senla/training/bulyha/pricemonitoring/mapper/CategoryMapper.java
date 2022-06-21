package by.senla.training.bulyha.pricemonitoring.mapper;

import by.senla.training.bulyha.pricemonitoring.category.CategoryAdminDto;
import by.senla.training.bulyha.pricemonitoring.category.CategoryDto;
import by.senla.training.bulyha.pricemonitoring.entity.Category;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.enums.GoodsCategoryEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDto getCategoryToCategoryDto(Category category) {
        return new CategoryDto(category.getName().toString());
    }

    public List<CategoryDto> getCategoryListToCategoryDtoList(List<Category> categoryList) {
        return categoryList.stream().map(this::getCategoryToCategoryDto).collect(Collectors.toList());
    }

    public Category getCategoryDtoToCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(GoodsCategoryEnum.valueOf(categoryDto.getCategory().toUpperCase()))
                .status(EntityStatusEnum.ACTUAL)
                .build();
    }

    public CategoryAdminDto getCategoryToCategoryAdminDto(Category category) {
        return CategoryAdminDto.builder()
                .id(category.getId())
                .category(category.getName().toString())
                .status(category.getStatus().toString())
                .build();
    }

    public List<CategoryAdminDto> getCategoryListToCategoryAdminDtoList(List<Category> categoryList) {
        return categoryList.stream().map(this::getCategoryToCategoryAdminDto).collect(Collectors.toList());
    }
}
