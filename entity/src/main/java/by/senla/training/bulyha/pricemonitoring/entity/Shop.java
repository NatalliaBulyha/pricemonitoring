package by.senla.training.bulyha.pricemonitoring.entity;

import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.enums.ShopTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "shops")
public class Shop extends IdEntity {

    @Column(columnDefinition = "address")
    private String address;

    @Column(columnDefinition = "contact_number")
    private String contactNumber;

    @Column(columnDefinition = "open_time")
    private LocalTime openTime;

    @Column(columnDefinition = "close_time")
    private LocalTime closeTime;

    @Column(columnDefinition = "status")
    @Enumerated(EnumType.STRING)
    private EntityStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "type")
    private ShopTypeEnum type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brands_id")
    private Brand brand;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    @Transient
    private List<Rating> ratingsList;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    @Transient
    private List<Price> pricesList;

    @Builder
    public Shop(String address, String contactNumber, LocalTime openTime, LocalTime closeTime, EntityStatusEnum status, ShopTypeEnum type, Brand brand) {
        this.address = address;
        this.contactNumber = contactNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
        this.type = type;
        this.brand = brand;
    }
}
