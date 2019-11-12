package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.SupplierAccount;
import com.alphadevs.pos.repository.SupplierAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link SupplierAccount}.
 */
@Service
@Transactional
public class SupplierAccountService {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountService.class);

    private final SupplierAccountRepository supplierAccountRepository;

    public SupplierAccountService(SupplierAccountRepository supplierAccountRepository) {
        this.supplierAccountRepository = supplierAccountRepository;
    }

    /**
     * Save a supplierAccount.
     *
     * @param supplierAccount the entity to save.
     * @return the persisted entity.
     */
    public SupplierAccount save(SupplierAccount supplierAccount) {
        log.debug("Request to save SupplierAccount : {}", supplierAccount);
        return supplierAccountRepository.save(supplierAccount);
    }

    /**
     * Get all the supplierAccounts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SupplierAccount> findAll() {
        log.debug("Request to get all SupplierAccounts");
        return supplierAccountRepository.findAll();
    }


    /**
     * Get one supplierAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierAccount> findOne(Long id) {
        log.debug("Request to get SupplierAccount : {}", id);
        return supplierAccountRepository.findById(id);
    }

    /**
     * Delete the supplierAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SupplierAccount : {}", id);
        supplierAccountRepository.deleteById(id);
    }
}
