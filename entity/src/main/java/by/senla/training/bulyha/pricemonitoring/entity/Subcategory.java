package by.senla.training.bulyha.pricemonitoring.entity;

import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import by.senla.training.bulyha.pricemonitoring.enums.GoodsSubcategoryEnum;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subcategories")
public class Subcategory extends IdEntity {

    @Column(columnDefinition = "name")
    @Enumerated(EnumType.STRING)
    private GoodsSubcategoryEnum name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id")
    private Category category;

    @Column(columnDefinition = "status")
    @Enumerated(EnumType.STRING)
    private EntityStatusEnum status;

    @OneToMany(mappedBy = "subcategory", fetch = FetchType.LAZY)
    @Transient
    private List<Good> goodList;

    public Subcategory(GoodsSubcategoryEnum name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Subcategory: " + name;
    }
}
