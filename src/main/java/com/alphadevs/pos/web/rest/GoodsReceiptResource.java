package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.GoodsReceipt;
import com.alphadevs.pos.service.GoodsReceiptService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.GoodsReceiptCriteria;
import com.alphadevs.pos.service.GoodsReceiptQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.GoodsReceipt}.
 */
@RestController
@RequestMapping("/api")
public class GoodsReceiptResource {

    private final Logger log = LoggerFactory.getLogger(GoodsReceiptResource.class);

    private static final String ENTITY_NAME = "goodsReceipt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoodsReceiptService goodsReceiptService;

    private final GoodsReceiptQueryService goodsReceiptQueryService;

    public GoodsReceiptResource(GoodsReceiptService goodsReceiptService, GoodsReceiptQueryService goodsReceiptQueryService) {
        this.goodsReceiptService = goodsReceiptService;
        this.goodsReceiptQueryService = goodsReceiptQueryService;
    }

    /**
     * {@code POST  /goods-receipts} : Create a new goodsReceipt.
     *
     * @param goodsReceipt the goodsReceipt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new goodsReceipt, or with status {@code 400 (Bad Request)} if the goodsReceipt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/goods-receipts")
    public ResponseEntity<GoodsReceipt> createGoodsReceipt(@Valid @RequestBody GoodsReceipt goodsReceipt) throws URISyntaxException {
        log.debug("REST request to save GoodsReceipt : {}", goodsReceipt);
        if (goodsReceipt.getId() != null) {
            throw new BadRequestAlertException("A new goodsReceipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoodsReceipt result = goodsReceiptService.save(goodsReceipt);
        return ResponseEntity.created(new URI("/api/goods-receipts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /goods-receipts} : Updates an existing goodsReceipt.
     *
     * @param goodsReceipt the goodsReceipt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goodsReceipt,
     * or with status {@code 400 (Bad Request)} if the goodsReceipt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the goodsReceipt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/goods-receipts")
    public ResponseEntity<GoodsReceipt> updateGoodsReceipt(@Valid @RequestBody GoodsReceipt goodsReceipt) throws URISyntaxException {
        log.debug("REST request to update GoodsReceipt : {}", goodsReceipt);
        if (goodsReceipt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GoodsReceipt result = goodsReceiptService.save(goodsReceipt);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, goodsReceipt.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /goods-receipts} : get all the goodsReceipts.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of goodsReceipts in body.
     */
    @GetMapping("/goods-receipts")
    public ResponseEntity<List<GoodsReceipt>> getAllGoodsReceipts(GoodsReceiptCriteria criteria) {
        log.debug("REST request to get GoodsReceipts by criteria: {}", criteria);
        List<GoodsReceipt> entityList = goodsReceiptQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /goods-receipts/count} : count all the goodsReceipts.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/goods-receipts/count")
    public ResponseEntity<Long> countGoodsReceipts(GoodsReceiptCriteria criteria) {
        log.debug("REST request to count GoodsReceipts by criteria: {}", criteria);
        return ResponseEntity.ok().body(goodsReceiptQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /goods-receipts/:id} : get the "id" goodsReceipt.
     *
     * @param id the id of the goodsReceipt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the goodsReceipt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/goods-receipts/{id}")
    public ResponseEntity<GoodsReceipt> getGoodsReceipt(@PathVariable Long id) {
        log.debug("REST request to get GoodsReceipt : {}", id);
        Optional<GoodsReceipt> goodsReceipt = goodsReceiptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(goodsReceipt);
    }

    /**
     * {@code DELETE  /goods-receipts/:id} : delete the "id" goodsReceipt.
     *
     * @param id the id of the goodsReceipt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/goods-receipts/{id}")
    public ResponseEntity<Void> deleteGoodsReceipt(@PathVariable Long id) {
        log.debug("REST request to delete GoodsReceipt : {}", id);
        goodsReceiptService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
