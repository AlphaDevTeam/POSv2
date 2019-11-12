package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.Job;
import com.alphadevs.pos.domain.JobStatus;
import com.alphadevs.pos.domain.JobDetais;
import com.alphadevs.pos.domain.Worker;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.domain.Customer;
import com.alphadevs.pos.repository.JobRepository;
import com.alphadevs.pos.service.JobService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.JobCriteria;
import com.alphadevs.pos.service.JobQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.alphadevs.pos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link JobResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class JobResourceIT {

    private static final String DEFAULT_JOB_CODE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_JOB_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_JOB_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_JOB_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_JOB_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_JOB_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_JOB_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_JOB_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_JOB_AMOUNT = 1D;
    private static final Double UPDATED_JOB_AMOUNT = 2D;
    private static final Double SMALLER_JOB_AMOUNT = 1D - 1D;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobQueryService jobQueryService;

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

    private MockMvc restJobMockMvc;

    private Job job;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobResource jobResource = new JobResource(jobService, jobQueryService);
        this.restJobMockMvc = MockMvcBuilders.standaloneSetup(jobResource)
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
    public static Job createEntity(EntityManager em) {
        Job job = new Job()
            .jobCode(DEFAULT_JOB_CODE)
            .jobDescription(DEFAULT_JOB_DESCRIPTION)
            .jobStartDate(DEFAULT_JOB_START_DATE)
            .jobEndDate(DEFAULT_JOB_END_DATE)
            .jobAmount(DEFAULT_JOB_AMOUNT);
        return job;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createUpdatedEntity(EntityManager em) {
        Job job = new Job()
            .jobCode(UPDATED_JOB_CODE)
            .jobDescription(UPDATED_JOB_DESCRIPTION)
            .jobStartDate(UPDATED_JOB_START_DATE)
            .jobEndDate(UPDATED_JOB_END_DATE)
            .jobAmount(UPDATED_JOB_AMOUNT);
        return job;
    }

    @BeforeEach
    public void initTest() {
        job = createEntity(em);
    }

    @Test
    @Transactional
    public void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getJobCode()).isEqualTo(DEFAULT_JOB_CODE);
        assertThat(testJob.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
        assertThat(testJob.getJobStartDate()).isEqualTo(DEFAULT_JOB_START_DATE);
        assertThat(testJob.getJobEndDate()).isEqualTo(DEFAULT_JOB_END_DATE);
        assertThat(testJob.getJobAmount()).isEqualTo(DEFAULT_JOB_AMOUNT);
    }

    @Test
    @Transactional
    public void createJobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job with an existing ID
        job.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkJobCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setJobCode(null);

        // Create the Job, which fails.

        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobCode").value(hasItem(DEFAULT_JOB_CODE)))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].jobStartDate").value(hasItem(DEFAULT_JOB_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].jobEndDate").value(hasItem(DEFAULT_JOB_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].jobAmount").value(hasItem(DEFAULT_JOB_AMOUNT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.jobCode").value(DEFAULT_JOB_CODE))
            .andExpect(jsonPath("$.jobDescription").value(DEFAULT_JOB_DESCRIPTION))
            .andExpect(jsonPath("$.jobStartDate").value(DEFAULT_JOB_START_DATE.toString()))
            .andExpect(jsonPath("$.jobEndDate").value(DEFAULT_JOB_END_DATE.toString()))
            .andExpect(jsonPath("$.jobAmount").value(DEFAULT_JOB_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllJobsByJobCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobCode equals to DEFAULT_JOB_CODE
        defaultJobShouldBeFound("jobCode.equals=" + DEFAULT_JOB_CODE);

        // Get all the jobList where jobCode equals to UPDATED_JOB_CODE
        defaultJobShouldNotBeFound("jobCode.equals=" + UPDATED_JOB_CODE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobCode not equals to DEFAULT_JOB_CODE
        defaultJobShouldNotBeFound("jobCode.notEquals=" + DEFAULT_JOB_CODE);

        // Get all the jobList where jobCode not equals to UPDATED_JOB_CODE
        defaultJobShouldBeFound("jobCode.notEquals=" + UPDATED_JOB_CODE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobCodeIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobCode in DEFAULT_JOB_CODE or UPDATED_JOB_CODE
        defaultJobShouldBeFound("jobCode.in=" + DEFAULT_JOB_CODE + "," + UPDATED_JOB_CODE);

        // Get all the jobList where jobCode equals to UPDATED_JOB_CODE
        defaultJobShouldNotBeFound("jobCode.in=" + UPDATED_JOB_CODE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobCode is not null
        defaultJobShouldBeFound("jobCode.specified=true");

        // Get all the jobList where jobCode is null
        defaultJobShouldNotBeFound("jobCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllJobsByJobCodeContainsSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobCode contains DEFAULT_JOB_CODE
        defaultJobShouldBeFound("jobCode.contains=" + DEFAULT_JOB_CODE);

        // Get all the jobList where jobCode contains UPDATED_JOB_CODE
        defaultJobShouldNotBeFound("jobCode.contains=" + UPDATED_JOB_CODE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobCodeNotContainsSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobCode does not contain DEFAULT_JOB_CODE
        defaultJobShouldNotBeFound("jobCode.doesNotContain=" + DEFAULT_JOB_CODE);

        // Get all the jobList where jobCode does not contain UPDATED_JOB_CODE
        defaultJobShouldBeFound("jobCode.doesNotContain=" + UPDATED_JOB_CODE);
    }


    @Test
    @Transactional
    public void getAllJobsByJobDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobDescription equals to DEFAULT_JOB_DESCRIPTION
        defaultJobShouldBeFound("jobDescription.equals=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the jobList where jobDescription equals to UPDATED_JOB_DESCRIPTION
        defaultJobShouldNotBeFound("jobDescription.equals=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJobsByJobDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobDescription not equals to DEFAULT_JOB_DESCRIPTION
        defaultJobShouldNotBeFound("jobDescription.notEquals=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the jobList where jobDescription not equals to UPDATED_JOB_DESCRIPTION
        defaultJobShouldBeFound("jobDescription.notEquals=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJobsByJobDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobDescription in DEFAULT_JOB_DESCRIPTION or UPDATED_JOB_DESCRIPTION
        defaultJobShouldBeFound("jobDescription.in=" + DEFAULT_JOB_DESCRIPTION + "," + UPDATED_JOB_DESCRIPTION);

        // Get all the jobList where jobDescription equals to UPDATED_JOB_DESCRIPTION
        defaultJobShouldNotBeFound("jobDescription.in=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJobsByJobDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobDescription is not null
        defaultJobShouldBeFound("jobDescription.specified=true");

        // Get all the jobList where jobDescription is null
        defaultJobShouldNotBeFound("jobDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllJobsByJobDescriptionContainsSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobDescription contains DEFAULT_JOB_DESCRIPTION
        defaultJobShouldBeFound("jobDescription.contains=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the jobList where jobDescription contains UPDATED_JOB_DESCRIPTION
        defaultJobShouldNotBeFound("jobDescription.contains=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJobsByJobDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobDescription does not contain DEFAULT_JOB_DESCRIPTION
        defaultJobShouldNotBeFound("jobDescription.doesNotContain=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the jobList where jobDescription does not contain UPDATED_JOB_DESCRIPTION
        defaultJobShouldBeFound("jobDescription.doesNotContain=" + UPDATED_JOB_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllJobsByJobStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStartDate equals to DEFAULT_JOB_START_DATE
        defaultJobShouldBeFound("jobStartDate.equals=" + DEFAULT_JOB_START_DATE);

        // Get all the jobList where jobStartDate equals to UPDATED_JOB_START_DATE
        defaultJobShouldNotBeFound("jobStartDate.equals=" + UPDATED_JOB_START_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStartDate not equals to DEFAULT_JOB_START_DATE
        defaultJobShouldNotBeFound("jobStartDate.notEquals=" + DEFAULT_JOB_START_DATE);

        // Get all the jobList where jobStartDate not equals to UPDATED_JOB_START_DATE
        defaultJobShouldBeFound("jobStartDate.notEquals=" + UPDATED_JOB_START_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStartDate in DEFAULT_JOB_START_DATE or UPDATED_JOB_START_DATE
        defaultJobShouldBeFound("jobStartDate.in=" + DEFAULT_JOB_START_DATE + "," + UPDATED_JOB_START_DATE);

        // Get all the jobList where jobStartDate equals to UPDATED_JOB_START_DATE
        defaultJobShouldNotBeFound("jobStartDate.in=" + UPDATED_JOB_START_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStartDate is not null
        defaultJobShouldBeFound("jobStartDate.specified=true");

        // Get all the jobList where jobStartDate is null
        defaultJobShouldNotBeFound("jobStartDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByJobStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStartDate is greater than or equal to DEFAULT_JOB_START_DATE
        defaultJobShouldBeFound("jobStartDate.greaterThanOrEqual=" + DEFAULT_JOB_START_DATE);

        // Get all the jobList where jobStartDate is greater than or equal to UPDATED_JOB_START_DATE
        defaultJobShouldNotBeFound("jobStartDate.greaterThanOrEqual=" + UPDATED_JOB_START_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStartDate is less than or equal to DEFAULT_JOB_START_DATE
        defaultJobShouldBeFound("jobStartDate.lessThanOrEqual=" + DEFAULT_JOB_START_DATE);

        // Get all the jobList where jobStartDate is less than or equal to SMALLER_JOB_START_DATE
        defaultJobShouldNotBeFound("jobStartDate.lessThanOrEqual=" + SMALLER_JOB_START_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStartDate is less than DEFAULT_JOB_START_DATE
        defaultJobShouldNotBeFound("jobStartDate.lessThan=" + DEFAULT_JOB_START_DATE);

        // Get all the jobList where jobStartDate is less than UPDATED_JOB_START_DATE
        defaultJobShouldBeFound("jobStartDate.lessThan=" + UPDATED_JOB_START_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStartDate is greater than DEFAULT_JOB_START_DATE
        defaultJobShouldNotBeFound("jobStartDate.greaterThan=" + DEFAULT_JOB_START_DATE);

        // Get all the jobList where jobStartDate is greater than SMALLER_JOB_START_DATE
        defaultJobShouldBeFound("jobStartDate.greaterThan=" + SMALLER_JOB_START_DATE);
    }


    @Test
    @Transactional
    public void getAllJobsByJobEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobEndDate equals to DEFAULT_JOB_END_DATE
        defaultJobShouldBeFound("jobEndDate.equals=" + DEFAULT_JOB_END_DATE);

        // Get all the jobList where jobEndDate equals to UPDATED_JOB_END_DATE
        defaultJobShouldNotBeFound("jobEndDate.equals=" + UPDATED_JOB_END_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobEndDate not equals to DEFAULT_JOB_END_DATE
        defaultJobShouldNotBeFound("jobEndDate.notEquals=" + DEFAULT_JOB_END_DATE);

        // Get all the jobList where jobEndDate not equals to UPDATED_JOB_END_DATE
        defaultJobShouldBeFound("jobEndDate.notEquals=" + UPDATED_JOB_END_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobEndDate in DEFAULT_JOB_END_DATE or UPDATED_JOB_END_DATE
        defaultJobShouldBeFound("jobEndDate.in=" + DEFAULT_JOB_END_DATE + "," + UPDATED_JOB_END_DATE);

        // Get all the jobList where jobEndDate equals to UPDATED_JOB_END_DATE
        defaultJobShouldNotBeFound("jobEndDate.in=" + UPDATED_JOB_END_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobEndDate is not null
        defaultJobShouldBeFound("jobEndDate.specified=true");

        // Get all the jobList where jobEndDate is null
        defaultJobShouldNotBeFound("jobEndDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByJobEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobEndDate is greater than or equal to DEFAULT_JOB_END_DATE
        defaultJobShouldBeFound("jobEndDate.greaterThanOrEqual=" + DEFAULT_JOB_END_DATE);

        // Get all the jobList where jobEndDate is greater than or equal to UPDATED_JOB_END_DATE
        defaultJobShouldNotBeFound("jobEndDate.greaterThanOrEqual=" + UPDATED_JOB_END_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobEndDate is less than or equal to DEFAULT_JOB_END_DATE
        defaultJobShouldBeFound("jobEndDate.lessThanOrEqual=" + DEFAULT_JOB_END_DATE);

        // Get all the jobList where jobEndDate is less than or equal to SMALLER_JOB_END_DATE
        defaultJobShouldNotBeFound("jobEndDate.lessThanOrEqual=" + SMALLER_JOB_END_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobEndDate is less than DEFAULT_JOB_END_DATE
        defaultJobShouldNotBeFound("jobEndDate.lessThan=" + DEFAULT_JOB_END_DATE);

        // Get all the jobList where jobEndDate is less than UPDATED_JOB_END_DATE
        defaultJobShouldBeFound("jobEndDate.lessThan=" + UPDATED_JOB_END_DATE);
    }

    @Test
    @Transactional
    public void getAllJobsByJobEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobEndDate is greater than DEFAULT_JOB_END_DATE
        defaultJobShouldNotBeFound("jobEndDate.greaterThan=" + DEFAULT_JOB_END_DATE);

        // Get all the jobList where jobEndDate is greater than SMALLER_JOB_END_DATE
        defaultJobShouldBeFound("jobEndDate.greaterThan=" + SMALLER_JOB_END_DATE);
    }


    @Test
    @Transactional
    public void getAllJobsByJobAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobAmount equals to DEFAULT_JOB_AMOUNT
        defaultJobShouldBeFound("jobAmount.equals=" + DEFAULT_JOB_AMOUNT);

        // Get all the jobList where jobAmount equals to UPDATED_JOB_AMOUNT
        defaultJobShouldNotBeFound("jobAmount.equals=" + UPDATED_JOB_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllJobsByJobAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobAmount not equals to DEFAULT_JOB_AMOUNT
        defaultJobShouldNotBeFound("jobAmount.notEquals=" + DEFAULT_JOB_AMOUNT);

        // Get all the jobList where jobAmount not equals to UPDATED_JOB_AMOUNT
        defaultJobShouldBeFound("jobAmount.notEquals=" + UPDATED_JOB_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllJobsByJobAmountIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobAmount in DEFAULT_JOB_AMOUNT or UPDATED_JOB_AMOUNT
        defaultJobShouldBeFound("jobAmount.in=" + DEFAULT_JOB_AMOUNT + "," + UPDATED_JOB_AMOUNT);

        // Get all the jobList where jobAmount equals to UPDATED_JOB_AMOUNT
        defaultJobShouldNotBeFound("jobAmount.in=" + UPDATED_JOB_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllJobsByJobAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobAmount is not null
        defaultJobShouldBeFound("jobAmount.specified=true");

        // Get all the jobList where jobAmount is null
        defaultJobShouldNotBeFound("jobAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByJobAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobAmount is greater than or equal to DEFAULT_JOB_AMOUNT
        defaultJobShouldBeFound("jobAmount.greaterThanOrEqual=" + DEFAULT_JOB_AMOUNT);

        // Get all the jobList where jobAmount is greater than or equal to UPDATED_JOB_AMOUNT
        defaultJobShouldNotBeFound("jobAmount.greaterThanOrEqual=" + UPDATED_JOB_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllJobsByJobAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobAmount is less than or equal to DEFAULT_JOB_AMOUNT
        defaultJobShouldBeFound("jobAmount.lessThanOrEqual=" + DEFAULT_JOB_AMOUNT);

        // Get all the jobList where jobAmount is less than or equal to SMALLER_JOB_AMOUNT
        defaultJobShouldNotBeFound("jobAmount.lessThanOrEqual=" + SMALLER_JOB_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllJobsByJobAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobAmount is less than DEFAULT_JOB_AMOUNT
        defaultJobShouldNotBeFound("jobAmount.lessThan=" + DEFAULT_JOB_AMOUNT);

        // Get all the jobList where jobAmount is less than UPDATED_JOB_AMOUNT
        defaultJobShouldBeFound("jobAmount.lessThan=" + UPDATED_JOB_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllJobsByJobAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobAmount is greater than DEFAULT_JOB_AMOUNT
        defaultJobShouldNotBeFound("jobAmount.greaterThan=" + DEFAULT_JOB_AMOUNT);

        // Get all the jobList where jobAmount is greater than SMALLER_JOB_AMOUNT
        defaultJobShouldBeFound("jobAmount.greaterThan=" + SMALLER_JOB_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        JobStatus status = JobStatusResourceIT.createEntity(em);
        em.persist(status);
        em.flush();
        job.setStatus(status);
        jobRepository.saveAndFlush(job);
        Long statusId = status.getId();

        // Get all the jobList where status equals to statusId
        defaultJobShouldBeFound("statusId.equals=" + statusId);

        // Get all the jobList where status equals to statusId + 1
        defaultJobShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }


    @Test
    @Transactional
    public void getAllJobsByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        JobDetais details = JobDetaisResourceIT.createEntity(em);
        em.persist(details);
        em.flush();
        job.addDetails(details);
        jobRepository.saveAndFlush(job);
        Long detailsId = details.getId();

        // Get all the jobList where details equals to detailsId
        defaultJobShouldBeFound("detailsId.equals=" + detailsId);

        // Get all the jobList where details equals to detailsId + 1
        defaultJobShouldNotBeFound("detailsId.equals=" + (detailsId + 1));
    }


    @Test
    @Transactional
    public void getAllJobsByAssignedToIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        Worker assignedTo = WorkerResourceIT.createEntity(em);
        em.persist(assignedTo);
        em.flush();
        job.addAssignedTo(assignedTo);
        jobRepository.saveAndFlush(job);
        Long assignedToId = assignedTo.getId();

        // Get all the jobList where assignedTo equals to assignedToId
        defaultJobShouldBeFound("assignedToId.equals=" + assignedToId);

        // Get all the jobList where assignedTo equals to assignedToId + 1
        defaultJobShouldNotBeFound("assignedToId.equals=" + (assignedToId + 1));
    }


    @Test
    @Transactional
    public void getAllJobsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        job.setLocation(location);
        jobRepository.saveAndFlush(job);
        Long locationId = location.getId();

        // Get all the jobList where location equals to locationId
        defaultJobShouldBeFound("locationId.equals=" + locationId);

        // Get all the jobList where location equals to locationId + 1
        defaultJobShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllJobsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        job.setCustomer(customer);
        jobRepository.saveAndFlush(job);
        Long customerId = customer.getId();

        // Get all the jobList where customer equals to customerId
        defaultJobShouldBeFound("customerId.equals=" + customerId);

        // Get all the jobList where customer equals to customerId + 1
        defaultJobShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobShouldBeFound(String filter) throws Exception {
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobCode").value(hasItem(DEFAULT_JOB_CODE)))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].jobStartDate").value(hasItem(DEFAULT_JOB_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].jobEndDate").value(hasItem(DEFAULT_JOB_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].jobAmount").value(hasItem(DEFAULT_JOB_AMOUNT.doubleValue())));

        // Check, that the count call also returns 1
        restJobMockMvc.perform(get("/api/jobs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobShouldNotBeFound(String filter) throws Exception {
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobMockMvc.perform(get("/api/jobs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJob() throws Exception {
        // Initialize the database
        jobService.save(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        Job updatedJob = jobRepository.findById(job.getId()).get();
        // Disconnect from session so that the updates on updatedJob are not directly saved in db
        em.detach(updatedJob);
        updatedJob
            .jobCode(UPDATED_JOB_CODE)
            .jobDescription(UPDATED_JOB_DESCRIPTION)
            .jobStartDate(UPDATED_JOB_START_DATE)
            .jobEndDate(UPDATED_JOB_END_DATE)
            .jobAmount(UPDATED_JOB_AMOUNT);

        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJob)))
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getJobCode()).isEqualTo(UPDATED_JOB_CODE);
        assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
        assertThat(testJob.getJobStartDate()).isEqualTo(UPDATED_JOB_START_DATE);
        assertThat(testJob.getJobEndDate()).isEqualTo(UPDATED_JOB_END_DATE);
        assertThat(testJob.getJobAmount()).isEqualTo(UPDATED_JOB_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Create the Job

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJob() throws Exception {
        // Initialize the database
        jobService.save(job);

        int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Delete the job
        restJobMockMvc.perform(delete("/api/jobs/{id}", job.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Job.class);
        Job job1 = new Job();
        job1.setId(1L);
        Job job2 = new Job();
        job2.setId(job1.getId());
        assertThat(job1).isEqualTo(job2);
        job2.setId(2L);
        assertThat(job1).isNotEqualTo(job2);
        job1.setId(null);
        assertThat(job1).isNotEqualTo(job2);
    }
}
