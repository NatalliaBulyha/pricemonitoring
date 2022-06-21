package by.senla.training.bulyha.pricemonitoring.controller;

import by.senla.training.bulyha.pricemonitoring.service.ShopService;
import by.senla.training.bulyha.pricemonitoring.shop.NewShopDto;
import by.senla.training.bulyha.pricemonitoring.shop.ShopAdminDto;
import by.senla.training.bulyha.pricemonitoring.shop.ShopDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Validated
@RestController
@RequestMapping("shops")
@Tag(name = "shop controller", description = "the shop API with description tag annotation.")
public class ShopController {

    private ShopService shopService;
    public static final Logger LOG = Logger.getLogger(ShopController.class.getName());

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @Operation(summary = "get list of shops",
            description = "method returns list of shops by name of brand, address or all shops;")
    @GetMapping("/list")
    public List<ShopDto> getShops(@RequestParam(required = false) String brandName,
                                  @RequestParam(required = false) String address) {
        List<ShopDto> shopList;
        if (brandName != null && address == null) {
            shopList = (shopService.getByBrandName(brandName));
        } else if (brandName == null && address != null) {
            shopList = shopService.getByAddress(address);
        } else {
            shopList = shopService.getAll();
        }
        return shopList;
    }

    @Operation(summary = "get list of shops for admin",
            description = "method returns list of shops by id, name of brand, address, status or all shops;")
    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public List<ShopAdminDto> getShopsAdmin(@RequestParam(required = false) @Min(1) Long shopId,
                                            @RequestParam(required = false) String brandName,
                                            @RequestParam(required = false) String address,
                                            @RequestParam(required = false) String status) {
        List<ShopAdminDto> shopList = new ArrayList<>();
        if (shopId != null && brandName == null && address == null && status == null) {
            shopList.add(shopService.getById(shopId));
        } else if (shopId == null && brandName != null && address == null && status == null) {
            shopList = (shopService.getByBrandNameAdmin(brandName));
        } else if (shopId == null && brandName == null && address != null && status == null) {
            shopList = shopService.getByAddressAdmin(address);
        } else if (shopId == null && brandName == null && address == null && status != null) {
            shopList = shopService.getShopListByStatus(status);
        } else {
            shopList = shopService.getAllAdmin();
        }
        return shopList;
    }

    @Operation(summary = "add new shop",
            description = "add NewShopDto: " +
                    "mobile number - format +375(33) 123-45-67; " +
                    "open and close time - format HH-mm.")
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public ShopAdminDto addShop(@RequestBody @Valid NewShopDto shopDto) {
        ShopAdminDto shopAdminDto = shopService.addShop(shopDto);
        LOG.info(String.format("Shop %s by address %s was added.", shopDto.getBrand(), shopDto.getAddress()));
        return shopAdminDto;
    }

    @Operation(summary = "deleted shop",
            description = "method changes status of shop from actual to deleted. " +
                    "Status of the prices and ratings for these shos also changes.")
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ShopAdminDto updateShopStatus(@PathVariable("id") Long shopId) {
        ShopAdminDto shopAdminDto = shopService.updateShopStatus(shopId);
        LOG.info(String.format("Shop with id = %s was deleted.", shopId));
        return shopAdminDto;
    }
}
