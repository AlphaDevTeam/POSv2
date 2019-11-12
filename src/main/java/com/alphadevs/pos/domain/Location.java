package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Location Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Location Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "location_code", nullable = false)
    private String locationCode;

    @NotNull
    @Column(name = "location_name", nullable = false)
    private String locationName;

    @NotNull
    @Column(name = "location_prof_margin", nullable = false)
    private Double locationProfMargin;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties("locations")
    private Company company;

    @ManyToMany(mappedBy = "locations")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ExUser> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public Location locationCode(String locationCode) {
        this.locationCode = locationCode;
        return this;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public Location locationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getLocationProfMargin() {
        return locationProfMargin;
    }

    public Location locationProfMargin(Double locationProfMargin) {
        this.locationProfMargin = locationProfMargin;
        return this;
    }

    public void setLocationProfMargin(Double locationProfMargin) {
        this.locationProfMargin = locationProfMargin;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Location isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Company getCompany() {
        return company;
    }

    public Location company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<ExUser> getUsers() {
        return users;
    }

    public Location users(Set<ExUser> exUsers) {
        this.users = exUsers;
        return this;
    }

    public Location addUsers(ExUser exUser) {
        this.users.add(exUser);
        exUser.getLocations().add(this);
        return this;
    }

    public Location removeUsers(ExUser exUser) {
        this.users.remove(exUser);
        exUser.getLocations().remove(this);
        return this;
    }

    public void setUsers(Set<ExUser> exUsers) {
        this.users = exUsers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return getLocationCode();
    }
}
