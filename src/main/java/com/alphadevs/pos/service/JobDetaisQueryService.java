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

import com.alphadevs.pos.domain.JobDetais;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.JobDetaisRepository;
import com.alphadevs.pos.service.dto.JobDetaisCriteria;

/**
 * Service for executing complex queries for {@link JobDetais} entities in the database.
 * The main input is a {@link JobDetaisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JobDetais} or a {@link Page} of {@link JobDetais} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobDetaisQueryService extends QueryService<JobDetais> {

    private final Logger log = LoggerFactory.getLogger(JobDetaisQueryService.class);

    private final JobDetaisRepository jobDetaisRepository;

    public JobDetaisQueryService(JobDetaisRepository jobDetaisRepository) {
        this.jobDetaisRepository = jobDetaisRepository;
    }

    /**
     * Return a {@link List} of {@link JobDetais} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobDetais> findByCriteria(JobDetaisCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<JobDetais> specification = createSpecification(criteria);
        return jobDetaisRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link JobDetais} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobDetais> findByCriteria(JobDetaisCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JobDetais> specification = createSpecification(criteria);
        return jobDetaisRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobDetaisCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<JobDetais> specification = createSpecification(criteria);
        return jobDetaisRepository.count(specification);
    }

    /**
     * Function to convert {@link JobDetaisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<JobDetais> createSpecification(JobDetaisCriteria criteria) {
        Specification<JobDetais> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), JobDetais_.id));
            }
            if (criteria.getJobItemPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobItemPrice(), JobDetais_.jobItemPrice));
            }
            if (criteria.getJobItemQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobItemQty(), JobDetais_.jobItemQty));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(JobDetais_.item, JoinType.LEFT).get(Items_.id)));
            }
            if (criteria.getJobId() != null) {
                specification = specification.and(buildSpecification(criteria.getJobId(),
                    root -> root.join(JobDetais_.job, JoinType.LEFT).get(Job_.id)));
            }
        }
        return specification;
    }
}
