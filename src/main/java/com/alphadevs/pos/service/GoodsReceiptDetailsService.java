package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.GoodsReceiptDetails;
import com.alphadevs.pos.repository.GoodsReceiptDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link GoodsReceiptDetails}.
 */
@Service
@Transactional
public class GoodsReceiptDetailsService {

    private final Logger log = LoggerFactory.getLogger(GoodsReceiptDetailsService.class);

    private final GoodsReceiptDetailsRepository goodsReceiptDetailsRepository;

    public GoodsReceiptDetailsService(GoodsReceiptDetailsRepository goodsReceiptDetailsRepository) {
        this.goodsReceiptDetailsRepository = goodsReceiptDetailsRepository;
    }

    /**
     * Save a goodsReceiptDetails.
     *
     * @param goodsReceiptDetails the entity to save.
     * @return the persisted entity.
     */
    public GoodsReceiptDetails save(GoodsReceiptDetails goodsReceiptDetails) {
        log.debug("Request to save GoodsReceiptDetails : {}", goodsReceiptDetails);
        return goodsReceiptDetailsRepository.save(goodsReceiptDetails);
    }

    /**
     * Get all the goodsReceiptDetails.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GoodsReceiptDetails> findAll() {
        log.debug("Request to get all GoodsReceiptDetails");
        return goodsReceiptDetailsRepository.findAll();
    }


    /**
     * Get one goodsReceiptDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GoodsReceiptDetails> findOne(Long id) {
        log.debug("Request to get GoodsReceiptDetails : {}", id);
        return goodsReceiptDetailsRepository.findById(id);
    }

    /**
     * Delete the goodsReceiptDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GoodsReceiptDetails : {}", id);
        goodsReceiptDetailsRepository.deleteById(id);
    }
}
