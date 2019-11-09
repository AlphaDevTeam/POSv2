package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.Desings;
import com.alphadevs.pos.repository.DesingsRepository;
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
 * REST controller for managing {@link com.alphadevs.pos.domain.Desings}.
 */
@RestController
@RequestMapping("/api")
public class DesingsResource {

    private final Logger log = LoggerFactory.getLogger(DesingsResource.class);

    private static final String ENTITY_NAME = "desings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DesingsRepository desingsRepository;

    public DesingsResource(DesingsRepository desingsRepository) {
        this.desingsRepository = desingsRepository;
    }

    /**
     * {@code POST  /desings} : Create a new desings.
     *
     * @param desings the desings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new desings, or with status {@code 400 (Bad Request)} if the desings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/desings")
    public ResponseEntity<Desings> createDesings(@Valid @RequestBody Desings desings) throws URISyntaxException {
        log.debug("REST request to save Desings : {}", desings);
        if (desings.getId() != null) {
            throw new BadRequestAlertException("A new desings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Desings result = desingsRepository.save(desings);
        return ResponseEntity.created(new URI("/api/desings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /desings} : Updates an existing desings.
     *
     * @param desings the desings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated desings,
     * or with status {@code 400 (Bad Request)} if the desings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the desings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/desings")
    public ResponseEntity<Desings> updateDesings(@Valid @RequestBody Desings desings) throws URISyntaxException {
        log.debug("REST request to update Desings : {}", desings);
        if (desings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Desings result = desingsRepository.save(desings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, desings.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /desings} : get all the desings.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of desings in body.
     */
    @GetMapping("/desings")
    public List<Desings> getAllDesings() {
        log.debug("REST request to get all Desings");
        return desingsRepository.findAll();
    }

    /**
     * {@code GET  /desings/:id} : get the "id" desings.
     *
     * @param id the id of the desings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the desings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/desings/{id}")
    public ResponseEntity<Desings> getDesings(@PathVariable Long id) {
        log.debug("REST request to get Desings : {}", id);
        Optional<Desings> desings = desingsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(desings);
    }

    /**
     * {@code DELETE  /desings/:id} : delete the "id" desings.
     *
     * @param id the id of the desings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/desings/{id}")
    public ResponseEntity<Void> deleteDesings(@PathVariable Long id) {
        log.debug("REST request to delete Desings : {}", id);
        desingsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
