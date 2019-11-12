package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.CustomerAccountBalance;
import com.alphadevs.pos.service.CustomerAccountBalanceService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.CustomerAccountBalanceCriteria;
import com.alphadevs.pos.service.CustomerAccountBalanceQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.CustomerAccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class CustomerAccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(CustomerAccountBalanceResource.class);

    private static final String ENTITY_NAME = "customerAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerAccountBalanceService customerAccountBalanceService;

    private final CustomerAccountBalanceQueryService customerAccountBalanceQueryService;

    public CustomerAccountBalanceResource(CustomerAccountBalanceService customerAccountBalanceService, CustomerAccountBalanceQueryService customerAccountBalanceQueryService) {
        this.customerAccountBalanceService = customerAccountBalanceService;
        this.customerAccountBalanceQueryService = customerAccountBalanceQueryService;
    }

    /**
     * {@code POST  /customer-account-balances} : Create a new customerAccountBalance.
     *
     * @param customerAccountBalance the customerAccountBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerAccountBalance, or with status {@code 400 (Bad Request)} if the customerAccountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-account-balances")
    public ResponseEntity<CustomerAccountBalance> createCustomerAccountBalance(@Valid @RequestBody CustomerAccountBalance customerAccountBalance) throws URISyntaxException {
        log.debug("REST request to save CustomerAccountBalance : {}", customerAccountBalance);
        if (customerAccountBalance.getId() != null) {
            throw new BadRequestAlertException("A new customerAccountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerAccountBalance result = customerAccountBalanceService.save(customerAccountBalance);
        return ResponseEntity.created(new URI("/api/customer-account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-account-balances} : Updates an existing customerAccountBalance.
     *
     * @param customerAccountBalance the customerAccountBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerAccountBalance,
     * or with status {@code 400 (Bad Request)} if the customerAccountBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerAccountBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-account-balances")
    public ResponseEntity<CustomerAccountBalance> updateCustomerAccountBalance(@Valid @RequestBody CustomerAccountBalance customerAccountBalance) throws URISyntaxException {
        log.debug("REST request to update CustomerAccountBalance : {}", customerAccountBalance);
        if (customerAccountBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomerAccountBalance result = customerAccountBalanceService.save(customerAccountBalance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerAccountBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /customer-account-balances} : get all the customerAccountBalances.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerAccountBalances in body.
     */
    @GetMapping("/customer-account-balances")
    public ResponseEntity<List<CustomerAccountBalance>> getAllCustomerAccountBalances(CustomerAccountBalanceCriteria criteria) {
        log.debug("REST request to get CustomerAccountBalances by criteria: {}", criteria);
        List<CustomerAccountBalance> entityList = customerAccountBalanceQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /customer-account-balances/count} : count all the customerAccountBalances.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/customer-account-balances/count")
    public ResponseEntity<Long> countCustomerAccountBalances(CustomerAccountBalanceCriteria criteria) {
        log.debug("REST request to count CustomerAccountBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerAccountBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-account-balances/:id} : get the "id" customerAccountBalance.
     *
     * @param id the id of the customerAccountBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerAccountBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-account-balances/{id}")
    public ResponseEntity<CustomerAccountBalance> getCustomerAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get CustomerAccountBalance : {}", id);
        Optional<CustomerAccountBalance> customerAccountBalance = customerAccountBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerAccountBalance);
    }

    /**
     * {@code DELETE  /customer-account-balances/:id} : delete the "id" customerAccountBalance.
     *
     * @param id the id of the customerAccountBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-account-balances/{id}")
    public ResponseEntity<Void> deleteCustomerAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete CustomerAccountBalance : {}", id);
        customerAccountBalanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
