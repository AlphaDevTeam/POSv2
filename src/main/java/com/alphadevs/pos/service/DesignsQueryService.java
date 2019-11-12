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

import com.alphadevs.pos.domain.Designs;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.DesignsRepository;
import com.alphadevs.pos.service.dto.DesignsCriteria;

/**
 * Service for executing complex queries for {@link Designs} entities in the database.
 * The main input is a {@link DesignsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Designs} or a {@link Page} of {@link Designs} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DesignsQueryService extends QueryService<Designs> {

    private final Logger log = LoggerFactory.getLogger(DesignsQueryService.class);

    private final DesignsRepository designsRepository;

    public DesignsQueryService(DesignsRepository designsRepository) {
        this.designsRepository = designsRepository;
    }

    /**
     * Return a {@link List} of {@link Designs} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Designs> findByCriteria(DesignsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Designs> specification = createSpecification(criteria);
        return designsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Designs} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Designs> findByCriteria(DesignsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Designs> specification = createSpecification(criteria);
        return designsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DesignsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Designs> specification = createSpecification(criteria);
        return designsRepository.count(specification);
    }

    /**
     * Function to convert {@link DesignsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Designs> createSpecification(DesignsCriteria criteria) {
        Specification<Designs> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Designs_.id));
            }
            if (criteria.getDesignCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesignCode(), Designs_.designCode));
            }
            if (criteria.getDesignName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesignName(), Designs_.designName));
            }
            if (criteria.getDesignPrefix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesignPrefix(), Designs_.designPrefix));
            }
            if (criteria.getDesignProfMargin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDesignProfMargin(), Designs_.designProfMargin));
            }
            if (criteria.getRelatedProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getRelatedProductId(),
                    root -> root.join(Designs_.relatedProduct, JoinType.LEFT).get(Products_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Designs_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
