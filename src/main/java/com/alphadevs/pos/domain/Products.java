package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Products Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Products Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "products")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "product_code", nullable = false)
    private String productCode;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "product_prefix", nullable = false)
    private String productPrefix;

    @NotNull
    @Column(name = "product_prof_margin", nullable = false)
    private Double productProfMargin;

    @ManyToOne
    @JsonIgnoreProperties("products")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public Products productCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public Products productName(String productName) {
        this.productName = productName;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrefix() {
        return productPrefix;
    }

    public Products productPrefix(String productPrefix) {
        this.productPrefix = productPrefix;
        return this;
    }

    public void setProductPrefix(String productPrefix) {
        this.productPrefix = productPrefix;
    }

    public Double getProductProfMargin() {
        return productProfMargin;
    }

    public Products productProfMargin(Double productProfMargin) {
        this.productProfMargin = productProfMargin;
        return this;
    }

    public void setProductProfMargin(Double productProfMargin) {
        this.productProfMargin = productProfMargin;
    }

    public Location getLocation() {
        return location;
    }

    public Products location(Location location) {
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
        if (!(o instanceof Products)) {
            return false;
        }
        return id != null && id.equals(((Products) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Products{" +
            "id=" + getId() +
            ", productCode='" + getProductCode() + "'" +
            ", productName='" + getProductName() + "'" +
            ", productPrefix='" + getProductPrefix() + "'" +
            ", productProfMargin=" + getProductProfMargin() +
            "}";
    }
}
