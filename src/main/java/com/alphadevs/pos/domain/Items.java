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
 * Items Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Items Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "items")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Items implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_code", nullable = false)
    private String itemCode;

    @NotNull
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @NotNull
    @Column(name = "item_description", nullable = false)
    private String itemDescription;

    @NotNull
    @Column(name = "item_price", nullable = false)
    private Double itemPrice;

    @NotNull
    @Column(name = "item_serial", nullable = false)
    private String itemSerial;

    @Column(name = "item_supplier_serial")
    private String itemSupplierSerial;

    @NotNull
    @Column(name = "item_cost", nullable = false)
    private Double itemCost;

    @Column(name = "item_sale_price")
    private Double itemSalePrice;

    @NotNull
    @Column(name = "original_stock_date", nullable = false)
    private LocalDate originalStockDate;

    @Column(name = "modified_stock_date")
    private LocalDate modifiedStockDate;

    @ManyToOne
    @JsonIgnoreProperties("items")
    private Desings relatedDesign;

    @ManyToOne
    @JsonIgnoreProperties("items")
    private Products relatedProduct;

    @ManyToOne
    @JsonIgnoreProperties("items")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public Items itemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public Items itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public Items itemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
        return this;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public Items itemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
        return this;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemSerial() {
        return itemSerial;
    }

    public Items itemSerial(String itemSerial) {
        this.itemSerial = itemSerial;
        return this;
    }

    public void setItemSerial(String itemSerial) {
        this.itemSerial = itemSerial;
    }

    public String getItemSupplierSerial() {
        return itemSupplierSerial;
    }

    public Items itemSupplierSerial(String itemSupplierSerial) {
        this.itemSupplierSerial = itemSupplierSerial;
        return this;
    }

    public void setItemSupplierSerial(String itemSupplierSerial) {
        this.itemSupplierSerial = itemSupplierSerial;
    }

    public Double getItemCost() {
        return itemCost;
    }

    public Items itemCost(Double itemCost) {
        this.itemCost = itemCost;
        return this;
    }

    public void setItemCost(Double itemCost) {
        this.itemCost = itemCost;
    }

    public Double getItemSalePrice() {
        return itemSalePrice;
    }

    public Items itemSalePrice(Double itemSalePrice) {
        this.itemSalePrice = itemSalePrice;
        return this;
    }

    public void setItemSalePrice(Double itemSalePrice) {
        this.itemSalePrice = itemSalePrice;
    }

    public LocalDate getOriginalStockDate() {
        return originalStockDate;
    }

    public Items originalStockDate(LocalDate originalStockDate) {
        this.originalStockDate = originalStockDate;
        return this;
    }

    public void setOriginalStockDate(LocalDate originalStockDate) {
        this.originalStockDate = originalStockDate;
    }

    public LocalDate getModifiedStockDate() {
        return modifiedStockDate;
    }

    public Items modifiedStockDate(LocalDate modifiedStockDate) {
        this.modifiedStockDate = modifiedStockDate;
        return this;
    }

    public void setModifiedStockDate(LocalDate modifiedStockDate) {
        this.modifiedStockDate = modifiedStockDate;
    }

    public Desings getRelatedDesign() {
        return relatedDesign;
    }

    public Items relatedDesign(Desings desings) {
        this.relatedDesign = desings;
        return this;
    }

    public void setRelatedDesign(Desings desings) {
        this.relatedDesign = desings;
    }

    public Products getRelatedProduct() {
        return relatedProduct;
    }

    public Items relatedProduct(Products products) {
        this.relatedProduct = products;
        return this;
    }

    public void setRelatedProduct(Products products) {
        this.relatedProduct = products;
    }

    public Location getLocation() {
        return location;
    }

    public Items location(Location location) {
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
        if (!(o instanceof Items)) {
            return false;
        }
        return id != null && id.equals(((Items) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Items{" +
            "id=" + getId() +
            ", itemCode='" + getItemCode() + "'" +
            ", itemName='" + getItemName() + "'" +
            ", itemDescription='" + getItemDescription() + "'" +
            ", itemPrice=" + getItemPrice() +
            ", itemSerial='" + getItemSerial() + "'" +
            ", itemSupplierSerial='" + getItemSupplierSerial() + "'" +
            ", itemCost=" + getItemCost() +
            ", itemSalePrice=" + getItemSalePrice() +
            ", originalStockDate='" + getOriginalStockDate() + "'" +
            ", modifiedStockDate='" + getModifiedStockDate() + "'" +
            "}";
    }
}
