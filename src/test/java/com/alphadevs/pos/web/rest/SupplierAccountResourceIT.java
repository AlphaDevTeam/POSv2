package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.SupplierAccount;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.domain.TransactionType;
import com.alphadevs.pos.domain.Supplier;
import com.alphadevs.pos.repository.SupplierAccountRepository;
import com.alphadevs.pos.service.SupplierAccountService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.SupplierAccountCriteria;
import com.alphadevs.pos.service.SupplierAccountQueryService;

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
 * Integration tests for the {@link SupplierAccountResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class SupplierAccountResourceIT {

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
    private SupplierAccountRepository supplierAccountRepository;

    @Autowired
    private SupplierAccountService supplierAccountService;

    @Autowired
    private SupplierAccountQueryService supplierAccountQueryService;

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

    private MockMvc restSupplierAccountMockMvc;

    private SupplierAccount supplierAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SupplierAccountResource supplierAccountResource = new SupplierAccountResource(supplierAccountService, supplierAccountQueryService);
        this.restSupplierAccountMockMvc = MockMvcBuilders.standaloneSetup(supplierAccountResource)
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
    public static SupplierAccount createEntity(EntityManager em) {
        SupplierAccount supplierAccount = new SupplierAccount()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(DEFAULT_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(DEFAULT_TRANSACTION_AMOUNT_CR)
            .transactionBalance(DEFAULT_TRANSACTION_BALANCE);
        return supplierAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierAccount createUpdatedEntity(EntityManager em) {
        SupplierAccount supplierAccount = new SupplierAccount()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);
        return supplierAccount;
    }

    @BeforeEach
    public void initTest() {
        supplierAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createSupplierAccount() throws Exception {
        int databaseSizeBeforeCreate = supplierAccountRepository.findAll().size();

        // Create the SupplierAccount
        restSupplierAccountMockMvc.perform(post("/api/supplier-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccount)))
            .andExpect(status().isCreated());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeCreate + 1);
        SupplierAccount testSupplierAccount = supplierAccountList.get(supplierAccountList.size() - 1);
        assertThat(testSupplierAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testSupplierAccount.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testSupplierAccount.getTransactionAmountDR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testSupplierAccount.getTransactionAmountCR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testSupplierAccount.getTransactionBalance()).isEqualTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void createSupplierAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = supplierAccountRepository.findAll().size();

        // Create the SupplierAccount with an existing ID
        supplierAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierAccountMockMvc.perform(post("/api/supplier-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccount)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierAccountRepository.findAll().size();
        // set the field null
        supplierAccount.setTransactionDate(null);

        // Create the SupplierAccount, which fails.

        restSupplierAccountMockMvc.perform(post("/api/supplier-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccount)))
            .andExpect(status().isBadRequest());

        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierAccountRepository.findAll().size();
        // set the field null
        supplierAccount.setTransactionDescription(null);

        // Create the SupplierAccount, which fails.

        restSupplierAccountMockMvc.perform(post("/api/supplier-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccount)))
            .andExpect(status().isBadRequest());

        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierAccountRepository.findAll().size();
        // set the field null
        supplierAccount.setTransactionAmountDR(null);

        // Create the SupplierAccount, which fails.

        restSupplierAccountMockMvc.perform(post("/api/supplier-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccount)))
            .andExpect(status().isBadRequest());

        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierAccountRepository.findAll().size();
        // set the field null
        supplierAccount.setTransactionAmountCR(null);

        // Create the SupplierAccount, which fails.

        restSupplierAccountMockMvc.perform(post("/api/supplier-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccount)))
            .andExpect(status().isBadRequest());

        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierAccountRepository.findAll().size();
        // set the field null
        supplierAccount.setTransactionBalance(null);

        // Create the SupplierAccount, which fails.

        restSupplierAccountMockMvc.perform(post("/api/supplier-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccount)))
            .andExpect(status().isBadRequest());

        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSupplierAccounts() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList
        restSupplierAccountMockMvc.perform(get("/api/supplier-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSupplierAccount() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get the supplierAccount
        restSupplierAccountMockMvc.perform(get("/api/supplier-accounts/{id}", supplierAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(supplierAccount.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmountDR").value(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue()))
            .andExpect(jsonPath("$.transactionAmountCR").value(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue()))
            .andExpect(jsonPath("$.transactionBalance").value(DEFAULT_TRANSACTION_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultSupplierAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the supplierAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultSupplierAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultSupplierAccountShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the supplierAccountList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultSupplierAccountShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultSupplierAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the supplierAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultSupplierAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDate is not null
        defaultSupplierAccountShouldBeFound("transactionDate.specified=true");

        // Get all the supplierAccountList where transactionDate is null
        defaultSupplierAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultSupplierAccountShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the supplierAccountList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultSupplierAccountShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultSupplierAccountShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the supplierAccountList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultSupplierAccountShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultSupplierAccountShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the supplierAccountList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultSupplierAccountShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultSupplierAccountShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the supplierAccountList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultSupplierAccountShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultSupplierAccountShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the supplierAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultSupplierAccountShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultSupplierAccountShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the supplierAccountList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultSupplierAccountShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultSupplierAccountShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the supplierAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultSupplierAccountShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDescription is not null
        defaultSupplierAccountShouldBeFound("transactionDescription.specified=true");

        // Get all the supplierAccountList where transactionDescription is null
        defaultSupplierAccountShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultSupplierAccountShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the supplierAccountList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultSupplierAccountShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultSupplierAccountShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the supplierAccountList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultSupplierAccountShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountDR equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldBeFound("transactionAmountDR.equals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the supplierAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldNotBeFound("transactionAmountDR.equals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountDRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountDR not equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldNotBeFound("transactionAmountDR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the supplierAccountList where transactionAmountDR not equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldBeFound("transactionAmountDR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountDR in DEFAULT_TRANSACTION_AMOUNT_DR or UPDATED_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldBeFound("transactionAmountDR.in=" + DEFAULT_TRANSACTION_AMOUNT_DR + "," + UPDATED_TRANSACTION_AMOUNT_DR);

        // Get all the supplierAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldNotBeFound("transactionAmountDR.in=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountDR is not null
        defaultSupplierAccountShouldBeFound("transactionAmountDR.specified=true");

        // Get all the supplierAccountList where transactionAmountDR is null
        defaultSupplierAccountShouldNotBeFound("transactionAmountDR.specified=false");
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountDR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldBeFound("transactionAmountDR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the supplierAccountList where transactionAmountDR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldNotBeFound("transactionAmountDR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountDR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldBeFound("transactionAmountDR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the supplierAccountList where transactionAmountDR is less than or equal to SMALLER_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldNotBeFound("transactionAmountDR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountDR is less than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldNotBeFound("transactionAmountDR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the supplierAccountList where transactionAmountDR is less than UPDATED_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldBeFound("transactionAmountDR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountDR is greater than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldNotBeFound("transactionAmountDR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the supplierAccountList where transactionAmountDR is greater than SMALLER_TRANSACTION_AMOUNT_DR
        defaultSupplierAccountShouldBeFound("transactionAmountDR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }


    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountCR equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldBeFound("transactionAmountCR.equals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the supplierAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldNotBeFound("transactionAmountCR.equals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountCRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountCR not equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldNotBeFound("transactionAmountCR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the supplierAccountList where transactionAmountCR not equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldBeFound("transactionAmountCR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountCR in DEFAULT_TRANSACTION_AMOUNT_CR or UPDATED_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldBeFound("transactionAmountCR.in=" + DEFAULT_TRANSACTION_AMOUNT_CR + "," + UPDATED_TRANSACTION_AMOUNT_CR);

        // Get all the supplierAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldNotBeFound("transactionAmountCR.in=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountCR is not null
        defaultSupplierAccountShouldBeFound("transactionAmountCR.specified=true");

        // Get all the supplierAccountList where transactionAmountCR is null
        defaultSupplierAccountShouldNotBeFound("transactionAmountCR.specified=false");
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountCR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldBeFound("transactionAmountCR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the supplierAccountList where transactionAmountCR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldNotBeFound("transactionAmountCR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountCR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldBeFound("transactionAmountCR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the supplierAccountList where transactionAmountCR is less than or equal to SMALLER_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldNotBeFound("transactionAmountCR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountCR is less than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldNotBeFound("transactionAmountCR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the supplierAccountList where transactionAmountCR is less than UPDATED_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldBeFound("transactionAmountCR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionAmountCR is greater than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldNotBeFound("transactionAmountCR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the supplierAccountList where transactionAmountCR is greater than SMALLER_TRANSACTION_AMOUNT_CR
        defaultSupplierAccountShouldBeFound("transactionAmountCR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }


    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultSupplierAccountShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the supplierAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultSupplierAccountShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionBalance not equals to DEFAULT_TRANSACTION_BALANCE
        defaultSupplierAccountShouldNotBeFound("transactionBalance.notEquals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the supplierAccountList where transactionBalance not equals to UPDATED_TRANSACTION_BALANCE
        defaultSupplierAccountShouldBeFound("transactionBalance.notEquals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultSupplierAccountShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the supplierAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultSupplierAccountShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionBalance is not null
        defaultSupplierAccountShouldBeFound("transactionBalance.specified=true");

        // Get all the supplierAccountList where transactionBalance is null
        defaultSupplierAccountShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultSupplierAccountShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the supplierAccountList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultSupplierAccountShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultSupplierAccountShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the supplierAccountList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultSupplierAccountShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultSupplierAccountShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the supplierAccountList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultSupplierAccountShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

        // Get all the supplierAccountList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultSupplierAccountShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the supplierAccountList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultSupplierAccountShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }


    @Test
    @Transactional
    public void getAllSupplierAccountsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        supplierAccount.setLocation(location);
        supplierAccountRepository.saveAndFlush(supplierAccount);
        Long locationId = location.getId();

        // Get all the supplierAccountList where location equals to locationId
        defaultSupplierAccountShouldBeFound("locationId.equals=" + locationId);

        // Get all the supplierAccountList where location equals to locationId + 1
        defaultSupplierAccountShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllSupplierAccountsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);
        TransactionType transactionType = TransactionTypeResourceIT.createEntity(em);
        em.persist(transactionType);
        em.flush();
        supplierAccount.setTransactionType(transactionType);
        supplierAccountRepository.saveAndFlush(supplierAccount);
        Long transactionTypeId = transactionType.getId();

        // Get all the supplierAccountList where transactionType equals to transactionTypeId
        defaultSupplierAccountShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the supplierAccountList where transactionType equals to transactionTypeId + 1
        defaultSupplierAccountShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllSupplierAccountsBySupplierIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);
        Supplier supplier = SupplierResourceIT.createEntity(em);
        em.persist(supplier);
        em.flush();
        supplierAccount.setSupplier(supplier);
        supplierAccountRepository.saveAndFlush(supplierAccount);
        Long supplierId = supplier.getId();

        // Get all the supplierAccountList where supplier equals to supplierId
        defaultSupplierAccountShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the supplierAccountList where supplier equals to supplierId + 1
        defaultSupplierAccountShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierAccountShouldBeFound(String filter) throws Exception {
        restSupplierAccountMockMvc.perform(get("/api/supplier-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restSupplierAccountMockMvc.perform(get("/api/supplier-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierAccountShouldNotBeFound(String filter) throws Exception {
        restSupplierAccountMockMvc.perform(get("/api/supplier-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierAccountMockMvc.perform(get("/api/supplier-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSupplierAccount() throws Exception {
        // Get the supplierAccount
        restSupplierAccountMockMvc.perform(get("/api/supplier-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupplierAccount() throws Exception {
        // Initialize the database
        supplierAccountService.save(supplierAccount);

        int databaseSizeBeforeUpdate = supplierAccountRepository.findAll().size();

        // Update the supplierAccount
        SupplierAccount updatedSupplierAccount = supplierAccountRepository.findById(supplierAccount.getId()).get();
        // Disconnect from session so that the updates on updatedSupplierAccount are not directly saved in db
        em.detach(updatedSupplierAccount);
        updatedSupplierAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restSupplierAccountMockMvc.perform(put("/api/supplier-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSupplierAccount)))
            .andExpect(status().isOk());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeUpdate);
        SupplierAccount testSupplierAccount = supplierAccountList.get(supplierAccountList.size() - 1);
        assertThat(testSupplierAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testSupplierAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testSupplierAccount.getTransactionAmountDR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testSupplierAccount.getTransactionAmountCR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testSupplierAccount.getTransactionBalance()).isEqualTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingSupplierAccount() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountRepository.findAll().size();

        // Create the SupplierAccount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierAccountMockMvc.perform(put("/api/supplier-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccount)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccount in the database
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSupplierAccount() throws Exception {
        // Initialize the database
        supplierAccountService.save(supplierAccount);

        int databaseSizeBeforeDelete = supplierAccountRepository.findAll().size();

        // Delete the supplierAccount
        restSupplierAccountMockMvc.perform(delete("/api/supplier-accounts/{id}", supplierAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SupplierAccount> supplierAccountList = supplierAccountRepository.findAll();
        assertThat(supplierAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierAccount.class);
        SupplierAccount supplierAccount1 = new SupplierAccount();
        supplierAccount1.setId(1L);
        SupplierAccount supplierAccount2 = new SupplierAccount();
        supplierAccount2.setId(supplierAccount1.getId());
        assertThat(supplierAccount1).isEqualTo(supplierAccount2);
        supplierAccount2.setId(2L);
        assertThat(supplierAccount1).isNotEqualTo(supplierAccount2);
        supplierAccount1.setId(null);
        assertThat(supplierAccount1).isNotEqualTo(supplierAccount2);
    }
}
