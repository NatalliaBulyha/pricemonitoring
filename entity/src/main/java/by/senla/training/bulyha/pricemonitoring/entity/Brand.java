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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "brands")
public class Brand extends IdEntity {

    @Column(columnDefinition = "name")
    private String name;

    @Column(columnDefinition = "email")
    private String email;

    @Column(columnDefinition = "contact_number")
    private String contactNumber;

    @Column(columnDefinition = "unp")
    private Integer unp;

    @Column(columnDefinition = "status")
    @Enumerated(EnumType.STRING)
    private EntityStatusEnum status;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    @Transient
    private List<Shop> shopsList;

    @Builder
    public Brand(String name, String email, String contactNumber, Integer unp, EntityStatusEnum status) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.unp = unp;
        this.status = status;
    }
}
