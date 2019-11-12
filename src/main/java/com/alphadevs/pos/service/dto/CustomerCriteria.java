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
 * Criteria class for the {@link com.alphadevs.pos.domain.Customer} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter customerCode;

    private StringFilter customerName;

    private DoubleFilter customerLimit;

    private BooleanFilter isActive;

    private LongFilter locationId;

    public CustomerCriteria(){
    }

    public CustomerCriteria(CustomerCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.customerCode = other.customerCode == null ? null : other.customerCode.copy();
        this.customerName = other.customerName == null ? null : other.customerName.copy();
        this.customerLimit = other.customerLimit == null ? null : other.customerLimit.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(StringFilter customerCode) {
        this.customerCode = customerCode;
    }

    public StringFilter getCustomerName() {
        return customerName;
    }

    public void setCustomerName(StringFilter customerName) {
        this.customerName = customerName;
    }

    public DoubleFilter getCustomerLimit() {
        return customerLimit;
    }

    public void setCustomerLimit(DoubleFilter customerLimit) {
        this.customerLimit = customerLimit;
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
        final CustomerCriteria that = (CustomerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(customerCode, that.customerCode) &&
            Objects.equals(customerName, that.customerName) &&
            Objects.equals(customerLimit, that.customerLimit) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        customerCode,
        customerName,
        customerLimit,
        isActive,
        locationId
        );
    }

    @Override
    public String toString() {
        return "CustomerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (customerCode != null ? "customerCode=" + customerCode + ", " : "") +
                (customerName != null ? "customerName=" + customerName + ", " : "") +
                (customerLimit != null ? "customerLimit=" + customerLimit + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
