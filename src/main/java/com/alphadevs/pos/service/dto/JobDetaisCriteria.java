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
 * Criteria class for the {@link com.alphadevs.pos.domain.JobDetais} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.JobDetaisResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /job-detais?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobDetaisCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter jobItemPrice;

    private IntegerFilter jobItemQty;

    private LongFilter itemId;

    private LongFilter jobId;

    public JobDetaisCriteria(){
    }

    public JobDetaisCriteria(JobDetaisCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.jobItemPrice = other.jobItemPrice == null ? null : other.jobItemPrice.copy();
        this.jobItemQty = other.jobItemQty == null ? null : other.jobItemQty.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
        this.jobId = other.jobId == null ? null : other.jobId.copy();
    }

    @Override
    public JobDetaisCriteria copy() {
        return new JobDetaisCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getJobItemPrice() {
        return jobItemPrice;
    }

    public void setJobItemPrice(DoubleFilter jobItemPrice) {
        this.jobItemPrice = jobItemPrice;
    }

    public IntegerFilter getJobItemQty() {
        return jobItemQty;
    }

    public void setJobItemQty(IntegerFilter jobItemQty) {
        this.jobItemQty = jobItemQty;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }

    public LongFilter getJobId() {
        return jobId;
    }

    public void setJobId(LongFilter jobId) {
        this.jobId = jobId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JobDetaisCriteria that = (JobDetaisCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(jobItemPrice, that.jobItemPrice) &&
            Objects.equals(jobItemQty, that.jobItemQty) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        jobItemPrice,
        jobItemQty,
        itemId,
        jobId
        );
    }

    @Override
    public String toString() {
        return "JobDetaisCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (jobItemPrice != null ? "jobItemPrice=" + jobItemPrice + ", " : "") +
                (jobItemQty != null ? "jobItemQty=" + jobItemQty + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
                (jobId != null ? "jobId=" + jobId + ", " : "") +
            "}";
    }

}
