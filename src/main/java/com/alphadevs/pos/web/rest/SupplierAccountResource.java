package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.SupplierAccount;
import com.alphadevs.pos.repository.SupplierAccountRepository;
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
 * REST controller for managing {@link com.alphadevs.pos.domain.SupplierAccount}.
 */
@RestController
@RequestMapping("/api")
public class SupplierAccountResource {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountResource.class);

    private static final String ENTITY_NAME = "supplierAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierAccountRepository supplierAccountRepository;

    public SupplierAccountResource(SupplierAccountRepository supplierAccountRepository) {
        this.supplierAccountRepository = supplierAccountRepository;
    }

    /**
     * {@code POST  /supplier-accounts} : Create a new supplierAccount.
     *
     * @param supplierAccount the supplierAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierAccount, or with status {@code 400 (Bad Request)} if the supplierAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/supplier-accounts")
    public ResponseEntity<SupplierAccount> createSupplierAccount(@Valid @RequestBody SupplierAccount supplierAccount) throws URISyntaxException {
        log.debug("REST request to save SupplierAccount : {}", supplierAccount);
        if (supplierAccount.getId() != null) {
            throw new BadRequestAlertException("A new supplierAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SupplierAccount result = supplierAccountRepository.save(supplierAccount);
        return ResponseEntity.created(new URI("/api/supplier-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /supplier-accounts} : Updates an existing supplierAccount.
     *
     * @param supplierAccount the supplierAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierAccount,
     * or with status {@code 400 (Bad Request)} if the supplierAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/supplier-accounts")
    public ResponseEntity<SupplierAccount> updateSupplierAccount(@Valid @RequestBody SupplierAccount supplierAccount) throws URISyntaxException {
        log.debug("REST request to update SupplierAccount : {}", supplierAccount);
        if (supplierAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SupplierAccount result = supplierAccountRepository.save(supplierAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, supplierAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /supplier-accounts} : get all the supplierAccounts.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierAccounts in body.
     */
    @GetMapping("/supplier-accounts")
    public List<SupplierAccount> getAllSupplierAccounts() {
        log.debug("REST request to get all SupplierAccounts");
        return supplierAccountRepository.findAll();
    }

    /**
     * {@code GET  /supplier-accounts/:id} : get the "id" supplierAccount.
     *
     * @param id the id of the supplierAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/supplier-accounts/{id}")
    public ResponseEntity<SupplierAccount> getSupplierAccount(@PathVariable Long id) {
        log.debug("REST request to get SupplierAccount : {}", id);
        Optional<SupplierAccount> supplierAccount = supplierAccountRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(supplierAccount);
    }

    /**
     * {@code DELETE  /supplier-accounts/:id} : delete the "id" supplierAccount.
     *
     * @param id the id of the supplierAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/supplier-accounts/{id}")
    public ResponseEntity<Void> deleteSupplierAccount(@PathVariable Long id) {
        log.debug("REST request to delete SupplierAccount : {}", id);
        supplierAccountRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
