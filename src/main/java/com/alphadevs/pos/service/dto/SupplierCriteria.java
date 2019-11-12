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
 * Criteria class for the {@link com.alphadevs.pos.domain.Supplier} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.SupplierResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /suppliers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SupplierCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter supplierCode;

    private StringFilter supplierName;

    private DoubleFilter supplierLimit;

    private BooleanFilter isActive;

    private LongFilter locationId;

    public SupplierCriteria(){
    }

    public SupplierCriteria(SupplierCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.supplierCode = other.supplierCode == null ? null : other.supplierCode.copy();
        this.supplierName = other.supplierName == null ? null : other.supplierName.copy();
        this.supplierLimit = other.supplierLimit == null ? null : other.supplierLimit.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public SupplierCriteria copy() {
        return new SupplierCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(StringFilter supplierCode) {
        this.supplierCode = supplierCode;
    }

    public StringFilter getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(StringFilter supplierName) {
        this.supplierName = supplierName;
    }

    public DoubleFilter getSupplierLimit() {
        return supplierLimit;
    }

    public void setSupplierLimit(DoubleFilter supplierLimit) {
        this.supplierLimit = supplierLimit;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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
        final SupplierCriteria that = (SupplierCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(supplierCode, that.supplierCode) &&
            Objects.equals(supplierName, that.supplierName) &&
            Objects.equals(supplierLimit, that.supplierLimit) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        supplierCode,
        supplierName,
        supplierLimit,
        isActive,
        locationId
        );
    }

    @Override
    public String toString() {
        return "SupplierCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (supplierCode != null ? "supplierCode=" + supplierCode + ", " : "") +
                (supplierName != null ? "supplierName=" + supplierName + ", " : "") +
                (supplierLimit != null ? "supplierLimit=" + supplierLimit + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
