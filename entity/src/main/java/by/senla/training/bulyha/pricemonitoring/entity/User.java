package by.senla.training.bulyha.pricemonitoring.entity;

import by.senla.training.bulyha.pricemonitoring.enums.EntityStatusEnum;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends IdEntity implements UserDetails {

    @Column(columnDefinition = "login", updatable = false)
    private String login;

    @Column(columnDefinition = "password")
    private String password;

    @Column(columnDefinition = "last_name")
    private String lastName;

    @Column(columnDefinition = "first_name")
    private String firstName;

    @Column(columnDefinition = "registration_date")
    private LocalDate registrationDate;

    @Column(columnDefinition = "mobile_number")
    private String mobileNumber;

    @Column(columnDefinition = "email")
    private String email;

    @Column(columnDefinition = "birthday", updatable = false)
    private LocalDate birthday;

    @Column(columnDefinition = "status")
    @Enumerated(EnumType.STRING)
    private EntityStatusEnum status;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Price> priceList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Rating> ratingList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_has_roles",
            joinColumns = {@JoinColumn(name = "users_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "roles_id", referencedColumnName = "id")})
    private Set<Role> roleList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Token> tokenList;

    public User(String login, String password, EntityStatusEnum status) {
        this.login = login;
        this.password = password;
        this.status = status;
    }

    @Builder
    public User(String login, String password, String lastName, String firstName, LocalDate registrationDate,
                String mobileNumber, String email, LocalDate birthday, EntityStatusEnum status) {
        this.login = login;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.registrationDate = registrationDate;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.birthday = birthday;
        this.status = status;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public EntityStatusEnum getStatus() {
        return status;
    }

    public void setStatus(EntityStatusEnum status) {
        this.status = status;
    }

    public List<Price> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<Price> priceList) {
        this.priceList = priceList;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    public Set<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(Set<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoleList();
    }

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
