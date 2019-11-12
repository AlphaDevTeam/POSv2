package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.CashBookBalance;
import com.alphadevs.pos.repository.CashBookBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CashBookBalance}.
 */
@Service
@Transactional
public class CashBookBalanceService {

    private final Logger log = LoggerFactory.getLogger(CashBookBalanceService.class);

    private final CashBookBalanceRepository cashBookBalanceRepository;

    public CashBookBalanceService(CashBookBalanceRepository cashBookBalanceRepository) {
        this.cashBookBalanceRepository = cashBookBalanceRepository;
    }

    /**
     * Save a cashBookBalance.
     *
     * @param cashBookBalance the entity to save.
     * @return the persisted entity.
     */
    public CashBookBalance save(CashBookBalance cashBookBalance) {
        log.debug("Request to save CashBookBalance : {}", cashBookBalance);
        return cashBookBalanceRepository.save(cashBookBalance);
    }

    /**
     * Get all the cashBookBalances.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CashBookBalance> findAll() {
        log.debug("Request to get all CashBookBalances");
        return cashBookBalanceRepository.findAll();
    }


    /**
     * Get one cashBookBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashBookBalance> findOne(Long id) {
        log.debug("Request to get CashBookBalance : {}", id);
        return cashBookBalanceRepository.findById(id);
    }

    /**
     * Delete the cashBookBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CashBookBalance : {}", id);
        cashBookBalanceRepository.deleteById(id);
    }
}
