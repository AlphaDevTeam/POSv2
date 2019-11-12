package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.Designs;
import com.alphadevs.pos.service.DesignsService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.DesignsCriteria;
import com.alphadevs.pos.service.DesignsQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.Designs}.
 */
@RestController
@RequestMapping("/api")
public class DesignsResource {

    private final Logger log = LoggerFactory.getLogger(DesignsResource.class);

    private static final String ENTITY_NAME = "designs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DesignsService designsService;

    private final DesignsQueryService designsQueryService;

    public DesignsResource(DesignsService designsService, DesignsQueryService designsQueryService) {
        this.designsService = designsService;
        this.designsQueryService = designsQueryService;
    }

    /**
     * {@code POST  /designs} : Create a new designs.
     *
     * @param designs the designs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new designs, or with status {@code 400 (Bad Request)} if the designs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/designs")
    public ResponseEntity<Designs> createDesigns(@Valid @RequestBody Designs designs) throws URISyntaxException {
        log.debug("REST request to save Designs : {}", designs);
        if (designs.getId() != null) {
            throw new BadRequestAlertException("A new designs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Designs result = designsService.save(designs);
        return ResponseEntity.created(new URI("/api/designs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /designs} : Updates an existing designs.
     *
     * @param designs the designs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated designs,
     * or with status {@code 400 (Bad Request)} if the designs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the designs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/designs")
    public ResponseEntity<Designs> updateDesigns(@Valid @RequestBody Designs designs) throws URISyntaxException {
        log.debug("REST request to update Designs : {}", designs);
        if (designs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Designs result = designsService.save(designs);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, designs.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /designs} : get all the designs.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of designs in body.
     */
    @GetMapping("/designs")
    public ResponseEntity<List<Designs>> getAllDesigns(DesignsCriteria criteria) {
        log.debug("REST request to get Designs by criteria: {}", criteria);
        List<Designs> entityList = designsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /designs/count} : count all the designs.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/designs/count")
    public ResponseEntity<Long> countDesigns(DesignsCriteria criteria) {
        log.debug("REST request to count Designs by criteria: {}", criteria);
        return ResponseEntity.ok().body(designsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /designs/:id} : get the "id" designs.
     *
     * @param id the id of the designs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the designs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/designs/{id}")
    public ResponseEntity<Designs> getDesigns(@PathVariable Long id) {
        log.debug("REST request to get Designs : {}", id);
        Optional<Designs> designs = designsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(designs);
    }

    /**
     * {@code DELETE  /designs/:id} : delete the "id" designs.
     *
     * @param id the id of the designs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/designs/{id}")
    public ResponseEntity<Void> deleteDesigns(@PathVariable Long id) {
        log.debug("REST request to delete Designs : {}", id);
        designsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
