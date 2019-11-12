package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Designs Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Designs Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "designs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Designs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "design_code", nullable = false)
    private String designCode;

    @NotNull
    @Column(name = "design_name", nullable = false)
    private String designName;

    @NotNull
    @Column(name = "design_prefix", nullable = false)
    private String designPrefix;

    @NotNull
    @Column(name = "design_prof_margin", nullable = false)
    private Double designProfMargin;

    @ManyToOne
    @JsonIgnoreProperties("designs")
    private Products relatedProduct;

    @ManyToOne
    @JsonIgnoreProperties("designs")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignCode() {
        return designCode;
    }

    public Designs designCode(String designCode) {
        this.designCode = designCode;
        return this;
    }

    public void setDesignCode(String designCode) {
        this.designCode = designCode;
    }

    public String getDesignName() {
        return designName;
    }

    public Designs designName(String designName) {
        this.designName = designName;
        return this;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public String getDesignPrefix() {
        return designPrefix;
    }

    public Designs designPrefix(String designPrefix) {
        this.designPrefix = designPrefix;
        return this;
    }

    public void setDesignPrefix(String designPrefix) {
        this.designPrefix = designPrefix;
    }

    public Double getDesignProfMargin() {
        return designProfMargin;
    }

    public Designs designProfMargin(Double designProfMargin) {
        this.designProfMargin = designProfMargin;
        return this;
    }

    public void setDesignProfMargin(Double designProfMargin) {
        this.designProfMargin = designProfMargin;
    }

    public Products getRelatedProduct() {
        return relatedProduct;
    }

    public Designs relatedProduct(Products products) {
        this.relatedProduct = products;
        return this;
    }

    public void setRelatedProduct(Products products) {
        this.relatedProduct = products;
    }

    public Location getLocation() {
        return location;
    }

    public Designs location(Location location) {
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
        if (!(o instanceof Designs)) {
            return false;
        }
        return id != null && id.equals(((Designs) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Designs{" +
            "id=" + getId() +
            ", designCode='" + getDesignCode() + "'" +
            ", designName='" + getDesignName() + "'" +
            ", designPrefix='" + getDesignPrefix() + "'" +
            ", designProfMargin=" + getDesignProfMargin() +
            "}";
    }
}
