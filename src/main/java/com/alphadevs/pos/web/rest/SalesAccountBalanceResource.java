package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.SalesAccountBalance;
import com.alphadevs.pos.service.SalesAccountBalanceService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.SalesAccountBalanceCriteria;
import com.alphadevs.pos.service.SalesAccountBalanceQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.SalesAccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class SalesAccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(SalesAccountBalanceResource.class);

    private static final String ENTITY_NAME = "salesAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalesAccountBalanceService salesAccountBalanceService;

    private final SalesAccountBalanceQueryService salesAccountBalanceQueryService;

    public SalesAccountBalanceResource(SalesAccountBalanceService salesAccountBalanceService, SalesAccountBalanceQueryService salesAccountBalanceQueryService) {
        this.salesAccountBalanceService = salesAccountBalanceService;
        this.salesAccountBalanceQueryService = salesAccountBalanceQueryService;
    }

    /**
     * {@code POST  /sales-account-balances} : Create a new salesAccountBalance.
     *
     * @param salesAccountBalance the salesAccountBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salesAccountBalance, or with status {@code 400 (Bad Request)} if the salesAccountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sales-account-balances")
    public ResponseEntity<SalesAccountBalance> createSalesAccountBalance(@Valid @RequestBody SalesAccountBalance salesAccountBalance) throws URISyntaxException {
        log.debug("REST request to save SalesAccountBalance : {}", salesAccountBalance);
        if (salesAccountBalance.getId() != null) {
            throw new BadRequestAlertException("A new salesAccountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalesAccountBalance result = salesAccountBalanceService.save(salesAccountBalance);
        return ResponseEntity.created(new URI("/api/sales-account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sales-account-balances} : Updates an existing salesAccountBalance.
     *
     * @param salesAccountBalance the salesAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salesAccountBalance,
     * or with status {@code 400 (Bad Request)} if the salesAccountBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salesAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sales-account-balances")
    public ResponseEntity<SalesAccountBalance> updateSalesAccountBalance(@Valid @RequestBody SalesAccountBalance salesAccountBalance) throws URISyntaxException {
        log.debug("REST request to update SalesAccountBalance : {}", salesAccountBalance);
        if (salesAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SalesAccountBalance result = salesAccountBalanceService.save(salesAccountBalance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, salesAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sales-account-balances} : get all the salesAccountBalances.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salesAccountBalances in body.
     */
    @GetMapping("/sales-account-balances")
    public ResponseEntity<List<SalesAccountBalance>> getAllSalesAccountBalances(SalesAccountBalanceCriteria criteria) {
        log.debug("REST request to get SalesAccountBalances by criteria: {}", criteria);
        List<SalesAccountBalance> entityList = salesAccountBalanceQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /sales-account-balances/count} : count all the salesAccountBalances.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/sales-account-balances/count")
    public ResponseEntity<Long> countSalesAccountBalances(SalesAccountBalanceCriteria criteria) {
        log.debug("REST request to count SalesAccountBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(salesAccountBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sales-account-balances/:id} : get the "id" salesAccountBalance.
     *
     * @param id the id of the salesAccountBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salesAccountBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sales-account-balances/{id}")
    public ResponseEntity<SalesAccountBalance> getSalesAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get SalesAccountBalance : {}", id);
        Optional<SalesAccountBalance> salesAccountBalance = salesAccountBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salesAccountBalance);
    }

    /**
     * {@code DELETE  /sales-account-balances/:id} : delete the "id" salesAccountBalance.
     *
     * @param id the id of the salesAccountBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sales-account-balances/{id}")
    public ResponseEntity<Void> deleteSalesAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete SalesAccountBalance : {}", id);
        salesAccountBalanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
