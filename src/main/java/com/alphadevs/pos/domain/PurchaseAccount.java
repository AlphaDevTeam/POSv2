package com.alphadevs.pos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * PurchaseAccount Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "PurchaseAccount Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "purchase_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PurchaseAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull
    @Column(name = "transaction_description", nullable = false)
    private String transactionDescription;

    @NotNull
    @Column(name = "transaction_amount_dr", nullable = false)
    private Double transactionAmountDR;

    @NotNull
    @Column(name = "transaction_amount_cr", nullable = false)
    private Double transactionAmountCR;

    @NotNull
    @Column(name = "transaction_balance", nullable = false)
    private Double transactionBalance;

    @ManyToOne
    @JsonIgnoreProperties("purchaseAccounts")
    private Location location;

    @ManyToOne
    @JsonIgnoreProperties("purchaseAccounts")
    private TransactionType transactionType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public PurchaseAccount transactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public PurchaseAccount transactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
        return this;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public Double getTransactionAmountDR() {
        return transactionAmountDR;
    }

    public PurchaseAccount transactionAmountDR(Double transactionAmountDR) {
        this.transactionAmountDR = transactionAmountDR;
        return this;
    }

    public void setTransactionAmountDR(Double transactionAmountDR) {
        this.transactionAmountDR = transactionAmountDR;
    }

    public Double getTransactionAmountCR() {
        return transactionAmountCR;
    }

    public PurchaseAccount transactionAmountCR(Double transactionAmountCR) {
        this.transactionAmountCR = transactionAmountCR;
        return this;
    }

    public void setTransactionAmountCR(Double transactionAmountCR) {
        this.transactionAmountCR = transactionAmountCR;
    }

    public Double getTransactionBalance() {
        return transactionBalance;
    }

    public PurchaseAccount transactionBalance(Double transactionBalance) {
        this.transactionBalance = transactionBalance;
        return this;
    }

    public void setTransactionBalance(Double transactionBalance) {
        this.transactionBalance = transactionBalance;
    }

    public Location getLocation() {
        return location;
    }

    public PurchaseAccount location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public PurchaseAccount transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseAccount)) {
            return false;
        }
        return id != null && id.equals(((PurchaseAccount) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PurchaseAccount{" +
            "id=" + getId() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionDescription='" + getTransactionDescription() + "'" +
            ", transactionAmountDR=" + getTransactionAmountDR() +
            ", transactionAmountCR=" + getTransactionAmountCR() +
            ", transactionBalance=" + getTransactionBalance() +
            "}";
    }
}
