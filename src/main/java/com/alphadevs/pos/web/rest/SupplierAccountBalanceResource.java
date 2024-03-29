package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.SupplierAccountBalance;
import com.alphadevs.pos.repository.SupplierAccountBalanceRepository;
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
 * REST controller for managing {@link com.alphadevs.pos.domain.SupplierAccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class SupplierAccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountBalanceResource.class);

    private static final String ENTITY_NAME = "supplierAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierAccountBalanceRepository supplierAccountBalanceRepository;

    public SupplierAccountBalanceResource(SupplierAccountBalanceRepository supplierAccountBalanceRepository) {
        this.supplierAccountBalanceRepository = supplierAccountBalanceRepository;
    }

    /**
     * {@code POST  /supplier-account-balances} : Create a new supplierAccountBalance.
     *
     * @param supplierAccountBalance the supplierAccountBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierAccountBalance, or with status {@code 400 (Bad Request)} if the supplierAccountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/supplier-account-balances")
    public ResponseEntity<SupplierAccountBalance> createSupplierAccountBalance(@Valid @RequestBody SupplierAccountBalance supplierAccountBalance) throws URISyntaxException {
        log.debug("REST request to save SupplierAccountBalance : {}", supplierAccountBalance);
        if (supplierAccountBalance.getId() != null) {
            throw new BadRequestAlertException("A new supplierAccountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SupplierAccountBalance result = supplierAccountBalanceRepository.save(supplierAccountBalance);
        return ResponseEntity.created(new URI("/api/supplier-account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /supplier-account-balances} : Updates an existing supplierAccountBalance.
     *
     * @param supplierAccountBalance the supplierAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierAccountBalance,
     * or with status {@code 400 (Bad Request)} if the supplierAccountBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/supplier-account-balances")
    public ResponseEntity<SupplierAccountBalance> updateSupplierAccountBalance(@Valid @RequestBody SupplierAccountBalance supplierAccountBalance) throws URISyntaxException {
        log.debug("REST request to update SupplierAccountBalance : {}", supplierAccountBalance);
        if (supplierAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SupplierAccountBalance result = supplierAccountBalanceRepository.save(supplierAccountBalance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, supplierAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /supplier-account-balances} : get all the supplierAccountBalances.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierAccountBalances in body.
     */
    @GetMapping("/supplier-account-balances")
    public List<SupplierAccountBalance> getAllSupplierAccountBalances() {
        log.debug("REST request to get all SupplierAccountBalances");
        return supplierAccountBalanceRepository.findAll();
    }

    /**
     * {@code GET  /supplier-account-balances/:id} : get the "id" supplierAccountBalance.
     *
     * @param id the id of the supplierAccountBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierAccountBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/supplier-account-balances/{id}")
    public ResponseEntity<SupplierAccountBalance> getSupplierAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get SupplierAccountBalance : {}", id);
        Optional<SupplierAccountBalance> supplierAccountBalance = supplierAccountBalanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(supplierAccountBalance);
    }

    /**
     * {@code DELETE  /supplier-account-balances/:id} : delete the "id" supplierAccountBalance.
     *
     * @param id the id of the supplierAccountBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/supplier-account-balances/{id}")
    public ResponseEntity<Void> deleteSupplierAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete SupplierAccountBalance : {}", id);
        supplierAccountBalanceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
