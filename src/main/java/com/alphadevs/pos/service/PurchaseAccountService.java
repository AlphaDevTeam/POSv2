package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.PurchaseAccount;
import com.alphadevs.pos.repository.PurchaseAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PurchaseAccount}.
 */
@Service
@Transactional
public class PurchaseAccountService {

    private final Logger log = LoggerFactory.getLogger(PurchaseAccountService.class);

    private final PurchaseAccountRepository purchaseAccountRepository;

    public PurchaseAccountService(PurchaseAccountRepository purchaseAccountRepository) {
        this.purchaseAccountRepository = purchaseAccountRepository;
    }

    /**
     * Save a purchaseAccount.
     *
     * @param purchaseAccount the entity to save.
     * @return the persisted entity.
     */
    public PurchaseAccount save(PurchaseAccount purchaseAccount) {
        log.debug("Request to save PurchaseAccount : {}", purchaseAccount);
        return purchaseAccountRepository.save(purchaseAccount);
    }

    /**
     * Get all the purchaseAccounts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseAccount> findAll() {
        log.debug("Request to get all PurchaseAccounts");
        return purchaseAccountRepository.findAll();
    }


    /**
     * Get one purchaseAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseAccount> findOne(Long id) {
        log.debug("Request to get PurchaseAccount : {}", id);
        return purchaseAccountRepository.findById(id);
    }

    /**
     * Delete the purchaseAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseAccount : {}", id);
        purchaseAccountRepository.deleteById(id);
    }
}
