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
 * Criteria class for the {@link com.alphadevs.pos.domain.CustomerAccount} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.CustomerAccountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customer-accounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerAccountCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter transactionDate;

    private StringFilter transactionDescription;

    private DoubleFilter transactionAmountDR;

    private DoubleFilter transactionAmountCR;

    private DoubleFilter transactionBalance;

    private LongFilter locationId;

    private LongFilter transactionTypeId;

    private LongFilter customerId;

    public CustomerAccountCriteria(){
    }

    public CustomerAccountCriteria(CustomerAccountCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.transactionDescription = other.transactionDescription == null ? null : other.transactionDescription.copy();
        this.transactionAmountDR = other.transactionAmountDR == null ? null : other.transactionAmountDR.copy();
        this.transactionAmountCR = other.transactionAmountCR == null ? null : other.transactionAmountCR.copy();
        this.transactionBalance = other.transactionBalance == null ? null : other.transactionBalance.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.transactionTypeId = other.transactionTypeId == null ? null : other.transactionTypeId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
    }

    @Override
    public CustomerAccountCriteria copy() {
        return new CustomerAccountCriteria(this);
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

    public DoubleFilter getTransactionAmountDR() {
        return transactionAmountDR;
    }

    public void setTransactionAmountDR(DoubleFilter transactionAmountDR) {
        this.transactionAmountDR = transactionAmountDR;
    }

    public DoubleFilter getTransactionAmountCR() {
        return transactionAmountCR;
    }

    public void setTransactionAmountCR(DoubleFilter transactionAmountCR) {
        this.transactionAmountCR = transactionAmountCR;
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

    public LongFilter getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(LongFilter transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerAccountCriteria that = (CustomerAccountCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(transactionDescription, that.transactionDescription) &&
            Objects.equals(transactionAmountDR, that.transactionAmountDR) &&
            Objects.equals(transactionAmountCR, that.transactionAmountCR) &&
            Objects.equals(transactionBalance, that.transactionBalance) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(transactionTypeId, that.transactionTypeId) &&
            Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        transactionDate,
        transactionDescription,
        transactionAmountDR,
        transactionAmountCR,
        transactionBalance,
        locationId,
        transactionTypeId,
        customerId
        );
    }

    @Override
    public String toString() {
        return "CustomerAccountCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
                (transactionDescription != null ? "transactionDescription=" + transactionDescription + ", " : "") +
                (transactionAmountDR != null ? "transactionAmountDR=" + transactionAmountDR + ", " : "") +
                (transactionAmountCR != null ? "transactionAmountCR=" + transactionAmountCR + ", " : "") +
                (transactionBalance != null ? "transactionBalance=" + transactionBalance + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (transactionTypeId != null ? "transactionTypeId=" + transactionTypeId + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
            "}";
    }

}
