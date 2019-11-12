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
 * Criteria class for the {@link com.alphadevs.pos.domain.SalesAccountBalance} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.SalesAccountBalanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sales-account-balances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SalesAccountBalanceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter balance;

    private LongFilter locationId;

    public SalesAccountBalanceCriteria(){
    }

    public SalesAccountBalanceCriteria(SalesAccountBalanceCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.balance = other.balance == null ? null : other.balance.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public SalesAccountBalanceCriteria copy() {
        return new SalesAccountBalanceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getBalance() {
        return balance;
    }

    public void setBalance(DoubleFilter balance) {
        this.balance = balance;
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
        final SalesAccountBalanceCriteria that = (SalesAccountBalanceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(balance, that.balance) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        balance,
        locationId
        );
    }

    @Override
    public String toString() {
        return "SalesAccountBalanceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (balance != null ? "balance=" + balance + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
