package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.Desings;
import com.alphadevs.pos.repository.DesingsRepository;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;

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
 * Integration tests for the {@link DesingsResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class DesingsResourceIT {

    private static final String DEFAULT_DESING_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DESING_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DESING_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESING_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_DESING_PREFIX = "BBBBBBBBBB";

    private static final Double DEFAULT_DESING_PROF_MARGIN = 1D;
    private static final Double UPDATED_DESING_PROF_MARGIN = 2D;

    @Autowired
    private DesingsRepository desingsRepository;

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

    private MockMvc restDesingsMockMvc;

    private Desings desings;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DesingsResource desingsResource = new DesingsResource(desingsRepository);
        this.restDesingsMockMvc = MockMvcBuilders.standaloneSetup(desingsResource)
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
    public static Desings createEntity(EntityManager em) {
        Desings desings = new Desings()
            .desingCode(DEFAULT_DESING_CODE)
            .desingName(DEFAULT_DESING_NAME)
            .desingPrefix(DEFAULT_DESING_PREFIX)
            .desingProfMargin(DEFAULT_DESING_PROF_MARGIN);
        return desings;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Desings createUpdatedEntity(EntityManager em) {
        Desings desings = new Desings()
            .desingCode(UPDATED_DESING_CODE)
            .desingName(UPDATED_DESING_NAME)
            .desingPrefix(UPDATED_DESING_PREFIX)
            .desingProfMargin(UPDATED_DESING_PROF_MARGIN);
        return desings;
    }

    @BeforeEach
    public void initTest() {
        desings = createEntity(em);
    }

    @Test
    @Transactional
    public void createDesings() throws Exception {
        int databaseSizeBeforeCreate = desingsRepository.findAll().size();

        // Create the Desings
        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isCreated());

        // Validate the Desings in the database
        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeCreate + 1);
        Desings testDesings = desingsList.get(desingsList.size() - 1);
        assertThat(testDesings.getDesingCode()).isEqualTo(DEFAULT_DESING_CODE);
        assertThat(testDesings.getDesingName()).isEqualTo(DEFAULT_DESING_NAME);
        assertThat(testDesings.getDesingPrefix()).isEqualTo(DEFAULT_DESING_PREFIX);
        assertThat(testDesings.getDesingProfMargin()).isEqualTo(DEFAULT_DESING_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void createDesingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = desingsRepository.findAll().size();

        // Create the Desings with an existing ID
        desings.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        // Validate the Desings in the database
        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDesingCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = desingsRepository.findAll().size();
        // set the field null
        desings.setDesingCode(null);

        // Create the Desings, which fails.

        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDesingNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = desingsRepository.findAll().size();
        // set the field null
        desings.setDesingName(null);

        // Create the Desings, which fails.

        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDesingPrefixIsRequired() throws Exception {
        int databaseSizeBeforeTest = desingsRepository.findAll().size();
        // set the field null
        desings.setDesingPrefix(null);

        // Create the Desings, which fails.

        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDesingProfMarginIsRequired() throws Exception {
        int databaseSizeBeforeTest = desingsRepository.findAll().size();
        // set the field null
        desings.setDesingProfMargin(null);

        // Create the Desings, which fails.

        restDesingsMockMvc.perform(post("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDesings() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get all the desingsList
        restDesingsMockMvc.perform(get("/api/desings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(desings.getId().intValue())))
            .andExpect(jsonPath("$.[*].desingCode").value(hasItem(DEFAULT_DESING_CODE)))
            .andExpect(jsonPath("$.[*].desingName").value(hasItem(DEFAULT_DESING_NAME)))
            .andExpect(jsonPath("$.[*].desingPrefix").value(hasItem(DEFAULT_DESING_PREFIX)))
            .andExpect(jsonPath("$.[*].desingProfMargin").value(hasItem(DEFAULT_DESING_PROF_MARGIN.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getDesings() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        // Get the desings
        restDesingsMockMvc.perform(get("/api/desings/{id}", desings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(desings.getId().intValue()))
            .andExpect(jsonPath("$.desingCode").value(DEFAULT_DESING_CODE))
            .andExpect(jsonPath("$.desingName").value(DEFAULT_DESING_NAME))
            .andExpect(jsonPath("$.desingPrefix").value(DEFAULT_DESING_PREFIX))
            .andExpect(jsonPath("$.desingProfMargin").value(DEFAULT_DESING_PROF_MARGIN.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDesings() throws Exception {
        // Get the desings
        restDesingsMockMvc.perform(get("/api/desings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDesings() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        int databaseSizeBeforeUpdate = desingsRepository.findAll().size();

        // Update the desings
        Desings updatedDesings = desingsRepository.findById(desings.getId()).get();
        // Disconnect from session so that the updates on updatedDesings are not directly saved in db
        em.detach(updatedDesings);
        updatedDesings
            .desingCode(UPDATED_DESING_CODE)
            .desingName(UPDATED_DESING_NAME)
            .desingPrefix(UPDATED_DESING_PREFIX)
            .desingProfMargin(UPDATED_DESING_PROF_MARGIN);

        restDesingsMockMvc.perform(put("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDesings)))
            .andExpect(status().isOk());

        // Validate the Desings in the database
        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeUpdate);
        Desings testDesings = desingsList.get(desingsList.size() - 1);
        assertThat(testDesings.getDesingCode()).isEqualTo(UPDATED_DESING_CODE);
        assertThat(testDesings.getDesingName()).isEqualTo(UPDATED_DESING_NAME);
        assertThat(testDesings.getDesingPrefix()).isEqualTo(UPDATED_DESING_PREFIX);
        assertThat(testDesings.getDesingProfMargin()).isEqualTo(UPDATED_DESING_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void updateNonExistingDesings() throws Exception {
        int databaseSizeBeforeUpdate = desingsRepository.findAll().size();

        // Create the Desings

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesingsMockMvc.perform(put("/api/desings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(desings)))
            .andExpect(status().isBadRequest());

        // Validate the Desings in the database
        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDesings() throws Exception {
        // Initialize the database
        desingsRepository.saveAndFlush(desings);

        int databaseSizeBeforeDelete = desingsRepository.findAll().size();

        // Delete the desings
        restDesingsMockMvc.perform(delete("/api/desings/{id}", desings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Desings> desingsList = desingsRepository.findAll();
        assertThat(desingsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Desings.class);
        Desings desings1 = new Desings();
        desings1.setId(1L);
        Desings desings2 = new Desings();
        desings2.setId(desings1.getId());
        assertThat(desings1).isEqualTo(desings2);
        desings2.setId(2L);
        assertThat(desings1).isNotEqualTo(desings2);
        desings1.setId(null);
        assertThat(desings1).isNotEqualTo(desings2);
    }
}
