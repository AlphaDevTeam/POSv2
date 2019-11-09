package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * PurchaseOrderDetails Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "PurchaseOrderDetails Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "purchase_order_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PurchaseOrderDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_qty", nullable = false)
    private Integer itemQty;

    @ManyToOne
    @JsonIgnoreProperties("purchaseOrderDetails")
    private Items item;

    @ManyToOne
    @JsonIgnoreProperties("details")
    private PurchaseOrder po;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getItemQty() {
        return itemQty;
    }

    public PurchaseOrderDetails itemQty(Integer itemQty) {
        this.itemQty = itemQty;
        return this;
    }

    public void setItemQty(Integer itemQty) {
        this.itemQty = itemQty;
    }

    public Items getItem() {
        return item;
    }

    public PurchaseOrderDetails item(Items items) {
        this.item = items;
        return this;
    }

    public void setItem(Items items) {
        this.item = items;
    }

    public PurchaseOrder getPo() {
        return po;
    }

    public PurchaseOrderDetails po(PurchaseOrder purchaseOrder) {
        this.po = purchaseOrder;
        return this;
    }

    public void setPo(PurchaseOrder purchaseOrder) {
        this.po = purchaseOrder;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrderDetails)) {
            return false;
        }
        return id != null && id.equals(((PurchaseOrderDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PurchaseOrderDetails{" +
            "id=" + getId() +
            ", itemQty=" + getItemQty() +
            "}";
    }
}
