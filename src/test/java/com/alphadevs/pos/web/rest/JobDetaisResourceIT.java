package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.JobDetais;
import com.alphadevs.pos.domain.Items;
import com.alphadevs.pos.domain.Job;
import com.alphadevs.pos.repository.JobDetaisRepository;
import com.alphadevs.pos.service.JobDetaisService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.JobDetaisCriteria;
import com.alphadevs.pos.service.JobDetaisQueryService;

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
 * Integration tests for the {@link JobDetaisResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class JobDetaisResourceIT {

    private static final Double DEFAULT_JOB_ITEM_PRICE = 1D;
    private static final Double UPDATED_JOB_ITEM_PRICE = 2D;
    private static final Double SMALLER_JOB_ITEM_PRICE = 1D - 1D;

    private static final Integer DEFAULT_JOB_ITEM_QTY = 1;
    private static final Integer UPDATED_JOB_ITEM_QTY = 2;
    private static final Integer SMALLER_JOB_ITEM_QTY = 1 - 1;

    @Autowired
    private JobDetaisRepository jobDetaisRepository;

    @Autowired
    private JobDetaisService jobDetaisService;

    @Autowired
    private JobDetaisQueryService jobDetaisQueryService;

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

    private MockMvc restJobDetaisMockMvc;

    private JobDetais jobDetais;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobDetaisResource jobDetaisResource = new JobDetaisResource(jobDetaisService, jobDetaisQueryService);
        this.restJobDetaisMockMvc = MockMvcBuilders.standaloneSetup(jobDetaisResource)
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
    public static JobDetais createEntity(EntityManager em) {
        JobDetais jobDetais = new JobDetais()
            .jobItemPrice(DEFAULT_JOB_ITEM_PRICE)
            .jobItemQty(DEFAULT_JOB_ITEM_QTY);
        return jobDetais;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobDetais createUpdatedEntity(EntityManager em) {
        JobDetais jobDetais = new JobDetais()
            .jobItemPrice(UPDATED_JOB_ITEM_PRICE)
            .jobItemQty(UPDATED_JOB_ITEM_QTY);
        return jobDetais;
    }

    @BeforeEach
    public void initTest() {
        jobDetais = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobDetais() throws Exception {
        int databaseSizeBeforeCreate = jobDetaisRepository.findAll().size();

        // Create the JobDetais
        restJobDetaisMockMvc.perform(post("/api/job-detais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetais)))
            .andExpect(status().isCreated());

        // Validate the JobDetais in the database
        List<JobDetais> jobDetaisList = jobDetaisRepository.findAll();
        assertThat(jobDetaisList).hasSize(databaseSizeBeforeCreate + 1);
        JobDetais testJobDetais = jobDetaisList.get(jobDetaisList.size() - 1);
        assertThat(testJobDetais.getJobItemPrice()).isEqualTo(DEFAULT_JOB_ITEM_PRICE);
        assertThat(testJobDetais.getJobItemQty()).isEqualTo(DEFAULT_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void createJobDetaisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobDetaisRepository.findAll().size();

        // Create the JobDetais with an existing ID
        jobDetais.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobDetaisMockMvc.perform(post("/api/job-detais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetais)))
            .andExpect(status().isBadRequest());

        // Validate the JobDetais in the database
        List<JobDetais> jobDetaisList = jobDetaisRepository.findAll();
        assertThat(jobDetaisList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkJobItemPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobDetaisRepository.findAll().size();
        // set the field null
        jobDetais.setJobItemPrice(null);

        // Create the JobDetais, which fails.

        restJobDetaisMockMvc.perform(post("/api/job-detais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetais)))
            .andExpect(status().isBadRequest());

        List<JobDetais> jobDetaisList = jobDetaisRepository.findAll();
        assertThat(jobDetaisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJobItemQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobDetaisRepository.findAll().size();
        // set the field null
        jobDetais.setJobItemQty(null);

        // Create the JobDetais, which fails.

        restJobDetaisMockMvc.perform(post("/api/job-detais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetais)))
            .andExpect(status().isBadRequest());

        List<JobDetais> jobDetaisList = jobDetaisRepository.findAll();
        assertThat(jobDetaisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobDetais() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList
        restJobDetaisMockMvc.perform(get("/api/job-detais?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobDetais.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobItemPrice").value(hasItem(DEFAULT_JOB_ITEM_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].jobItemQty").value(hasItem(DEFAULT_JOB_ITEM_QTY)));
    }
    
    @Test
    @Transactional
    public void getJobDetais() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get the jobDetais
        restJobDetaisMockMvc.perform(get("/api/job-detais/{id}", jobDetais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobDetais.getId().intValue()))
            .andExpect(jsonPath("$.jobItemPrice").value(DEFAULT_JOB_ITEM_PRICE.doubleValue()))
            .andExpect(jsonPath("$.jobItemQty").value(DEFAULT_JOB_ITEM_QTY));
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemPrice equals to DEFAULT_JOB_ITEM_PRICE
        defaultJobDetaisShouldBeFound("jobItemPrice.equals=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetaisList where jobItemPrice equals to UPDATED_JOB_ITEM_PRICE
        defaultJobDetaisShouldNotBeFound("jobItemPrice.equals=" + UPDATED_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemPrice not equals to DEFAULT_JOB_ITEM_PRICE
        defaultJobDetaisShouldNotBeFound("jobItemPrice.notEquals=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetaisList where jobItemPrice not equals to UPDATED_JOB_ITEM_PRICE
        defaultJobDetaisShouldBeFound("jobItemPrice.notEquals=" + UPDATED_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemPriceIsInShouldWork() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemPrice in DEFAULT_JOB_ITEM_PRICE or UPDATED_JOB_ITEM_PRICE
        defaultJobDetaisShouldBeFound("jobItemPrice.in=" + DEFAULT_JOB_ITEM_PRICE + "," + UPDATED_JOB_ITEM_PRICE);

        // Get all the jobDetaisList where jobItemPrice equals to UPDATED_JOB_ITEM_PRICE
        defaultJobDetaisShouldNotBeFound("jobItemPrice.in=" + UPDATED_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemPrice is not null
        defaultJobDetaisShouldBeFound("jobItemPrice.specified=true");

        // Get all the jobDetaisList where jobItemPrice is null
        defaultJobDetaisShouldNotBeFound("jobItemPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemPrice is greater than or equal to DEFAULT_JOB_ITEM_PRICE
        defaultJobDetaisShouldBeFound("jobItemPrice.greaterThanOrEqual=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetaisList where jobItemPrice is greater than or equal to UPDATED_JOB_ITEM_PRICE
        defaultJobDetaisShouldNotBeFound("jobItemPrice.greaterThanOrEqual=" + UPDATED_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemPrice is less than or equal to DEFAULT_JOB_ITEM_PRICE
        defaultJobDetaisShouldBeFound("jobItemPrice.lessThanOrEqual=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetaisList where jobItemPrice is less than or equal to SMALLER_JOB_ITEM_PRICE
        defaultJobDetaisShouldNotBeFound("jobItemPrice.lessThanOrEqual=" + SMALLER_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemPrice is less than DEFAULT_JOB_ITEM_PRICE
        defaultJobDetaisShouldNotBeFound("jobItemPrice.lessThan=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetaisList where jobItemPrice is less than UPDATED_JOB_ITEM_PRICE
        defaultJobDetaisShouldBeFound("jobItemPrice.lessThan=" + UPDATED_JOB_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemPrice is greater than DEFAULT_JOB_ITEM_PRICE
        defaultJobDetaisShouldNotBeFound("jobItemPrice.greaterThan=" + DEFAULT_JOB_ITEM_PRICE);

        // Get all the jobDetaisList where jobItemPrice is greater than SMALLER_JOB_ITEM_PRICE
        defaultJobDetaisShouldBeFound("jobItemPrice.greaterThan=" + SMALLER_JOB_ITEM_PRICE);
    }


    @Test
    @Transactional
    public void getAllJobDetaisByJobItemQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemQty equals to DEFAULT_JOB_ITEM_QTY
        defaultJobDetaisShouldBeFound("jobItemQty.equals=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetaisList where jobItemQty equals to UPDATED_JOB_ITEM_QTY
        defaultJobDetaisShouldNotBeFound("jobItemQty.equals=" + UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemQtyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemQty not equals to DEFAULT_JOB_ITEM_QTY
        defaultJobDetaisShouldNotBeFound("jobItemQty.notEquals=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetaisList where jobItemQty not equals to UPDATED_JOB_ITEM_QTY
        defaultJobDetaisShouldBeFound("jobItemQty.notEquals=" + UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemQtyIsInShouldWork() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemQty in DEFAULT_JOB_ITEM_QTY or UPDATED_JOB_ITEM_QTY
        defaultJobDetaisShouldBeFound("jobItemQty.in=" + DEFAULT_JOB_ITEM_QTY + "," + UPDATED_JOB_ITEM_QTY);

        // Get all the jobDetaisList where jobItemQty equals to UPDATED_JOB_ITEM_QTY
        defaultJobDetaisShouldNotBeFound("jobItemQty.in=" + UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemQty is not null
        defaultJobDetaisShouldBeFound("jobItemQty.specified=true");

        // Get all the jobDetaisList where jobItemQty is null
        defaultJobDetaisShouldNotBeFound("jobItemQty.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemQty is greater than or equal to DEFAULT_JOB_ITEM_QTY
        defaultJobDetaisShouldBeFound("jobItemQty.greaterThanOrEqual=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetaisList where jobItemQty is greater than or equal to UPDATED_JOB_ITEM_QTY
        defaultJobDetaisShouldNotBeFound("jobItemQty.greaterThanOrEqual=" + UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemQty is less than or equal to DEFAULT_JOB_ITEM_QTY
        defaultJobDetaisShouldBeFound("jobItemQty.lessThanOrEqual=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetaisList where jobItemQty is less than or equal to SMALLER_JOB_ITEM_QTY
        defaultJobDetaisShouldNotBeFound("jobItemQty.lessThanOrEqual=" + SMALLER_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemQty is less than DEFAULT_JOB_ITEM_QTY
        defaultJobDetaisShouldNotBeFound("jobItemQty.lessThan=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetaisList where jobItemQty is less than UPDATED_JOB_ITEM_QTY
        defaultJobDetaisShouldBeFound("jobItemQty.lessThan=" + UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllJobDetaisByJobItemQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);

        // Get all the jobDetaisList where jobItemQty is greater than DEFAULT_JOB_ITEM_QTY
        defaultJobDetaisShouldNotBeFound("jobItemQty.greaterThan=" + DEFAULT_JOB_ITEM_QTY);

        // Get all the jobDetaisList where jobItemQty is greater than SMALLER_JOB_ITEM_QTY
        defaultJobDetaisShouldBeFound("jobItemQty.greaterThan=" + SMALLER_JOB_ITEM_QTY);
    }


    @Test
    @Transactional
    public void getAllJobDetaisByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);
        Items item = ItemsResourceIT.createEntity(em);
        em.persist(item);
        em.flush();
        jobDetais.setItem(item);
        jobDetaisRepository.saveAndFlush(jobDetais);
        Long itemId = item.getId();

        // Get all the jobDetaisList where item equals to itemId
        defaultJobDetaisShouldBeFound("itemId.equals=" + itemId);

        // Get all the jobDetaisList where item equals to itemId + 1
        defaultJobDetaisShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }


    @Test
    @Transactional
    public void getAllJobDetaisByJobIsEqualToSomething() throws Exception {
        // Initialize the database
        jobDetaisRepository.saveAndFlush(jobDetais);
        Job job = JobResourceIT.createEntity(em);
        em.persist(job);
        em.flush();
        jobDetais.setJob(job);
        jobDetaisRepository.saveAndFlush(jobDetais);
        Long jobId = job.getId();

        // Get all the jobDetaisList where job equals to jobId
        defaultJobDetaisShouldBeFound("jobId.equals=" + jobId);

        // Get all the jobDetaisList where job equals to jobId + 1
        defaultJobDetaisShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobDetaisShouldBeFound(String filter) throws Exception {
        restJobDetaisMockMvc.perform(get("/api/job-detais?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobDetais.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobItemPrice").value(hasItem(DEFAULT_JOB_ITEM_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].jobItemQty").value(hasItem(DEFAULT_JOB_ITEM_QTY)));

        // Check, that the count call also returns 1
        restJobDetaisMockMvc.perform(get("/api/job-detais/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobDetaisShouldNotBeFound(String filter) throws Exception {
        restJobDetaisMockMvc.perform(get("/api/job-detais?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobDetaisMockMvc.perform(get("/api/job-detais/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingJobDetais() throws Exception {
        // Get the jobDetais
        restJobDetaisMockMvc.perform(get("/api/job-detais/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobDetais() throws Exception {
        // Initialize the database
        jobDetaisService.save(jobDetais);

        int databaseSizeBeforeUpdate = jobDetaisRepository.findAll().size();

        // Update the jobDetais
        JobDetais updatedJobDetais = jobDetaisRepository.findById(jobDetais.getId()).get();
        // Disconnect from session so that the updates on updatedJobDetais are not directly saved in db
        em.detach(updatedJobDetais);
        updatedJobDetais
            .jobItemPrice(UPDATED_JOB_ITEM_PRICE)
            .jobItemQty(UPDATED_JOB_ITEM_QTY);

        restJobDetaisMockMvc.perform(put("/api/job-detais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobDetais)))
            .andExpect(status().isOk());

        // Validate the JobDetais in the database
        List<JobDetais> jobDetaisList = jobDetaisRepository.findAll();
        assertThat(jobDetaisList).hasSize(databaseSizeBeforeUpdate);
        JobDetais testJobDetais = jobDetaisList.get(jobDetaisList.size() - 1);
        assertThat(testJobDetais.getJobItemPrice()).isEqualTo(UPDATED_JOB_ITEM_PRICE);
        assertThat(testJobDetais.getJobItemQty()).isEqualTo(UPDATED_JOB_ITEM_QTY);
    }

    @Test
    @Transactional
    public void updateNonExistingJobDetais() throws Exception {
        int databaseSizeBeforeUpdate = jobDetaisRepository.findAll().size();

        // Create the JobDetais

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobDetaisMockMvc.perform(put("/api/job-detais")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetais)))
            .andExpect(status().isBadRequest());

        // Validate the JobDetais in the database
        List<JobDetais> jobDetaisList = jobDetaisRepository.findAll();
        assertThat(jobDetaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJobDetais() throws Exception {
        // Initialize the database
        jobDetaisService.save(jobDetais);

        int databaseSizeBeforeDelete = jobDetaisRepository.findAll().size();

        // Delete the jobDetais
        restJobDetaisMockMvc.perform(delete("/api/job-detais/{id}", jobDetais.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobDetais> jobDetaisList = jobDetaisRepository.findAll();
        assertThat(jobDetaisList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobDetais.class);
        JobDetais jobDetais1 = new JobDetais();
        jobDetais1.setId(1L);
        JobDetais jobDetais2 = new JobDetais();
        jobDetais2.setId(jobDetais1.getId());
        assertThat(jobDetais1).isEqualTo(jobDetais2);
        jobDetais2.setId(2L);
        assertThat(jobDetais1).isNotEqualTo(jobDetais2);
        jobDetais1.setId(null);
        assertThat(jobDetais1).isNotEqualTo(jobDetais2);
    }
}
