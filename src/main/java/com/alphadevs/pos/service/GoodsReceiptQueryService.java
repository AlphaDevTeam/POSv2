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

import com.alphadevs.pos.domain.GoodsReceipt;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.GoodsReceiptRepository;
import com.alphadevs.pos.service.dto.GoodsReceiptCriteria;

/**
 * Service for executing complex queries for {@link GoodsReceipt} entities in the database.
 * The main input is a {@link GoodsReceiptCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GoodsReceipt} or a {@link Page} of {@link GoodsReceipt} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GoodsReceiptQueryService extends QueryService<GoodsReceipt> {

    private final Logger log = LoggerFactory.getLogger(GoodsReceiptQueryService.class);

    private final GoodsReceiptRepository goodsReceiptRepository;

    public GoodsReceiptQueryService(GoodsReceiptRepository goodsReceiptRepository) {
        this.goodsReceiptRepository = goodsReceiptRepository;
    }

    /**
     * Return a {@link List} of {@link GoodsReceipt} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GoodsReceipt> findByCriteria(GoodsReceiptCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GoodsReceipt> specification = createSpecification(criteria);
        return goodsReceiptRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GoodsReceipt} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GoodsReceipt> findByCriteria(GoodsReceiptCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GoodsReceipt> specification = createSpecification(criteria);
        return goodsReceiptRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GoodsReceiptCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GoodsReceipt> specification = createSpecification(criteria);
        return goodsReceiptRepository.count(specification);
    }

    /**
     * Function to convert {@link GoodsReceiptCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GoodsReceipt> createSpecification(GoodsReceiptCriteria criteria) {
        Specification<GoodsReceipt> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GoodsReceipt_.id));
            }
            if (criteria.getGrnNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGrnNumber(), GoodsReceipt_.grnNumber));
            }
            if (criteria.getGrnDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGrnDate(), GoodsReceipt_.grnDate));
            }
            if (criteria.getPoNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPoNumber(), GoodsReceipt_.poNumber));
            }
            if (criteria.getDetailsId() != null) {
                specification = specification.and(buildSpecification(criteria.getDetailsId(),
                    root -> root.join(GoodsReceipt_.details, JoinType.LEFT).get(GoodsReceiptDetails_.id)));
            }
            if (criteria.getLinkedPOsId() != null) {
                specification = specification.and(buildSpecification(criteria.getLinkedPOsId(),
                    root -> root.join(GoodsReceipt_.linkedPOs, JoinType.LEFT).get(PurchaseOrder_.id)));
            }
            if (criteria.getSupplierId() != null) {
                specification = specification.and(buildSpecification(criteria.getSupplierId(),
                    root -> root.join(GoodsReceipt_.supplier, JoinType.LEFT).get(Supplier_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(GoodsReceipt_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
