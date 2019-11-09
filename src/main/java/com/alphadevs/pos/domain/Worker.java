package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Worker Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Worker Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "worker")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Worker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "worker_code", nullable = false)
    private String workerCode;

    @NotNull
    @Column(name = "worker_name", nullable = false)
    private String workerName;

    @Column(name = "worker_limit")
    private Double workerLimit;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties("workers")
    private Location location;

    @ManyToOne
    @JsonIgnoreProperties("assignedTos")
    private Job jobs;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkerCode() {
        return workerCode;
    }

    public Worker workerCode(String workerCode) {
        this.workerCode = workerCode;
        return this;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    public String getWorkerName() {
        return workerName;
    }

    public Worker workerName(String workerName) {
        this.workerName = workerName;
        return this;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public Double getWorkerLimit() {
        return workerLimit;
    }

    public Worker workerLimit(Double workerLimit) {
        this.workerLimit = workerLimit;
        return this;
    }

    public void setWorkerLimit(Double workerLimit) {
        this.workerLimit = workerLimit;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Worker isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Location getLocation() {
        return location;
    }

    public Worker location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Job getJobs() {
        return jobs;
    }

    public Worker jobs(Job job) {
        this.jobs = job;
        return this;
    }

    public void setJobs(Job job) {
        this.jobs = job;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Worker)) {
            return false;
        }
        return id != null && id.equals(((Worker) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Worker{" +
            "id=" + getId() +
            ", workerCode='" + getWorkerCode() + "'" +
            ", workerName='" + getWorkerName() + "'" +
            ", workerLimit=" + getWorkerLimit() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
