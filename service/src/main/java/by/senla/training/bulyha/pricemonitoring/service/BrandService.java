package by.senla.training.bulyha.pricemonitoring.service;

import by.senla.training.bulyha.pricemonitoring.brand.BrandAdminDto;
import by.senla.training.bulyha.pricemonitoring.brand.BrandSortColumnNameDto;
import by.senla.training.bulyha.pricemonitoring.brand.NewBrandDto;

import java.util.List;

public interface BrandService {

    BrandAdminDto getById(Long brandId);

    List<BrandAdminDto> getAll();

    BrandAdminDto getByName(String name);

    BrandAdminDto getByUnp(Integer unp);

    BrandAdminDto addBrand(NewBrandDto brandDto);

    BrandAdminDto updateBrandStatus(Long brandId);

    List<BrandAdminDto> getSortList(BrandSortColumnNameDto brandSortColumnNameDto);

    List<BrandAdminDto> getBrandListByStatus(String status);
}
