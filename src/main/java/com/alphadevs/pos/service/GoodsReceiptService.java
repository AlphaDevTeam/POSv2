package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.GoodsReceipt;
import com.alphadevs.pos.repository.GoodsReceiptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link GoodsReceipt}.
 */
@Service
@Transactional
public class GoodsReceiptService {

    private final Logger log = LoggerFactory.getLogger(GoodsReceiptService.class);

    private final GoodsReceiptRepository goodsReceiptRepository;

    public GoodsReceiptService(GoodsReceiptRepository goodsReceiptRepository) {
        this.goodsReceiptRepository = goodsReceiptRepository;
    }

    /**
     * Save a goodsReceipt.
     *
     * @param goodsReceipt the entity to save.
     * @return the persisted entity.
     */
    public GoodsReceipt save(GoodsReceipt goodsReceipt) {
        log.debug("Request to save GoodsReceipt : {}", goodsReceipt);
        return goodsReceiptRepository.save(goodsReceipt);
    }

    /**
     * Get all the goodsReceipts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GoodsReceipt> findAll() {
        log.debug("Request to get all GoodsReceipts");
        return goodsReceiptRepository.findAll();
    }


    /**
     * Get one goodsReceipt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GoodsReceipt> findOne(Long id) {
        log.debug("Request to get GoodsReceipt : {}", id);
        return goodsReceiptRepository.findById(id);
    }

    /**
     * Delete the goodsReceipt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GoodsReceipt : {}", id);
        goodsReceiptRepository.deleteById(id);
    }
}
