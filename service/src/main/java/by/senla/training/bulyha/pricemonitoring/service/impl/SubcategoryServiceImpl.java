package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.CategoryDao;
import by.senla.training.bulyha.pricemonitoring.SubcategoryDao;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {

    private SubcategoryDao subcategoryDao;
    private CategoryDao categoryDao;
    private SubcategoryMapper mapper;
    public static final Logger LOG = Logger.getLogger(SubcategoryServiceImpl.class.getName());

    @Autowired
    public SubcategoryServiceImpl(SubcategoryDao subcategoryDao, CategoryDao categoryDao, SubcategoryMapper mapper) {
        this.subcategoryDao = subcategoryDao;
        this.categoryDao = categoryDao;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public SubcategoryDto getSubcategoryByCategory(String categoryName) {
        Category category = categoryDao.findCategoryByName(GoodsCategoryEnum.valueOf(categoryName.toUpperCase()));
        if (category == null || category.getStatus().equals(EntityStatusEnum.DELETED)) {
            LOG.warning(String.format("Category with name = %s is not found.", categoryName));
            throw new EntityNotFoundException(String.format("Category with name = %s is not found.", categoryName));
        }
        return mapper.getSubcategoryToSubcategoryDto(category);
    }

    @Transactional
    @Override
    public List<SubcategoryDto> getAllSubcategory() {
        List<Category> categories = categoryDao.findCategoriesByStatus(EntityStatusEnum.ACTUAL);
        if (categories.isEmpty()) {
            LOG.warning("Subcategory is not found.");
            throw new EntityNotFoundException("Subcategory is not found.");
        }
        return mapper.getSubcategoryListToSubcategoryDtoList(categories);
    }

    @Transactional
    @Override
    public SubcategoryAdminDto addSubcategory(NewSubcategoryDto newSubCategoryDto) {
        Subcategory subcategory = subcategoryDao.findSubcategoryByName(GoodsSubcategoryEnum.valueOf(newSubCategoryDto.getSubcategoryName().toUpperCase()));
        if (subcategory != null) {
            LOG.warning(String.format("This %s is already in the database!", newSubCategoryDto.getSubcategoryName()));
            throw new InternalException(String.format("This %s is already in the database!", newSubCategoryDto.getSubcategoryName()));
        }
        return mapper.getSubcategoryToSubcategoryAdminDto(subcategoryDao.save(mapper.getNewSubcategoryDtoToSubcategory(newSubCategoryDto)));
    }
}
