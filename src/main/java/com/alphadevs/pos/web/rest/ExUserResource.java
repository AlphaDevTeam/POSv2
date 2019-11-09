package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.ExUser;
import com.alphadevs.pos.repository.ExUserRepository;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alphadevs.pos.domain.ExUser}.
 */
@RestController
@RequestMapping("/api")
public class ExUserResource {

    private final Logger log = LoggerFactory.getLogger(ExUserResource.class);

    private static final String ENTITY_NAME = "exUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExUserRepository exUserRepository;

    public ExUserResource(ExUserRepository exUserRepository) {
        this.exUserRepository = exUserRepository;
    }

    /**
     * {@code POST  /ex-users} : Create a new exUser.
     *
     * @param exUser the exUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exUser, or with status {@code 400 (Bad Request)} if the exUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ex-users")
    public ResponseEntity<ExUser> createExUser(@RequestBody ExUser exUser) throws URISyntaxException {
        log.debug("REST request to save ExUser : {}", exUser);
        if (exUser.getId() != null) {
            throw new BadRequestAlertException("A new exUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExUser result = exUserRepository.save(exUser);
        return ResponseEntity.created(new URI("/api/ex-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ex-users} : Updates an existing exUser.
     *
     * @param exUser the exUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exUser,
     * or with status {@code 400 (Bad Request)} if the exUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ex-users")
    public ResponseEntity<ExUser> updateExUser(@RequestBody ExUser exUser) throws URISyntaxException {
        log.debug("REST request to update ExUser : {}", exUser);
        if (exUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExUser result = exUserRepository.save(exUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ex-users} : get all the exUsers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exUsers in body.
     */
    @GetMapping("/ex-users")
    public List<ExUser> getAllExUsers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ExUsers");
        return exUserRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /ex-users/:id} : get the "id" exUser.
     *
     * @param id the id of the exUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ex-users/{id}")
    public ResponseEntity<ExUser> getExUser(@PathVariable Long id) {
        log.debug("REST request to get ExUser : {}", id);
        Optional<ExUser> exUser = exUserRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(exUser);
    }

    /**
     * {@code DELETE  /ex-users/:id} : delete the "id" exUser.
     *
     * @param id the id of the exUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ex-users/{id}")
    public ResponseEntity<Void> deleteExUser(@PathVariable Long id) {
        log.debug("REST request to delete ExUser : {}", id);
        exUserRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
