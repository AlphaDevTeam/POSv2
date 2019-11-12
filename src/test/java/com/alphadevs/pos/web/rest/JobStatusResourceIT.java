package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.JobStatus;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.repository.JobStatusRepository;
import com.alphadevs.pos.service.JobStatusService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.JobStatusCriteria;
import com.alphadevs.pos.service.JobStatusQueryService;

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
 * Integration tests for the {@link JobStatusResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class JobStatusResourceIT {

    private static final String DEFAULT_JOB_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_STATUS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_STATUS_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_JOB_STATUS_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private JobStatusRepository jobStatusRepository;

    @Autowired
    private JobStatusService jobStatusService;

    @Autowired
    private JobStatusQueryService jobStatusQueryService;

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

    private MockMvc restJobStatusMockMvc;

    private JobStatus jobStatus;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobStatusResource jobStatusResource = new JobStatusResource(jobStatusService, jobStatusQueryService);
        this.restJobStatusMockMvc = MockMvcBuilders.standaloneSetup(jobStatusResource)
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
    public static JobStatus createEntity(EntityManager em) {
        JobStatus jobStatus = new JobStatus()
            .jobStatusCode(DEFAULT_JOB_STATUS_CODE)
            .jobStatusDescription(DEFAULT_JOB_STATUS_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return jobStatus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobStatus createUpdatedEntity(EntityManager em) {
        JobStatus jobStatus = new JobStatus()
            .jobStatusCode(UPDATED_JOB_STATUS_CODE)
            .jobStatusDescription(UPDATED_JOB_STATUS_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return jobStatus;
    }

    @BeforeEach
    public void initTest() {
        jobStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobStatus() throws Exception {
        int databaseSizeBeforeCreate = jobStatusRepository.findAll().size();

        // Create the JobStatus
        restJobStatusMockMvc.perform(post("/api/job-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobStatus)))
            .andExpect(status().isCreated());

        // Validate the JobStatus in the database
        List<JobStatus> jobStatusList = jobStatusRepository.findAll();
        assertThat(jobStatusList).hasSize(databaseSizeBeforeCreate + 1);
        JobStatus testJobStatus = jobStatusList.get(jobStatusList.size() - 1);
        assertThat(testJobStatus.getJobStatusCode()).isEqualTo(DEFAULT_JOB_STATUS_CODE);
        assertThat(testJobStatus.getJobStatusDescription()).isEqualTo(DEFAULT_JOB_STATUS_DESCRIPTION);
        assertThat(testJobStatus.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createJobStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobStatusRepository.findAll().size();

        // Create the JobStatus with an existing ID
        jobStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobStatusMockMvc.perform(post("/api/job-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobStatus)))
            .andExpect(status().isBadRequest());

        // Validate the JobStatus in the database
        List<JobStatus> jobStatusList = jobStatusRepository.findAll();
        assertThat(jobStatusList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkJobStatusCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobStatusRepository.findAll().size();
        // set the field null
        jobStatus.setJobStatusCode(null);

        // Create the JobStatus, which fails.

        restJobStatusMockMvc.perform(post("/api/job-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobStatus)))
            .andExpect(status().isBadRequest());

        List<JobStatus> jobStatusList = jobStatusRepository.findAll();
        assertThat(jobStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobStatuses() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList
        restJobStatusMockMvc.perform(get("/api/job-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobStatusCode").value(hasItem(DEFAULT_JOB_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].jobStatusDescription").value(hasItem(DEFAULT_JOB_STATUS_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getJobStatus() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get the jobStatus
        restJobStatusMockMvc.perform(get("/api/job-statuses/{id}", jobStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobStatus.getId().intValue()))
            .andExpect(jsonPath("$.jobStatusCode").value(DEFAULT_JOB_STATUS_CODE))
            .andExpect(jsonPath("$.jobStatusDescription").value(DEFAULT_JOB_STATUS_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllJobStatusesByJobStatusCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusCode equals to DEFAULT_JOB_STATUS_CODE
        defaultJobStatusShouldBeFound("jobStatusCode.equals=" + DEFAULT_JOB_STATUS_CODE);

        // Get all the jobStatusList where jobStatusCode equals to UPDATED_JOB_STATUS_CODE
        defaultJobStatusShouldNotBeFound("jobStatusCode.equals=" + UPDATED_JOB_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllJobStatusesByJobStatusCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusCode not equals to DEFAULT_JOB_STATUS_CODE
        defaultJobStatusShouldNotBeFound("jobStatusCode.notEquals=" + DEFAULT_JOB_STATUS_CODE);

        // Get all the jobStatusList where jobStatusCode not equals to UPDATED_JOB_STATUS_CODE
        defaultJobStatusShouldBeFound("jobStatusCode.notEquals=" + UPDATED_JOB_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllJobStatusesByJobStatusCodeIsInShouldWork() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusCode in DEFAULT_JOB_STATUS_CODE or UPDATED_JOB_STATUS_CODE
        defaultJobStatusShouldBeFound("jobStatusCode.in=" + DEFAULT_JOB_STATUS_CODE + "," + UPDATED_JOB_STATUS_CODE);

        // Get all the jobStatusList where jobStatusCode equals to UPDATED_JOB_STATUS_CODE
        defaultJobStatusShouldNotBeFound("jobStatusCode.in=" + UPDATED_JOB_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllJobStatusesByJobStatusCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusCode is not null
        defaultJobStatusShouldBeFound("jobStatusCode.specified=true");

        // Get all the jobStatusList where jobStatusCode is null
        defaultJobStatusShouldNotBeFound("jobStatusCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllJobStatusesByJobStatusCodeContainsSomething() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusCode contains DEFAULT_JOB_STATUS_CODE
        defaultJobStatusShouldBeFound("jobStatusCode.contains=" + DEFAULT_JOB_STATUS_CODE);

        // Get all the jobStatusList where jobStatusCode contains UPDATED_JOB_STATUS_CODE
        defaultJobStatusShouldNotBeFound("jobStatusCode.contains=" + UPDATED_JOB_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllJobStatusesByJobStatusCodeNotContainsSomething() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusCode does not contain DEFAULT_JOB_STATUS_CODE
        defaultJobStatusShouldNotBeFound("jobStatusCode.doesNotContain=" + DEFAULT_JOB_STATUS_CODE);

        // Get all the jobStatusList where jobStatusCode does not contain UPDATED_JOB_STATUS_CODE
        defaultJobStatusShouldBeFound("jobStatusCode.doesNotContain=" + UPDATED_JOB_STATUS_CODE);
    }


    @Test
    @Transactional
    public void getAllJobStatusesByJobStatusDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusDescription equals to DEFAULT_JOB_STATUS_DESCRIPTION
        defaultJobStatusShouldBeFound("jobStatusDescription.equals=" + DEFAULT_JOB_STATUS_DESCRIPTION);

        // Get all the jobStatusList where jobStatusDescription equals to UPDATED_JOB_STATUS_DESCRIPTION
        defaultJobStatusShouldNotBeFound("jobStatusDescription.equals=" + UPDATED_JOB_STATUS_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJobStatusesByJobStatusDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusDescription not equals to DEFAULT_JOB_STATUS_DESCRIPTION
        defaultJobStatusShouldNotBeFound("jobStatusDescription.notEquals=" + DEFAULT_JOB_STATUS_DESCRIPTION);

        // Get all the jobStatusList where jobStatusDescription not equals to UPDATED_JOB_STATUS_DESCRIPTION
        defaultJobStatusShouldBeFound("jobStatusDescription.notEquals=" + UPDATED_JOB_STATUS_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJobStatusesByJobStatusDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusDescription in DEFAULT_JOB_STATUS_DESCRIPTION or UPDATED_JOB_STATUS_DESCRIPTION
        defaultJobStatusShouldBeFound("jobStatusDescription.in=" + DEFAULT_JOB_STATUS_DESCRIPTION + "," + UPDATED_JOB_STATUS_DESCRIPTION);

        // Get all the jobStatusList where jobStatusDescription equals to UPDATED_JOB_STATUS_DESCRIPTION
        defaultJobStatusShouldNotBeFound("jobStatusDescription.in=" + UPDATED_JOB_STATUS_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJobStatusesByJobStatusDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusDescription is not null
        defaultJobStatusShouldBeFound("jobStatusDescription.specified=true");

        // Get all the jobStatusList where jobStatusDescription is null
        defaultJobStatusShouldNotBeFound("jobStatusDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllJobStatusesByJobStatusDescriptionContainsSomething() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusDescription contains DEFAULT_JOB_STATUS_DESCRIPTION
        defaultJobStatusShouldBeFound("jobStatusDescription.contains=" + DEFAULT_JOB_STATUS_DESCRIPTION);

        // Get all the jobStatusList where jobStatusDescription contains UPDATED_JOB_STATUS_DESCRIPTION
        defaultJobStatusShouldNotBeFound("jobStatusDescription.contains=" + UPDATED_JOB_STATUS_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJobStatusesByJobStatusDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where jobStatusDescription does not contain DEFAULT_JOB_STATUS_DESCRIPTION
        defaultJobStatusShouldNotBeFound("jobStatusDescription.doesNotContain=" + DEFAULT_JOB_STATUS_DESCRIPTION);

        // Get all the jobStatusList where jobStatusDescription does not contain UPDATED_JOB_STATUS_DESCRIPTION
        defaultJobStatusShouldBeFound("jobStatusDescription.doesNotContain=" + UPDATED_JOB_STATUS_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllJobStatusesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where isActive equals to DEFAULT_IS_ACTIVE
        defaultJobStatusShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the jobStatusList where isActive equals to UPDATED_IS_ACTIVE
        defaultJobStatusShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllJobStatusesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultJobStatusShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the jobStatusList where isActive not equals to UPDATED_IS_ACTIVE
        defaultJobStatusShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllJobStatusesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultJobStatusShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the jobStatusList where isActive equals to UPDATED_IS_ACTIVE
        defaultJobStatusShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllJobStatusesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatusList where isActive is not null
        defaultJobStatusShouldBeFound("isActive.specified=true");

        // Get all the jobStatusList where isActive is null
        defaultJobStatusShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobStatusesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        jobStatus.setLocation(location);
        jobStatusRepository.saveAndFlush(jobStatus);
        Long locationId = location.getId();

        // Get all the jobStatusList where location equals to locationId
        defaultJobStatusShouldBeFound("locationId.equals=" + locationId);

        // Get all the jobStatusList where location equals to locationId + 1
        defaultJobStatusShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobStatusShouldBeFound(String filter) throws Exception {
        restJobStatusMockMvc.perform(get("/api/job-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobStatusCode").value(hasItem(DEFAULT_JOB_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].jobStatusDescription").value(hasItem(DEFAULT_JOB_STATUS_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restJobStatusMockMvc.perform(get("/api/job-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobStatusShouldNotBeFound(String filter) throws Exception {
        restJobStatusMockMvc.perform(get("/api/job-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobStatusMockMvc.perform(get("/api/job-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingJobStatus() throws Exception {
        // Get the jobStatus
        restJobStatusMockMvc.perform(get("/api/job-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobStatus() throws Exception {
        // Initialize the database
        jobStatusService.save(jobStatus);

        int databaseSizeBeforeUpdate = jobStatusRepository.findAll().size();

        // Update the jobStatus
        JobStatus updatedJobStatus = jobStatusRepository.findById(jobStatus.getId()).get();
        // Disconnect from session so that the updates on updatedJobStatus are not directly saved in db
        em.detach(updatedJobStatus);
        updatedJobStatus
            .jobStatusCode(UPDATED_JOB_STATUS_CODE)
            .jobStatusDescription(UPDATED_JOB_STATUS_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restJobStatusMockMvc.perform(put("/api/job-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobStatus)))
            .andExpect(status().isOk());

        // Validate the JobStatus in the database
        List<JobStatus> jobStatusList = jobStatusRepository.findAll();
        assertThat(jobStatusList).hasSize(databaseSizeBeforeUpdate);
        JobStatus testJobStatus = jobStatusList.get(jobStatusList.size() - 1);
        assertThat(testJobStatus.getJobStatusCode()).isEqualTo(UPDATED_JOB_STATUS_CODE);
        assertThat(testJobStatus.getJobStatusDescription()).isEqualTo(UPDATED_JOB_STATUS_DESCRIPTION);
        assertThat(testJobStatus.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingJobStatus() throws Exception {
        int databaseSizeBeforeUpdate = jobStatusRepository.findAll().size();

        // Create the JobStatus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobStatusMockMvc.perform(put("/api/job-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobStatus)))
            .andExpect(status().isBadRequest());

        // Validate the JobStatus in the database
        List<JobStatus> jobStatusList = jobStatusRepository.findAll();
        assertThat(jobStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJobStatus() throws Exception {
        // Initialize the database
        jobStatusService.save(jobStatus);

        int databaseSizeBeforeDelete = jobStatusRepository.findAll().size();

        // Delete the jobStatus
        restJobStatusMockMvc.perform(delete("/api/job-statuses/{id}", jobStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobStatus> jobStatusList = jobStatusRepository.findAll();
        assertThat(jobStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobStatus.class);
        JobStatus jobStatus1 = new JobStatus();
        jobStatus1.setId(1L);
        JobStatus jobStatus2 = new JobStatus();
        jobStatus2.setId(jobStatus1.getId());
        assertThat(jobStatus1).isEqualTo(jobStatus2);
        jobStatus2.setId(2L);
        assertThat(jobStatus1).isNotEqualTo(jobStatus2);
        jobStatus1.setId(null);
        assertThat(jobStatus1).isNotEqualTo(jobStatus2);
    }
}
