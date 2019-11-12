package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.PurchaseAccountBalance;
import com.alphadevs.pos.service.PurchaseAccountBalanceService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.PurchaseAccountBalanceCriteria;
import com.alphadevs.pos.service.PurchaseAccountBalanceQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.PurchaseAccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseAccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseAccountBalanceResource.class);

    private static final String ENTITY_NAME = "purchaseAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseAccountBalanceService purchaseAccountBalanceService;

    private final PurchaseAccountBalanceQueryService purchaseAccountBalanceQueryService;

    public PurchaseAccountBalanceResource(PurchaseAccountBalanceService purchaseAccountBalanceService, PurchaseAccountBalanceQueryService purchaseAccountBalanceQueryService) {
        this.purchaseAccountBalanceService = purchaseAccountBalanceService;
        this.purchaseAccountBalanceQueryService = purchaseAccountBalanceQueryService;
    }

    /**
     * {@code POST  /purchase-account-balances} : Create a new purchaseAccountBalance.
     *
     * @param purchaseAccountBalance the purchaseAccountBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseAccountBalance, or with status {@code 400 (Bad Request)} if the purchaseAccountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-account-balances")
    public ResponseEntity<PurchaseAccountBalance> createPurchaseAccountBalance(@Valid @RequestBody PurchaseAccountBalance purchaseAccountBalance) throws URISyntaxException {
        log.debug("REST request to save PurchaseAccountBalance : {}", purchaseAccountBalance);
        if (purchaseAccountBalance.getId() != null) {
            throw new BadRequestAlertException("A new purchaseAccountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseAccountBalance result = purchaseAccountBalanceService.save(purchaseAccountBalance);
        return ResponseEntity.created(new URI("/api/purchase-account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-account-balances} : Updates an existing purchaseAccountBalance.
     *
     * @param purchaseAccountBalance the purchaseAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseAccountBalance,
     * or with status {@code 400 (Bad Request)} if the purchaseAccountBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-account-balances")
    public ResponseEntity<PurchaseAccountBalance> updatePurchaseAccountBalance(@Valid @RequestBody PurchaseAccountBalance purchaseAccountBalance) throws URISyntaxException {
        log.debug("REST request to update PurchaseAccountBalance : {}", purchaseAccountBalance);
        if (purchaseAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchaseAccountBalance result = purchaseAccountBalanceService.save(purchaseAccountBalance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, purchaseAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /purchase-account-balances} : get all the purchaseAccountBalances.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseAccountBalances in body.
     */
    @GetMapping("/purchase-account-balances")
    public ResponseEntity<List<PurchaseAccountBalance>> getAllPurchaseAccountBalances(PurchaseAccountBalanceCriteria criteria) {
        log.debug("REST request to get PurchaseAccountBalances by criteria: {}", criteria);
        List<PurchaseAccountBalance> entityList = purchaseAccountBalanceQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /purchase-account-balances/count} : count all the purchaseAccountBalances.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/purchase-account-balances/count")
    public ResponseEntity<Long> countPurchaseAccountBalances(PurchaseAccountBalanceCriteria criteria) {
        log.debug("REST request to count PurchaseAccountBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(purchaseAccountBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /purchase-account-balances/:id} : get the "id" purchaseAccountBalance.
     *
     * @param id the id of the purchaseAccountBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseAccountBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-account-balances/{id}")
    public ResponseEntity<PurchaseAccountBalance> getPurchaseAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get PurchaseAccountBalance : {}", id);
        Optional<PurchaseAccountBalance> purchaseAccountBalance = purchaseAccountBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseAccountBalance);
    }

    /**
     * {@code DELETE  /purchase-account-balances/:id} : delete the "id" purchaseAccountBalance.
     *
     * @param id the id of the purchaseAccountBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-account-balances/{id}")
    public ResponseEntity<Void> deletePurchaseAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseAccountBalance : {}", id);
        purchaseAccountBalanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
