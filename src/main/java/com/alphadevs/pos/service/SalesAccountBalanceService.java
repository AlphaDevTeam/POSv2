package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.SalesAccountBalance;
import com.alphadevs.pos.repository.SalesAccountBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link SalesAccountBalance}.
 */
@Service
@Transactional
public class SalesAccountBalanceService {

    private final Logger log = LoggerFactory.getLogger(SalesAccountBalanceService.class);

    private final SalesAccountBalanceRepository salesAccountBalanceRepository;

    public SalesAccountBalanceService(SalesAccountBalanceRepository salesAccountBalanceRepository) {
        this.salesAccountBalanceRepository = salesAccountBalanceRepository;
    }

    /**
     * Save a salesAccountBalance.
     *
     * @param salesAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public SalesAccountBalance save(SalesAccountBalance salesAccountBalance) {
        log.debug("Request to save SalesAccountBalance : {}", salesAccountBalance);
        return salesAccountBalanceRepository.save(salesAccountBalance);
    }

    /**
     * Get all the salesAccountBalances.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SalesAccountBalance> findAll() {
        log.debug("Request to get all SalesAccountBalances");
        return salesAccountBalanceRepository.findAll();
    }


    /**
     * Get one salesAccountBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SalesAccountBalance> findOne(Long id) {
        log.debug("Request to get SalesAccountBalance : {}", id);
        return salesAccountBalanceRepository.findById(id);
    }

    /**
     * Delete the salesAccountBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SalesAccountBalance : {}", id);
        salesAccountBalanceRepository.deleteById(id);
    }
}
