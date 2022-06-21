package by.senla.training.bulyha.pricemonitoring.service;

import by.senla.training.bulyha.pricemonitoring.shop.NewShopDto;
import by.senla.training.bulyha.pricemonitoring.shop.ShopAdminDto;
import by.senla.training.bulyha.pricemonitoring.shop.ShopDto;

import java.util.List;

public interface ShopService {

    ShopAdminDto getById(Long shopId);

    List<ShopDto> getAll();

    List<ShopDto> getByBrandName(String name);

    List<ShopAdminDto> getByBrandNameAdmin(String name);

    List<ShopDto> getByAddress(String address);

    List<ShopAdminDto> getByAddressAdmin(String address);

    ShopAdminDto addShop(NewShopDto shopDto);

    ShopAdminDto updateShopStatus(Long shopId);

    List<ShopAdminDto> getShopListByStatus(String status);

    List<ShopAdminDto> getAllAdmin();
}
