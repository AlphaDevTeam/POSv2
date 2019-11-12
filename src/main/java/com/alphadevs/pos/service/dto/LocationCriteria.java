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
 * Criteria class for the {@link com.alphadevs.pos.domain.Location} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.LocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter locationCode;

    private StringFilter locationName;

    private DoubleFilter locationProfMargin;

    private BooleanFilter isActive;

    private LongFilter companyId;

    private LongFilter usersId;

    public LocationCriteria(){
    }

    public LocationCriteria(LocationCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.locationCode = other.locationCode == null ? null : other.locationCode.copy();
        this.locationName = other.locationName == null ? null : other.locationName.copy();
        this.locationProfMargin = other.locationProfMargin == null ? null : other.locationProfMargin.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
    }

    @Override
    public LocationCriteria copy() {
        return new LocationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(StringFilter locationCode) {
        this.locationCode = locationCode;
    }

    public StringFilter getLocationName() {
        return locationName;
    }

    public void setLocationName(StringFilter locationName) {
        this.locationName = locationName;
    }

    public DoubleFilter getLocationProfMargin() {
        return locationProfMargin;
    }

    public void setLocationProfMargin(DoubleFilter locationProfMargin) {
        this.locationProfMargin = locationProfMargin;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LocationCriteria that = (LocationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(locationCode, that.locationCode) &&
            Objects.equals(locationName, that.locationName) &&
            Objects.equals(locationProfMargin, that.locationProfMargin) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(usersId, that.usersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        locationCode,
        locationName,
        locationProfMargin,
        isActive,
        companyId,
        usersId
        );
    }

    @Override
    public String toString() {
        return "LocationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (locationCode != null ? "locationCode=" + locationCode + ", " : "") +
                (locationName != null ? "locationName=" + locationName + ", " : "") +
                (locationProfMargin != null ? "locationProfMargin=" + locationProfMargin + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
            "}";
    }

}
