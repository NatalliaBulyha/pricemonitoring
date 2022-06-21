package by.senla.training.bulyha.pricemonitoring.service;

import by.senla.training.bulyha.pricemonitoring.category.CategoryAdminDto;
import by.senla.training.bulyha.pricemonitoring.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto getById(Long categoryId);

    CategoryAdminDto getByName(String categoryName);

    CategoryAdminDto addCategory(CategoryDto categoryDto);

    List<CategoryAdminDto> getAllAdmin();

    List<CategoryAdminDto> getByStatus(String status);

    CategoryAdminDto updateCategoryStatus(Long categoryId);
}
