package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.CashBook;
import com.alphadevs.pos.repository.CashBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CashBook}.
 */
@Service
@Transactional
public class CashBookService {

    private final Logger log = LoggerFactory.getLogger(CashBookService.class);

    private final CashBookRepository cashBookRepository;

    public CashBookService(CashBookRepository cashBookRepository) {
        this.cashBookRepository = cashBookRepository;
    }

    /**
     * Save a cashBook.
     *
     * @param cashBook the entity to save.
     * @return the persisted entity.
     */
    public CashBook save(CashBook cashBook) {
        log.debug("Request to save CashBook : {}", cashBook);
        return cashBookRepository.save(cashBook);
    }

    /**
     * Get all the cashBooks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CashBook> findAll() {
        log.debug("Request to get all CashBooks");
        return cashBookRepository.findAll();
    }


    /**
     * Get one cashBook by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashBook> findOne(Long id) {
        log.debug("Request to get CashBook : {}", id);
        return cashBookRepository.findById(id);
    }

    /**
     * Delete the cashBook by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CashBook : {}", id);
        cashBookRepository.deleteById(id);
    }
}
