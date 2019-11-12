package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.ExUser;
import com.alphadevs.pos.repository.ExUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ExUser}.
 */
@Service
@Transactional
public class ExUserService {

    private final Logger log = LoggerFactory.getLogger(ExUserService.class);

    private final ExUserRepository exUserRepository;

    public ExUserService(ExUserRepository exUserRepository) {
        this.exUserRepository = exUserRepository;
    }

    /**
     * Save a exUser.
     *
     * @param exUser the entity to save.
     * @return the persisted entity.
     */
    public ExUser save(ExUser exUser) {
        log.debug("Request to save ExUser : {}", exUser);
        return exUserRepository.save(exUser);
    }

    /**
     * Get all the exUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ExUser> findAll() {
        log.debug("Request to get all ExUsers");
        return exUserRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the exUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ExUser> findAllWithEagerRelationships(Pageable pageable) {
        return exUserRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one exUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExUser> findOne(Long id) {
        log.debug("Request to get ExUser : {}", id);
        return exUserRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the exUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExUser : {}", id);
        exUserRepository.deleteById(id);
    }
}
