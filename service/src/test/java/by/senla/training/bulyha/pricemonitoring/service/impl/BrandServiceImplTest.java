package by.senla.training.bulyha.pricemonitoring.service.impl;

import by.senla.training.bulyha.pricemonitoring.BrandDao;
import by.senla.training.bulyha.pricemonitoring.BrandSortColumnName;
import by.senla.training.bulyha.pricemonitoring.PriceDao;
import by.senla.training.bulyha.pricemonitoring.RatingDao;
import by.senla.training.bulyha.pricemonitoring.brand.BrandAdminDto;
import by.senla.training.bulyha.pricemonitoring.brand.BrandSortColumnNameDto;
import by.senla.training.bulyha.pricemonitoring.brand.NewBrandDto;
import by.senla.training.bulyha.pricemonitoring.entity.Brand;
import by.senla.training.bulyha.pricemonitoring.enums.BrandColumnEnum;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.exception.EntityNotFoundException;
import by.senla.training.bulyha.pricemonitoring.exception.InternalException;
import by.senla.training.bulyha.pricemonitoring.mapper.BrandMapper;
import by.senla.training.bulyha.pricemonitoring.service.BrandService;
import by.senla.training.bulyha.pricemonitoring.shop.ShopDao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BrandServiceImplTest {

    private BrandService brandService;

    @Mock
    private BrandDao brandDao;

    @Mock
    private BrandMapper mapper;

    @Mock
    private ShopDao shopDao;

    @Mock
    private RatingDao ratingDao;

    @Mock
    private PriceDao priceDao;

    public static final Logger LOG = Logger.getLogger(BrandServiceImplTest.class.getName());

    @BeforeEach
    public void setUp() {
        brandService = new BrandServiceImpl(brandDao, mapper, shopDao, ratingDao, priceDao);
    }

    @BeforeAll
    static void start() {
        LOG.info("Testing in BrandServiceImplTest started.");
    }

    @AfterAll
    static void done() {
        LOG.info("Testing in BrandServiceImplTest is over.");
    }

    @Test
    public void brandServiceImpl_getById() {
        Brand brand = new Brand();
        Long brandId = 1L;
        brand.setId(brandId);
        BrandAdminDto brandAdminDto = new BrandAdminDto();
        brandAdminDto.setId(brandId);

        when(brandDao.findById(brandId)).thenReturn(Optional.of(brand));
        when(mapper.getBrandToAdminBrandDto(brand)).thenReturn(brandAdminDto);

        BrandAdminDto returnBrandAdminDto = brandService.getById(brandId);
        Assertions.assertEquals(brandId, returnBrandAdminDto.getId());
    }

    @Test
    public void brandServiceImpl_getById_entityNotFoundExc() {
        Long brandId = 1L;

        when(brandDao.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            brandService.getById(brandId);
        });
    }

    @Test
    public void brandServiceImpl_getAll() {
        List<Brand> brands = new ArrayList<>();
        brands.add(new Brand());
        brands.add(new Brand());
        brands.add(new Brand());
        List<BrandAdminDto> brandAdminDto = new ArrayList<>();
        brandAdminDto.add(new BrandAdminDto());
        brandAdminDto.add(new BrandAdminDto());
        brandAdminDto.add(new BrandAdminDto());

        when(brandDao.findAll()).thenReturn(brands);
        when(mapper.getBrandListToAdminBrandDtoList(brands)).thenReturn(brandAdminDto);

        List <BrandAdminDto> returnBrandAdminDto = brandService.getAll();
        Assertions.assertEquals(brandAdminDto, returnBrandAdminDto);
    }

    @Test
    public void brandServiceImpl_getAll_entityNotFoundExc() {
        List<Brand> brands = new ArrayList<>();

        when(brandDao.findAll()).thenReturn(brands);

        assertThrows(RuntimeException.class, () -> {
            brandService.getAll();
        });
    }

    @Test
    public void brandServiceImpl_getByName() {
        Brand brand = new Brand();
        String name = "A";
        brand.setName(name);
        BrandAdminDto brandAdminDto = new BrandAdminDto();
        brandAdminDto.setName(name);

        when(brandDao.findBrandByName(name)).thenReturn(brand);
        when(mapper.getBrandToAdminBrandDto(brand)).thenReturn(brandAdminDto);

        BrandAdminDto returnBrandAdminDto = brandService.getByName(name);
        Assertions.assertEquals(name, returnBrandAdminDto.getName());
    }

    @Test
    public void brandServiceImpl_getByName_entityNotFoundExc() {
        String name = "A";

        when(brandDao.findBrandByName(name)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            brandService.getByName(name);
        });
    }

    @Test
    public void brandServiceImpl_getByUnp() {
        Brand brand = new Brand();
        Integer unp = 123456789;
        brand.setUnp(unp);
        BrandAdminDto brandAdminDto = new BrandAdminDto();
        brandAdminDto.setUnp(unp);

        when(brandDao.findBrandByUnp(unp)).thenReturn(brand);
        when(mapper.getBrandToAdminBrandDto(brand)).thenReturn(brandAdminDto);

        BrandAdminDto returnBrandAdminDto = brandService.getByUnp(unp);
        Assertions.assertEquals(unp, returnBrandAdminDto.getUnp());
    }

    @Test
    public void brandServiceImpl_getByUnp_entityNotFoundExc() {
        Integer unp = 123456789;

        when(brandDao.findBrandByUnp(unp)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            brandService.getByUnp(unp);
        });
    }

    @Test
    public void brandServiceImpl_addBrand() {
        NewBrandDto newBrandDto = new NewBrandDto();
        Integer unp = 123456789;
        newBrandDto.setUnp(unp.toString());

        Brand brand = new Brand();
        brand.setUnp(unp);

        BrandAdminDto brandAdminDto = new BrandAdminDto();
        brandAdminDto.setUnp(unp);

        when(brandDao.findBrandByUnp(unp)).thenReturn(null);
        when(mapper.getBrandDtoToBrand(newBrandDto)).thenReturn(brand);
        when(brandDao.save(brand)).thenReturn(brand);
        when(mapper.getBrandToAdminBrandDto(brand)).thenReturn(brandAdminDto);

        BrandAdminDto returnBrandAdminDto = brandService.addBrand(newBrandDto);
        Assertions.assertEquals(unp, returnBrandAdminDto.getUnp());
    }

    @Test
    public void brandServiceImpl_addBrand_InternalException() {
        Integer unp = 123456789;
        NewBrandDto newBrandDto = new NewBrandDto();
        newBrandDto.setUnp(unp.toString());
        Brand brand = new Brand();
        brand.setUnp(unp);

        when(brandDao.findBrandByUnp(unp)).thenReturn(brand);

        assertThrows(InternalException.class, () -> {
            brandService.addBrand(newBrandDto);
        });
    }

    @Test
    public void  brandServiceImpl_getSortList() {
        String name = "name";
        BrandSortColumnNameDto columnNameDto = new BrandSortColumnNameDto(name);
        BrandSortColumnName columnName = new BrandSortColumnName(BrandColumnEnum.valueOf(name.toUpperCase()));

        List<Brand> brands = new ArrayList<>();
        brands.add(new Brand());
        brands.add(new Brand());
        brands.add(new Brand());

        List<BrandAdminDto> brandAdminDto = new ArrayList<>();
        brandAdminDto.add(new BrandAdminDto());
        brandAdminDto.add(new BrandAdminDto());
        brandAdminDto.add(new BrandAdminDto());

        when(mapper.mappingColumnNameDtoByColumnName(columnNameDto)).thenReturn(columnName);
        when(brandDao.findBrandsOrderBy(Sort.by(Sort.Direction.ASC, name))).thenReturn(brands);
        when(mapper.getBrandListToAdminBrandDtoList(brands)).thenReturn(brandAdminDto);

        List<BrandAdminDto> returnBrands = brandService.getSortList(columnNameDto);
        assertEquals(brandAdminDto, returnBrands);
    }

    @Test
    public void  brandServiceImpl_getSortList_NullPointerException() {
        String name = "";
        BrandSortColumnNameDto columnNameDto = new BrandSortColumnNameDto(name);
        BrandSortColumnName columnName = new BrandSortColumnName();

        when(mapper.mappingColumnNameDtoByColumnName(columnNameDto)).thenReturn(columnName);

        assertThrows(NullPointerException.class, () -> {
            brandService.getSortList(columnNameDto);
        });
    }

    @Test
    public void  brandServiceImpl_getBrandListByStatus() {
        String status = "deleted";
        List<Brand> brands = new ArrayList<>();
        brands.add(new Brand());
        brands.add(new Brand());
        brands.add(new Brand());

        List<BrandAdminDto> brandAdminDto = new ArrayList<>();
        brandAdminDto.add(new BrandAdminDto());
        brandAdminDto.add(new BrandAdminDto());
        brandAdminDto.add(new BrandAdminDto());

        when(brandDao.findBrandsByStatus(EntityStatusEnum.valueOf(status.toUpperCase()))).thenReturn(brands);
        when(mapper.getBrandListToAdminBrandDtoList(brands)).thenReturn(brandAdminDto);

        List<BrandAdminDto> returnBrands = brandService.getBrandListByStatus(status);
        assertEquals(brandAdminDto, returnBrands);
    }

    @Test
    public void  brandServiceImpl_getBrandListByStatus_NullPointerException() {
        String status = "deleted";
        List<Brand> brands = new ArrayList<>();

        when(brandDao.findBrandsByStatus(EntityStatusEnum.valueOf(status.toUpperCase()))).thenReturn(brands);

        assertThrows(EntityNotFoundException.class, () -> {
            brandService.getBrandListByStatus(status);
        });
    }

    @Test
    public void  brandServiceImpl_updateBrandStatus_EntityNotFoundException() {
        Long id = 1L;

        when(brandDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            brandService.updateBrandStatus(id);
        });
    }
}
