package by.senla.training.bulyha.pricemonitoring.controller;

import by.senla.training.bulyha.pricemonitoring.price.NewPriceDto;
import by.senla.training.bulyha.pricemonitoring.price.PriceAdminDto;
import by.senla.training.bulyha.pricemonitoring.price.priceComparison.PriceComparisonDto;
import by.senla.training.bulyha.pricemonitoring.price.PriceDto;
import by.senla.training.bulyha.pricemonitoring.price.priceDynamics.PriceDynamicsForPeriodDao;
import by.senla.training.bulyha.pricemonitoring.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RestController
@Validated
@RequestMapping("prices")
@Tag(name = "price controller", description = "the price API with description tag annotation.")
public class PriceController {

    private PriceService priceService;
    public static final Logger LOG = Logger.getLogger(PriceController.class.getName());

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @Operation(summary = "add price",
            description = "method adds new price. This prise is tied to specific shop, good, user and day.")
    @PostMapping
    public PriceDto addPrice(@RequestBody @Valid NewPriceDto newPriceDto) {
        PriceDto priceDto = priceService.addPrice(newPriceDto);
        LOG.info(String.format("Price %s of good with id = %s was added.", newPriceDto.getPrice(), newPriceDto.getGoodId()));
        return priceDto;
    }

    @Operation(summary = "get price by shop id",
            description = "method returns list of prices with all info by shop id.")
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/list")
    public List<PriceAdminDto> getByShopId(@RequestParam @Min(1) Long shopId) {
        return priceService.getByShopId(shopId);
    }


    @Operation(summary = "get dynamic list of prices for period",
            description = "method returns sorted by date list of all prices for period.")
    @GetMapping("/dynamics")
    public PriceDynamicsForPeriodDao getPriceDynamicsForPeriod(@RequestParam @Min(1) Long goodId,
                                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate finishDate,
                                                               @RequestParam(required = false) @Min(1) Long shopId) {
        return priceService.getPriceDynamicsForPeriod(goodId, startDate, finishDate, shopId);
    }

    @Operation(summary = "get comparison list of prices",
            description = "method returns list of actual prices in several shops min 2 max 5.")
    @GetMapping("/comparison")
    public PriceComparisonDto getPriceComparison(@RequestParam @Min(1) Long goodId,
                                                 @RequestParam @Min(1) Long shopId1,
                                                 @RequestParam @Min(1) Long shopId2,
                                                 @RequestParam(required = false) @Min(1) Long shopId3,
                                                 @RequestParam(required = false) @Min(1) Long shopId4,
                                                 @RequestParam(required = false) @Min(1) Long shopId5) {
        Set<Long> shops = new HashSet<>();
        shops.add(shopId1);
        shops.add(shopId2);
        if (shopId3 != null) {
            shops.add(shopId3);
        }
        if (shopId4 != null) {
            shops.add(shopId4);
        }
        if (shopId5 != null) {
            shops.add(shopId5);
        }
        return priceService.getPriceComparison(goodId, shops);
    }

    @Operation(summary = "get list of prices",
            description = "method returns list of all prices for a specific product.")
    @GetMapping("/graphical")
    public PriceDynamicsForPeriodDao showGraphicalChangeOfPriceDynamics(@RequestParam @Min(1) Long goodId) {
        return priceService.getListPriceGraphicalChangeOfPriceDynamics(goodId);
    }

    @Operation(summary = "import file with prices",
            description = "method adds prices from scv file. if date this price is before date of actual price on same " +
                    "good in database price from csv file id added to database with status history, if after - with " +
                    "status actual and price from database is changed status to history. If date is same this price is ignored.")
    @Secured({"ROLE_ADMIN"})
    @PostMapping("/csv")
    public ResponseEntity<String> importFile(@Valid @RequestParam("file") MultipartFile file) {
        String message = "";
        priceService.importFile(file);
        LOG.info(String.format("Imported the file %s successfully: ", file.getOriginalFilename()));
        message = "Imported the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.ok().body(message);
    }
}
