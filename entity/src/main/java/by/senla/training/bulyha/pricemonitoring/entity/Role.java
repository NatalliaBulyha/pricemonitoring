package by.senla.training.bulyha.pricemonitoring.entity;

import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends IdEntity implements GrantedAuthority {

    @Column(columnDefinition = "name")
    private String name;

    @Column(columnDefinition = "status")
    @Enumerated(EnumType.STRING)
    private EntityStatusEnum status;

    @ManyToMany(fetch = FetchType.LAZY)
    @Transient
    private List<User> userList;

    @Override
    public String getAuthority() {
        return getName();
    }
}
