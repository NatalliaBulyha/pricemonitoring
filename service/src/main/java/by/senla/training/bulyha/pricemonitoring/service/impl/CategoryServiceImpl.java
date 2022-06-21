package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.CategoryDao;
import by.senla.training.bulyha.pricemonitoring.SubcategoryDao;
import by.senla.training.bulyha.pricemonitoring.category.CategoryAdminDto;
import by.senla.training.bulyha.pricemonitoring.category.CategoryDto;
import by.senla.training.bulyha.pricemonitoring.entity.Category;
import by.senla.training.bulyha.pricemonitoring.entity.Good;
import by.senla.training.bulyha.pricemonitoring.entity.Price;
import by.senla.training.bulyha.pricemonitoring.entity.Subcategory;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.enums.GoodsCategoryEnum;
import by.senla.training.bulyha.pricemonitoring.enums.PricesStatusEnum;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.exception.InternalException;
import by.senla.training.bulyha.pricemonitoring.good.GoodDao;
import by.senla.training.bulyha.pricemonitoring.mapper.CategoryMapper;
import by.senla.training.bulyha.pricemonitoring.PriceDao;
import by.senla.training.bulyha.pricemonitoring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao;
    private CategoryMapper mapper;
    private SubcategoryDao subcategoryDao;
    private PriceDao priceDao;
    private GoodDao goodDao;
    public static final Logger LOG = Logger.getLogger(CategoryServiceImpl.class.getName());

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao, CategoryMapper mapper, SubcategoryDao subcategoryDao, PriceDao priceDao, GoodDao goodDao) {
        this.categoryDao = categoryDao;
        this.mapper = mapper;
        this.subcategoryDao = subcategoryDao;
        this.priceDao = priceDao;
        this.goodDao = goodDao;
    }

    @Transactional
    public CategoryDto getById(Long categoryId) {
        return mapper.getCategoryToCategoryDto(categoryDao.findById(categoryId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Category with id = %s is not found!", categoryId))));
    }

    @Transactional
    public CategoryAdminDto getByName(String categoryName) {
        Category category = categoryDao.findCategoryByName(GoodsCategoryEnum.valueOf(categoryName));
        if (category == null) {
            throw new EntityNotFoundException(String.format("Category with name = %s is not found!", categoryName));
        }
        return mapper.getCategoryToCategoryAdminDto(category);
    }

    @Transactional
    public CategoryAdminDto addCategory(CategoryDto categoryDto) {
        Category category = categoryDao.findCategoryByName(GoodsCategoryEnum.valueOf(categoryDto.getCategory().toUpperCase()));
        if (category != null) {
            throw new InternalException(String.format("This %s is already in the database!", categoryDto.getCategory()));
        }
        return mapper.getCategoryToCategoryAdminDto(categoryDao.save(mapper.getCategoryDtoToCategory(categoryDto)));
    }

    @Override
    @Transactional
    public List<CategoryAdminDto> getAllAdmin() {
        List<Category> category = categoryDao.findAll();
        if (category.isEmpty()) {
            throw new EntityNotFoundException("There are no categories, you can create a new category!");
        }
        return mapper.getCategoryListToCategoryAdminDtoList(category);
    }

    @Override
    @Transactional
    public List<CategoryAdminDto> getByStatus(String status) {
        List<CategoryAdminDto> categoryAdminDtoList =
                mapper.getCategoryListToCategoryAdminDtoList(categoryDao.findCategoriesByStatus(EntityStatusEnum.valueOf(status.toUpperCase())));
        if (categoryAdminDtoList.isEmpty()) {
            throw new EntityNotFoundException("There are no categories !");
        }
        return categoryAdminDtoList;
    }

    @Override
    @Transactional
    public CategoryAdminDto updateCategoryStatus(Long categoryId) {
        Category category = categoryDao.findById(categoryId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Category with id = %s is not found", categoryId)));
        category.setStatus(EntityStatusEnum.DELETED);
        categoryDao.save(category);

        List<Subcategory> subcategories = subcategoryDao.findSubcategoriesByCategoryIdAndStatus(categoryId, EntityStatusEnum.ACTUAL);
        List<Good> goods = new ArrayList<>();
        List<Price> prices = new ArrayList<>();

        for (Subcategory subcategory : subcategories) {
            goods.addAll(goodDao.findGoodsBySubcategoryIdAndStatus(subcategory.getId(), EntityStatusEnum.ACTUAL));
            for (Good good : goods) {
                prices.addAll(priceDao.findPriceByGoodIdAndStatus(good.getId(), PricesStatusEnum.ACTUAL));
                for (Price price : prices) {
                    price.setStatus(PricesStatusEnum.DELETED);
                    priceDao.save(price);
                }
                good.setStatus(EntityStatusEnum.DELETED);
                goodDao.save(good);
            }
            subcategory.setStatus(EntityStatusEnum.DELETED);
            subcategoryDao.save(subcategory);
        }
        return mapper.getCategoryToCategoryAdminDto(categoryDao.findById(categoryId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Category with id = %s is not found", categoryId))));
    }
}
