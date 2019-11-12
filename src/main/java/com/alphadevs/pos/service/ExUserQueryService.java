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

import com.alphadevs.pos.domain.ExUser;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.ExUserRepository;
import com.alphadevs.pos.service.dto.ExUserCriteria;

/**
 * Service for executing complex queries for {@link ExUser} entities in the database.
 * The main input is a {@link ExUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExUser} or a {@link Page} of {@link ExUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExUserQueryService extends QueryService<ExUser> {

    private final Logger log = LoggerFactory.getLogger(ExUserQueryService.class);

    private final ExUserRepository exUserRepository;

    public ExUserQueryService(ExUserRepository exUserRepository) {
        this.exUserRepository = exUserRepository;
    }

    /**
     * Return a {@link List} of {@link ExUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExUser> findByCriteria(ExUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExUser> specification = createSpecification(criteria);
        return exUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExUser> findByCriteria(ExUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExUser> specification = createSpecification(criteria);
        return exUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExUser> specification = createSpecification(criteria);
        return exUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ExUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExUser> createSpecification(ExUserCriteria criteria) {
        Specification<ExUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ExUser_.id));
            }
            if (criteria.getUserKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserKey(), ExUser_.userKey));
            }
            if (criteria.getRelatedUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getRelatedUserId(),
                    root -> root.join(ExUser_.relatedUser, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildSpecification(criteria.getCompanyId(),
                    root -> root.join(ExUser_.company, JoinType.LEFT).get(Company_.id)));
            }
            if (criteria.getLocationsId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationsId(),
                    root -> root.join(ExUser_.locations, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
