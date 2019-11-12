package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.domain.JobStatus;
import com.alphadevs.pos.service.JobStatusService;
import com.alphadevs.pos.web.rest.errors.BadRequestAlertException;
import com.alphadevs.pos.service.dto.JobStatusCriteria;
import com.alphadevs.pos.service.JobStatusQueryService;

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
 * REST controller for managing {@link com.alphadevs.pos.domain.JobStatus}.
 */
@RestController
@RequestMapping("/api")
public class JobStatusResource {

    private final Logger log = LoggerFactory.getLogger(JobStatusResource.class);

    private static final String ENTITY_NAME = "jobStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobStatusService jobStatusService;

    private final JobStatusQueryService jobStatusQueryService;

    public JobStatusResource(JobStatusService jobStatusService, JobStatusQueryService jobStatusQueryService) {
        this.jobStatusService = jobStatusService;
        this.jobStatusQueryService = jobStatusQueryService;
    }

    /**
     * {@code POST  /job-statuses} : Create a new jobStatus.
     *
     * @param jobStatus the jobStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobStatus, or with status {@code 400 (Bad Request)} if the jobStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-statuses")
    public ResponseEntity<JobStatus> createJobStatus(@Valid @RequestBody JobStatus jobStatus) throws URISyntaxException {
        log.debug("REST request to save JobStatus : {}", jobStatus);
        if (jobStatus.getId() != null) {
            throw new BadRequestAlertException("A new jobStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobStatus result = jobStatusService.save(jobStatus);
        return ResponseEntity.created(new URI("/api/job-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-statuses} : Updates an existing jobStatus.
     *
     * @param jobStatus the jobStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobStatus,
     * or with status {@code 400 (Bad Request)} if the jobStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-statuses")
    public ResponseEntity<JobStatus> updateJobStatus(@Valid @RequestBody JobStatus jobStatus) throws URISyntaxException {
        log.debug("REST request to update JobStatus : {}", jobStatus);
        if (jobStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JobStatus result = jobStatusService.save(jobStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jobStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /job-statuses} : get all the jobStatuses.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobStatuses in body.
     */
    @GetMapping("/job-statuses")
    public ResponseEntity<List<JobStatus>> getAllJobStatuses(JobStatusCriteria criteria) {
        log.debug("REST request to get JobStatuses by criteria: {}", criteria);
        List<JobStatus> entityList = jobStatusQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /job-statuses/count} : count all the jobStatuses.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/job-statuses/count")
    public ResponseEntity<Long> countJobStatuses(JobStatusCriteria criteria) {
        log.debug("REST request to count JobStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(jobStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /job-statuses/:id} : get the "id" jobStatus.
     *
     * @param id the id of the jobStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-statuses/{id}")
    public ResponseEntity<JobStatus> getJobStatus(@PathVariable Long id) {
        log.debug("REST request to get JobStatus : {}", id);
        Optional<JobStatus> jobStatus = jobStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobStatus);
    }

    /**
     * {@code DELETE  /job-statuses/:id} : delete the "id" jobStatus.
     *
     * @param id the id of the jobStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-statuses/{id}")
    public ResponseEntity<Void> deleteJobStatus(@PathVariable Long id) {
        log.debug("REST request to delete JobStatus : {}", id);
        jobStatusService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
