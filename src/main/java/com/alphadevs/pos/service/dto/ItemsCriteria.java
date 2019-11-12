package com.alphadevs.pos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.alphadevs.pos.domain.Items} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.ItemsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ItemsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter itemCode;

    private StringFilter itemName;

    private StringFilter itemDescription;

    private DoubleFilter itemPrice;

    private StringFilter itemSerial;

    private StringFilter itemSupplierSerial;

    private DoubleFilter itemCost;

    private DoubleFilter itemSalePrice;

    private LocalDateFilter originalStockDate;

    private LocalDateFilter modifiedStockDate;

    private LongFilter relatedDesignId;

    private LongFilter relatedProductId;

    private LongFilter locationId;

    public ItemsCriteria(){
    }

    public ItemsCriteria(ItemsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.itemCode = other.itemCode == null ? null : other.itemCode.copy();
        this.itemName = other.itemName == null ? null : other.itemName.copy();
        this.itemDescription = other.itemDescription == null ? null : other.itemDescription.copy();
        this.itemPrice = other.itemPrice == null ? null : other.itemPrice.copy();
        this.itemSerial = other.itemSerial == null ? null : other.itemSerial.copy();
        this.itemSupplierSerial = other.itemSupplierSerial == null ? null : other.itemSupplierSerial.copy();
        this.itemCost = other.itemCost == null ? null : other.itemCost.copy();
        this.itemSalePrice = other.itemSalePrice == null ? null : other.itemSalePrice.copy();
        this.originalStockDate = other.originalStockDate == null ? null : other.originalStockDate.copy();
        this.modifiedStockDate = other.modifiedStockDate == null ? null : other.modifiedStockDate.copy();
        this.relatedDesignId = other.relatedDesignId == null ? null : other.relatedDesignId.copy();
        this.relatedProductId = other.relatedProductId == null ? null : other.relatedProductId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public ItemsCriteria copy() {
        return new ItemsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getItemCode() {
        return itemCode;
    }

    public void setItemCode(StringFilter itemCode) {
        this.itemCode = itemCode;
    }

    public StringFilter getItemName() {
        return itemName;
    }

    public void setItemName(StringFilter itemName) {
        this.itemName = itemName;
    }

    public StringFilter getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(StringFilter itemDescription) {
        this.itemDescription = itemDescription;
    }

    public DoubleFilter getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(DoubleFilter itemPrice) {
        this.itemPrice = itemPrice;
    }

    public StringFilter getItemSerial() {
        return itemSerial;
    }

    public void setItemSerial(StringFilter itemSerial) {
        this.itemSerial = itemSerial;
    }

    public StringFilter getItemSupplierSerial() {
        return itemSupplierSerial;
    }

    public void setItemSupplierSerial(StringFilter itemSupplierSerial) {
        this.itemSupplierSerial = itemSupplierSerial;
    }

    public DoubleFilter getItemCost() {
        return itemCost;
    }

    public void setItemCost(DoubleFilter itemCost) {
        this.itemCost = itemCost;
    }

    public DoubleFilter getItemSalePrice() {
        return itemSalePrice;
    }

    public void setItemSalePrice(DoubleFilter itemSalePrice) {
        this.itemSalePrice = itemSalePrice;
    }

    public LocalDateFilter getOriginalStockDate() {
        return originalStockDate;
    }

    public void setOriginalStockDate(LocalDateFilter originalStockDate) {
        this.originalStockDate = originalStockDate;
    }

    public LocalDateFilter getModifiedStockDate() {
        return modifiedStockDate;
    }

    public void setModifiedStockDate(LocalDateFilter modifiedStockDate) {
        this.modifiedStockDate = modifiedStockDate;
    }

    public LongFilter getRelatedDesignId() {
        return relatedDesignId;
    }

    public void setRelatedDesignId(LongFilter relatedDesignId) {
        this.relatedDesignId = relatedDesignId;
    }

    public LongFilter getRelatedProductId() {
        return relatedProductId;
    }

    public void setRelatedProductId(LongFilter relatedProductId) {
        this.relatedProductId = relatedProductId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ItemsCriteria that = (ItemsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(itemCode, that.itemCode) &&
            Objects.equals(itemName, that.itemName) &&
            Objects.equals(itemDescription, that.itemDescription) &&
            Objects.equals(itemPrice, that.itemPrice) &&
            Objects.equals(itemSerial, that.itemSerial) &&
            Objects.equals(itemSupplierSerial, that.itemSupplierSerial) &&
            Objects.equals(itemCost, that.itemCost) &&
            Objects.equals(itemSalePrice, that.itemSalePrice) &&
            Objects.equals(originalStockDate, that.originalStockDate) &&
            Objects.equals(modifiedStockDate, that.modifiedStockDate) &&
            Objects.equals(relatedDesignId, that.relatedDesignId) &&
            Objects.equals(relatedProductId, that.relatedProductId) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        itemCode,
        itemName,
        itemDescription,
        itemPrice,
        itemSerial,
        itemSupplierSerial,
        itemCost,
        itemSalePrice,
        originalStockDate,
        modifiedStockDate,
        relatedDesignId,
        relatedProductId,
        locationId
        );
    }

    @Override
    public String toString() {
        return "ItemsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (itemCode != null ? "itemCode=" + itemCode + ", " : "") +
                (itemName != null ? "itemName=" + itemName + ", " : "") +
                (itemDescription != null ? "itemDescription=" + itemDescription + ", " : "") +
                (itemPrice != null ? "itemPrice=" + itemPrice + ", " : "") +
                (itemSerial != null ? "itemSerial=" + itemSerial + ", " : "") +
                (itemSupplierSerial != null ? "itemSupplierSerial=" + itemSupplierSerial + ", " : "") +
                (itemCost != null ? "itemCost=" + itemCost + ", " : "") +
                (itemSalePrice != null ? "itemSalePrice=" + itemSalePrice + ", " : "") +
                (originalStockDate != null ? "originalStockDate=" + originalStockDate + ", " : "") +
                (modifiedStockDate != null ? "modifiedStockDate=" + modifiedStockDate + ", " : "") +
                (relatedDesignId != null ? "relatedDesignId=" + relatedDesignId + ", " : "") +
                (relatedProductId != null ? "relatedProductId=" + relatedProductId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
