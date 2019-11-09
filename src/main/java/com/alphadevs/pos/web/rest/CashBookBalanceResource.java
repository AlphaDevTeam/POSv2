package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.CashBookBalance;
import com.alphadevs.pos.repository.CashBookBalanceRepository;
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
 * REST controller for managing {@link com.alphadevs.pos.domain.CashBookBalance}.
 */
@RestController
@RequestMapping("/api")
public class CashBookBalanceResource {

    private final Logger log = LoggerFactory.getLogger(CashBookBalanceResource.class);

    private static final String ENTITY_NAME = "cashBookBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CashBookBalanceRepository cashBookBalanceRepository;

    public CashBookBalanceResource(CashBookBalanceRepository cashBookBalanceRepository) {
        this.cashBookBalanceRepository = cashBookBalanceRepository;
    }

    /**
     * {@code POST  /cash-book-balances} : Create a new cashBookBalance.
     *
     * @param cashBookBalance the cashBookBalance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashBookBalance, or with status {@code 400 (Bad Request)} if the cashBookBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cash-book-balances")
    public ResponseEntity<CashBookBalance> createCashBookBalance(@Valid @RequestBody CashBookBalance cashBookBalance) throws URISyntaxException {
        log.debug("REST request to save CashBookBalance : {}", cashBookBalance);
        if (cashBookBalance.getId() != null) {
            throw new BadRequestAlertException("A new cashBookBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CashBookBalance result = cashBookBalanceRepository.save(cashBookBalance);
        return ResponseEntity.created(new URI("/api/cash-book-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cash-book-balances} : Updates an existing cashBookBalance.
     *
     * @param cashBookBalance the cashBookBalance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashBookBalance,
     * or with status {@code 400 (Bad Request)} if the cashBookBalance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashBookBalance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cash-book-balances")
    public ResponseEntity<CashBookBalance> updateCashBookBalance(@Valid @RequestBody CashBookBalance cashBookBalance) throws URISyntaxException {
        log.debug("REST request to update CashBookBalance : {}", cashBookBalance);
        if (cashBookBalance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CashBookBalance result = cashBookBalanceRepository.save(cashBookBalance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashBookBalance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cash-book-balances} : get all the cashBookBalances.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashBookBalances in body.
     */
    @GetMapping("/cash-book-balances")
    public List<CashBookBalance> getAllCashBookBalances() {
        log.debug("REST request to get all CashBookBalances");
        return cashBookBalanceRepository.findAll();
    }

    /**
     * {@code GET  /cash-book-balances/:id} : get the "id" cashBookBalance.
     *
     * @param id the id of the cashBookBalance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashBookBalance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cash-book-balances/{id}")
    public ResponseEntity<CashBookBalance> getCashBookBalance(@PathVariable Long id) {
        log.debug("REST request to get CashBookBalance : {}", id);
        Optional<CashBookBalance> cashBookBalance = cashBookBalanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cashBookBalance);
    }

    /**
     * {@code DELETE  /cash-book-balances/:id} : delete the "id" cashBookBalance.
     *
     * @param id the id of the cashBookBalance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cash-book-balances/{id}")
    public ResponseEntity<Void> deleteCashBookBalance(@PathVariable Long id) {
        log.debug("REST request to delete CashBookBalance : {}", id);
        cashBookBalanceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
