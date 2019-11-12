package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.ItemBinCard;
import com.alphadevs.pos.repository.ItemBinCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ItemBinCard}.
 */
@Service
@Transactional
public class ItemBinCardService {

    private final Logger log = LoggerFactory.getLogger(ItemBinCardService.class);

    private final ItemBinCardRepository itemBinCardRepository;

    public ItemBinCardService(ItemBinCardRepository itemBinCardRepository) {
        this.itemBinCardRepository = itemBinCardRepository;
    }

    /**
     * Save a itemBinCard.
     *
     * @param itemBinCard the entity to save.
     * @return the persisted entity.
     */
    public ItemBinCard save(ItemBinCard itemBinCard) {
        log.debug("Request to save ItemBinCard : {}", itemBinCard);
        return itemBinCardRepository.save(itemBinCard);
    }

    /**
     * Get all the itemBinCards.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ItemBinCard> findAll() {
        log.debug("Request to get all ItemBinCards");
        return itemBinCardRepository.findAll();
    }


    /**
     * Get one itemBinCard by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemBinCard> findOne(Long id) {
        log.debug("Request to get ItemBinCard : {}", id);
        return itemBinCardRepository.findById(id);
    }

    /**
     * Delete the itemBinCard by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ItemBinCard : {}", id);
        itemBinCardRepository.deleteById(id);
    }
}
