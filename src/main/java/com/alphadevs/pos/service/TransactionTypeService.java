package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.TransactionType;
import com.alphadevs.pos.repository.TransactionTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link TransactionType}.
 */
@Service
@Transactional
public class TransactionTypeService {

    private final Logger log = LoggerFactory.getLogger(TransactionTypeService.class);

    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeService(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    /**
     * Save a transactionType.
     *
     * @param transactionType the entity to save.
     * @return the persisted entity.
     */
    public TransactionType save(TransactionType transactionType) {
        log.debug("Request to save TransactionType : {}", transactionType);
        return transactionTypeRepository.save(transactionType);
    }

    /**
     * Get all the transactionTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionType> findAll() {
        log.debug("Request to get all TransactionTypes");
        return transactionTypeRepository.findAll();
    }


    /**
     * Get one transactionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransactionType> findOne(Long id) {
        log.debug("Request to get TransactionType : {}", id);
        return transactionTypeRepository.findById(id);
    }

    /**
     * Delete the transactionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TransactionType : {}", id);
        transactionTypeRepository.deleteById(id);
    }
}
