package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Customer Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Customer Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "stock_qty", nullable = false)
    private Double stockQty;

    @ManyToOne
    @JsonIgnoreProperties("stocks")
    private Items item;

    @ManyToOne
    @JsonIgnoreProperties("stocks")
    private Location location;

    @ManyToOne
    @JsonIgnoreProperties("stocks")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getStockQty() {
        return stockQty;
    }

    public Stock stockQty(Double stockQty) {
        this.stockQty = stockQty;
        return this;
    }

    public void setStockQty(Double stockQty) {
        this.stockQty = stockQty;
    }

    public Items getItem() {
        return item;
    }

    public Stock item(Items items) {
        this.item = items;
        return this;
    }

    public void setItem(Items items) {
        this.item = items;
    }

    public Location getLocation() {
        return location;
    }

    public Stock location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Company getCompany() {
        return company;
    }

    public Stock company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock)) {
            return false;
        }
        return id != null && id.equals(((Stock) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", stockQty=" + getStockQty() +
            "}";
    }
}
