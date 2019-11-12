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

import com.alphadevs.pos.domain.Products;
import com.alphadevs.pos.domain.*; // for static metamodels
import com.alphadevs.pos.repository.ProductsRepository;
import com.alphadevs.pos.service.dto.ProductsCriteria;

/**
 * Service for executing complex queries for {@link Products} entities in the database.
 * The main input is a {@link ProductsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Products} or a {@link Page} of {@link Products} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductsQueryService extends QueryService<Products> {

    private final Logger log = LoggerFactory.getLogger(ProductsQueryService.class);

    private final ProductsRepository productsRepository;

    public ProductsQueryService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    /**
     * Return a {@link List} of {@link Products} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Products> findByCriteria(ProductsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Products> specification = createSpecification(criteria);
        return productsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Products} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Products> findByCriteria(ProductsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Products> specification = createSpecification(criteria);
        return productsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Products> specification = createSpecification(criteria);
        return productsRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Products> createSpecification(ProductsCriteria criteria) {
        Specification<Products> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Products_.id));
            }
            if (criteria.getProductCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductCode(), Products_.productCode));
            }
            if (criteria.getProductName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductName(), Products_.productName));
            }
            if (criteria.getProductPrefix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductPrefix(), Products_.productPrefix));
            }
            if (criteria.getProductProfMargin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProductProfMargin(), Products_.productProfMargin));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Products_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
