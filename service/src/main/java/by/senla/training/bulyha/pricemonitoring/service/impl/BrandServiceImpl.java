package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.BrandDao;
import by.senla.training.bulyha.pricemonitoring.RatingDao;
import by.senla.training.bulyha.pricemonitoring.entity.Price;
import by.senla.training.bulyha.pricemonitoring.entity.Rating;
import by.senla.training.bulyha.pricemonitoring.enums.PricesStatusEnum;
import by.senla.training.bulyha.pricemonitoring.PriceDao;
import by.senla.training.bulyha.pricemonitoring.shop.ShopDao;
import by.senla.training.bulyha.pricemonitoring.brand.BrandAdminDto;
import by.senla.training.bulyha.pricemonitoring.brand.BrandSortColumnNameDto;
import by.senla.training.bulyha.pricemonitoring.brand.NewBrandDto;
import by.senla.training.bulyha.pricemonitoring.entity.Brand;
import by.senla.training.bulyha.pricemonitoring.BrandSortColumnName;
import by.senla.training.bulyha.pricemonitoring.entity.Shop;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.exception.InternalException;
import by.senla.training.bulyha.pricemonitoring.mapper.BrandMapper;
import by.senla.training.bulyha.pricemonitoring.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class BrandServiceImpl implements BrandService {

    private BrandDao brandDao;
    private BrandMapper mapper;
    private ShopDao shopDao;
    private RatingDao ratingDao;
    private PriceDao priceDao;
    public static final Logger LOG = Logger.getLogger(BrandServiceImpl.class.getName());

    @Autowired
    public BrandServiceImpl(BrandDao brandDao, BrandMapper mapper, ShopDao shopDao, RatingDao ratingDao,
                            PriceDao priceDao) {
        this.brandDao = brandDao;
        this.mapper = mapper;
        this.shopDao = shopDao;
        this.ratingDao = ratingDao;
        this.priceDao = priceDao;
    }

    @Transactional
    public BrandAdminDto getById(Long brandId) {
        Brand brand = brandDao.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Brand with id = %s is not found", brandId)));
        return mapper.getBrandToAdminBrandDto(brand);
    }

    @Transactional
    public List<BrandAdminDto> getAll() {
        List<Brand> brands = brandDao.findAll();
        if (brands.isEmpty()) {
            LOG.warning("Brands list is empty.");
            throw new EntityNotFoundException("Brands list is empty.");
        }
        return mapper.getBrandListToAdminBrandDtoList(brands);
    }

    @Transactional
    public BrandAdminDto getByName(String name) {
        Brand brand = brandDao.findBrandByName(name);
        if (brand == null) {
            LOG.warning(String.format("Brand with name = %s is not found", name));
            throw new EntityNotFoundException(String.format("Brand with name = %s is not found", name));
        }
        return mapper.getBrandToAdminBrandDto(brand);
    }

    @Transactional
    public BrandAdminDto getByUnp(Integer unp) {
        Brand brand = brandDao.findBrandByUnp(unp);
        if (brand == null) {
            LOG.warning(String.format("Brand with unp = %s is not found", unp));
            throw new EntityNotFoundException(String.format("Brand with unp = %s is not found", unp));
        }
        return mapper.getBrandToAdminBrandDto(brand);
    }

    @Transactional
    public BrandAdminDto addBrand(NewBrandDto brandDto) {
        Brand brandByUnp = brandDao.findBrandByUnp(Integer.valueOf(brandDto.getUnp()));

        if (brandByUnp != null) {
            LOG.warning(String.format("This %s is already in the database", brandDto.getName()));
            throw new InternalException(String.format("This %s is already in the database", brandDto.getName()));
        }
        Brand brand = brandDao.save(mapper.getBrandDtoToBrand(brandDto));
        return mapper.getBrandToAdminBrandDto(brand);
    }

    @Transactional
    public BrandAdminDto updateBrandStatus(Long brandId) {
        Brand brand = brandDao.findById(brandId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Brand with id = %s is not found", brandId)));
        brand.setStatus(EntityStatusEnum.DELETED);
        LOG.info(String.format("Status of brand with id = %s and name = %s has been changed to deleted", brandId,
                brand.getName()));
        brandDao.save(brand);

        List<Shop> shopList = shopDao.findShopByBrandIdAndStatus(brandId, EntityStatusEnum.ACTUAL);
        shopList.stream().peek(s -> s.setStatus(EntityStatusEnum.DELETED)).forEach(s -> shopDao.save(s));
        LOG.info(String.format("Status of shops with Brand's id = %s and name = %s has been changed to deleted",
                brand.getId(), brand.getName()));

        List<Rating> ratings = new ArrayList<>();
        List<Price> prices = new ArrayList<>();

        for (Shop shop : shopList) {
            ratings.addAll(ratingDao.findAllByShopIdAndStatus(shop.getId(), EntityStatusEnum.ACTUAL));
            prices.addAll(priceDao.findAllByShopIdAndStatus(shop.getId(), PricesStatusEnum.ACTUAL));
        }

        ratings.stream()
                .peek(r -> r.setStatus(EntityStatusEnum.DELETED))
                .forEach(r -> ratingDao.save(r));
        LOG.info(String.format("Status of ratings with Brand's id = %s and name = %s has been changed to deleted",
                brand.getId(), brand.getName()));

        prices.stream().peek(p -> p.setStatus(PricesStatusEnum.HISTORY))
                .forEach(p -> priceDao.save(p));
        LOG.info(String.format("Status of prices with Brand's id = %s and name = %s has been changed to deleted",
                brand.getId(), brand.getName()));

        return mapper.getBrandToAdminBrandDto(brandDao.findById(brandId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Brand with id = %s is not found", brandId))));
    }

    @Transactional
    public List<BrandAdminDto> getSortList(BrandSortColumnNameDto brandSortColumnNameDto) {
        BrandSortColumnName columnName = mapper.mappingColumnNameDtoByColumnName(brandSortColumnNameDto);
        List<Brand> brandList = brandDao.findBrandsOrderBy(Sort.by(Sort.Direction.ASC,
                columnName.getColumn().toString().toLowerCase()));
        return mapper.getBrandListToAdminBrandDtoList(brandList);
    }

    @Transactional
    @Override
    public List<BrandAdminDto> getBrandListByStatus(String status) {
        List<Brand> brands = brandDao.findBrandsByStatus(EntityStatusEnum.valueOf(status.toUpperCase()));
        if (brands.isEmpty()) {
            LOG.warning(String.format("No brands with status %s.", status));
            throw new EntityNotFoundException(String.format("No brands with status %s.", status));
        }
        return mapper.getBrandListToAdminBrandDtoList(brands);
    }
}
