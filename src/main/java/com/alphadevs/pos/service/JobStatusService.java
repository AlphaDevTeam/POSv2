package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.JobStatus;
import com.alphadevs.pos.repository.JobStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link JobStatus}.
 */
@Service
@Transactional
public class JobStatusService {

    private final Logger log = LoggerFactory.getLogger(JobStatusService.class);

    private final JobStatusRepository jobStatusRepository;

    public JobStatusService(JobStatusRepository jobStatusRepository) {
        this.jobStatusRepository = jobStatusRepository;
    }

    /**
     * Save a jobStatus.
     *
     * @param jobStatus the entity to save.
     * @return the persisted entity.
     */
    public JobStatus save(JobStatus jobStatus) {
        log.debug("Request to save JobStatus : {}", jobStatus);
        return jobStatusRepository.save(jobStatus);
    }

    /**
     * Get all the jobStatuses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<JobStatus> findAll() {
        log.debug("Request to get all JobStatuses");
        return jobStatusRepository.findAll();
    }


    /**
     * Get one jobStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JobStatus> findOne(Long id) {
        log.debug("Request to get JobStatus : {}", id);
        return jobStatusRepository.findById(id);
    }

    /**
     * Delete the jobStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete JobStatus : {}", id);
        jobStatusRepository.deleteById(id);
    }
}
