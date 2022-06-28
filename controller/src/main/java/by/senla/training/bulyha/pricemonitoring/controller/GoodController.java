package by.senla.training.bulyha.pricemonitoring.controller;

import by.senla.training.bulyha.pricemonitoring.exception.EmptyEntityException;
import by.senla.training.bulyha.pricemonitoring.good.GoodAdminDto;
import by.senla.training.bulyha.pricemonitoring.good.GoodListBySubcategoryDto;
import by.senla.training.bulyha.pricemonitoring.good.GoodSortAndFilterDto;
import by.senla.training.bulyha.pricemonitoring.good.GoodUpdateDto;
import by.senla.training.bulyha.pricemonitoring.good.NewGoodDto;
import by.senla.training.bulyha.pricemonitoring.service.GoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Validated
@RestController
@RequestMapping("goods")
@Tag(name = "good controller", description = "the good API with description tag annotation.")
public class GoodController {

    private GoodService goodService;
    public static final Logger LOG = Logger.getLogger(GoodController.class.getName());

    @Autowired
    public GoodController(GoodService goodService) {
        this.goodService = goodService;
    }

    @Operation(summary = "Get list of goods by subcategory",
            description = "This method returns list of goods by subcategory." +
                    "subcategories - DAIRY_PRODUCTS, FRUITS, CHICKEN, LAUNDRY, DISHWASHER, KITCHEN, NOTEBOOKS, STATIONERY, FOR_CREATIVITY")
    @GetMapping("/subcategory")
    public List<GoodListBySubcategoryDto> getBySubcategory(@RequestParam @Min(1) Long subcategory) {
        List<GoodListBySubcategoryDto> goodListBySubcategoryDto = new ArrayList<>();
        goodListBySubcategoryDto.add(goodService.getGoodsListBySubcategory(subcategory));
        return goodListBySubcategoryDto;
    }


    @Operation(summary = "Get list of goods by subcategory",
            description = "This method returns filtered list of goods with optional parameters or sorted by price." +
                    "subcategories - DAIRY_PRODUCTS, FRUITS, CHICKEN, LAUNDRY, DISHWASHER, KITCHEN, NOTEBOOKS, STATIONERY, FOR_CREATIVITY; " +
                    "goodsName - yogurt, mango, apples, chicken, washing+powder+Color, washing+powder, washing+gel+Color, " +
                    "dishwasher+tablets, salt+for+dishwashers, dishwashing+liquid, notebook, gel+pen+set+Neon, " +
                    "glitter+Gel+Pen+Set, pen, plasticine+wax, modeling+dough; " +
                    "producersName - Babushkina+krynka, Bellakt, ImportFrut, Agrokombinat+Dzerzhinskij, Pticefabrika+Druzhba, " +
                    "Ariel, Somat, Finish, Sorti, Fairy, Erich+Krause, Dobrushskaya+bumazhnaya+fabrika, Darvish, Multi-Pulti, Cvetik;" +
                    "description - 130+gr, 350+gr, 450+gr, Ready+to+eat, red, green, cooled, frozen, automat,+6+kg, " +
                    "automat,+3+kg, automat,+1,95+l, All+in+One+Max,+100+pieses, special,+3+kg, special,+3+kg, Lemon,+900m+l, " +
                    "Bergamot,+900+ml, Lavender,+450+ml, cell,+80+sheet, cell,+48+sheet, 12+pieses, 6+pieses, black, 6+pieses; " +
                    "brand - Gippo, Perekrestok, Belmarket, Kopeechka, Rodny+kut; " +
                    "address - Mikrorajon+1+str.,+4a,+Zaslavl, Sovetskaya+str.,+48,+Zaslavl, Studeneckij+str.,+7,+Zaslavl, " +
                    "Studeneckij+str.,+5,+Zaslavl, Sovetskaya+str.,+81b,+Zaslavl, Sovetskaya+str.,+98,+Zaslavl, Velikaya+str.,+37,+Zaslavl; " +
                    "sortFirstPrice - true, false.")
    @GetMapping("/filter")
    public List<GoodSortAndFilterDto> getSearchAndSort(@RequestParam(required = false) String subcategory,
                                                       @RequestParam(required = false) String goodsName,
                                                       @RequestParam(required = false) String producersName,
                                                       @RequestParam(required = false) String description,
                                                       @RequestParam(required = false) String brand,
                                                       @RequestParam(required = false) String address,
                                                       @RequestParam(required = false) Boolean sortFirstPrice) {
        Map<String, String> filterParameters = new HashMap<>();

        if (subcategory != null) {
            filterParameters.put("subcategory", subcategory);
        }
        if (goodsName != null) {
            filterParameters.put("name", goodsName);
        }
        if (producersName != null) {
            filterParameters.put("producer", producersName);
        }
        if (description != null) {
            filterParameters.put("description", description);
        }
        if (brand != null) {
            filterParameters.put("brand", brand);
        }
        if (address != null) {
            filterParameters.put("address", address);
        }
        if (sortFirstPrice != null) {
            filterParameters.put("sortFirstMinPrice", sortFirstPrice.toString());
        }
        return filterParameters.size() == 0 ? goodService.getAllByFilter() : goodService.getFilterAndSortGoodsList(filterParameters);
    }


    @Operation(summary = "Get list of goods",
            description = "This method returns good by id, list of goods by status, name or all goods. " +
                    "status - deleted, actual; " +
                    "name - yogurt, mango, apples, chicken, washing+powder+Color, washing+powder, washing+gel+Color, " +
                    "dishwasher+tablets, salt+for+dishwashers, dishwashing+liquid, notebook, gel+pen+set+Neon, " +
                    "glitter+Gel+Pen+Set, pen, plasticine+wax, modeling+dough.")
    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public List<GoodAdminDto> getAllByStatusOrIdOrName(@RequestParam(required = false) String status,
                                             @RequestParam(required = false) @Min(1) Long id,
                                             @RequestParam(required = false) String name) {
        List<GoodAdminDto> goodAdminDtoList = new ArrayList<>();
        if (status != null && id == null && name == null) {
            goodAdminDtoList = goodService.getAllGoodsByStatus(status);
        } else if (status == null && id != null && name == null) {
            goodAdminDtoList.add(goodService.getById(id));
        } else if (status == null && id == null && name != null) {
            goodAdminDtoList = goodService.getByName(name);
        } else if (status != null && id == null) {
            goodAdminDtoList = goodService.getByNameAndStatus(status, name);
        } else {
            goodAdminDtoList = goodService.getAll();
        }
        if (goodAdminDtoList.isEmpty()) {
            throw new EmptyEntityException("Wrong filter value.");
        }
        return goodAdminDtoList;
    }

    @Operation(summary = "add new good",
            description = "method adds NewGoodDto: " +
            "subcategory -  id: 1 DAIRY_PRODUCTS, 2 FRUITS, 3 CHICKEN, 4 LAUNDRY, 5 DISHWASHER, 6 KITCHEN, 7 NOTEBOOKS, " +
            "8 STATIONERY, 9 FOR_CREATIVITY; " +
            "shopId - id: 1. Gippo Mikrorajon 1 str., 4a, 2. Perekrestok Sovetskaya str., 48, 3. Belmarket Studeneckij str., 7, " +
            "4. Kopeechka Studeneckij str., 5, 5. Kopeechka Sovetskaya+str., 81b, 6. Rodny+kut Sovetskaya str., 98, " +
            "7. Rodny+kut Velikaya str., 37.")
    @Secured({"ROLE_ADMIN"})
    @PostMapping("/new_good")
    public GoodAdminDto addGood(@RequestBody @Valid NewGoodDto newGoodDto) {
        GoodAdminDto good = goodService.addGood(newGoodDto);
        LOG.info(String.format("New good with name = %s added.", newGoodDto.getName()));
        return good;
    }

    @Operation(summary = "deleted good",
            description = "method changes status of good from actual to deleted. " +
            "Status of the prices of this good also changes.")
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/removal")
    public GoodAdminDto updateGoodStatus(@RequestParam @Min(1) Long goodId) {
        GoodAdminDto goodAdminDto = goodService.updateGoodStatus(goodId);
        LOG.info(String.format("Good with id = %s was deleted.", goodId));
        return goodAdminDto;
    }

    @Operation(summary = "update good",
            description = "method updates good; goodId is necessarily; name, producer, country, " +
            "description, subcategoryId - are no necessarily, only what will be change.")
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/editing")
    public GoodUpdateDto updateGood(@RequestParam @Min(1) Long goodId,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String producer,
                                    @RequestParam(required = false) String country,
                                    @RequestParam(required = false) String description,
                                    @RequestParam(required = false) @Min(1) Long subcategoryId) {
        Map<String, String> updateGoodMap = new HashMap<>();
        updateGoodMap.put("id", goodId.toString());
        if (name != null) {
            updateGoodMap.put("name", name);
        }
        if (producer != null) {
            updateGoodMap.put("producer", producer);
        }
        if (country != null) {
            updateGoodMap.put("country", country);
        }
        if (description != null) {
            updateGoodMap.put("description", description);
        }
        if (subcategoryId != null) {
            updateGoodMap.put("subcategories_id", subcategoryId.toString());
        }
        GoodUpdateDto goodUpdateDto = goodService.updateGood(updateGoodMap);
        LOG.info(String.format("Good with id = %s was update.", goodId));
        return goodUpdateDto;
    }

    @Operation(summary = "import file with goods",
            description = "method adds goods from scv file. if good with same name, " +
            "producer and description is in database - good don't add to database.")
    @Secured({"ROLE_ADMIN"})
    @PostMapping("/csv")
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        goodService.importFile(file);
        LOG.info(String.format("Imported the file %s successfully: ", file.getOriginalFilename()));
        message = "Imported the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.ok().body(message);
    }
}
