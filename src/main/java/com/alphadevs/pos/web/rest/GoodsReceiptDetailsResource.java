package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.GoodsReceiptDetails;
import com.alphadevs.pos.service.GoodsReceiptDetailsService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.GoodsReceiptDetailsCriteria;
import com.alphadevs.pos.service.GoodsReceiptDetailsQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.GoodsReceiptDetails}.
 */
@RestController
@RequestMapping("/api")
public class GoodsReceiptDetailsResource {

    private final Logger log = LoggerFactory.getLogger(GoodsReceiptDetailsResource.class);

    private static final String ENTITY_NAME = "goodsReceiptDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoodsReceiptDetailsService goodsReceiptDetailsService;

    private final GoodsReceiptDetailsQueryService goodsReceiptDetailsQueryService;

    public GoodsReceiptDetailsResource(GoodsReceiptDetailsService goodsReceiptDetailsService, GoodsReceiptDetailsQueryService goodsReceiptDetailsQueryService) {
        this.goodsReceiptDetailsService = goodsReceiptDetailsService;
        this.goodsReceiptDetailsQueryService = goodsReceiptDetailsQueryService;
    }

    /**
     * {@code POST  /goods-receipt-details} : Create a new goodsReceiptDetails.
     *
     * @param goodsReceiptDetails the goodsReceiptDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new goodsReceiptDetails, or with status {@code 400 (Bad Request)} if the goodsReceiptDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/goods-receipt-details")
    public ResponseEntity<GoodsReceiptDetails> createGoodsReceiptDetails(@Valid @RequestBody GoodsReceiptDetails goodsReceiptDetails) throws URISyntaxException {
        log.debug("REST request to save GoodsReceiptDetails : {}", goodsReceiptDetails);
        if (goodsReceiptDetails.getId() != null) {
            throw new BadRequestAlertException("A new goodsReceiptDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoodsReceiptDetails result = goodsReceiptDetailsService.save(goodsReceiptDetails);
        return ResponseEntity.created(new URI("/api/goods-receipt-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /goods-receipt-details} : Updates an existing goodsReceiptDetails.
     *
     * @param goodsReceiptDetails the goodsReceiptDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goodsReceiptDetails,
     * or with status {@code 400 (Bad Request)} if the goodsReceiptDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the goodsReceiptDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/goods-receipt-details")
    public ResponseEntity<GoodsReceiptDetails> updateGoodsReceiptDetails(@Valid @RequestBody GoodsReceiptDetails goodsReceiptDetails) throws URISyntaxException {
        log.debug("REST request to update GoodsReceiptDetails : {}", goodsReceiptDetails);
        if (goodsReceiptDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GoodsReceiptDetails result = goodsReceiptDetailsService.save(goodsReceiptDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, goodsReceiptDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /goods-receipt-details} : get all the goodsReceiptDetails.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of goodsReceiptDetails in body.
     */
    @GetMapping("/goods-receipt-details")
    public ResponseEntity<List<GoodsReceiptDetails>> getAllGoodsReceiptDetails(GoodsReceiptDetailsCriteria criteria) {
        log.debug("REST request to get GoodsReceiptDetails by criteria: {}", criteria);
        List<GoodsReceiptDetails> entityList = goodsReceiptDetailsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /goods-receipt-details/count} : count all the goodsReceiptDetails.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/goods-receipt-details/count")
    public ResponseEntity<Long> countGoodsReceiptDetails(GoodsReceiptDetailsCriteria criteria) {
        log.debug("REST request to count GoodsReceiptDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(goodsReceiptDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /goods-receipt-details/:id} : get the "id" goodsReceiptDetails.
     *
     * @param id the id of the goodsReceiptDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the goodsReceiptDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/goods-receipt-details/{id}")
    public ResponseEntity<GoodsReceiptDetails> getGoodsReceiptDetails(@PathVariable Long id) {
        log.debug("REST request to get GoodsReceiptDetails : {}", id);
        Optional<GoodsReceiptDetails> goodsReceiptDetails = goodsReceiptDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(goodsReceiptDetails);
    }

    /**
     * {@code DELETE  /goods-receipt-details/:id} : delete the "id" goodsReceiptDetails.
     *
     * @param id the id of the goodsReceiptDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/goods-receipt-details/{id}")
    public ResponseEntity<Void> deleteGoodsReceiptDetails(@PathVariable Long id) {
        log.debug("REST request to delete GoodsReceiptDetails : {}", id);
        goodsReceiptDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
