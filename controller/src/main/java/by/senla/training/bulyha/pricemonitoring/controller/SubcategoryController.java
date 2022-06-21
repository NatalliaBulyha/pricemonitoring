package by.senla.training.bulyha.pricemonitoring.controller;

import by.senla.training.bulyha.pricemonitoring.service.SubcategoryService;
import by.senla.training.bulyha.pricemonitoring.subcategory.NewSubcategoryDto;
import by.senla.training.bulyha.pricemonitoring.subcategory.SubcategoryAdminDto;
import by.senla.training.bulyha.pricemonitoring.subcategory.SubcategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("subcategories")
@Tag(name = "subcategory controller", description = "the subcategory API with description tag annotation.")
public class SubcategoryController {

    private SubcategoryService subcategoryService;
    public static final Logger LOG = Logger.getLogger(BrandController.class.getName());

    @Autowired
    public SubcategoryController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }

    @Operation(summary = "Get subcategory or list of subcategories",
            description = "This method returns subcategory by category or list of subcategories.")
    @GetMapping
    public List<SubcategoryDto> getSubcategoryByCategory(@RequestParam(required = false) String category) {
        List<SubcategoryDto> subcategoryDtoList = new ArrayList<>();
        if (category != null) {
            subcategoryDtoList.add(subcategoryService.getSubcategoryByCategory(category));
        } else {
            subcategoryDtoList = subcategoryService.getAllSubcategory();
        }
        return subcategoryDtoList;
    }

    @Operation(summary = "add new subcategory",
            description = "method adds NewSubcategoryDto: " +
            "name - BACKPACKS, WAIST_BAGS, SANDALS, SNEAKERS")
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public SubcategoryAdminDto addSubcategory(@RequestBody NewSubcategoryDto newSubCategoryDto) {
        SubcategoryAdminDto subcategory = subcategoryService.addSubcategory(newSubCategoryDto);
        LOG.info(String.format("New Subcategory added with name = %s and id = %s.", newSubCategoryDto.getSubcategoryName(), subcategory.getId()));
        return subcategory;
    }
}
