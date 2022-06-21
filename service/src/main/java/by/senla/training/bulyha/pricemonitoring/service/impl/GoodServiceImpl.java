package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.BrandDao;
import by.senla.training.bulyha.pricemonitoring.exception.CsvException;
import by.senla.training.bulyha.pricemonitoring.good.GoodListBySubcategoryDto;
import by.senla.training.bulyha.pricemonitoring.PriceDao;
import by.senla.training.bulyha.pricemonitoring.entity.Price;
import by.senla.training.bulyha.pricemonitoring.entity.Brand;
import by.senla.training.bulyha.pricemonitoring.enums.PricesStatusEnum;
import by.senla.training.bulyha.pricemonitoring.exception.InternalException;
import by.senla.training.bulyha.pricemonitoring.good.GoodAdminDto;
import by.senla.training.bulyha.pricemonitoring.good.GoodDao;
import by.senla.training.bulyha.pricemonitoring.good.GoodUpdateDto;
import by.senla.training.bulyha.pricemonitoring.service.util.CSVHelper;
import by.senla.training.bulyha.pricemonitoring.shop.ShopDao;
import by.senla.training.bulyha.pricemonitoring.SubcategoryDao;
import by.senla.training.bulyha.pricemonitoring.entity.Subcategory;
import by.senla.training.bulyha.pricemonitoring.entity.Shop;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.enums.GoodsSubcategoryEnum;
import by.senla.training.bulyha.pricemonitoring.good.NewGoodDto;
import by.senla.training.bulyha.pricemonitoring.entity.Good;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.good.GoodSortAndFilterDto;
import by.senla.training.bulyha.pricemonitoring.mapper.GoodMapper;
import by.senla.training.bulyha.pricemonitoring.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static by.senla.training.bulyha.pricemonitoring.good.GoodSpecifications.withDescription;
import static by.senla.training.bulyha.pricemonitoring.good.GoodSpecifications.withGoodsName;
import static by.senla.training.bulyha.pricemonitoring.good.GoodSpecifications.withProducer;
import static by.senla.training.bulyha.pricemonitoring.good.GoodSpecifications.withStatus;
import static by.senla.training.bulyha.pricemonitoring.good.GoodSpecifications.withSubcategory;
import static by.senla.training.bulyha.pricemonitoring.shop.ShopSpecifications.withAddress;
import static by.senla.training.bulyha.pricemonitoring.shop.ShopSpecifications.withShopsName;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class GoodServiceImpl implements GoodService {

    private GoodDao goodDao;
    private GoodMapper mapper;
    private SubcategoryDao subcategoryDao;
    private ShopDao shopDao;
    private BrandDao brandDao;
    private PriceDao priceDao;
    private CSVHelper csvHelper;
    public static final Logger LOG = Logger.getLogger(GoodServiceImpl.class.getName());

    @Autowired
    public GoodServiceImpl(GoodDao goodDao, GoodMapper mapper, SubcategoryDao subcategoryDao, ShopDao shopDao,
                           BrandDao brandDao, PriceDao priceDao, CSVHelper csvHelper) {
        this.goodDao = goodDao;
        this.mapper = mapper;
        this.subcategoryDao = subcategoryDao;
        this.shopDao = shopDao;
        this.brandDao = brandDao;
        this.priceDao = priceDao;
        this.csvHelper = csvHelper;
    }

    @Transactional
    @Override
    public GoodListBySubcategoryDto getGoodsListBySubcategory(Long subcategoryId) {
        List<Good> goods = goodDao.findGoodsBySubcategoryIdAndStatus(subcategoryId, EntityStatusEnum.ACTUAL);
        return mapper.getGoodListToGoodListBySubcategoryDto(subcategoryId, goods);
    }

    @Transactional
    @Override
    public GoodAdminDto getById(Long goodId) {
        return mapper.getGoodToGoodAdminDto(goodDao.findById(goodId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Good with id = %s is not found", goodId))));
    }

    @Transactional
    @Override
    public List<GoodSortAndFilterDto> getAllByFilter() {
        List<Good> goods = goodDao.findGoodsByStatus(EntityStatusEnum.ACTUAL);
        List<Subcategory> subcategories = subcategoryDao.findSubcategoriesByStatus(EntityStatusEnum.ACTUAL);
        List<Shop> shops = shopDao.findShopsByStatus(EntityStatusEnum.ACTUAL);
        return mapper.getGoodListToGoodSortAndFilterDtoList(subcategories, shops, goods, new HashMap<String, String>());
    }

    @Transactional
    @Override
    public List<GoodAdminDto> getByName(String goodName) {
        List<Good> good = goodDao.findGoodLikeByName(goodName);
        if (good.isEmpty()) {
            LOG.warning(String.format("Goods with name like %s are not found.", goodName));
            throw new EntityNotFoundException(String.format("Goods with name like %s are not found", goodName));
        }
        return mapper.getGoodListToGoodAdminDtoList(good);
    }

    @Transactional
    @Override
    public List<GoodAdminDto> getByNameAndStatus(String status, String goodName) {
        List<Good> good = goodDao.findGoodByNameLikeAndStatus(goodName, EntityStatusEnum.valueOf(status.toUpperCase()));
        if (good.isEmpty()) {
            LOG.warning(String.format("%s goods with name like %s are not found.", status, goodName));
            throw new EntityNotFoundException(String.format("%s goods with name like %s are not found", status, goodName));
        }
        return mapper.getGoodListToGoodAdminDtoList(good);
    }

    @Transactional
    @Override
    public List<GoodAdminDto> getAll() {
        List<Good> goodList = goodDao.findAll();
        if (goodList.isEmpty()) {
            LOG.warning("Goods are not found");
            throw new EntityNotFoundException("Goods are not found");
        }
        return mapper.getGoodListToGoodAdminDtoList(goodList);
    }

    @Transactional
    @Override
    public List<GoodAdminDto> getAllGoodsByStatus(String status) {
        List<GoodAdminDto> goodAdminDtoList =
                mapper.getGoodListToGoodAdminDtoList(goodDao.findGoodsByStatus(EntityStatusEnum.valueOf(status.toUpperCase())));
        if (goodAdminDtoList.isEmpty()) {
            LOG.warning(String.format("Goods with status = %s are not found.", status));
            throw new EntityNotFoundException(String.format("Goods with status = %s are not found", status));
        }
        return goodAdminDtoList;
    }

    @Transactional
    @Override
    public GoodAdminDto addGood(NewGoodDto newGoodDto) {
        Good good = goodDao.findGoodByNameAndProducer(newGoodDto.getName(), newGoodDto.getProducer());
        if (good != null && good.getStatus().equals(EntityStatusEnum.ACTUAL)) {
            LOG.warning(String.format("This %s is already in the database.", newGoodDto.getName()));
            throw new InternalException(String.format("This %s is already in the database", newGoodDto.getName()));
        }

        Good returnGood = goodDao.save(mapper.getNewGoodDtoToGood(newGoodDto));
        priceDao.save(mapper.getNewGoodDtoToPrice(newGoodDto, returnGood));
        LOG.info(String.format("Price for good with name = %s was saved.", newGoodDto.getName()));

        return mapper.getGoodToGoodAdminDto(returnGood);
    }

    @Transactional
    @Override
    public GoodAdminDto updateGoodStatus(Long id) {
        Good good = goodDao.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Good with id = %s is not found", id)));
        if (good.getStatus().equals(EntityStatusEnum.DELETED)) {
            LOG.warning(String.format("Good with id = %s is already deleted.", id));
            throw new EntityNotFoundException(String.format("Good with id = %s is already deleted", id));
        }
        good.setStatus(EntityStatusEnum.DELETED);
        goodDao.save(good);
        LOG.info(String.format("Status of good with id = %s has been changed to deleted.", id));

        List<Price> prices = priceDao.findPriceByGoodIdAndStatus(good.getId(), PricesStatusEnum.ACTUAL);
        prices.stream()
                .peek(r -> r.setStatus(PricesStatusEnum.HISTORY))
                .forEach(r -> priceDao.save(r));
        LOG.info(String.format("Status of prices with Good's id = %s has been changed to deleted", id));

        return mapper.getGoodToGoodAdminDto(good);
    }

    @Transactional
    @Override
    public GoodUpdateDto updateGood(Map<String, String> map) {
        Good good = goodDao.findById(Long.parseLong(map.get("id")))
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Good with id = %s is not found", map.get("id"))));
        if (map.size() > 1) {
            if (map.containsKey("name")) {
                good.setName(map.get("name"));
            }
            if (map.containsKey("producer")) {
                good.setProducer(map.get("producer"));
            }
            if (map.containsKey("country")) {
                good.setCountry(map.get("country"));
            }
            if (map.containsKey("description")) {
                good.setDescription(map.get("description"));
            }
        }

        if (map.containsKey("subcategories_id")) {
            Subcategory subcategory = subcategoryDao.findById(Long.parseLong(map.get("subcategories_id")))
                    .orElseThrow(() ->
                            new EntityNotFoundException(String.format("Subcategory with id = %s is not found.", map.get("subcategories_id"))));
            good.setSubcategory(subcategory);
        }
        goodDao.save(good);
        LOG.info(String.format("Good with id = %s was changed.", good.getId()));
        return mapper.getGoodToGoodUpdateDto(good);
    }

    @Transactional
    @Override
    public List<GoodSortAndFilterDto> getFilterAndSortGoodsList(Map<String, String> map) {
        List<Shop> shops;
        if (map.containsKey("brand")) {
            String brandsName = "%" + map.get("brand") + "%";
            Brand brand = brandDao.findBrandLikeName(brandsName);
            map.put("brand", brand.getId().toString());
        }
        if (map.containsKey("brand") || map.containsKey("address")) {
            shops = shopDao.findAll(where(withShopsName(map))
                    .and(withAddress(map)));
            map.remove("brand");
            map.remove("address");
        } else {
            shops = shopDao.findShopsByStatus(EntityStatusEnum.ACTUAL);
        }
        Subcategory subcategory = null;
        List<Good> goods;
        Long subcategoryId = null;
        if (map.containsKey("subcategory")) {
            subcategory = subcategoryDao.findSubcategoryByName(GoodsSubcategoryEnum.valueOf(map.get("subcategory").toUpperCase()));
            map.remove("subcategory");
            subcategoryId = subcategory.getId();
            if (subcategory.getStatus().equals(EntityStatusEnum.DELETED)) {
                throw new EntityNotFoundException(String.format("Subcategory with id = %s is not found", subcategory.getName()));
            }
        }

        goods = goodDao.findAll(where(withSubcategory(subcategoryId))
                .and(withStatus(EntityStatusEnum.ACTUAL))
                .and(withGoodsName(map))
                .and(withProducer(map))
                .and(withDescription(map)));

        List<GoodSortAndFilterDto> goodSortAndFilterDtos = new ArrayList<>();
        if (subcategory != null) {
            goodSortAndFilterDtos.add(mapper.getGoodGoodToGoodShop(subcategory.getName().toString(), shops, goods, map));
        } else {
            goodSortAndFilterDtos = mapper.getGoodListToGoodSortAndFilterDtoList(subcategoryDao.findSubcategoriesByStatus(EntityStatusEnum.ACTUAL), shops, goods, map);
        }
        return goodSortAndFilterDtos;
    }

    @Transactional
    public void importFile(MultipartFile file) {
        if (csvHelper.hasCSVFormat(file)) {
            List<Good> goodList = mapper.getGoodAdminDtoListToGoodList(csvHelper.loadObjectList(GoodAdminDto.class, file.getOriginalFilename()));
            if (goodList.isEmpty()) {
                LOG.warning(String.format("List from file %s is empty.", file.getOriginalFilename()));
                throw new CsvException(String.format("List from file %s is empty.", file.getOriginalFilename()));
            }
            for (Good good : goodList) {
                Good goodCheck = goodDao.findGoodByNameAndProducerAndDescription(good.getName(),
                        good.getProducer(), good.getDescription());
                if (goodCheck == null) {
                    goodDao.save(good);
                }
            }
        } else {
            LOG.warning(String.format("File %s haven't csv format.", file.getOriginalFilename()));
            throw new CsvException("Please import a csv file!");
        }
    }
}
