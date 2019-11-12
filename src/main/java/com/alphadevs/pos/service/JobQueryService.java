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

import com.alphadevs.pos.domain.Job;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.JobRepository;
import com.alphadevs.pos.service.dto.JobCriteria;

/**
 * Service for executing complex queries for {@link Job} entities in the database.
 * The main input is a {@link JobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Job} or a {@link Page} of {@link Job} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobQueryService extends QueryService<Job> {

    private final Logger log = LoggerFactory.getLogger(JobQueryService.class);

    private final JobRepository jobRepository;

    public JobQueryService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Return a {@link List} of {@link Job} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Job> findByCriteria(JobCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Job} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Job> findByCriteria(JobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.count(specification);
    }

    /**
     * Function to convert {@link JobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Job> createSpecification(JobCriteria criteria) {
        Specification<Job> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Job_.id));
            }
            if (criteria.getJobCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobCode(), Job_.jobCode));
            }
            if (criteria.getJobDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobDescription(), Job_.jobDescription));
            }
            if (criteria.getJobStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobStartDate(), Job_.jobStartDate));
            }
            if (criteria.getJobEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobEndDate(), Job_.jobEndDate));
            }
            if (criteria.getJobAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobAmount(), Job_.jobAmount));
            }
            if (criteria.getStatusId() != null) {
                specification = specification.and(buildSpecification(criteria.getStatusId(),
                    root -> root.join(Job_.status, JoinType.LEFT).get(JobStatus_.id)));
            }
            if (criteria.getDetailsId() != null) {
                specification = specification.and(buildSpecification(criteria.getDetailsId(),
                    root -> root.join(Job_.details, JoinType.LEFT).get(JobDetais_.id)));
            }
            if (criteria.getAssignedToId() != null) {
                specification = specification.and(buildSpecification(criteria.getAssignedToId(),
                    root -> root.join(Job_.assignedTos, JoinType.LEFT).get(Worker_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Job_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(Job_.customer, JoinType.LEFT).get(Customer_.id)));
            }
        }
        return specification;
    }
}
