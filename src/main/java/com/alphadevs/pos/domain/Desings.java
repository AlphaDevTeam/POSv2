package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Desings Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Desings Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "desings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Desings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "desing_code", nullable = false)
    private String desingCode;

    @NotNull
    @Column(name = "desing_name", nullable = false)
    private String desingName;

    @NotNull
    @Column(name = "desing_prefix", nullable = false)
    private String desingPrefix;

    @NotNull
    @Column(name = "desing_prof_margin", nullable = false)
    private Double desingProfMargin;

    @ManyToOne
    @JsonIgnoreProperties("desings")
    private Products relatedProduct;

    @ManyToOne
    @JsonIgnoreProperties("desings")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesingCode() {
        return desingCode;
    }

    public Desings desingCode(String desingCode) {
        this.desingCode = desingCode;
        return this;
    }

    public void setDesingCode(String desingCode) {
        this.desingCode = desingCode;
    }

    public String getDesingName() {
        return desingName;
    }

    public Desings desingName(String desingName) {
        this.desingName = desingName;
        return this;
    }

    public void setDesingName(String desingName) {
        this.desingName = desingName;
    }

    public String getDesingPrefix() {
        return desingPrefix;
    }

    public Desings desingPrefix(String desingPrefix) {
        this.desingPrefix = desingPrefix;
        return this;
    }

    public void setDesingPrefix(String desingPrefix) {
        this.desingPrefix = desingPrefix;
    }

    public Double getDesingProfMargin() {
        return desingProfMargin;
    }

    public Desings desingProfMargin(Double desingProfMargin) {
        this.desingProfMargin = desingProfMargin;
        return this;
    }

    public void setDesingProfMargin(Double desingProfMargin) {
        this.desingProfMargin = desingProfMargin;
    }

    public Products getRelatedProduct() {
        return relatedProduct;
    }

    public Desings relatedProduct(Products products) {
        this.relatedProduct = products;
        return this;
    }

    public void setRelatedProduct(Products products) {
        this.relatedProduct = products;
    }

    public Location getLocation() {
        return location;
    }

    public Desings location(Location location) {
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
        if (!(o instanceof Desings)) {
            return false;
        }
        return id != null && id.equals(((Desings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Desings{" +
            "id=" + getId() +
            ", desingCode='" + getDesingCode() + "'" +
            ", desingName='" + getDesingName() + "'" +
            ", desingPrefix='" + getDesingPrefix() + "'" +
            ", desingProfMargin=" + getDesingProfMargin() +
            "}";
    }
}
