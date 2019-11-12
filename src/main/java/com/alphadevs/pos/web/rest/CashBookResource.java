package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.CashBook;
import com.alphadevs.pos.service.CashBookService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.CashBookCriteria;
import com.alphadevs.pos.service.CashBookQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.CashBook}.
 */
@RestController
@RequestMapping("/api")
public class CashBookResource {

    private final Logger log = LoggerFactory.getLogger(CashBookResource.class);

    private static final String ENTITY_NAME = "cashBook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CashBookService cashBookService;

    private final CashBookQueryService cashBookQueryService;

    public CashBookResource(CashBookService cashBookService, CashBookQueryService cashBookQueryService) {
        this.cashBookService = cashBookService;
        this.cashBookQueryService = cashBookQueryService;
    }

    /**
     * {@code POST  /cash-books} : Create a new cashBook.
     *
     * @param cashBook the cashBook to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashBook, or with status {@code 400 (Bad Request)} if the cashBook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cash-books")
    public ResponseEntity<CashBook> createCashBook(@Valid @RequestBody CashBook cashBook) throws URISyntaxException {
        log.debug("REST request to save CashBook : {}", cashBook);
        if (cashBook.getId() != null) {
            throw new BadRequestAlertException("A new cashBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CashBook result = cashBookService.save(cashBook);
        return ResponseEntity.created(new URI("/api/cash-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cash-books} : Updates an existing cashBook.
     *
     * @param cashBook the cashBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashBook,
     * or with status {@code 400 (Bad Request)} if the cashBook is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashBook couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cash-books")
    public ResponseEntity<CashBook> updateCashBook(@Valid @RequestBody CashBook cashBook) throws URISyntaxException {
        log.debug("REST request to update CashBook : {}", cashBook);
        if (cashBook.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CashBook result = cashBookService.save(cashBook);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashBook.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cash-books} : get all the cashBooks.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashBooks in body.
     */
    @GetMapping("/cash-books")
    public ResponseEntity<List<CashBook>> getAllCashBooks(CashBookCriteria criteria) {
        log.debug("REST request to get CashBooks by criteria: {}", criteria);
        List<CashBook> entityList = cashBookQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /cash-books/count} : count all the cashBooks.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/cash-books/count")
    public ResponseEntity<Long> countCashBooks(CashBookCriteria criteria) {
        log.debug("REST request to count CashBooks by criteria: {}", criteria);
        return ResponseEntity.ok().body(cashBookQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cash-books/:id} : get the "id" cashBook.
     *
     * @param id the id of the cashBook to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashBook, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cash-books/{id}")
    public ResponseEntity<CashBook> getCashBook(@PathVariable Long id) {
        log.debug("REST request to get CashBook : {}", id);
        Optional<CashBook> cashBook = cashBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashBook);
    }

    /**
     * {@code DELETE  /cash-books/:id} : delete the "id" cashBook.
     *
     * @param id the id of the cashBook to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cash-books/{id}")
    public ResponseEntity<Void> deleteCashBook(@PathVariable Long id) {
        log.debug("REST request to delete CashBook : {}", id);
        cashBookService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
