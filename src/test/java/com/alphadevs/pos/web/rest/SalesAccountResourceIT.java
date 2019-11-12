package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.SalesAccount;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.domain.TransactionType;
import com.alphadevs.pos.repository.SalesAccountRepository;
import com.alphadevs.pos.service.SalesAccountService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.SalesAccountCriteria;
import com.alphadevs.pos.service.SalesAccountQueryService;

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
 * Integration tests for the {@link SalesAccountResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class SalesAccountResourceIT {

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TRANSACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_TRANSACTION_AMOUNT_DR = 1D;
    private static final Double UPDATED_TRANSACTION_AMOUNT_DR = 2D;
    private static final Double SMALLER_TRANSACTION_AMOUNT_DR = 1D - 1D;

    private static final Double DEFAULT_TRANSACTION_AMOUNT_CR = 1D;
    private static final Double UPDATED_TRANSACTION_AMOUNT_CR = 2D;
    private static final Double SMALLER_TRANSACTION_AMOUNT_CR = 1D - 1D;

    private static final Double DEFAULT_TRANSACTION_BALANCE = 1D;
    private static final Double UPDATED_TRANSACTION_BALANCE = 2D;
    private static final Double SMALLER_TRANSACTION_BALANCE = 1D - 1D;

    @Autowired
    private SalesAccountRepository salesAccountRepository;

    @Autowired
    private SalesAccountService salesAccountService;

    @Autowired
    private SalesAccountQueryService salesAccountQueryService;

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

    private MockMvc restSalesAccountMockMvc;

    private SalesAccount salesAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalesAccountResource salesAccountResource = new SalesAccountResource(salesAccountService, salesAccountQueryService);
        this.restSalesAccountMockMvc = MockMvcBuilders.standaloneSetup(salesAccountResource)
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
    public static SalesAccount createEntity(EntityManager em) {
        SalesAccount salesAccount = new SalesAccount()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(DEFAULT_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(DEFAULT_TRANSACTION_AMOUNT_CR)
            .transactionBalance(DEFAULT_TRANSACTION_BALANCE);
        return salesAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesAccount createUpdatedEntity(EntityManager em) {
        SalesAccount salesAccount = new SalesAccount()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);
        return salesAccount;
    }

    @BeforeEach
    public void initTest() {
        salesAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalesAccount() throws Exception {
        int databaseSizeBeforeCreate = salesAccountRepository.findAll().size();

        // Create the SalesAccount
        restSalesAccountMockMvc.perform(post("/api/sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccount)))
            .andExpect(status().isCreated());

        // Validate the SalesAccount in the database
        List<SalesAccount> salesAccountList = salesAccountRepository.findAll();
        assertThat(salesAccountList).hasSize(databaseSizeBeforeCreate + 1);
        SalesAccount testSalesAccount = salesAccountList.get(salesAccountList.size() - 1);
        assertThat(testSalesAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testSalesAccount.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testSalesAccount.getTransactionAmountDR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testSalesAccount.getTransactionAmountCR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testSalesAccount.getTransactionBalance()).isEqualTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void createSalesAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesAccountRepository.findAll().size();

        // Create the SalesAccount with an existing ID
        salesAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesAccountMockMvc.perform(post("/api/sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccount)))
            .andExpect(status().isBadRequest());

        // Validate the SalesAccount in the database
        List<SalesAccount> salesAccountList = salesAccountRepository.findAll();
        assertThat(salesAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesAccountRepository.findAll().size();
        // set the field null
        salesAccount.setTransactionDate(null);

        // Create the SalesAccount, which fails.

        restSalesAccountMockMvc.perform(post("/api/sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccount)))
            .andExpect(status().isBadRequest());

        List<SalesAccount> salesAccountList = salesAccountRepository.findAll();
        assertThat(salesAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesAccountRepository.findAll().size();
        // set the field null
        salesAccount.setTransactionDescription(null);

        // Create the SalesAccount, which fails.

        restSalesAccountMockMvc.perform(post("/api/sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccount)))
            .andExpect(status().isBadRequest());

        List<SalesAccount> salesAccountList = salesAccountRepository.findAll();
        assertThat(salesAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesAccountRepository.findAll().size();
        // set the field null
        salesAccount.setTransactionAmountDR(null);

        // Create the SalesAccount, which fails.

        restSalesAccountMockMvc.perform(post("/api/sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccount)))
            .andExpect(status().isBadRequest());

        List<SalesAccount> salesAccountList = salesAccountRepository.findAll();
        assertThat(salesAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesAccountRepository.findAll().size();
        // set the field null
        salesAccount.setTransactionAmountCR(null);

        // Create the SalesAccount, which fails.

        restSalesAccountMockMvc.perform(post("/api/sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccount)))
            .andExpect(status().isBadRequest());

        List<SalesAccount> salesAccountList = salesAccountRepository.findAll();
        assertThat(salesAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesAccountRepository.findAll().size();
        // set the field null
        salesAccount.setTransactionBalance(null);

        // Create the SalesAccount, which fails.

        restSalesAccountMockMvc.perform(post("/api/sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccount)))
            .andExpect(status().isBadRequest());

        List<SalesAccount> salesAccountList = salesAccountRepository.findAll();
        assertThat(salesAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSalesAccounts() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList
        restSalesAccountMockMvc.perform(get("/api/sales-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSalesAccount() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get the salesAccount
        restSalesAccountMockMvc.perform(get("/api/sales-accounts/{id}", salesAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salesAccount.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmountDR").value(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue()))
            .andExpect(jsonPath("$.transactionAmountCR").value(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue()))
            .andExpect(jsonPath("$.transactionBalance").value(DEFAULT_TRANSACTION_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultSalesAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the salesAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultSalesAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultSalesAccountShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the salesAccountList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultSalesAccountShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultSalesAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the salesAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultSalesAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDate is not null
        defaultSalesAccountShouldBeFound("transactionDate.specified=true");

        // Get all the salesAccountList where transactionDate is null
        defaultSalesAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultSalesAccountShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the salesAccountList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultSalesAccountShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultSalesAccountShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the salesAccountList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultSalesAccountShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultSalesAccountShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the salesAccountList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultSalesAccountShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultSalesAccountShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the salesAccountList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultSalesAccountShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultSalesAccountShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the salesAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultSalesAccountShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultSalesAccountShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the salesAccountList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultSalesAccountShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultSalesAccountShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the salesAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultSalesAccountShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDescription is not null
        defaultSalesAccountShouldBeFound("transactionDescription.specified=true");

        // Get all the salesAccountList where transactionDescription is null
        defaultSalesAccountShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultSalesAccountShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the salesAccountList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultSalesAccountShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultSalesAccountShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the salesAccountList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultSalesAccountShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountDR equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldBeFound("transactionAmountDR.equals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the salesAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldNotBeFound("transactionAmountDR.equals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountDRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountDR not equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldNotBeFound("transactionAmountDR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the salesAccountList where transactionAmountDR not equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldBeFound("transactionAmountDR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountDR in DEFAULT_TRANSACTION_AMOUNT_DR or UPDATED_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldBeFound("transactionAmountDR.in=" + DEFAULT_TRANSACTION_AMOUNT_DR + "," + UPDATED_TRANSACTION_AMOUNT_DR);

        // Get all the salesAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldNotBeFound("transactionAmountDR.in=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountDR is not null
        defaultSalesAccountShouldBeFound("transactionAmountDR.specified=true");

        // Get all the salesAccountList where transactionAmountDR is null
        defaultSalesAccountShouldNotBeFound("transactionAmountDR.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountDR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldBeFound("transactionAmountDR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the salesAccountList where transactionAmountDR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldNotBeFound("transactionAmountDR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountDR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldBeFound("transactionAmountDR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the salesAccountList where transactionAmountDR is less than or equal to SMALLER_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldNotBeFound("transactionAmountDR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountDR is less than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldNotBeFound("transactionAmountDR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the salesAccountList where transactionAmountDR is less than UPDATED_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldBeFound("transactionAmountDR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountDR is greater than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldNotBeFound("transactionAmountDR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the salesAccountList where transactionAmountDR is greater than SMALLER_TRANSACTION_AMOUNT_DR
        defaultSalesAccountShouldBeFound("transactionAmountDR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }


    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountCR equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldBeFound("transactionAmountCR.equals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the salesAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldNotBeFound("transactionAmountCR.equals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountCRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountCR not equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldNotBeFound("transactionAmountCR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the salesAccountList where transactionAmountCR not equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldBeFound("transactionAmountCR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountCR in DEFAULT_TRANSACTION_AMOUNT_CR or UPDATED_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldBeFound("transactionAmountCR.in=" + DEFAULT_TRANSACTION_AMOUNT_CR + "," + UPDATED_TRANSACTION_AMOUNT_CR);

        // Get all the salesAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldNotBeFound("transactionAmountCR.in=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountCR is not null
        defaultSalesAccountShouldBeFound("transactionAmountCR.specified=true");

        // Get all the salesAccountList where transactionAmountCR is null
        defaultSalesAccountShouldNotBeFound("transactionAmountCR.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountCR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldBeFound("transactionAmountCR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the salesAccountList where transactionAmountCR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldNotBeFound("transactionAmountCR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountCR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldBeFound("transactionAmountCR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the salesAccountList where transactionAmountCR is less than or equal to SMALLER_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldNotBeFound("transactionAmountCR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountCR is less than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldNotBeFound("transactionAmountCR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the salesAccountList where transactionAmountCR is less than UPDATED_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldBeFound("transactionAmountCR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionAmountCR is greater than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldNotBeFound("transactionAmountCR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the salesAccountList where transactionAmountCR is greater than SMALLER_TRANSACTION_AMOUNT_CR
        defaultSalesAccountShouldBeFound("transactionAmountCR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }


    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultSalesAccountShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the salesAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultSalesAccountShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionBalance not equals to DEFAULT_TRANSACTION_BALANCE
        defaultSalesAccountShouldNotBeFound("transactionBalance.notEquals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the salesAccountList where transactionBalance not equals to UPDATED_TRANSACTION_BALANCE
        defaultSalesAccountShouldBeFound("transactionBalance.notEquals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultSalesAccountShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the salesAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultSalesAccountShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionBalance is not null
        defaultSalesAccountShouldBeFound("transactionBalance.specified=true");

        // Get all the salesAccountList where transactionBalance is null
        defaultSalesAccountShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultSalesAccountShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the salesAccountList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultSalesAccountShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultSalesAccountShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the salesAccountList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultSalesAccountShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultSalesAccountShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the salesAccountList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultSalesAccountShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

        // Get all the salesAccountList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultSalesAccountShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the salesAccountList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultSalesAccountShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }


    @Test
    @Transactional
    public void getAllSalesAccountsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        salesAccount.setLocation(location);
        salesAccountRepository.saveAndFlush(salesAccount);
        Long locationId = location.getId();

        // Get all the salesAccountList where location equals to locationId
        defaultSalesAccountShouldBeFound("locationId.equals=" + locationId);

        // Get all the salesAccountList where location equals to locationId + 1
        defaultSalesAccountShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllSalesAccountsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);
        TransactionType transactionType = TransactionTypeResourceIT.createEntity(em);
        em.persist(transactionType);
        em.flush();
        salesAccount.setTransactionType(transactionType);
        salesAccountRepository.saveAndFlush(salesAccount);
        Long transactionTypeId = transactionType.getId();

        // Get all the salesAccountList where transactionType equals to transactionTypeId
        defaultSalesAccountShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the salesAccountList where transactionType equals to transactionTypeId + 1
        defaultSalesAccountShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSalesAccountShouldBeFound(String filter) throws Exception {
        restSalesAccountMockMvc.perform(get("/api/sales-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restSalesAccountMockMvc.perform(get("/api/sales-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSalesAccountShouldNotBeFound(String filter) throws Exception {
        restSalesAccountMockMvc.perform(get("/api/sales-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSalesAccountMockMvc.perform(get("/api/sales-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSalesAccount() throws Exception {
        // Get the salesAccount
        restSalesAccountMockMvc.perform(get("/api/sales-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalesAccount() throws Exception {
        // Initialize the database
        salesAccountService.save(salesAccount);

        int databaseSizeBeforeUpdate = salesAccountRepository.findAll().size();

        // Update the salesAccount
        SalesAccount updatedSalesAccount = salesAccountRepository.findById(salesAccount.getId()).get();
        // Disconnect from session so that the updates on updatedSalesAccount are not directly saved in db
        em.detach(updatedSalesAccount);
        updatedSalesAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restSalesAccountMockMvc.perform(put("/api/sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSalesAccount)))
            .andExpect(status().isOk());

        // Validate the SalesAccount in the database
        List<SalesAccount> salesAccountList = salesAccountRepository.findAll();
        assertThat(salesAccountList).hasSize(databaseSizeBeforeUpdate);
        SalesAccount testSalesAccount = salesAccountList.get(salesAccountList.size() - 1);
        assertThat(testSalesAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testSalesAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testSalesAccount.getTransactionAmountDR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testSalesAccount.getTransactionAmountCR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testSalesAccount.getTransactionBalance()).isEqualTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingSalesAccount() throws Exception {
        int databaseSizeBeforeUpdate = salesAccountRepository.findAll().size();

        // Create the SalesAccount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesAccountMockMvc.perform(put("/api/sales-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccount)))
            .andExpect(status().isBadRequest());

        // Validate the SalesAccount in the database
        List<SalesAccount> salesAccountList = salesAccountRepository.findAll();
        assertThat(salesAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSalesAccount() throws Exception {
        // Initialize the database
        salesAccountService.save(salesAccount);

        int databaseSizeBeforeDelete = salesAccountRepository.findAll().size();

        // Delete the salesAccount
        restSalesAccountMockMvc.perform(delete("/api/sales-accounts/{id}", salesAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SalesAccount> salesAccountList = salesAccountRepository.findAll();
        assertThat(salesAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesAccount.class);
        SalesAccount salesAccount1 = new SalesAccount();
        salesAccount1.setId(1L);
        SalesAccount salesAccount2 = new SalesAccount();
        salesAccount2.setId(salesAccount1.getId());
        assertThat(salesAccount1).isEqualTo(salesAccount2);
        salesAccount2.setId(2L);
        assertThat(salesAccount1).isNotEqualTo(salesAccount2);
        salesAccount1.setId(null);
        assertThat(salesAccount1).isNotEqualTo(salesAccount2);
    }
}
