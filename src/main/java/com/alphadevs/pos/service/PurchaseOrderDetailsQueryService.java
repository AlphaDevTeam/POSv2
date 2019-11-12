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

import com.alphadevs.pos.domain.PurchaseOrderDetails;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.PurchaseOrderDetailsRepository;
import com.alphadevs.pos.service.dto.PurchaseOrderDetailsCriteria;

/**
 * Service for executing complex queries for {@link PurchaseOrderDetails} entities in the database.
 * The main input is a {@link PurchaseOrderDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PurchaseOrderDetails} or a {@link Page} of {@link PurchaseOrderDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PurchaseOrderDetailsQueryService extends QueryService<PurchaseOrderDetails> {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderDetailsQueryService.class);

    private final PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    public PurchaseOrderDetailsQueryService(PurchaseOrderDetailsRepository purchaseOrderDetailsRepository) {
        this.purchaseOrderDetailsRepository = purchaseOrderDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link PurchaseOrderDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseOrderDetails> findByCriteria(PurchaseOrderDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PurchaseOrderDetails> specification = createSpecification(criteria);
        return purchaseOrderDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PurchaseOrderDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseOrderDetails> findByCriteria(PurchaseOrderDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PurchaseOrderDetails> specification = createSpecification(criteria);
        return purchaseOrderDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PurchaseOrderDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PurchaseOrderDetails> specification = createSpecification(criteria);
        return purchaseOrderDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link PurchaseOrderDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PurchaseOrderDetails> createSpecification(PurchaseOrderDetailsCriteria criteria) {
        Specification<PurchaseOrderDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PurchaseOrderDetails_.id));
            }
            if (criteria.getItemQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemQty(), PurchaseOrderDetails_.itemQty));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(PurchaseOrderDetails_.item, JoinType.LEFT).get(Items_.id)));
            }
            if (criteria.getPoId() != null) {
                specification = specification.and(buildSpecification(criteria.getPoId(),
                    root -> root.join(PurchaseOrderDetails_.po, JoinType.LEFT).get(PurchaseOrder_.id)));
            }
        }
        return specification;
    }
}
