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
 * Criteria class for the {@link com.alphadevs.pos.domain.JobStatus} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.JobStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /job-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobStatusCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter jobStatusCode;

    private StringFilter jobStatusDescription;

    private BooleanFilter isActive;

    private LongFilter locationId;

    public JobStatusCriteria(){
    }

    public JobStatusCriteria(JobStatusCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.jobStatusCode = other.jobStatusCode == null ? null : other.jobStatusCode.copy();
        this.jobStatusDescription = other.jobStatusDescription == null ? null : other.jobStatusDescription.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public JobStatusCriteria copy() {
        return new JobStatusCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getJobStatusCode() {
        return jobStatusCode;
    }

    public void setJobStatusCode(StringFilter jobStatusCode) {
        this.jobStatusCode = jobStatusCode;
    }

    public StringFilter getJobStatusDescription() {
        return jobStatusDescription;
    }

    public void setJobStatusDescription(StringFilter jobStatusDescription) {
        this.jobStatusDescription = jobStatusDescription;
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
        final JobStatusCriteria that = (JobStatusCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(jobStatusCode, that.jobStatusCode) &&
            Objects.equals(jobStatusDescription, that.jobStatusDescription) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        jobStatusCode,
        jobStatusDescription,
        isActive,
        locationId
        );
    }

    @Override
    public String toString() {
        return "JobStatusCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (jobStatusCode != null ? "jobStatusCode=" + jobStatusCode + ", " : "") +
                (jobStatusDescription != null ? "jobStatusDescription=" + jobStatusDescription + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
