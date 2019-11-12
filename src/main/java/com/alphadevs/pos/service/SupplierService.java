package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.Supplier;
import com.alphadevs.pos.repository.SupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Supplier}.
 */
@Service
@Transactional
public class SupplierService {

    private final Logger log = LoggerFactory.getLogger(SupplierService.class);

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    /**
     * Save a supplier.
     *
     * @param supplier the entity to save.
     * @return the persisted entity.
     */
    public Supplier save(Supplier supplier) {
        log.debug("Request to save Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    /**
     * Get all the suppliers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Supplier> findAll() {
        log.debug("Request to get all Suppliers");
        return supplierRepository.findAll();
    }


    /**
     * Get one supplier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Supplier> findOne(Long id) {
        log.debug("Request to get Supplier : {}", id);
        return supplierRepository.findById(id);
    }

    /**
     * Delete the supplier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Supplier : {}", id);
        supplierRepository.deleteById(id);
    }
}
