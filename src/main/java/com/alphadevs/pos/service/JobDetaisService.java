package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.JobDetais;
import com.alphadevs.pos.repository.JobDetaisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link JobDetais}.
 */
@Service
@Transactional
public class JobDetaisService {

    private final Logger log = LoggerFactory.getLogger(JobDetaisService.class);

    private final JobDetaisRepository jobDetaisRepository;

    public JobDetaisService(JobDetaisRepository jobDetaisRepository) {
        this.jobDetaisRepository = jobDetaisRepository;
    }

    /**
     * Save a jobDetais.
     *
     * @param jobDetais the entity to save.
     * @return the persisted entity.
     */
    public JobDetais save(JobDetais jobDetais) {
        log.debug("Request to save JobDetais : {}", jobDetais);
        return jobDetaisRepository.save(jobDetais);
    }

    /**
     * Get all the jobDetais.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<JobDetais> findAll() {
        log.debug("Request to get all JobDetais");
        return jobDetaisRepository.findAll();
    }


    /**
     * Get one jobDetais by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JobDetais> findOne(Long id) {
        log.debug("Request to get JobDetais : {}", id);
        return jobDetaisRepository.findById(id);
    }

    /**
     * Delete the jobDetais by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete JobDetais : {}", id);
        jobDetaisRepository.deleteById(id);
    }
}
