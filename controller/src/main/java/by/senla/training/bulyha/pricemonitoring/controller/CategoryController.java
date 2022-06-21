package by.senla.training.bulyha.pricemonitoring.controller;

import by.senla.training.bulyha.pricemonitoring.category.CategoryAdminDto;
import by.senla.training.bulyha.pricemonitoring.category.CategoryDto;
import by.senla.training.bulyha.pricemonitoring.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Validated
@RestController
@RequestMapping("category")
@Tag(name = "category controller", description = "the category API with description tag annotation.")
public class CategoryController {

    private CategoryService categoryService;
    public static final Logger LOG = Logger.getLogger(BrandController.class.getName());

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get category or list of categories",
            description = "This method returns category by name, all list of categories or by status.")
    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public List<CategoryAdminDto> getCategoriesList(@RequestParam(required = false) String status,
                                                     @RequestParam(required = false) String categoryName) {
        List<CategoryAdminDto> categoryAdminDtoList = new ArrayList<>();
        if (status != null && categoryName == null) {
            categoryAdminDtoList = categoryService.getByStatus(status);
        } else if (status == null && categoryName != null) {
            categoryAdminDtoList.add(categoryService.getByName(categoryName));
        } else {
            categoryAdminDtoList = categoryService.getAllAdmin();
        }
        return categoryAdminDtoList;
    }

    @Operation(summary = "add new category", description = "method adds CategoryDto: " +
            "name - BAGS, SHOES")
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public CategoryAdminDto addCategory(@RequestBody CategoryDto categoryDto) {
        CategoryAdminDto category = categoryService.addCategory(categoryDto);
        LOG.info(String.format("New Category added with name = %s and id = %s.", categoryDto.getCategory(), category.getId()));
        return category;
    }

    @Operation(summary = "deleted category",
            description = "method changes status of category from actual to deleted. Status of the subcategories " +
                    "of this category, goods from this subcategories and prices of these goods also changes.")
    @Secured({"ROLE_ADMIN"})
    @PutMapping
    public CategoryAdminDto updateCategoryStatus(@RequestParam @Min(1) Long id) {
        CategoryAdminDto categoryAdminDto = categoryService.updateCategoryStatus(id);
        LOG.info(String.format("Category with id = %s was deleted.", id));
        return categoryAdminDto;
    }
}
