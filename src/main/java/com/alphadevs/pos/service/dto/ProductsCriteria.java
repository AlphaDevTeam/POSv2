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
 * Criteria class for the {@link com.alphadevs.pos.domain.Products} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.ProductsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter productCode;

    private StringFilter productName;

    private StringFilter productPrefix;

    private DoubleFilter productProfMargin;

    private LongFilter locationId;

    public ProductsCriteria(){
    }

    public ProductsCriteria(ProductsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.productCode = other.productCode == null ? null : other.productCode.copy();
        this.productName = other.productName == null ? null : other.productName.copy();
        this.productPrefix = other.productPrefix == null ? null : other.productPrefix.copy();
        this.productProfMargin = other.productProfMargin == null ? null : other.productProfMargin.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public ProductsCriteria copy() {
        return new ProductsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProductCode() {
        return productCode;
    }

    public void setProductCode(StringFilter productCode) {
        this.productCode = productCode;
    }

    public StringFilter getProductName() {
        return productName;
    }

    public void setProductName(StringFilter productName) {
        this.productName = productName;
    }

    public StringFilter getProductPrefix() {
        return productPrefix;
    }

    public void setProductPrefix(StringFilter productPrefix) {
        this.productPrefix = productPrefix;
    }

    public DoubleFilter getProductProfMargin() {
        return productProfMargin;
    }

    public void setProductProfMargin(DoubleFilter productProfMargin) {
        this.productProfMargin = productProfMargin;
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
        final ProductsCriteria that = (ProductsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(productCode, that.productCode) &&
            Objects.equals(productName, that.productName) &&
            Objects.equals(productPrefix, that.productPrefix) &&
            Objects.equals(productProfMargin, that.productProfMargin) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        productCode,
        productName,
        productPrefix,
        productProfMargin,
        locationId
        );
    }

    @Override
    public String toString() {
        return "ProductsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (productCode != null ? "productCode=" + productCode + ", " : "") +
                (productName != null ? "productName=" + productName + ", " : "") +
                (productPrefix != null ? "productPrefix=" + productPrefix + ", " : "") +
                (productProfMargin != null ? "productProfMargin=" + productProfMargin + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
