package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.PurchaseAccountBalance;
import com.alphadevs.pos.repository.PurchaseAccountBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PurchaseAccountBalance}.
 */
@Service
@Transactional
public class PurchaseAccountBalanceService {

    private final Logger log = LoggerFactory.getLogger(PurchaseAccountBalanceService.class);

    private final PurchaseAccountBalanceRepository purchaseAccountBalanceRepository;

    public PurchaseAccountBalanceService(PurchaseAccountBalanceRepository purchaseAccountBalanceRepository) {
        this.purchaseAccountBalanceRepository = purchaseAccountBalanceRepository;
    }

    /**
     * Save a purchaseAccountBalance.
     *
     * @param purchaseAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public PurchaseAccountBalance save(PurchaseAccountBalance purchaseAccountBalance) {
        log.debug("Request to save PurchaseAccountBalance : {}", purchaseAccountBalance);
        return purchaseAccountBalanceRepository.save(purchaseAccountBalance);
    }

    /**
     * Get all the purchaseAccountBalances.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseAccountBalance> findAll() {
        log.debug("Request to get all PurchaseAccountBalances");
        return purchaseAccountBalanceRepository.findAll();
    }


    /**
     * Get one purchaseAccountBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseAccountBalance> findOne(Long id) {
        log.debug("Request to get PurchaseAccountBalance : {}", id);
        return purchaseAccountBalanceRepository.findById(id);
    }

    /**
     * Delete the purchaseAccountBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseAccountBalance : {}", id);
        purchaseAccountBalanceRepository.deleteById(id);
    }
}
