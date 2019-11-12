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
 * Criteria class for the {@link com.alphadevs.pos.domain.Designs} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.DesignsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /designs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DesignsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter designCode;

    private StringFilter designName;

    private StringFilter designPrefix;

    private DoubleFilter designProfMargin;

    private LongFilter relatedProductId;

    private LongFilter locationId;

    public DesignsCriteria(){
    }

    public DesignsCriteria(DesignsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.designCode = other.designCode == null ? null : other.designCode.copy();
        this.designName = other.designName == null ? null : other.designName.copy();
        this.designPrefix = other.designPrefix == null ? null : other.designPrefix.copy();
        this.designProfMargin = other.designProfMargin == null ? null : other.designProfMargin.copy();
        this.relatedProductId = other.relatedProductId == null ? null : other.relatedProductId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public DesignsCriteria copy() {
        return new DesignsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDesignCode() {
        return designCode;
    }

    public void setDesignCode(StringFilter designCode) {
        this.designCode = designCode;
    }

    public StringFilter getDesignName() {
        return designName;
    }

    public void setDesignName(StringFilter designName) {
        this.designName = designName;
    }

    public StringFilter getDesignPrefix() {
        return designPrefix;
    }

    public void setDesignPrefix(StringFilter designPrefix) {
        this.designPrefix = designPrefix;
    }

    public DoubleFilter getDesignProfMargin() {
        return designProfMargin;
    }

    public void setDesignProfMargin(DoubleFilter designProfMargin) {
        this.designProfMargin = designProfMargin;
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
        final DesignsCriteria that = (DesignsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(designCode, that.designCode) &&
            Objects.equals(designName, that.designName) &&
            Objects.equals(designPrefix, that.designPrefix) &&
            Objects.equals(designProfMargin, that.designProfMargin) &&
            Objects.equals(relatedProductId, that.relatedProductId) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        designCode,
        designName,
        designPrefix,
        designProfMargin,
        relatedProductId,
        locationId
        );
    }

    @Override
    public String toString() {
        return "DesignsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (designCode != null ? "designCode=" + designCode + ", " : "") +
                (designName != null ? "designName=" + designName + ", " : "") +
                (designPrefix != null ? "designPrefix=" + designPrefix + ", " : "") +
                (designProfMargin != null ? "designProfMargin=" + designProfMargin + ", " : "") +
                (relatedProductId != null ? "relatedProductId=" + relatedProductId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
