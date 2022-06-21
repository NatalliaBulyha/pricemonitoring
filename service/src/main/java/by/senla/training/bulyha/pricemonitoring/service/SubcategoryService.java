package by.senla.training.bulyha.pricemonitoring.service;

import by.senla.training.bulyha.pricemonitoring.subcategory.NewSubcategoryDto;
import by.senla.training.bulyha.pricemonitoring.subcategory.SubcategoryAdminDto;
import by.senla.training.bulyha.pricemonitoring.subcategory.SubcategoryDto;

import java.util.List;

public interface SubcategoryService {

    SubcategoryDto getSubcategoryByCategory(String category);

    List<SubcategoryDto> getAllSubcategory();

    SubcategoryAdminDto addSubcategory(NewSubcategoryDto newSubCategoryDto);
}
