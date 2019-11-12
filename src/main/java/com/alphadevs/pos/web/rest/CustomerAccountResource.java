package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.CustomerAccount;
import com.alphadevs.pos.service.CustomerAccountService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.CustomerAccountCriteria;
import com.alphadevs.pos.service.CustomerAccountQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.CustomerAccount}.
 */
@RestController
@RequestMapping("/api")
public class CustomerAccountResource {

    private final Logger log = LoggerFactory.getLogger(CustomerAccountResource.class);

    private static final String ENTITY_NAME = "customerAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerAccountService customerAccountService;

    private final CustomerAccountQueryService customerAccountQueryService;

    public CustomerAccountResource(CustomerAccountService customerAccountService, CustomerAccountQueryService customerAccountQueryService) {
        this.customerAccountService = customerAccountService;
        this.customerAccountQueryService = customerAccountQueryService;
    }

    /**
     * {@code POST  /customer-accounts} : Create a new customerAccount.
     *
     * @param customerAccount the customerAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerAccount, or with status {@code 400 (Bad Request)} if the customerAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-accounts")
    public ResponseEntity<CustomerAccount> createCustomerAccount(@Valid @RequestBody CustomerAccount customerAccount) throws URISyntaxException {
        log.debug("REST request to save CustomerAccount : {}", customerAccount);
        if (customerAccount.getId() != null) {
            throw new BadRequestAlertException("A new customerAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerAccount result = customerAccountService.save(customerAccount);
        return ResponseEntity.created(new URI("/api/customer-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-accounts} : Updates an existing customerAccount.
     *
     * @param customerAccount the customerAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerAccount,
     * or with status {@code 400 (Bad Request)} if the customerAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-accounts")
    public ResponseEntity<CustomerAccount> updateCustomerAccount(@Valid @RequestBody CustomerAccount customerAccount) throws URISyntaxException {
        log.debug("REST request to update CustomerAccount : {}", customerAccount);
        if (customerAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomerAccount result = customerAccountService.save(customerAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /customer-accounts} : get all the customerAccounts.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerAccounts in body.
     */
    @GetMapping("/customer-accounts")
    public ResponseEntity<List<CustomerAccount>> getAllCustomerAccounts(CustomerAccountCriteria criteria) {
        log.debug("REST request to get CustomerAccounts by criteria: {}", criteria);
        List<CustomerAccount> entityList = customerAccountQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /customer-accounts/count} : count all the customerAccounts.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/customer-accounts/count")
    public ResponseEntity<Long> countCustomerAccounts(CustomerAccountCriteria criteria) {
        log.debug("REST request to count CustomerAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-accounts/:id} : get the "id" customerAccount.
     *
     * @param id the id of the customerAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-accounts/{id}")
    public ResponseEntity<CustomerAccount> getCustomerAccount(@PathVariable Long id) {
        log.debug("REST request to get CustomerAccount : {}", id);
        Optional<CustomerAccount> customerAccount = customerAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerAccount);
    }

    /**
     * {@code DELETE  /customer-accounts/:id} : delete the "id" customerAccount.
     *
     * @param id the id of the customerAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-accounts/{id}")
    public ResponseEntity<Void> deleteCustomerAccount(@PathVariable Long id) {
        log.debug("REST request to delete CustomerAccount : {}", id);
        customerAccountService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
