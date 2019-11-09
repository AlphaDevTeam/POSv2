package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Job Details Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Job Details Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "job_detais")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JobDetais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "job_item_price", nullable = false)
    private Double jobItemPrice;

    @NotNull
    @Column(name = "job_item_qty", nullable = false)
    private Integer jobItemQty;

    @ManyToOne
    @JsonIgnoreProperties("jobDetais")
    private Items item;

    @ManyToOne
    @JsonIgnoreProperties("details")
    private Job job;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getJobItemPrice() {
        return jobItemPrice;
    }

    public JobDetais jobItemPrice(Double jobItemPrice) {
        this.jobItemPrice = jobItemPrice;
        return this;
    }

    public void setJobItemPrice(Double jobItemPrice) {
        this.jobItemPrice = jobItemPrice;
    }

    public Integer getJobItemQty() {
        return jobItemQty;
    }

    public JobDetais jobItemQty(Integer jobItemQty) {
        this.jobItemQty = jobItemQty;
        return this;
    }

    public void setJobItemQty(Integer jobItemQty) {
        this.jobItemQty = jobItemQty;
    }

    public Items getItem() {
        return item;
    }

    public JobDetais item(Items items) {
        this.item = items;
        return this;
    }

    public void setItem(Items items) {
        this.item = items;
    }

    public Job getJob() {
        return job;
    }

    public JobDetais job(Job job) {
        this.job = job;
        return this;
    }

    public void setJob(Job job) {
        this.job = job;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobDetais)) {
            return false;
        }
        return id != null && id.equals(((JobDetais) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "JobDetais{" +
            "id=" + getId() +
            ", jobItemPrice=" + getJobItemPrice() +
            ", jobItemQty=" + getJobItemQty() +
            "}";
    }
}
