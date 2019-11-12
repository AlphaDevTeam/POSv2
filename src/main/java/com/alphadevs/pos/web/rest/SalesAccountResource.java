package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.SalesAccount;
import com.alphadevs.pos.service.SalesAccountService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.SalesAccountCriteria;
import com.alphadevs.pos.service.SalesAccountQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.SalesAccount}.
 */
@RestController
@RequestMapping("/api")
public class SalesAccountResource {

    private final Logger log = LoggerFactory.getLogger(SalesAccountResource.class);

    private static final String ENTITY_NAME = "salesAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalesAccountService salesAccountService;

    private final SalesAccountQueryService salesAccountQueryService;

    public SalesAccountResource(SalesAccountService salesAccountService, SalesAccountQueryService salesAccountQueryService) {
        this.salesAccountService = salesAccountService;
        this.salesAccountQueryService = salesAccountQueryService;
    }

    /**
     * {@code POST  /sales-accounts} : Create a new salesAccount.
     *
     * @param salesAccount the salesAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salesAccount, or with status {@code 400 (Bad Request)} if the salesAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sales-accounts")
    public ResponseEntity<SalesAccount> createSalesAccount(@Valid @RequestBody SalesAccount salesAccount) throws URISyntaxException {
        log.debug("REST request to save SalesAccount : {}", salesAccount);
        if (salesAccount.getId() != null) {
            throw new BadRequestAlertException("A new salesAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalesAccount result = salesAccountService.save(salesAccount);
        return ResponseEntity.created(new URI("/api/sales-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sales-accounts} : Updates an existing salesAccount.
     *
     * @param salesAccount the salesAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salesAccount,
     * or with status {@code 400 (Bad Request)} if the salesAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salesAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sales-accounts")
    public ResponseEntity<SalesAccount> updateSalesAccount(@Valid @RequestBody SalesAccount salesAccount) throws URISyntaxException {
        log.debug("REST request to update SalesAccount : {}", salesAccount);
        if (salesAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SalesAccount result = salesAccountService.save(salesAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, salesAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sales-accounts} : get all the salesAccounts.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salesAccounts in body.
     */
    @GetMapping("/sales-accounts")
    public ResponseEntity<List<SalesAccount>> getAllSalesAccounts(SalesAccountCriteria criteria) {
        log.debug("REST request to get SalesAccounts by criteria: {}", criteria);
        List<SalesAccount> entityList = salesAccountQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /sales-accounts/count} : count all the salesAccounts.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/sales-accounts/count")
    public ResponseEntity<Long> countSalesAccounts(SalesAccountCriteria criteria) {
        log.debug("REST request to count SalesAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(salesAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sales-accounts/:id} : get the "id" salesAccount.
     *
     * @param id the id of the salesAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salesAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sales-accounts/{id}")
    public ResponseEntity<SalesAccount> getSalesAccount(@PathVariable Long id) {
        log.debug("REST request to get SalesAccount : {}", id);
        Optional<SalesAccount> salesAccount = salesAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salesAccount);
    }

    /**
     * {@code DELETE  /sales-accounts/:id} : delete the "id" salesAccount.
     *
     * @param id the id of the salesAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sales-accounts/{id}")
    public ResponseEntity<Void> deleteSalesAccount(@PathVariable Long id) {
        log.debug("REST request to delete SalesAccount : {}", id);
        salesAccountService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
