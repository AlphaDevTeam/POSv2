package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.PurchaseAccount;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.domain.TransactionType;
import com.alphadevs.pos.repository.PurchaseAccountRepository;
import com.alphadevs.pos.service.PurchaseAccountService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.PurchaseAccountCriteria;
import com.alphadevs.pos.service.PurchaseAccountQueryService;

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
 * Integration tests for the {@link PurchaseAccountResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class PurchaseAccountResourceIT {

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
    private PurchaseAccountRepository purchaseAccountRepository;

    @Autowired
    private PurchaseAccountService purchaseAccountService;

    @Autowired
    private PurchaseAccountQueryService purchaseAccountQueryService;

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

    private MockMvc restPurchaseAccountMockMvc;

    private PurchaseAccount purchaseAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseAccountResource purchaseAccountResource = new PurchaseAccountResource(purchaseAccountService, purchaseAccountQueryService);
        this.restPurchaseAccountMockMvc = MockMvcBuilders.standaloneSetup(purchaseAccountResource)
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
    public static PurchaseAccount createEntity(EntityManager em) {
        PurchaseAccount purchaseAccount = new PurchaseAccount()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(DEFAULT_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(DEFAULT_TRANSACTION_AMOUNT_CR)
            .transactionBalance(DEFAULT_TRANSACTION_BALANCE);
        return purchaseAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseAccount createUpdatedEntity(EntityManager em) {
        PurchaseAccount purchaseAccount = new PurchaseAccount()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);
        return purchaseAccount;
    }

    @BeforeEach
    public void initTest() {
        purchaseAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseAccount() throws Exception {
        int databaseSizeBeforeCreate = purchaseAccountRepository.findAll().size();

        // Create the PurchaseAccount
        restPurchaseAccountMockMvc.perform(post("/api/purchase-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccount)))
            .andExpect(status().isCreated());

        // Validate the PurchaseAccount in the database
        List<PurchaseAccount> purchaseAccountList = purchaseAccountRepository.findAll();
        assertThat(purchaseAccountList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseAccount testPurchaseAccount = purchaseAccountList.get(purchaseAccountList.size() - 1);
        assertThat(testPurchaseAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testPurchaseAccount.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testPurchaseAccount.getTransactionAmountDR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testPurchaseAccount.getTransactionAmountCR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testPurchaseAccount.getTransactionBalance()).isEqualTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void createPurchaseAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseAccountRepository.findAll().size();

        // Create the PurchaseAccount with an existing ID
        purchaseAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseAccountMockMvc.perform(post("/api/purchase-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccount)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseAccount in the database
        List<PurchaseAccount> purchaseAccountList = purchaseAccountRepository.findAll();
        assertThat(purchaseAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseAccountRepository.findAll().size();
        // set the field null
        purchaseAccount.setTransactionDate(null);

        // Create the PurchaseAccount, which fails.

        restPurchaseAccountMockMvc.perform(post("/api/purchase-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccount)))
            .andExpect(status().isBadRequest());

        List<PurchaseAccount> purchaseAccountList = purchaseAccountRepository.findAll();
        assertThat(purchaseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseAccountRepository.findAll().size();
        // set the field null
        purchaseAccount.setTransactionDescription(null);

        // Create the PurchaseAccount, which fails.

        restPurchaseAccountMockMvc.perform(post("/api/purchase-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccount)))
            .andExpect(status().isBadRequest());

        List<PurchaseAccount> purchaseAccountList = purchaseAccountRepository.findAll();
        assertThat(purchaseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseAccountRepository.findAll().size();
        // set the field null
        purchaseAccount.setTransactionAmountDR(null);

        // Create the PurchaseAccount, which fails.

        restPurchaseAccountMockMvc.perform(post("/api/purchase-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccount)))
            .andExpect(status().isBadRequest());

        List<PurchaseAccount> purchaseAccountList = purchaseAccountRepository.findAll();
        assertThat(purchaseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseAccountRepository.findAll().size();
        // set the field null
        purchaseAccount.setTransactionAmountCR(null);

        // Create the PurchaseAccount, which fails.

        restPurchaseAccountMockMvc.perform(post("/api/purchase-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccount)))
            .andExpect(status().isBadRequest());

        List<PurchaseAccount> purchaseAccountList = purchaseAccountRepository.findAll();
        assertThat(purchaseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseAccountRepository.findAll().size();
        // set the field null
        purchaseAccount.setTransactionBalance(null);

        // Create the PurchaseAccount, which fails.

        restPurchaseAccountMockMvc.perform(post("/api/purchase-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccount)))
            .andExpect(status().isBadRequest());

        List<PurchaseAccount> purchaseAccountList = purchaseAccountRepository.findAll();
        assertThat(purchaseAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccounts() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList
        restPurchaseAccountMockMvc.perform(get("/api/purchase-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getPurchaseAccount() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get the purchaseAccount
        restPurchaseAccountMockMvc.perform(get("/api/purchase-accounts/{id}", purchaseAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseAccount.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmountDR").value(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue()))
            .andExpect(jsonPath("$.transactionAmountCR").value(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue()))
            .andExpect(jsonPath("$.transactionBalance").value(DEFAULT_TRANSACTION_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultPurchaseAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the purchaseAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultPurchaseAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultPurchaseAccountShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the purchaseAccountList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultPurchaseAccountShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultPurchaseAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the purchaseAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultPurchaseAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDate is not null
        defaultPurchaseAccountShouldBeFound("transactionDate.specified=true");

        // Get all the purchaseAccountList where transactionDate is null
        defaultPurchaseAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultPurchaseAccountShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the purchaseAccountList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultPurchaseAccountShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultPurchaseAccountShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the purchaseAccountList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultPurchaseAccountShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultPurchaseAccountShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the purchaseAccountList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultPurchaseAccountShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultPurchaseAccountShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the purchaseAccountList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultPurchaseAccountShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultPurchaseAccountShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the purchaseAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultPurchaseAccountShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultPurchaseAccountShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the purchaseAccountList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultPurchaseAccountShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultPurchaseAccountShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the purchaseAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultPurchaseAccountShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDescription is not null
        defaultPurchaseAccountShouldBeFound("transactionDescription.specified=true");

        // Get all the purchaseAccountList where transactionDescription is null
        defaultPurchaseAccountShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultPurchaseAccountShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the purchaseAccountList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultPurchaseAccountShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultPurchaseAccountShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the purchaseAccountList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultPurchaseAccountShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountDR equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldBeFound("transactionAmountDR.equals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the purchaseAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountDR.equals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountDRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountDR not equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountDR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the purchaseAccountList where transactionAmountDR not equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldBeFound("transactionAmountDR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountDR in DEFAULT_TRANSACTION_AMOUNT_DR or UPDATED_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldBeFound("transactionAmountDR.in=" + DEFAULT_TRANSACTION_AMOUNT_DR + "," + UPDATED_TRANSACTION_AMOUNT_DR);

        // Get all the purchaseAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountDR.in=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountDR is not null
        defaultPurchaseAccountShouldBeFound("transactionAmountDR.specified=true");

        // Get all the purchaseAccountList where transactionAmountDR is null
        defaultPurchaseAccountShouldNotBeFound("transactionAmountDR.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountDR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldBeFound("transactionAmountDR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the purchaseAccountList where transactionAmountDR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountDR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountDR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldBeFound("transactionAmountDR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the purchaseAccountList where transactionAmountDR is less than or equal to SMALLER_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountDR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountDR is less than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountDR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the purchaseAccountList where transactionAmountDR is less than UPDATED_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldBeFound("transactionAmountDR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountDR is greater than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountDR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the purchaseAccountList where transactionAmountDR is greater than SMALLER_TRANSACTION_AMOUNT_DR
        defaultPurchaseAccountShouldBeFound("transactionAmountDR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }


    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountCR equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldBeFound("transactionAmountCR.equals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the purchaseAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountCR.equals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountCRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountCR not equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountCR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the purchaseAccountList where transactionAmountCR not equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldBeFound("transactionAmountCR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountCR in DEFAULT_TRANSACTION_AMOUNT_CR or UPDATED_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldBeFound("transactionAmountCR.in=" + DEFAULT_TRANSACTION_AMOUNT_CR + "," + UPDATED_TRANSACTION_AMOUNT_CR);

        // Get all the purchaseAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountCR.in=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountCR is not null
        defaultPurchaseAccountShouldBeFound("transactionAmountCR.specified=true");

        // Get all the purchaseAccountList where transactionAmountCR is null
        defaultPurchaseAccountShouldNotBeFound("transactionAmountCR.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountCR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldBeFound("transactionAmountCR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the purchaseAccountList where transactionAmountCR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountCR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountCR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldBeFound("transactionAmountCR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the purchaseAccountList where transactionAmountCR is less than or equal to SMALLER_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountCR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountCR is less than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountCR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the purchaseAccountList where transactionAmountCR is less than UPDATED_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldBeFound("transactionAmountCR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionAmountCR is greater than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldNotBeFound("transactionAmountCR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the purchaseAccountList where transactionAmountCR is greater than SMALLER_TRANSACTION_AMOUNT_CR
        defaultPurchaseAccountShouldBeFound("transactionAmountCR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }


    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the purchaseAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionBalance not equals to DEFAULT_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldNotBeFound("transactionBalance.notEquals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the purchaseAccountList where transactionBalance not equals to UPDATED_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldBeFound("transactionBalance.notEquals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the purchaseAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionBalance is not null
        defaultPurchaseAccountShouldBeFound("transactionBalance.specified=true");

        // Get all the purchaseAccountList where transactionBalance is null
        defaultPurchaseAccountShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the purchaseAccountList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the purchaseAccountList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the purchaseAccountList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

        // Get all the purchaseAccountList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the purchaseAccountList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultPurchaseAccountShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }


    @Test
    @Transactional
    public void getAllPurchaseAccountsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        purchaseAccount.setLocation(location);
        purchaseAccountRepository.saveAndFlush(purchaseAccount);
        Long locationId = location.getId();

        // Get all the purchaseAccountList where location equals to locationId
        defaultPurchaseAccountShouldBeFound("locationId.equals=" + locationId);

        // Get all the purchaseAccountList where location equals to locationId + 1
        defaultPurchaseAccountShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllPurchaseAccountsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);
        TransactionType transactionType = TransactionTypeResourceIT.createEntity(em);
        em.persist(transactionType);
        em.flush();
        purchaseAccount.setTransactionType(transactionType);
        purchaseAccountRepository.saveAndFlush(purchaseAccount);
        Long transactionTypeId = transactionType.getId();

        // Get all the purchaseAccountList where transactionType equals to transactionTypeId
        defaultPurchaseAccountShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the purchaseAccountList where transactionType equals to transactionTypeId + 1
        defaultPurchaseAccountShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPurchaseAccountShouldBeFound(String filter) throws Exception {
        restPurchaseAccountMockMvc.perform(get("/api/purchase-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restPurchaseAccountMockMvc.perform(get("/api/purchase-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPurchaseAccountShouldNotBeFound(String filter) throws Exception {
        restPurchaseAccountMockMvc.perform(get("/api/purchase-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseAccountMockMvc.perform(get("/api/purchase-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPurchaseAccount() throws Exception {
        // Get the purchaseAccount
        restPurchaseAccountMockMvc.perform(get("/api/purchase-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseAccount() throws Exception {
        // Initialize the database
        purchaseAccountService.save(purchaseAccount);

        int databaseSizeBeforeUpdate = purchaseAccountRepository.findAll().size();

        // Update the purchaseAccount
        PurchaseAccount updatedPurchaseAccount = purchaseAccountRepository.findById(purchaseAccount.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseAccount are not directly saved in db
        em.detach(updatedPurchaseAccount);
        updatedPurchaseAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restPurchaseAccountMockMvc.perform(put("/api/purchase-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseAccount)))
            .andExpect(status().isOk());

        // Validate the PurchaseAccount in the database
        List<PurchaseAccount> purchaseAccountList = purchaseAccountRepository.findAll();
        assertThat(purchaseAccountList).hasSize(databaseSizeBeforeUpdate);
        PurchaseAccount testPurchaseAccount = purchaseAccountList.get(purchaseAccountList.size() - 1);
        assertThat(testPurchaseAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPurchaseAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testPurchaseAccount.getTransactionAmountDR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testPurchaseAccount.getTransactionAmountCR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testPurchaseAccount.getTransactionBalance()).isEqualTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseAccount() throws Exception {
        int databaseSizeBeforeUpdate = purchaseAccountRepository.findAll().size();

        // Create the PurchaseAccount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseAccountMockMvc.perform(put("/api/purchase-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccount)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseAccount in the database
        List<PurchaseAccount> purchaseAccountList = purchaseAccountRepository.findAll();
        assertThat(purchaseAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePurchaseAccount() throws Exception {
        // Initialize the database
        purchaseAccountService.save(purchaseAccount);

        int databaseSizeBeforeDelete = purchaseAccountRepository.findAll().size();

        // Delete the purchaseAccount
        restPurchaseAccountMockMvc.perform(delete("/api/purchase-accounts/{id}", purchaseAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseAccount> purchaseAccountList = purchaseAccountRepository.findAll();
        assertThat(purchaseAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseAccount.class);
        PurchaseAccount purchaseAccount1 = new PurchaseAccount();
        purchaseAccount1.setId(1L);
        PurchaseAccount purchaseAccount2 = new PurchaseAccount();
        purchaseAccount2.setId(purchaseAccount1.getId());
        assertThat(purchaseAccount1).isEqualTo(purchaseAccount2);
        purchaseAccount2.setId(2L);
        assertThat(purchaseAccount1).isNotEqualTo(purchaseAccount2);
        purchaseAccount1.setId(null);
        assertThat(purchaseAccount1).isNotEqualTo(purchaseAccount2);
    }
}
