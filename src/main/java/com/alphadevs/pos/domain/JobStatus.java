package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Job Status Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Job Status Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "job_status")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JobStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "job_status_code", nullable = false)
    private String jobStatusCode;

    @Column(name = "job_status_description")
    private String jobStatusDescription;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties("jobStatuses")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobStatusCode() {
        return jobStatusCode;
    }

    public JobStatus jobStatusCode(String jobStatusCode) {
        this.jobStatusCode = jobStatusCode;
        return this;
    }

    public void setJobStatusCode(String jobStatusCode) {
        this.jobStatusCode = jobStatusCode;
    }

    public String getJobStatusDescription() {
        return jobStatusDescription;
    }

    public JobStatus jobStatusDescription(String jobStatusDescription) {
        this.jobStatusDescription = jobStatusDescription;
        return this;
    }

    public void setJobStatusDescription(String jobStatusDescription) {
        this.jobStatusDescription = jobStatusDescription;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public JobStatus isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Location getLocation() {
        return location;
    }

    public JobStatus location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobStatus)) {
            return false;
        }
        return id != null && id.equals(((JobStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "JobStatus{" +
            "id=" + getId() +
            ", jobStatusCode='" + getJobStatusCode() + "'" +
            ", jobStatusDescription='" + getJobStatusDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
