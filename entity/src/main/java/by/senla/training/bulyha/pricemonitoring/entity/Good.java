package by.senla.training.bulyha.pricemonitoring.entity;

import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
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
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "goods")
public class Good extends IdEntity {

    @Column(columnDefinition = "name")
    private String name;

    @Column(columnDefinition = "producer")
    private String producer;

    @Column(columnDefinition = "country")
    private String country;

    @Column(columnDefinition = "description")
    private String description;

    @Column(columnDefinition = "status")
    @Enumerated(EnumType.STRING)
    private EntityStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategories_id")
    private Subcategory subcategory;

    @OneToMany(mappedBy = "good", fetch = FetchType.LAZY)
    @Transient
    private List<Price> priceList;

    @Builder
    public Good(String name, String producer, String country, String description, EntityStatusEnum status, Subcategory subcategory) {
        this.name = name;
        this.producer = producer;
        this.country = country;
        this.description = description;
        this.status = status;
        this.subcategory = subcategory;
    }
}
