package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.BrandDao;
import by.senla.training.bulyha.pricemonitoring.PriceDao;
import by.senla.training.bulyha.pricemonitoring.RatingDao;
import by.senla.training.bulyha.pricemonitoring.entity.Price;
import by.senla.training.bulyha.pricemonitoring.entity.Rating;
import by.senla.training.bulyha.pricemonitoring.enums.PricesStatusEnum;
import by.senla.training.bulyha.pricemonitoring.shop.ShopDao;
import by.senla.training.bulyha.pricemonitoring.entity.Shop;
import by.senla.training.bulyha.pricemonitoring.entity.Brand;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.exception.InternalException;
import by.senla.training.bulyha.pricemonitoring.mapper.ShopMapper;
import by.senla.training.bulyha.pricemonitoring.service.ShopService;
import by.senla.training.bulyha.pricemonitoring.service.util.Control;
import by.senla.training.bulyha.pricemonitoring.shop.NewShopDto;
import by.senla.training.bulyha.pricemonitoring.shop.ShopAdminDto;
import by.senla.training.bulyha.pricemonitoring.shop.ShopDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ShopServiceImpl implements ShopService {

    private ShopDao shopDao;
    private BrandDao brandDao;
    private ShopMapper mapper;
    private PriceDao priceDao;
    private RatingDao ratingDao;
    public static final Logger LOG = Logger.getLogger(ShopServiceImpl.class.getName());

    @Autowired
    public ShopServiceImpl(ShopDao shopDao, BrandDao brandDao, ShopMapper mapper, PriceDao priceDao, RatingDao ratingDao) {
        this.shopDao = shopDao;
        this.brandDao = brandDao;
        this.mapper = mapper;
        this.priceDao = priceDao;
        this.ratingDao = ratingDao;
    }

    @Transactional
    @Override
    public ShopAdminDto getById(Long shopId) {
        Shop shop = shopDao.findById(shopId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Shop with id = %s is not found", shopId)));
        return mapper.getShopToShopAdminDto(shop);
    }

    @Transactional
    @Override
    public List<ShopDto> getAll() {
        List<Shop> shops = shopDao.findShopsByStatus(EntityStatusEnum.ACTUAL);
        if (shops.isEmpty()) {
            LOG.warning("List of shops is empty.");
            throw new EntityNotFoundException("List of shops is empty.");
        }
        return mapper.getShopListToShopDtoList(shops);
    }

    @Transactional
    @Override
    public List<ShopDto> getByBrandName(String name) {
        Brand brand = brandDao.findBrandByName(name);
        if (brand == null || Control.deletedOrNot(brand.getStatus())) {
            LOG.warning(String.format("Brand with name = %s is not found.", name));
            throw new EntityNotFoundException(String.format("Brand with name = %s is not found.", name));
        }
        List<Shop> shopList = shopDao.findShopByBrandIdAndStatus(brand.getId(), EntityStatusEnum.ACTUAL);
        if (shopList.isEmpty()) {
            LOG.warning(String.format("Shop with name = %s is not found.", name));
            throw new EntityNotFoundException(String.format("Shop with name = %s is not found", name));
        }
        return mapper.getShopListToShopDtoList(shopList);
    }

    @Transactional
    @Override
    public List<ShopAdminDto> getByBrandNameAdmin(String name) {
        Brand brand = brandDao.findBrandByName(name);
        if (brand == null) {
            LOG.warning(String.format("Brand with name = %s does not exist.", name));
            throw new EntityNotFoundException(String.format("Brand with name = %s does not exist.", name));
        }
        List<Shop> shopList = shopDao.findShopByBrandId(brand.getId());
        if (shopList.isEmpty()) {
            LOG.warning(String.format("Shop with name = %s is not found", name));
            throw new EntityNotFoundException(String.format("Shop with name = %s is not found", name));
        }
        return mapper.getShopListToShopAdminDtoList(shopList);
    }

    @Transactional
    @Override
    public List<ShopDto> getByAddress(String address) {
        List<Shop> shop = shopDao.findShopsByAddressContainsAndStatus(address, EntityStatusEnum.ACTUAL);
        if (shop.isEmpty()) {
            LOG.warning(String.format("Shop with address = %s is not found", address));
            throw new EntityNotFoundException(String.format("Shop with address = %s is not found", address));
        }
        return mapper.getShopListToShopDtoList(shop);
    }

    @Transactional
    @Override
    public List<ShopAdminDto> getByAddressAdmin(String address) {
        List<Shop> shop = shopDao.findShopsByAddressContainsAndStatus(address, EntityStatusEnum.ACTUAL);
        if (shop.isEmpty()) {
            LOG.warning(String.format("Shop with address = %s is not found", address));
            throw new EntityNotFoundException(String.format("Shop with address = %s is not found", address));
        }
        return mapper.getShopListToShopAdminDtoList(shop);
    }

    @Transactional
    @Override
    public ShopAdminDto addShop(NewShopDto shopDto) {
        Shop shop = shopDao.findShopByBrandIdAndAddress(shopDto.getBrand(), shopDto.getAddress());

        if (shop != null) {
            LOG.info(String.format("This %s is already in the database", shopDto));
            throw new InternalException(String.format("This %s is already in the database", shopDto));
        }

        return mapper.getShopToShopAdminDto(shopDao.save(mapper.getNewShopDtoToShop(shopDto)));
    }

    @Transactional
    @Override
    public ShopAdminDto updateShopStatus(Long shopId) {
        Shop shop = shopDao.findById(shopId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Shop with id = %s is not found", shopId)));
        shop.setStatus(EntityStatusEnum.DELETED);
        LOG.info(String.format("Status of shop with id = %s, name = %s and address %s has been changed to deleted",
                shopId, shop.getBrand().getName(), shop.getAddress()));
        shopDao.save(shop);

        List<Price> prices = priceDao.findAllByShopIdAndStatus(shopId, PricesStatusEnum.ACTUAL);
        if (!prices.isEmpty()) {
            prices.stream().peek(p -> p.setStatus(PricesStatusEnum.HISTORY))
                    .forEach(p -> priceDao.save(p));
            LOG.info(String.format("Status of prices with shop id = %s, name = %s and address %s has been changed to deleted",
                    shopId, shop.getBrand().getName(), shop.getAddress()));
        }

        List<Rating> ratings = ratingDao.findAllByShopIdAndStatus(shopId, EntityStatusEnum.ACTUAL);
        if (!ratings.isEmpty()) {
            ratings.stream()
                    .peek(r -> r.setStatus(EntityStatusEnum.DELETED))
                    .forEach(r -> ratingDao.save(r));
            LOG.info(String.format("Status of ratings with shop id = %s, name = %s and address %s has been changed to deleted",
                    shopId, shop.getBrand().getName(), shop.getAddress()));
        }

        return mapper.getShopToShopAdminDto(shopDao.findById(shopId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Shop with id = %s is not found", shopId))));
    }

    @Transactional
    @Override
    public List<ShopAdminDto> getShopListByStatus(String status) {
        List<ShopAdminDto> shopAdminDtos = mapper.getShopListToShopAdminDtoListAdmin(shopDao.findShopsByStatus(EntityStatusEnum.valueOf(status.toUpperCase())));
        if (shopAdminDtos.isEmpty()) {
            LOG.warning(String.format("Shops list with status = %s is not found", status));
            throw new EntityNotFoundException(String.format("Shops list with status = %s is not found", status));
        }
        return shopAdminDtos;
    }

    @Transactional
    @Override
    public List<ShopAdminDto> getAllAdmin() {
        List<ShopAdminDto> shopAdminDtos = mapper.getShopListToShopAdminDtoListAdmin(shopDao.findShopsByStatus(EntityStatusEnum.ACTUAL));
        if (shopAdminDtos.isEmpty()) {
            LOG.warning("Shops list not found.");
            throw new EntityNotFoundException("Shops list not found.");
        }
        return shopAdminDtos;
    }
}
