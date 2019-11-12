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
 * Criteria class for the {@link com.alphadevs.pos.domain.ItemBinCard} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.ItemBinCardResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /item-bin-cards?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ItemBinCardCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter transactionDate;

    private StringFilter transactionDescription;

    private DoubleFilter transactionQty;

    private DoubleFilter transactionBalance;

    private LongFilter locationId;

    private LongFilter itemId;

    public ItemBinCardCriteria(){
    }

    public ItemBinCardCriteria(ItemBinCardCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.transactionDescription = other.transactionDescription == null ? null : other.transactionDescription.copy();
        this.transactionQty = other.transactionQty == null ? null : other.transactionQty.copy();
        this.transactionBalance = other.transactionBalance == null ? null : other.transactionBalance.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
    }

    @Override
    public ItemBinCardCriteria copy() {
        return new ItemBinCardCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public StringFilter getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(StringFilter transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public DoubleFilter getTransactionQty() {
        return transactionQty;
    }

    public void setTransactionQty(DoubleFilter transactionQty) {
        this.transactionQty = transactionQty;
    }

    public DoubleFilter getTransactionBalance() {
        return transactionBalance;
    }

    public void setTransactionBalance(DoubleFilter transactionBalance) {
        this.transactionBalance = transactionBalance;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ItemBinCardCriteria that = (ItemBinCardCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(transactionDescription, that.transactionDescription) &&
            Objects.equals(transactionQty, that.transactionQty) &&
            Objects.equals(transactionBalance, that.transactionBalance) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        transactionDate,
        transactionDescription,
        transactionQty,
        transactionBalance,
        locationId,
        itemId
        );
    }

    @Override
    public String toString() {
        return "ItemBinCardCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
                (transactionDescription != null ? "transactionDescription=" + transactionDescription + ", " : "") +
                (transactionQty != null ? "transactionQty=" + transactionQty + ", " : "") +
                (transactionBalance != null ? "transactionBalance=" + transactionBalance + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
            "}";
    }

}
