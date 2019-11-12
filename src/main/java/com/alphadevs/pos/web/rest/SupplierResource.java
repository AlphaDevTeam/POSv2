package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.Supplier;
import com.alphadevs.pos.service.SupplierService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.SupplierCriteria;
import com.alphadevs.pos.service.SupplierQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.Supplier}.
 */
@RestController
@RequestMapping("/api")
public class SupplierResource {

    private final Logger log = LoggerFactory.getLogger(SupplierResource.class);

    private static final String ENTITY_NAME = "supplier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierService supplierService;

    private final SupplierQueryService supplierQueryService;

    public SupplierResource(SupplierService supplierService, SupplierQueryService supplierQueryService) {
        this.supplierService = supplierService;
        this.supplierQueryService = supplierQueryService;
    }

    /**
     * {@code POST  /suppliers} : Create a new supplier.
     *
     * @param supplier the supplier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplier, or with status {@code 400 (Bad Request)} if the supplier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/suppliers")
    public ResponseEntity<Supplier> createSupplier(@Valid @RequestBody Supplier supplier) throws URISyntaxException {
        log.debug("REST request to save Supplier : {}", supplier);
        if (supplier.getId() != null) {
            throw new BadRequestAlertException("A new supplier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Supplier result = supplierService.save(supplier);
        return ResponseEntity.created(new URI("/api/suppliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /suppliers} : Updates an existing supplier.
     *
     * @param supplier the supplier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplier,
     * or with status {@code 400 (Bad Request)} if the supplier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/suppliers")
    public ResponseEntity<Supplier> updateSupplier(@Valid @RequestBody Supplier supplier) throws URISyntaxException {
        log.debug("REST request to update Supplier : {}", supplier);
        if (supplier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Supplier result = supplierService.save(supplier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, supplier.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /suppliers} : get all the suppliers.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suppliers in body.
     */
    @GetMapping("/suppliers")
    public ResponseEntity<List<Supplier>> getAllSuppliers(SupplierCriteria criteria) {
        log.debug("REST request to get Suppliers by criteria: {}", criteria);
        List<Supplier> entityList = supplierQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /suppliers/count} : count all the suppliers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/suppliers/count")
    public ResponseEntity<Long> countSuppliers(SupplierCriteria criteria) {
        log.debug("REST request to count Suppliers by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /suppliers/:id} : get the "id" supplier.
     *
     * @param id the id of the supplier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/suppliers/{id}")
    public ResponseEntity<Supplier> getSupplier(@PathVariable Long id) {
        log.debug("REST request to get Supplier : {}", id);
        Optional<Supplier> supplier = supplierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplier);
    }

    /**
     * {@code DELETE  /suppliers/:id} : delete the "id" supplier.
     *
     * @param id the id of the supplier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/suppliers/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        log.debug("REST request to delete Supplier : {}", id);
        supplierService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
