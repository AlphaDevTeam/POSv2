package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * ItemBinCard Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "ItemBinCard Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "item_bin_card")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ItemBinCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull
    @Column(name = "transaction_description", nullable = false)
    private String transactionDescription;

    @NotNull
    @Column(name = "transaction_qty", nullable = false)
    private Double transactionQty;

    @NotNull
    @Column(name = "transaction_balance", nullable = false)
    private Double transactionBalance;

    @ManyToOne
    @JsonIgnoreProperties("itemBinCards")
    private Location location;

    @ManyToOne
    @JsonIgnoreProperties("itemBinCards")
    private Items item;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public ItemBinCard transactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public ItemBinCard transactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
        return this;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public Double getTransactionQty() {
        return transactionQty;
    }

    public ItemBinCard transactionQty(Double transactionQty) {
        this.transactionQty = transactionQty;
        return this;
    }

    public void setTransactionQty(Double transactionQty) {
        this.transactionQty = transactionQty;
    }

    public Double getTransactionBalance() {
        return transactionBalance;
    }

    public ItemBinCard transactionBalance(Double transactionBalance) {
        this.transactionBalance = transactionBalance;
        return this;
    }

    public void setTransactionBalance(Double transactionBalance) {
        this.transactionBalance = transactionBalance;
    }

    public Location getLocation() {
        return location;
    }

    public ItemBinCard location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Items getItem() {
        return item;
    }

    public ItemBinCard item(Items items) {
        this.item = items;
        return this;
    }

    public void setItem(Items items) {
        this.item = items;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemBinCard)) {
            return false;
        }
        return id != null && id.equals(((ItemBinCard) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ItemBinCard{" +
            "id=" + getId() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionDescription='" + getTransactionDescription() + "'" +
            ", transactionQty=" + getTransactionQty() +
            ", transactionBalance=" + getTransactionBalance() +
            "}";
    }
}
