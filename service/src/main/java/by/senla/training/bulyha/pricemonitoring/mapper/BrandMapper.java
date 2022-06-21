package by.senla.training.bulyha.pricemonitoring.mapper;

import by.senla.training.bulyha.pricemonitoring.brand.BrandAdminDto;
import by.senla.training.bulyha.pricemonitoring.brand.BrandSortColumnNameDto;
import by.senla.training.bulyha.pricemonitoring.brand.NewBrandDto;
import by.senla.training.bulyha.pricemonitoring.entity.Brand;
import by.senla.training.bulyha.pricemonitoring.BrandSortColumnName;
import by.senla.training.bulyha.pricemonitoring.enums.BrandColumnEnum;
import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BrandMapper {

    public NewBrandDto getBrandToBrandDto(Brand brand) {
        return NewBrandDto.builder()
                .name(brand.getName())
                .email(brand.getEmail())
                .contactNumber(brand.getContactNumber())
                .unp(brand.getUnp().toString())
                .build();
    }

    public List<NewBrandDto> getBrandListToBrandDtoList(List<Brand> brandList) {
        return brandList.stream().map(this::getBrandToBrandDto).collect(Collectors.toList());
    }

    public Brand getBrandDtoToBrand(NewBrandDto brandDto) {
        return Brand.builder()
                .name(brandDto.getName())
                .email(brandDto.getEmail())
                .contactNumber(brandDto.getContactNumber())
                .unp(Integer.parseInt(brandDto.getUnp()))
                .status(EntityStatusEnum.ACTUAL)
                .build();
    }

    public BrandAdminDto getBrandToAdminBrandDto(Brand brand) {
        return BrandAdminDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .email(brand.getEmail())
                .contactNumber(brand.getContactNumber())
                .unp(brand.getUnp())
                .status(brand.getStatus().toString())
                .build();
    }

    public List<BrandAdminDto> getBrandListToAdminBrandDtoList(List<Brand> brandList) {
        return brandList.stream().map(this::getBrandToAdminBrandDto).collect(Collectors.toList());
    }

    public BrandSortColumnName mappingColumnNameDtoByColumnName(BrandSortColumnNameDto columnNameDto) {
        return new BrandSortColumnName(BrandColumnEnum.valueOf(columnNameDto.getBrandSortColumnNameDto().toUpperCase()));
    }
}
