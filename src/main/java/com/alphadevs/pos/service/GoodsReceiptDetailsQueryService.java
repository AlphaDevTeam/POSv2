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

import com.alphadevs.pos.domain.GoodsReceiptDetails;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.GoodsReceiptDetailsRepository;
import com.alphadevs.pos.service.dto.GoodsReceiptDetailsCriteria;

/**
 * Service for executing complex queries for {@link GoodsReceiptDetails} entities in the database.
 * The main input is a {@link GoodsReceiptDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GoodsReceiptDetails} or a {@link Page} of {@link GoodsReceiptDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GoodsReceiptDetailsQueryService extends QueryService<GoodsReceiptDetails> {

    private final Logger log = LoggerFactory.getLogger(GoodsReceiptDetailsQueryService.class);

    private final GoodsReceiptDetailsRepository goodsReceiptDetailsRepository;

    public GoodsReceiptDetailsQueryService(GoodsReceiptDetailsRepository goodsReceiptDetailsRepository) {
        this.goodsReceiptDetailsRepository = goodsReceiptDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link GoodsReceiptDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GoodsReceiptDetails> findByCriteria(GoodsReceiptDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GoodsReceiptDetails> specification = createSpecification(criteria);
        return goodsReceiptDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GoodsReceiptDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GoodsReceiptDetails> findByCriteria(GoodsReceiptDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GoodsReceiptDetails> specification = createSpecification(criteria);
        return goodsReceiptDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GoodsReceiptDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GoodsReceiptDetails> specification = createSpecification(criteria);
        return goodsReceiptDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link GoodsReceiptDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GoodsReceiptDetails> createSpecification(GoodsReceiptDetailsCriteria criteria) {
        Specification<GoodsReceiptDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GoodsReceiptDetails_.id));
            }
            if (criteria.getGrnQty() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGrnQty(), GoodsReceiptDetails_.grnQty));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(GoodsReceiptDetails_.item, JoinType.LEFT).get(Items_.id)));
            }
            if (criteria.getGrnId() != null) {
                specification = specification.and(buildSpecification(criteria.getGrnId(),
                    root -> root.join(GoodsReceiptDetails_.grn, JoinType.LEFT).get(GoodsReceipt_.id)));
            }
        }
        return specification;
    }
}
