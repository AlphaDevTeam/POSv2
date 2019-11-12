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

import com.alphadevs.pos.domain.ItemBinCard;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.ItemBinCardRepository;
import com.alphadevs.pos.service.dto.ItemBinCardCriteria;

/**
 * Service for executing complex queries for {@link ItemBinCard} entities in the database.
 * The main input is a {@link ItemBinCardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ItemBinCard} or a {@link Page} of {@link ItemBinCard} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemBinCardQueryService extends QueryService<ItemBinCard> {

    private final Logger log = LoggerFactory.getLogger(ItemBinCardQueryService.class);

    private final ItemBinCardRepository itemBinCardRepository;

    public ItemBinCardQueryService(ItemBinCardRepository itemBinCardRepository) {
        this.itemBinCardRepository = itemBinCardRepository;
    }

    /**
     * Return a {@link List} of {@link ItemBinCard} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ItemBinCard> findByCriteria(ItemBinCardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ItemBinCard> specification = createSpecification(criteria);
        return itemBinCardRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ItemBinCard} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemBinCard> findByCriteria(ItemBinCardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ItemBinCard> specification = createSpecification(criteria);
        return itemBinCardRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemBinCardCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ItemBinCard> specification = createSpecification(criteria);
        return itemBinCardRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemBinCardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ItemBinCard> createSpecification(ItemBinCardCriteria criteria) {
        Specification<ItemBinCard> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ItemBinCard_.id));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), ItemBinCard_.transactionDate));
            }
            if (criteria.getTransactionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionDescription(), ItemBinCard_.transactionDescription));
            }
            if (criteria.getTransactionQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionQty(), ItemBinCard_.transactionQty));
            }
            if (criteria.getTransactionBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionBalance(), ItemBinCard_.transactionBalance));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(ItemBinCard_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(ItemBinCard_.item, JoinType.LEFT).get(Items_.id)));
            }
        }
        return specification;
    }
}
