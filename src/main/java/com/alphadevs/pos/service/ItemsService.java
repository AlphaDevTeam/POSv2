package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.Items;
import com.alphadevs.pos.repository.ItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Items}.
 */
@Service
@Transactional
public class ItemsService {

    private final Logger log = LoggerFactory.getLogger(ItemsService.class);

    private final ItemsRepository itemsRepository;

    public ItemsService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    /**
     * Save a items.
     *
     * @param items the entity to save.
     * @return the persisted entity.
     */
    public Items save(Items items) {
        log.debug("Request to save Items : {}", items);
        return itemsRepository.save(items);
    }

    /**
     * Get all the items.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Items> findAll() {
        log.debug("Request to get all Items");
        return itemsRepository.findAll();
    }


    /**
     * Get one items by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Items> findOne(Long id) {
        log.debug("Request to get Items : {}", id);
        return itemsRepository.findById(id);
    }

    /**
     * Delete the items by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Items : {}", id);
        itemsRepository.deleteById(id);
    }
}
