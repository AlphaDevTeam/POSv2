package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.Worker;
import com.alphadevs.pos.repository.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Worker}.
 */
@Service
@Transactional
public class WorkerService {

    private final Logger log = LoggerFactory.getLogger(WorkerService.class);

    private final WorkerRepository workerRepository;

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    /**
     * Save a worker.
     *
     * @param worker the entity to save.
     * @return the persisted entity.
     */
    public Worker save(Worker worker) {
        log.debug("Request to save Worker : {}", worker);
        return workerRepository.save(worker);
    }

    /**
     * Get all the workers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Worker> findAll() {
        log.debug("Request to get all Workers");
        return workerRepository.findAll();
    }


    /**
     * Get one worker by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Worker> findOne(Long id) {
        log.debug("Request to get Worker : {}", id);
        return workerRepository.findById(id);
    }

    /**
     * Delete the worker by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Worker : {}", id);
        workerRepository.deleteById(id);
    }
}
