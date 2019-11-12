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

/**
 * Criteria class for the {@link com.alphadevs.pos.domain.Stock} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.StockResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stocks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter stockQty;

    private LongFilter itemId;

    private LongFilter locationId;

    private LongFilter companyId;

    public StockCriteria(){
    }

    public StockCriteria(StockCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.stockQty = other.stockQty == null ? null : other.stockQty.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
    }

    @Override
    public StockCriteria copy() {
        return new StockCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getStockQty() {
        return stockQty;
    }

    public void setStockQty(DoubleFilter stockQty) {
        this.stockQty = stockQty;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StockCriteria that = (StockCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(stockQty, that.stockQty) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(companyId, that.companyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        stockQty,
        itemId,
        locationId,
        companyId
        );
    }

    @Override
    public String toString() {
        return "StockCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (stockQty != null ? "stockQty=" + stockQty + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
            "}";
    }

}
