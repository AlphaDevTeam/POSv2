package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.Products;
import com.alphadevs.pos.repository.ProductsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Products}.
 */
@Service
@Transactional
public class ProductsService {

    private final Logger log = LoggerFactory.getLogger(ProductsService.class);

    private final ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    /**
     * Save a products.
     *
     * @param products the entity to save.
     * @return the persisted entity.
     */
    public Products save(Products products) {
        log.debug("Request to save Products : {}", products);
        return productsRepository.save(products);
    }

    /**
     * Get all the products.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Products> findAll() {
        log.debug("Request to get all Products");
        return productsRepository.findAll();
    }


    /**
     * Get one products by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Products> findOne(Long id) {
        log.debug("Request to get Products : {}", id);
        return productsRepository.findById(id);
    }

    /**
     * Delete the products by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Products : {}", id);
        productsRepository.deleteById(id);
    }
}
