package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.PurchaseOrderDetails;
import com.alphadevs.pos.service.PurchaseOrderDetailsService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.PurchaseOrderDetailsCriteria;
import com.alphadevs.pos.service.PurchaseOrderDetailsQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.PurchaseOrderDetails}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseOrderDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderDetailsResource.class);

    private static final String ENTITY_NAME = "purchaseOrderDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseOrderDetailsService purchaseOrderDetailsService;

    private final PurchaseOrderDetailsQueryService purchaseOrderDetailsQueryService;

    public PurchaseOrderDetailsResource(PurchaseOrderDetailsService purchaseOrderDetailsService, PurchaseOrderDetailsQueryService purchaseOrderDetailsQueryService) {
        this.purchaseOrderDetailsService = purchaseOrderDetailsService;
        this.purchaseOrderDetailsQueryService = purchaseOrderDetailsQueryService;
    }

    /**
     * {@code POST  /purchase-order-details} : Create a new purchaseOrderDetails.
     *
     * @param purchaseOrderDetails the purchaseOrderDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseOrderDetails, or with status {@code 400 (Bad Request)} if the purchaseOrderDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-order-details")
    public ResponseEntity<PurchaseOrderDetails> createPurchaseOrderDetails(@Valid @RequestBody PurchaseOrderDetails purchaseOrderDetails) throws URISyntaxException {
        log.debug("REST request to save PurchaseOrderDetails : {}", purchaseOrderDetails);
        if (purchaseOrderDetails.getId() != null) {
            throw new BadRequestAlertException("A new purchaseOrderDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseOrderDetails result = purchaseOrderDetailsService.save(purchaseOrderDetails);
        return ResponseEntity.created(new URI("/api/purchase-order-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-order-details} : Updates an existing purchaseOrderDetails.
     *
     * @param purchaseOrderDetails the purchaseOrderDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrderDetails,
     * or with status {@code 400 (Bad Request)} if the purchaseOrderDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrderDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-order-details")
    public ResponseEntity<PurchaseOrderDetails> updatePurchaseOrderDetails(@Valid @RequestBody PurchaseOrderDetails purchaseOrderDetails) throws URISyntaxException {
        log.debug("REST request to update PurchaseOrderDetails : {}", purchaseOrderDetails);
        if (purchaseOrderDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchaseOrderDetails result = purchaseOrderDetailsService.save(purchaseOrderDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, purchaseOrderDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /purchase-order-details} : get all the purchaseOrderDetails.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseOrderDetails in body.
     */
    @GetMapping("/purchase-order-details")
    public ResponseEntity<List<PurchaseOrderDetails>> getAllPurchaseOrderDetails(PurchaseOrderDetailsCriteria criteria) {
        log.debug("REST request to get PurchaseOrderDetails by criteria: {}", criteria);
        List<PurchaseOrderDetails> entityList = purchaseOrderDetailsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /purchase-order-details/count} : count all the purchaseOrderDetails.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/purchase-order-details/count")
    public ResponseEntity<Long> countPurchaseOrderDetails(PurchaseOrderDetailsCriteria criteria) {
        log.debug("REST request to count PurchaseOrderDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(purchaseOrderDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /purchase-order-details/:id} : get the "id" purchaseOrderDetails.
     *
     * @param id the id of the purchaseOrderDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseOrderDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-order-details/{id}")
    public ResponseEntity<PurchaseOrderDetails> getPurchaseOrderDetails(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrderDetails : {}", id);
        Optional<PurchaseOrderDetails> purchaseOrderDetails = purchaseOrderDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseOrderDetails);
    }

    /**
     * {@code DELETE  /purchase-order-details/:id} : delete the "id" purchaseOrderDetails.
     *
     * @param id the id of the purchaseOrderDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-order-details/{id}")
    public ResponseEntity<Void> deletePurchaseOrderDetails(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseOrderDetails : {}", id);
        purchaseOrderDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
