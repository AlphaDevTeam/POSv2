package com.alphadevs.pos.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.alphadevs.pos.domain.Worker;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.WorkerRepository;
import com.alphadevs.pos.service.dto.WorkerCriteria;

/**
 * Service for executing complex queries for {@link Worker} entities in the database.
 * The main input is a {@link WorkerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Worker} or a {@link Page} of {@link Worker} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkerQueryService extends QueryService<Worker> {

    private final Logger log = LoggerFactory.getLogger(WorkerQueryService.class);

    private final WorkerRepository workerRepository;

    public WorkerQueryService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    /**
     * Return a {@link List} of {@link Worker} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Worker> findByCriteria(WorkerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Worker> specification = createSpecification(criteria);
        return workerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Worker} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Worker> findByCriteria(WorkerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Worker> specification = createSpecification(criteria);
        return workerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Worker> specification = createSpecification(criteria);
        return workerRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Worker> createSpecification(WorkerCriteria criteria) {
        Specification<Worker> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Worker_.id));
            }
            if (criteria.getWorkerCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWorkerCode(), Worker_.workerCode));
            }
            if (criteria.getWorkerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWorkerName(), Worker_.workerName));
            }
            if (criteria.getWorkerLimit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWorkerLimit(), Worker_.workerLimit));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Worker_.isActive));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Worker_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getJobsId() != null) {
                specification = specification.and(buildSpecification(criteria.getJobsId(),
                    root -> root.join(Worker_.jobs, JoinType.LEFT).get(Job_.id)));
            }
        }
        return specification;
    }
}
