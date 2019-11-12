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

import com.alphadevs.pos.domain.PurchaseOrder;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.PurchaseOrderRepository;
import com.alphadevs.pos.service.dto.PurchaseOrderCriteria;

/**
 * Service for executing complex queries for {@link PurchaseOrder} entities in the database.
 * The main input is a {@link PurchaseOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PurchaseOrder} or a {@link Page} of {@link PurchaseOrder} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PurchaseOrderQueryService extends QueryService<PurchaseOrder> {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderQueryService.class);

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderQueryService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    /**
     * Return a {@link List} of {@link PurchaseOrder} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findByCriteria(PurchaseOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PurchaseOrder> specification = createSpecification(criteria);
        return purchaseOrderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PurchaseOrder} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseOrder> findByCriteria(PurchaseOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PurchaseOrder> specification = createSpecification(criteria);
        return purchaseOrderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PurchaseOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PurchaseOrder> specification = createSpecification(criteria);
        return purchaseOrderRepository.count(specification);
    }

    /**
     * Function to convert {@link PurchaseOrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PurchaseOrder> createSpecification(PurchaseOrderCriteria criteria) {
        Specification<PurchaseOrder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PurchaseOrder_.id));
            }
            if (criteria.getPoNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPoNumber(), PurchaseOrder_.poNumber));
            }
            if (criteria.getPoDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoDate(), PurchaseOrder_.poDate));
            }
            if (criteria.getDetailsId() != null) {
                specification = specification.and(buildSpecification(criteria.getDetailsId(),
                    root -> root.join(PurchaseOrder_.details, JoinType.LEFT).get(PurchaseOrderDetails_.id)));
            }
            if (criteria.getSupplierId() != null) {
                specification = specification.and(buildSpecification(criteria.getSupplierId(),
                    root -> root.join(PurchaseOrder_.supplier, JoinType.LEFT).get(Supplier_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(PurchaseOrder_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getRelatedGRNId() != null) {
                specification = specification.and(buildSpecification(criteria.getRelatedGRNId(),
                    root -> root.join(PurchaseOrder_.relatedGRN, JoinType.LEFT).get(GoodsReceipt_.id)));
            }
        }
        return specification;
    }
}
