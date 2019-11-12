package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.PurchaseAccount;
import com.alphadevs.pos.service.PurchaseAccountService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.PurchaseAccountCriteria;
import com.alphadevs.pos.service.PurchaseAccountQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alphadevs.pos.domain.PurchaseAccount}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseAccountResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseAccountResource.class);

    private static final String ENTITY_NAME = "purchaseAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseAccountService purchaseAccountService;

    private final PurchaseAccountQueryService purchaseAccountQueryService;

    public PurchaseAccountResource(PurchaseAccountService purchaseAccountService, PurchaseAccountQueryService purchaseAccountQueryService) {
        this.purchaseAccountService = purchaseAccountService;
        this.purchaseAccountQueryService = purchaseAccountQueryService;
    }

    /**
     * {@code POST  /purchase-accounts} : Create a new purchaseAccount.
     *
     * @param purchaseAccount the purchaseAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseAccount, or with status {@code 400 (Bad Request)} if the purchaseAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-accounts")
    public ResponseEntity<PurchaseAccount> createPurchaseAccount(@Valid @RequestBody PurchaseAccount purchaseAccount) throws URISyntaxException {
        log.debug("REST request to save PurchaseAccount : {}", purchaseAccount);
        if (purchaseAccount.getId() != null) {
            throw new BadRequestAlertException("A new purchaseAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseAccount result = purchaseAccountService.save(purchaseAccount);
        return ResponseEntity.created(new URI("/api/purchase-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-accounts} : Updates an existing purchaseAccount.
     *
     * @param purchaseAccount the purchaseAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseAccount,
     * or with status {@code 400 (Bad Request)} if the purchaseAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-accounts")
    public ResponseEntity<PurchaseAccount> updatePurchaseAccount(@Valid @RequestBody PurchaseAccount purchaseAccount) throws URISyntaxException {
        log.debug("REST request to update PurchaseAccount : {}", purchaseAccount);
        if (purchaseAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchaseAccount result = purchaseAccountService.save(purchaseAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, purchaseAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /purchase-accounts} : get all the purchaseAccounts.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseAccounts in body.
     */
    @GetMapping("/purchase-accounts")
    public ResponseEntity<List<PurchaseAccount>> getAllPurchaseAccounts(PurchaseAccountCriteria criteria) {
        log.debug("REST request to get PurchaseAccounts by criteria: {}", criteria);
        List<PurchaseAccount> entityList = purchaseAccountQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /purchase-accounts/count} : count all the purchaseAccounts.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/purchase-accounts/count")
    public ResponseEntity<Long> countPurchaseAccounts(PurchaseAccountCriteria criteria) {
        log.debug("REST request to count PurchaseAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(purchaseAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /purchase-accounts/:id} : get the "id" purchaseAccount.
     *
     * @param id the id of the purchaseAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-accounts/{id}")
    public ResponseEntity<PurchaseAccount> getPurchaseAccount(@PathVariable Long id) {
        log.debug("REST request to get PurchaseAccount : {}", id);
        Optional<PurchaseAccount> purchaseAccount = purchaseAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseAccount);
    }

    /**
     * {@code DELETE  /purchase-accounts/:id} : delete the "id" purchaseAccount.
     *
     * @param id the id of the purchaseAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-accounts/{id}")
    public ResponseEntity<Void> deletePurchaseAccount(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseAccount : {}", id);
        purchaseAccountService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
