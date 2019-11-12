package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.PurchaseOrder;
import com.alphadevs.pos.repository.PurchaseOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PurchaseOrder}.
 */
@Service
@Transactional
public class PurchaseOrderService {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderService.class);

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    /**
     * Save a purchaseOrder.
     *
     * @param purchaseOrder the entity to save.
     * @return the persisted entity.
     */
    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        log.debug("Request to save PurchaseOrder : {}", purchaseOrder);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    /**
     * Get all the purchaseOrders.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseOrder> findAll() {
        log.debug("Request to get all PurchaseOrders");
        return purchaseOrderRepository.findAll();
    }


    /**
     * Get one purchaseOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> findOne(Long id) {
        log.debug("Request to get PurchaseOrder : {}", id);
        return purchaseOrderRepository.findById(id);
    }

    /**
     * Delete the purchaseOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseOrder : {}", id);
        purchaseOrderRepository.deleteById(id);
    }
}
