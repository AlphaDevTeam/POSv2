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
 * Criteria class for the {@link com.alphadevs.pos.domain.PurchaseOrderDetails} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.PurchaseOrderDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /purchase-order-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PurchaseOrderDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter itemQty;

    private LongFilter itemId;

    private LongFilter poId;

    public PurchaseOrderDetailsCriteria(){
    }

    public PurchaseOrderDetailsCriteria(PurchaseOrderDetailsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.itemQty = other.itemQty == null ? null : other.itemQty.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
        this.poId = other.poId == null ? null : other.poId.copy();
    }

    @Override
    public PurchaseOrderDetailsCriteria copy() {
        return new PurchaseOrderDetailsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getItemQty() {
        return itemQty;
    }

    public void setItemQty(IntegerFilter itemQty) {
        this.itemQty = itemQty;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }

    public LongFilter getPoId() {
        return poId;
    }

    public void setPoId(LongFilter poId) {
        this.poId = poId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PurchaseOrderDetailsCriteria that = (PurchaseOrderDetailsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(itemQty, that.itemQty) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(poId, that.poId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        itemQty,
        itemId,
        poId
        );
    }

    @Override
    public String toString() {
        return "PurchaseOrderDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (itemQty != null ? "itemQty=" + itemQty + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
                (poId != null ? "poId=" + poId + ", " : "") +
            "}";
    }

}
