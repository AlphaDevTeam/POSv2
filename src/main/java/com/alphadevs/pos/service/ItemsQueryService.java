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

import com.alphadevs.pos.domain.Items;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.ItemsRepository;
import com.alphadevs.pos.service.dto.ItemsCriteria;

/**
 * Service for executing complex queries for {@link Items} entities in the database.
 * The main input is a {@link ItemsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Items} or a {@link Page} of {@link Items} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemsQueryService extends QueryService<Items> {

    private final Logger log = LoggerFactory.getLogger(ItemsQueryService.class);

    private final ItemsRepository itemsRepository;

    public ItemsQueryService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    /**
     * Return a {@link List} of {@link Items} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Items> findByCriteria(ItemsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Items> specification = createSpecification(criteria);
        return itemsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Items} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Items> findByCriteria(ItemsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Items> specification = createSpecification(criteria);
        return itemsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Items> specification = createSpecification(criteria);
        return itemsRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Items> createSpecification(ItemsCriteria criteria) {
        Specification<Items> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Items_.id));
            }
            if (criteria.getItemCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemCode(), Items_.itemCode));
            }
            if (criteria.getItemName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemName(), Items_.itemName));
            }
            if (criteria.getItemDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemDescription(), Items_.itemDescription));
            }
            if (criteria.getItemPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemPrice(), Items_.itemPrice));
            }
            if (criteria.getItemSerial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemSerial(), Items_.itemSerial));
            }
            if (criteria.getItemSupplierSerial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemSupplierSerial(), Items_.itemSupplierSerial));
            }
            if (criteria.getItemCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemCost(), Items_.itemCost));
            }
            if (criteria.getItemSalePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemSalePrice(), Items_.itemSalePrice));
            }
            if (criteria.getOriginalStockDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOriginalStockDate(), Items_.originalStockDate));
            }
            if (criteria.getModifiedStockDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedStockDate(), Items_.modifiedStockDate));
            }
            if (criteria.getRelatedDesignId() != null) {
                specification = specification.and(buildSpecification(criteria.getRelatedDesignId(),
                    root -> root.join(Items_.relatedDesign, JoinType.LEFT).get(Designs_.id)));
            }
            if (criteria.getRelatedProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getRelatedProductId(),
                    root -> root.join(Items_.relatedProduct, JoinType.LEFT).get(Products_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Items_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
