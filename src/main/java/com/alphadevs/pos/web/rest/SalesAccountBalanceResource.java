package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.SalesAccountBalance;
import com.alphadevs.pos.repository.SalesAccountBalanceRepository;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;

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

    private final SalesAccountBalanceRepository salesAccountBalanceRepository;

    public SalesAccountBalanceResource(SalesAccountBalanceRepository salesAccountBalanceRepository) {
        this.salesAccountBalanceRepository = salesAccountBalanceRepository;
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
        SalesAccountBalance result = salesAccountBalanceRepository.save(salesAccountBalance);
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
        SalesAccountBalance result = salesAccountBalanceRepository.save(salesAccountBalance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, salesAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sales-account-balances} : get all the salesAccountBalances.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salesAccountBalances in body.
     */
    @GetMapping("/sales-account-balances")
    public List<SalesAccountBalance> getAllSalesAccountBalances() {
        log.debug("REST request to get all SalesAccountBalances");
        return salesAccountBalanceRepository.findAll();
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
        Optional<SalesAccountBalance> salesAccountBalance = salesAccountBalanceRepository.findById(id);
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
        salesAccountBalanceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
