package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Supplier Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Supplier Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "supplier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "supplier_code", nullable = false)
    private String supplierCode;

    @NotNull
    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    @NotNull
    @Column(name = "supplier_limit", nullable = false)
    private Double supplierLimit;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties("suppliers")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public Supplier supplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
        return this;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public Supplier supplierName(String supplierName) {
        this.supplierName = supplierName;
        return this;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Double getSupplierLimit() {
        return supplierLimit;
    }

    public Supplier supplierLimit(Double supplierLimit) {
        this.supplierLimit = supplierLimit;
        return this;
    }

    public void setSupplierLimit(Double supplierLimit) {
        this.supplierLimit = supplierLimit;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Supplier isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Location getLocation() {
        return location;
    }

    public Supplier location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Supplier)) {
            return false;
        }
        return id != null && id.equals(((Supplier) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Supplier{" +
            "id=" + getId() +
            ", supplierCode='" + getSupplierCode() + "'" +
            ", supplierName='" + getSupplierName() + "'" +
            ", supplierLimit=" + getSupplierLimit() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
