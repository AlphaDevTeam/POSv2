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
 * GRN Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "GRN Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "goods_receipt")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GoodsReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "grn_number", nullable = false)
    private String grnNumber;

    @NotNull
    @Column(name = "grn_date", nullable = false)
    private LocalDate grnDate;

    @Column(name = "po_number")
    private String poNumber;

    @OneToMany(mappedBy = "grn")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GoodsReceiptDetails> details = new HashSet<>();

    @OneToMany(mappedBy = "relatedGRN")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PurchaseOrder> linkedPOs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("goodsReceipts")
    private Supplier supplier;

    @ManyToOne
    @JsonIgnoreProperties("goodsReceipts")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrnNumber() {
        return grnNumber;
    }

    public GoodsReceipt grnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
        return this;
    }

    public void setGrnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
    }

    public LocalDate getGrnDate() {
        return grnDate;
    }

    public GoodsReceipt grnDate(LocalDate grnDate) {
        this.grnDate = grnDate;
        return this;
    }

    public void setGrnDate(LocalDate grnDate) {
        this.grnDate = grnDate;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public GoodsReceipt poNumber(String poNumber) {
        this.poNumber = poNumber;
        return this;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Set<GoodsReceiptDetails> getDetails() {
        return details;
    }

    public GoodsReceipt details(Set<GoodsReceiptDetails> goodsReceiptDetails) {
        this.details = goodsReceiptDetails;
        return this;
    }

    public GoodsReceipt addDetails(GoodsReceiptDetails goodsReceiptDetails) {
        this.details.add(goodsReceiptDetails);
        goodsReceiptDetails.setGrn(this);
        return this;
    }

    public GoodsReceipt removeDetails(GoodsReceiptDetails goodsReceiptDetails) {
        this.details.remove(goodsReceiptDetails);
        goodsReceiptDetails.setGrn(null);
        return this;
    }

    public void setDetails(Set<GoodsReceiptDetails> goodsReceiptDetails) {
        this.details = goodsReceiptDetails;
    }

    public Set<PurchaseOrder> getLinkedPOs() {
        return linkedPOs;
    }

    public GoodsReceipt linkedPOs(Set<PurchaseOrder> purchaseOrders) {
        this.linkedPOs = purchaseOrders;
        return this;
    }

    public GoodsReceipt addLinkedPOs(PurchaseOrder purchaseOrder) {
        this.linkedPOs.add(purchaseOrder);
        purchaseOrder.setRelatedGRN(this);
        return this;
    }

    public GoodsReceipt removeLinkedPOs(PurchaseOrder purchaseOrder) {
        this.linkedPOs.remove(purchaseOrder);
        purchaseOrder.setRelatedGRN(null);
        return this;
    }

    public void setLinkedPOs(Set<PurchaseOrder> purchaseOrders) {
        this.linkedPOs = purchaseOrders;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public GoodsReceipt supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Location getLocation() {
        return location;
    }

    public GoodsReceipt location(Location location) {
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
        if (!(o instanceof GoodsReceipt)) {
            return false;
        }
        return id != null && id.equals(((GoodsReceipt) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GoodsReceipt{" +
            "id=" + getId() +
            ", grnNumber='" + getGrnNumber() + "'" +
            ", grnDate='" + getGrnDate() + "'" +
            ", poNumber='" + getPoNumber() + "'" +
            "}";
    }
}
