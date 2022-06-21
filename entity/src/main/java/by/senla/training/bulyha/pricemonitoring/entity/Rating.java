package by.senla.training.bulyha.pricemonitoring.entity;

import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import lombok.AllArgsConstructor;
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
import javax.persistence.Table;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ratings")
public class Rating extends IdEntity {

    @Column(columnDefinition = "assortment")
    private Integer assortment;

    @Column(columnDefinition = "quality_of_service")
    private Integer qualityOfService;

    @Column(columnDefinition = "prices")
    private Integer prices;

    @Column(columnDefinition = "date")
    private LocalDate date;

    @Column(columnDefinition = "status")
    @Enumerated(EnumType.STRING)
    private EntityStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shops_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;
}
