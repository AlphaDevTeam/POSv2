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
 * Criteria class for the {@link com.alphadevs.pos.domain.Job} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.JobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter jobCode;

    private StringFilter jobDescription;

    private LocalDateFilter jobStartDate;

    private LocalDateFilter jobEndDate;

    private DoubleFilter jobAmount;

    private LongFilter statusId;

    private LongFilter detailsId;

    private LongFilter assignedToId;

    private LongFilter locationId;

    private LongFilter customerId;

    public JobCriteria(){
    }

    public JobCriteria(JobCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.jobCode = other.jobCode == null ? null : other.jobCode.copy();
        this.jobDescription = other.jobDescription == null ? null : other.jobDescription.copy();
        this.jobStartDate = other.jobStartDate == null ? null : other.jobStartDate.copy();
        this.jobEndDate = other.jobEndDate == null ? null : other.jobEndDate.copy();
        this.jobAmount = other.jobAmount == null ? null : other.jobAmount.copy();
        this.statusId = other.statusId == null ? null : other.statusId.copy();
        this.detailsId = other.detailsId == null ? null : other.detailsId.copy();
        this.assignedToId = other.assignedToId == null ? null : other.assignedToId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
    }

    @Override
    public JobCriteria copy() {
        return new JobCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getJobCode() {
        return jobCode;
    }

    public void setJobCode(StringFilter jobCode) {
        this.jobCode = jobCode;
    }

    public StringFilter getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(StringFilter jobDescription) {
        this.jobDescription = jobDescription;
    }

    public LocalDateFilter getJobStartDate() {
        return jobStartDate;
    }

    public void setJobStartDate(LocalDateFilter jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public LocalDateFilter getJobEndDate() {
        return jobEndDate;
    }

    public void setJobEndDate(LocalDateFilter jobEndDate) {
        this.jobEndDate = jobEndDate;
    }

    public DoubleFilter getJobAmount() {
        return jobAmount;
    }

    public void setJobAmount(DoubleFilter jobAmount) {
        this.jobAmount = jobAmount;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
    }

    public LongFilter getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(LongFilter detailsId) {
        this.detailsId = detailsId;
    }

    public LongFilter getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(LongFilter assignedToId) {
        this.assignedToId = assignedToId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
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
        final JobCriteria that = (JobCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(jobCode, that.jobCode) &&
            Objects.equals(jobDescription, that.jobDescription) &&
            Objects.equals(jobStartDate, that.jobStartDate) &&
            Objects.equals(jobEndDate, that.jobEndDate) &&
            Objects.equals(jobAmount, that.jobAmount) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(detailsId, that.detailsId) &&
            Objects.equals(assignedToId, that.assignedToId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        jobCode,
        jobDescription,
        jobStartDate,
        jobEndDate,
        jobAmount,
        statusId,
        detailsId,
        assignedToId,
        locationId,
        customerId
        );
    }

    @Override
    public String toString() {
        return "JobCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (jobCode != null ? "jobCode=" + jobCode + ", " : "") +
                (jobDescription != null ? "jobDescription=" + jobDescription + ", " : "") +
                (jobStartDate != null ? "jobStartDate=" + jobStartDate + ", " : "") +
                (jobEndDate != null ? "jobEndDate=" + jobEndDate + ", " : "") +
                (jobAmount != null ? "jobAmount=" + jobAmount + ", " : "") +
                (statusId != null ? "statusId=" + statusId + ", " : "") +
                (detailsId != null ? "detailsId=" + detailsId + ", " : "") +
                (assignedToId != null ? "assignedToId=" + assignedToId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
            "}";
    }

}
