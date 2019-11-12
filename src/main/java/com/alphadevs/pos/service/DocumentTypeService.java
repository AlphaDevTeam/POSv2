package com.alphadevs.pos.service;

import com.alphadevs.pos.domain.DocumentType;
import com.alphadevs.pos.repository.DocumentTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link DocumentType}.
 */
@Service
@Transactional
public class DocumentTypeService {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeService.class);

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeService(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    /**
     * Save a documentType.
     *
     * @param documentType the entity to save.
     * @return the persisted entity.
     */
    public DocumentType save(DocumentType documentType) {
        log.debug("Request to save DocumentType : {}", documentType);
        return documentTypeRepository.save(documentType);
    }

    /**
     * Get all the documentTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentType> findAll() {
        log.debug("Request to get all DocumentTypes");
        return documentTypeRepository.findAll();
    }


    /**
     * Get one documentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentType> findOne(Long id) {
        log.debug("Request to get DocumentType : {}", id);
        return documentTypeRepository.findById(id);
    }

    /**
     * Delete the documentType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocumentType : {}", id);
        documentTypeRepository.deleteById(id);
    }
}
