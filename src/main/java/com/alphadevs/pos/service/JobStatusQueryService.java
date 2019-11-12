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

import com.alphadevs.pos.domain.JobStatus;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.JobStatusRepository;
import com.alphadevs.pos.service.dto.JobStatusCriteria;

/**
 * Service for executing complex queries for {@link JobStatus} entities in the database.
 * The main input is a {@link JobStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JobStatus} or a {@link Page} of {@link JobStatus} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobStatusQueryService extends QueryService<JobStatus> {

    private final Logger log = LoggerFactory.getLogger(JobStatusQueryService.class);

    private final JobStatusRepository jobStatusRepository;

    public JobStatusQueryService(JobStatusRepository jobStatusRepository) {
        this.jobStatusRepository = jobStatusRepository;
    }

    /**
     * Return a {@link List} of {@link JobStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobStatus> findByCriteria(JobStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<JobStatus> specification = createSpecification(criteria);
        return jobStatusRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link JobStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobStatus> findByCriteria(JobStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JobStatus> specification = createSpecification(criteria);
        return jobStatusRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<JobStatus> specification = createSpecification(criteria);
        return jobStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link JobStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<JobStatus> createSpecification(JobStatusCriteria criteria) {
        Specification<JobStatus> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), JobStatus_.id));
            }
            if (criteria.getJobStatusCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobStatusCode(), JobStatus_.jobStatusCode));
            }
            if (criteria.getJobStatusDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobStatusDescription(), JobStatus_.jobStatusDescription));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), JobStatus_.isActive));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(JobStatus_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
