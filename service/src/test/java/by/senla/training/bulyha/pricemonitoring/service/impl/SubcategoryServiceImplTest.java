package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.CategoryDao;
import by.senla.training.bulyha.pricemonitoring.SubcategoryDao;
import by.senla.training.bulyha.pricemonitoring.brand.BrandAdminDto;
import by.senla.training.bulyha.pricemonitoring.entity.Brand;
import by.senla.training.bulyha.pricemonitoring.entity.Category;
import by.senla.training.bulyha.pricemonitoring.entity.Subcategory;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.enums.GoodsCategoryEnum;
import by.senla.training.bulyha.pricemonitoring.enums.GoodsSubcategoryEnum;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.exception.InternalException;
import by.senla.training.bulyha.pricemonitoring.mapper.SubcategoryMapper;
import by.senla.training.bulyha.pricemonitoring.service.SubcategoryService;
import by.senla.training.bulyha.pricemonitoring.subcategory.NewSubcategoryDto;
import by.senla.training.bulyha.pricemonitoring.subcategory.SubcategoryAdminDto;
import by.senla.training.bulyha.pricemonitoring.subcategory.SubcategoryDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubcategoryServiceImplTest {

    private SubcategoryService subcategoryService;

    @Mock
    private SubcategoryDao subcategoryDao;

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private SubcategoryMapper mapper;

    public static final Logger LOG = Logger.getLogger(SubcategoryServiceImplTest.class.getName());

    @BeforeEach
    public void setUp() {
        subcategoryService = new SubcategoryServiceImpl(subcategoryDao, categoryDao, mapper);
    }

    @BeforeAll
    static void start() {
        LOG.info("Testing in SubcategoryServiceImplTest started.");
    }

    @AfterAll
    static void done() {
        LOG.info("Testing in SubcategoryServiceImplTest is over.");
    }

    @Test
    public void subcategoryServiceImpl_getSubcategoryByCategory() {
        String name = "products";
        Category category = new Category();
        category.setName(GoodsCategoryEnum.valueOf(name.toUpperCase()));
        category.setStatus(EntityStatusEnum.ACTUAL);
        SubcategoryDto subcategoryDto = new SubcategoryDto();
        subcategoryDto.setCategoryName(name);

        when(categoryDao.findCategoryByName(GoodsCategoryEnum.valueOf(name.toUpperCase()))).thenReturn(category);
        when(mapper.getSubcategoryToSubcategoryDto(category)).thenReturn(subcategoryDto);

        SubcategoryDto returnSubcategory = subcategoryService.getSubcategoryByCategory(name);
        Assertions.assertEquals(name, returnSubcategory.getCategoryName());
    }

    @Test
    public void subcategoryServiceImpl_getSubcategoryByCategory_EntityNotFoundException() {
        String name = "products";
        Category category = new Category();
        category.setName(GoodsCategoryEnum.valueOf(name.toUpperCase()));
        category.setStatus(EntityStatusEnum.DELETED);

        when(categoryDao.findCategoryByName(GoodsCategoryEnum.valueOf(name.toUpperCase()))).thenReturn(category);

        assertThrows(EntityNotFoundException.class, () -> {
            subcategoryService.getSubcategoryByCategory(name);
        });
    }

    @Test
    public void subcategoryServiceImpl_getAllSubcategory() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        categories.add(new Category());
        categories.add(new Category());

        List<SubcategoryDto> subcategoryDtoList = new ArrayList<>();
        subcategoryDtoList.add(new SubcategoryDto());
        subcategoryDtoList.add(new SubcategoryDto());
        subcategoryDtoList.add(new SubcategoryDto());

        when(categoryDao.findCategoriesByStatus(EntityStatusEnum.ACTUAL)).thenReturn(categories);
        when(mapper.getSubcategoryListToSubcategoryDtoList(categories)).thenReturn(subcategoryDtoList);

        List<SubcategoryDto> returnSubcategoryList = subcategoryService.getAllSubcategory();
        Assertions.assertEquals(subcategoryDtoList, returnSubcategoryList);
    }

    @Test
    public void subcategoryServiceImpl_getAllSubcategory_EntityNotFoundException() {
        List<Category> categories = new ArrayList<>();

        when(categoryDao.findCategoriesByStatus(EntityStatusEnum.ACTUAL)).thenReturn(categories);

        assertThrows(EntityNotFoundException.class, () -> {
            subcategoryService.getAllSubcategory();
        });
    }

    @Test
    public void subcategoryServiceImpl_addSubcategory() {
        String name = "fruits";
        NewSubcategoryDto newSubcategoryDto = new NewSubcategoryDto();
        newSubcategoryDto.setSubcategoryName(name);
        Subcategory subcategory = new Subcategory();
        subcategory.setName(GoodsSubcategoryEnum.valueOf(name.toUpperCase()));
        SubcategoryAdminDto subcategoryAdminDto = new SubcategoryAdminDto();
        subcategoryAdminDto.setSubcategoryName(name);

        when(subcategoryDao.findSubcategoryByName(GoodsSubcategoryEnum.valueOf(name.toUpperCase()))).thenReturn(null);
        when(mapper.getNewSubcategoryDtoToSubcategory(newSubcategoryDto)).thenReturn(subcategory);
        when(subcategoryDao.save(subcategory)).thenReturn(subcategory);
        when(mapper.getSubcategoryToSubcategoryAdminDto(subcategory)).thenReturn(subcategoryAdminDto);

        SubcategoryAdminDto returnSubcategory = subcategoryService.addSubcategory(newSubcategoryDto);
        assertEquals(subcategoryAdminDto, returnSubcategory);
    }

    @Test
    public void subcategoryServiceImpl_addSubcategory_InternalException() {
        String name = "fruits";
        NewSubcategoryDto newSubcategoryDto = new NewSubcategoryDto();
        newSubcategoryDto.setSubcategoryName(name);
        Subcategory subcategory = new Subcategory();
        subcategory.setName(GoodsSubcategoryEnum.valueOf(name.toUpperCase()));

        when(subcategoryDao.findSubcategoryByName(GoodsSubcategoryEnum.valueOf(name.toUpperCase()))).thenReturn(subcategory);

        assertThrows(InternalException.class, () -> {
            subcategoryService.addSubcategory(newSubcategoryDto);
        });
    }
}
