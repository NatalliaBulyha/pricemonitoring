package by.senla.training.bulyha.pricemonitoring.service;

import by.senla.training.bulyha.pricemonitoring.good.GoodAdminDto;
import by.senla.training.bulyha.pricemonitoring.good.GoodListBySubcategoryDto;
import by.senla.training.bulyha.pricemonitoring.good.GoodUpdateDto;
import by.senla.training.bulyha.pricemonitoring.good.NewGoodDto;
import by.senla.training.bulyha.pricemonitoring.good.GoodSortAndFilterDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface GoodService {

    GoodListBySubcategoryDto getGoodsListBySubcategory(Long subcategory);

    GoodAdminDto getById(Long goodId);

    List<GoodSortAndFilterDto> getAllByFilter();

    List<GoodSortAndFilterDto> getFilterAndSortGoodsList(Map<String, String>  map);

    List<GoodAdminDto> getByName(String name);

    List<GoodAdminDto> getByNameAndStatus(String status, String name);

    List<GoodAdminDto> getAll();

    List<GoodAdminDto> getAllGoodsByStatus(String status);

    GoodAdminDto addGood(NewGoodDto newGoodDto);

    GoodAdminDto updateGoodStatus(Long id);

    GoodUpdateDto updateGood(Map<String, String> updateGoodMap);

    void importFile(MultipartFile file);
}
