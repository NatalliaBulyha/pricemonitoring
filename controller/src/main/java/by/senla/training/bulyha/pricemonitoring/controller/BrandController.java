package by.senla.training.bulyha.pricemonitoring.controller;

import by.senla.training.bulyha.pricemonitoring.brand.BrandAdminDto;
import by.senla.training.bulyha.pricemonitoring.brand.BrandSortColumnNameDto;
import by.senla.training.bulyha.pricemonitoring.brand.NewBrandDto;
import by.senla.training.bulyha.pricemonitoring.service.BrandService;
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
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("brands")
@Validated
@Tag(name = "brand controller", description = "the brand API with description tag annotation.")
public class BrandController {

    public BrandService brandService;
    public static final Logger LOG = Logger.getLogger(BrandController.class.getName());

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @Operation(summary = "Get brand or list of brands",
    description = "This method return brand by its id, name, unp or list of brands by status or brands list sorted by name, " +
            "email, contact_number, unp, status. If no parameter is specified will return list with all brands." +
            "brand - Gippo, Perekrestok, Belmarket, Kopeechka, Rodny kut;" +
            "unp - 9 numbers, ex. 123456789; " +
            "sort - NAME, EMAIL, CONTACT_NUMBER, UNP, STATUS;" +
            "status - deleted, actual.")
    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public List<BrandAdminDto> getBrand(@RequestParam(required = false) @Min(1) Long brandId,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) @Size(min = 9, max = 9, message = "Unp must be 9 characters.") String unp,
                                        @RequestParam(required = false) BrandSortColumnNameDto sort,
                                        @RequestParam(required = false) String status) {
        List<BrandAdminDto> brandDtoList = new ArrayList<>();
        if (brandId != null && name == null && unp == null && sort == null && status == null) {
            brandDtoList.add(brandService.getById(brandId));
        } else if (brandId == null && name != null && unp == null && sort == null && status == null) {
            brandDtoList.add(brandService.getByName(name));
        } else if (brandId == null && name == null && unp != null && sort == null && status == null) {
            brandDtoList.add(brandService.getByUnp(Integer.parseInt(unp)));
        } else if (brandId == null && name == null && unp == null && sort != null && status == null) {
            brandDtoList = brandService.getSortList(sort);
        } else if (brandId == null && name == null && unp == null && sort == null && status != null) {
            brandDtoList = brandService.getBrandListByStatus(status);
        } else {
            brandDtoList = brandService.getAll();
        }
        return brandDtoList;
    }

    @Operation(summary = "add new brand",
            description = "add NewBrandDto: " +
            "unp - 9 numbers, ex. 123456789;" +
            "mobile number - format +375(33) 123-45-67.")
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public BrandAdminDto addBrand(@RequestBody @Valid NewBrandDto brandDto) {
        BrandAdminDto brandAdminDto = brandService.addBrand(brandDto);
        LOG.info(String.format("Added a new Brand with name = %s.", brandDto.getName()));
        return brandAdminDto;
    }

    @Operation(summary = "deleted brand",
            description = "method changes status of brand from actual to deleted. " +
            "Status of the stores of this brand, of the prices and ratings for these shops also changes.")
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    public BrandAdminDto updateBrandStatus(@PathVariable("id") @Min(1) Long brandId) {
        BrandAdminDto brandAdminDto = brandService.updateBrandStatus(brandId);
        LOG.info(String.format("Brand with id = %s was deleted.", brandId));
        return brandAdminDto;
    }
}
