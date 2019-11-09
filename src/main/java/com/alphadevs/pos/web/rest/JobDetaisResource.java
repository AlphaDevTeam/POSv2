package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.JobDetais;
import com.alphadevs.pos.repository.JobDetaisRepository;
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
 * REST controller for managing {@link com.alphadevs.pos.domain.JobDetais}.
 */
@RestController
@RequestMapping("/api")
public class JobDetaisResource {

    private final Logger log = LoggerFactory.getLogger(JobDetaisResource.class);

    private static final String ENTITY_NAME = "jobDetais";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobDetaisRepository jobDetaisRepository;

    public JobDetaisResource(JobDetaisRepository jobDetaisRepository) {
        this.jobDetaisRepository = jobDetaisRepository;
    }

    /**
     * {@code POST  /job-detais} : Create a new jobDetais.
     *
     * @param jobDetais the jobDetais to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobDetais, or with status {@code 400 (Bad Request)} if the jobDetais has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-detais")
    public ResponseEntity<JobDetais> createJobDetais(@Valid @RequestBody JobDetais jobDetais) throws URISyntaxException {
        log.debug("REST request to save JobDetais : {}", jobDetais);
        if (jobDetais.getId() != null) {
            throw new BadRequestAlertException("A new jobDetais cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobDetais result = jobDetaisRepository.save(jobDetais);
        return ResponseEntity.created(new URI("/api/job-detais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-detais} : Updates an existing jobDetais.
     *
     * @param jobDetais the jobDetais to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobDetais,
     * or with status {@code 400 (Bad Request)} if the jobDetais is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobDetais couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-detais")
    public ResponseEntity<JobDetais> updateJobDetais(@Valid @RequestBody JobDetais jobDetais) throws URISyntaxException {
        log.debug("REST request to update JobDetais : {}", jobDetais);
        if (jobDetais.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JobDetais result = jobDetaisRepository.save(jobDetais);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jobDetais.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /job-detais} : get all the jobDetais.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobDetais in body.
     */
    @GetMapping("/job-detais")
    public List<JobDetais> getAllJobDetais() {
        log.debug("REST request to get all JobDetais");
        return jobDetaisRepository.findAll();
    }

    /**
     * {@code GET  /job-detais/:id} : get the "id" jobDetais.
     *
     * @param id the id of the jobDetais to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobDetais, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-detais/{id}")
    public ResponseEntity<JobDetais> getJobDetais(@PathVariable Long id) {
        log.debug("REST request to get JobDetais : {}", id);
        Optional<JobDetais> jobDetais = jobDetaisRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(jobDetais);
    }

    /**
     * {@code DELETE  /job-detais/:id} : delete the "id" jobDetais.
     *
     * @param id the id of the jobDetais to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-detais/{id}")
    public ResponseEntity<Void> deleteJobDetais(@PathVariable Long id) {
        log.debug("REST request to delete JobDetais : {}", id);
        jobDetaisRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
