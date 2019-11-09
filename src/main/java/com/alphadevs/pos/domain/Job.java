package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Job Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Job Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "job_code", nullable = false)
    private String jobCode;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "job_start_date")
    private LocalDate jobStartDate;

    @Column(name = "job_end_date")
    private LocalDate jobEndDate;

    @Column(name = "job_amount")
    private Double jobAmount;

    @OneToOne
    @JoinColumn(unique = true)
    private JobStatus status;

    @OneToMany(mappedBy = "job")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<JobDetais> details = new HashSet<>();

    @OneToMany(mappedBy = "jobs")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Worker> assignedTos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("jobs")
    private Location location;

    @ManyToOne
    @JsonIgnoreProperties("jobs")
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobCode() {
        return jobCode;
    }

    public Job jobCode(String jobCode) {
        this.jobCode = jobCode;
        return this;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public Job jobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
        return this;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public LocalDate getJobStartDate() {
        return jobStartDate;
    }

    public Job jobStartDate(LocalDate jobStartDate) {
        this.jobStartDate = jobStartDate;
        return this;
    }

    public void setJobStartDate(LocalDate jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public LocalDate getJobEndDate() {
        return jobEndDate;
    }

    public Job jobEndDate(LocalDate jobEndDate) {
        this.jobEndDate = jobEndDate;
        return this;
    }

    public void setJobEndDate(LocalDate jobEndDate) {
        this.jobEndDate = jobEndDate;
    }

    public Double getJobAmount() {
        return jobAmount;
    }

    public Job jobAmount(Double jobAmount) {
        this.jobAmount = jobAmount;
        return this;
    }

    public void setJobAmount(Double jobAmount) {
        this.jobAmount = jobAmount;
    }

    public JobStatus getStatus() {
        return status;
    }

    public Job status(JobStatus jobStatus) {
        this.status = jobStatus;
        return this;
    }

    public void setStatus(JobStatus jobStatus) {
        this.status = jobStatus;
    }

    public Set<JobDetais> getDetails() {
        return details;
    }

    public Job details(Set<JobDetais> jobDetais) {
        this.details = jobDetais;
        return this;
    }

    public Job addDetails(JobDetais jobDetais) {
        this.details.add(jobDetais);
        jobDetais.setJob(this);
        return this;
    }

    public Job removeDetails(JobDetais jobDetais) {
        this.details.remove(jobDetais);
        jobDetais.setJob(null);
        return this;
    }

    public void setDetails(Set<JobDetais> jobDetais) {
        this.details = jobDetais;
    }

    public Set<Worker> getAssignedTos() {
        return assignedTos;
    }

    public Job assignedTos(Set<Worker> workers) {
        this.assignedTos = workers;
        return this;
    }

    public Job addAssignedTo(Worker worker) {
        this.assignedTos.add(worker);
        worker.setJobs(this);
        return this;
    }

    public Job removeAssignedTo(Worker worker) {
        this.assignedTos.remove(worker);
        worker.setJobs(null);
        return this;
    }

    public void setAssignedTos(Set<Worker> workers) {
        this.assignedTos = workers;
    }

    public Location getLocation() {
        return location;
    }

    public Job location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Job customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }
        return id != null && id.equals(((Job) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", jobCode='" + getJobCode() + "'" +
            ", jobDescription='" + getJobDescription() + "'" +
            ", jobStartDate='" + getJobStartDate() + "'" +
            ", jobEndDate='" + getJobEndDate() + "'" +
            ", jobAmount=" + getJobAmount() +
            "}";
    }
}
