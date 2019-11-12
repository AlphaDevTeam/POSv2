package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.DocumentType;
import com.alphadevs.pos.repository.DocumentTypeRepository;
import com.alphadevs.pos.service.DocumentTypeService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.DocumentTypeCriteria;
import com.alphadevs.pos.service.DocumentTypeQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.alphadevs.pos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DocumentTypeResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class DocumentTypeResourceIT {

    private static final String DEFAULT_DOCUMENT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private DocumentTypeQueryService documentTypeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDocumentTypeMockMvc;

    private DocumentType documentType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocumentTypeResource documentTypeResource = new DocumentTypeResource(documentTypeService, documentTypeQueryService);
        this.restDocumentTypeMockMvc = MockMvcBuilders.standaloneSetup(documentTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentType createEntity(EntityManager em) {
        DocumentType documentType = new DocumentType()
            .documentTypeCode(DEFAULT_DOCUMENT_TYPE_CODE)
            .documentType(DEFAULT_DOCUMENT_TYPE);
        return documentType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentType createUpdatedEntity(EntityManager em) {
        DocumentType documentType = new DocumentType()
            .documentTypeCode(UPDATED_DOCUMENT_TYPE_CODE)
            .documentType(UPDATED_DOCUMENT_TYPE);
        return documentType;
    }

    @BeforeEach
    public void initTest() {
        documentType = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocumentType() throws Exception {
        int databaseSizeBeforeCreate = documentTypeRepository.findAll().size();

        // Create the DocumentType
        restDocumentTypeMockMvc.perform(post("/api/document-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentType)))
            .andExpect(status().isCreated());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentType testDocumentType = documentTypeList.get(documentTypeList.size() - 1);
        assertThat(testDocumentType.getDocumentTypeCode()).isEqualTo(DEFAULT_DOCUMENT_TYPE_CODE);
        assertThat(testDocumentType.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    public void createDocumentTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentTypeRepository.findAll().size();

        // Create the DocumentType with an existing ID
        documentType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentTypeMockMvc.perform(post("/api/document-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentType)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDocumentTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentTypeRepository.findAll().size();
        // set the field null
        documentType.setDocumentTypeCode(null);

        // Create the DocumentType, which fails.

        restDocumentTypeMockMvc.perform(post("/api/document-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentType)))
            .andExpect(status().isBadRequest());

        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDocumentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentTypeRepository.findAll().size();
        // set the field null
        documentType.setDocumentType(null);

        // Create the DocumentType, which fails.

        restDocumentTypeMockMvc.perform(post("/api/document-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentType)))
            .andExpect(status().isBadRequest());

        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocumentTypes() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList
        restDocumentTypeMockMvc.perform(get("/api/document-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentTypeCode").value(hasItem(DEFAULT_DOCUMENT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getDocumentType() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get the documentType
        restDocumentTypeMockMvc.perform(get("/api/document-types/{id}", documentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(documentType.getId().intValue()))
            .andExpect(jsonPath("$.documentTypeCode").value(DEFAULT_DOCUMENT_TYPE_CODE))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE));
    }

    @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentTypeCode equals to DEFAULT_DOCUMENT_TYPE_CODE
        defaultDocumentTypeShouldBeFound("documentTypeCode.equals=" + DEFAULT_DOCUMENT_TYPE_CODE);

        // Get all the documentTypeList where documentTypeCode equals to UPDATED_DOCUMENT_TYPE_CODE
        defaultDocumentTypeShouldNotBeFound("documentTypeCode.equals=" + UPDATED_DOCUMENT_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentTypeCode not equals to DEFAULT_DOCUMENT_TYPE_CODE
        defaultDocumentTypeShouldNotBeFound("documentTypeCode.notEquals=" + DEFAULT_DOCUMENT_TYPE_CODE);

        // Get all the documentTypeList where documentTypeCode not equals to UPDATED_DOCUMENT_TYPE_CODE
        defaultDocumentTypeShouldBeFound("documentTypeCode.notEquals=" + UPDATED_DOCUMENT_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentTypeCode in DEFAULT_DOCUMENT_TYPE_CODE or UPDATED_DOCUMENT_TYPE_CODE
        defaultDocumentTypeShouldBeFound("documentTypeCode.in=" + DEFAULT_DOCUMENT_TYPE_CODE + "," + UPDATED_DOCUMENT_TYPE_CODE);

        // Get all the documentTypeList where documentTypeCode equals to UPDATED_DOCUMENT_TYPE_CODE
        defaultDocumentTypeShouldNotBeFound("documentTypeCode.in=" + UPDATED_DOCUMENT_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentTypeCode is not null
        defaultDocumentTypeShouldBeFound("documentTypeCode.specified=true");

        // Get all the documentTypeList where documentTypeCode is null
        defaultDocumentTypeShouldNotBeFound("documentTypeCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentTypeCode contains DEFAULT_DOCUMENT_TYPE_CODE
        defaultDocumentTypeShouldBeFound("documentTypeCode.contains=" + DEFAULT_DOCUMENT_TYPE_CODE);

        // Get all the documentTypeList where documentTypeCode contains UPDATED_DOCUMENT_TYPE_CODE
        defaultDocumentTypeShouldNotBeFound("documentTypeCode.contains=" + UPDATED_DOCUMENT_TYPE_CODE);
    }

    @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentTypeCode does not contain DEFAULT_DOCUMENT_TYPE_CODE
        defaultDocumentTypeShouldNotBeFound("documentTypeCode.doesNotContain=" + DEFAULT_DOCUMENT_TYPE_CODE);

        // Get all the documentTypeList where documentTypeCode does not contain UPDATED_DOCUMENT_TYPE_CODE
        defaultDocumentTypeShouldBeFound("documentTypeCode.doesNotContain=" + UPDATED_DOCUMENT_TYPE_CODE);
    }


    @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentType equals to DEFAULT_DOCUMENT_TYPE
        defaultDocumentTypeShouldBeFound("documentType.equals=" + DEFAULT_DOCUMENT_TYPE);

        // Get all the documentTypeList where documentType equals to UPDATED_DOCUMENT_TYPE
        defaultDocumentTypeShouldNotBeFound("documentType.equals=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentType not equals to DEFAULT_DOCUMENT_TYPE
        defaultDocumentTypeShouldNotBeFound("documentType.notEquals=" + DEFAULT_DOCUMENT_TYPE);

        // Get all the documentTypeList where documentType not equals to UPDATED_DOCUMENT_TYPE
        defaultDocumentTypeShouldBeFound("documentType.notEquals=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentType in DEFAULT_DOCUMENT_TYPE or UPDATED_DOCUMENT_TYPE
        defaultDocumentTypeShouldBeFound("documentType.in=" + DEFAULT_DOCUMENT_TYPE + "," + UPDATED_DOCUMENT_TYPE);

        // Get all the documentTypeList where documentType equals to UPDATED_DOCUMENT_TYPE
        defaultDocumentTypeShouldNotBeFound("documentType.in=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentType is not null
        defaultDocumentTypeShouldBeFound("documentType.specified=true");

        // Get all the documentTypeList where documentType is null
        defaultDocumentTypeShouldNotBeFound("documentType.specified=false");
    }
                @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeContainsSomething() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentType contains DEFAULT_DOCUMENT_TYPE
        defaultDocumentTypeShouldBeFound("documentType.contains=" + DEFAULT_DOCUMENT_TYPE);

        // Get all the documentTypeList where documentType contains UPDATED_DOCUMENT_TYPE
        defaultDocumentTypeShouldNotBeFound("documentType.contains=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllDocumentTypesByDocumentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentType does not contain DEFAULT_DOCUMENT_TYPE
        defaultDocumentTypeShouldNotBeFound("documentType.doesNotContain=" + DEFAULT_DOCUMENT_TYPE);

        // Get all the documentTypeList where documentType does not contain UPDATED_DOCUMENT_TYPE
        defaultDocumentTypeShouldBeFound("documentType.doesNotContain=" + UPDATED_DOCUMENT_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentTypeShouldBeFound(String filter) throws Exception {
        restDocumentTypeMockMvc.perform(get("/api/document-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentTypeCode").value(hasItem(DEFAULT_DOCUMENT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)));

        // Check, that the count call also returns 1
        restDocumentTypeMockMvc.perform(get("/api/document-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentTypeShouldNotBeFound(String filter) throws Exception {
        restDocumentTypeMockMvc.perform(get("/api/document-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentTypeMockMvc.perform(get("/api/document-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDocumentType() throws Exception {
        // Get the documentType
        restDocumentTypeMockMvc.perform(get("/api/document-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumentType() throws Exception {
        // Initialize the database
        documentTypeService.save(documentType);

        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().size();

        // Update the documentType
        DocumentType updatedDocumentType = documentTypeRepository.findById(documentType.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentType are not directly saved in db
        em.detach(updatedDocumentType);
        updatedDocumentType
            .documentTypeCode(UPDATED_DOCUMENT_TYPE_CODE)
            .documentType(UPDATED_DOCUMENT_TYPE);

        restDocumentTypeMockMvc.perform(put("/api/document-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocumentType)))
            .andExpect(status().isOk());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
        DocumentType testDocumentType = documentTypeList.get(documentTypeList.size() - 1);
        assertThat(testDocumentType.getDocumentTypeCode()).isEqualTo(UPDATED_DOCUMENT_TYPE_CODE);
        assertThat(testDocumentType.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().size();

        // Create the DocumentType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc.perform(put("/api/document-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentType)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDocumentType() throws Exception {
        // Initialize the database
        documentTypeService.save(documentType);

        int databaseSizeBeforeDelete = documentTypeRepository.findAll().size();

        // Delete the documentType
        restDocumentTypeMockMvc.perform(delete("/api/document-types/{id}", documentType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentType.class);
        DocumentType documentType1 = new DocumentType();
        documentType1.setId(1L);
        DocumentType documentType2 = new DocumentType();
        documentType2.setId(documentType1.getId());
        assertThat(documentType1).isEqualTo(documentType2);
        documentType2.setId(2L);
        assertThat(documentType1).isNotEqualTo(documentType2);
        documentType1.setId(null);
        assertThat(documentType1).isNotEqualTo(documentType2);
    }
}
