package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.CustomerAccount;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.domain.TransactionType;
import com.alphadevs.pos.domain.Customer;
import com.alphadevs.pos.repository.CustomerAccountRepository;
import com.alphadevs.pos.service.CustomerAccountService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.CustomerAccountCriteria;
import com.alphadevs.pos.service.CustomerAccountQueryService;

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
 * Integration tests for the {@link CustomerAccountResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class CustomerAccountResourceIT {

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
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private CustomerAccountService customerAccountService;

    @Autowired
    private CustomerAccountQueryService customerAccountQueryService;

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

    private MockMvc restCustomerAccountMockMvc;

    private CustomerAccount customerAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerAccountResource customerAccountResource = new CustomerAccountResource(customerAccountService, customerAccountQueryService);
        this.restCustomerAccountMockMvc = MockMvcBuilders.standaloneSetup(customerAccountResource)
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
    public static CustomerAccount createEntity(EntityManager em) {
        CustomerAccount customerAccount = new CustomerAccount()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(DEFAULT_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(DEFAULT_TRANSACTION_AMOUNT_CR)
            .transactionBalance(DEFAULT_TRANSACTION_BALANCE);
        return customerAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAccount createUpdatedEntity(EntityManager em) {
        CustomerAccount customerAccount = new CustomerAccount()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);
        return customerAccount;
    }

    @BeforeEach
    public void initTest() {
        customerAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerAccount() throws Exception {
        int databaseSizeBeforeCreate = customerAccountRepository.findAll().size();

        // Create the CustomerAccount
        restCustomerAccountMockMvc.perform(post("/api/customer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccount)))
            .andExpect(status().isCreated());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerAccount testCustomerAccount = customerAccountList.get(customerAccountList.size() - 1);
        assertThat(testCustomerAccount.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCustomerAccount.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testCustomerAccount.getTransactionAmountDR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testCustomerAccount.getTransactionAmountCR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testCustomerAccount.getTransactionBalance()).isEqualTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void createCustomerAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerAccountRepository.findAll().size();

        // Create the CustomerAccount with an existing ID
        customerAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerAccountMockMvc.perform(post("/api/customer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccount)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAccountRepository.findAll().size();
        // set the field null
        customerAccount.setTransactionDate(null);

        // Create the CustomerAccount, which fails.

        restCustomerAccountMockMvc.perform(post("/api/customer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccount)))
            .andExpect(status().isBadRequest());

        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAccountRepository.findAll().size();
        // set the field null
        customerAccount.setTransactionDescription(null);

        // Create the CustomerAccount, which fails.

        restCustomerAccountMockMvc.perform(post("/api/customer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccount)))
            .andExpect(status().isBadRequest());

        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAccountRepository.findAll().size();
        // set the field null
        customerAccount.setTransactionAmountDR(null);

        // Create the CustomerAccount, which fails.

        restCustomerAccountMockMvc.perform(post("/api/customer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccount)))
            .andExpect(status().isBadRequest());

        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAccountRepository.findAll().size();
        // set the field null
        customerAccount.setTransactionAmountCR(null);

        // Create the CustomerAccount, which fails.

        restCustomerAccountMockMvc.perform(post("/api/customer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccount)))
            .andExpect(status().isBadRequest());

        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAccountRepository.findAll().size();
        // set the field null
        customerAccount.setTransactionBalance(null);

        // Create the CustomerAccount, which fails.

        restCustomerAccountMockMvc.perform(post("/api/customer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccount)))
            .andExpect(status().isBadRequest());

        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerAccounts() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList
        restCustomerAccountMockMvc.perform(get("/api/customer-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCustomerAccount() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get the customerAccount
        restCustomerAccountMockMvc.perform(get("/api/customer-accounts/{id}", customerAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerAccount.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmountDR").value(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue()))
            .andExpect(jsonPath("$.transactionAmountCR").value(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue()))
            .andExpect(jsonPath("$.transactionBalance").value(DEFAULT_TRANSACTION_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultCustomerAccountShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the customerAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCustomerAccountShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultCustomerAccountShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the customerAccountList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultCustomerAccountShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultCustomerAccountShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the customerAccountList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCustomerAccountShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDate is not null
        defaultCustomerAccountShouldBeFound("transactionDate.specified=true");

        // Get all the customerAccountList where transactionDate is null
        defaultCustomerAccountShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultCustomerAccountShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the customerAccountList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultCustomerAccountShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultCustomerAccountShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the customerAccountList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultCustomerAccountShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultCustomerAccountShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the customerAccountList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultCustomerAccountShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultCustomerAccountShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the customerAccountList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultCustomerAccountShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCustomerAccountShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the customerAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCustomerAccountShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCustomerAccountShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the customerAccountList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCustomerAccountShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultCustomerAccountShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the customerAccountList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCustomerAccountShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDescription is not null
        defaultCustomerAccountShouldBeFound("transactionDescription.specified=true");

        // Get all the customerAccountList where transactionDescription is null
        defaultCustomerAccountShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultCustomerAccountShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the customerAccountList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultCustomerAccountShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultCustomerAccountShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the customerAccountList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultCustomerAccountShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountDR equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldBeFound("transactionAmountDR.equals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the customerAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldNotBeFound("transactionAmountDR.equals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountDRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountDR not equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldNotBeFound("transactionAmountDR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the customerAccountList where transactionAmountDR not equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldBeFound("transactionAmountDR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountDR in DEFAULT_TRANSACTION_AMOUNT_DR or UPDATED_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldBeFound("transactionAmountDR.in=" + DEFAULT_TRANSACTION_AMOUNT_DR + "," + UPDATED_TRANSACTION_AMOUNT_DR);

        // Get all the customerAccountList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldNotBeFound("transactionAmountDR.in=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountDR is not null
        defaultCustomerAccountShouldBeFound("transactionAmountDR.specified=true");

        // Get all the customerAccountList where transactionAmountDR is null
        defaultCustomerAccountShouldNotBeFound("transactionAmountDR.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountDR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldBeFound("transactionAmountDR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the customerAccountList where transactionAmountDR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldNotBeFound("transactionAmountDR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountDR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldBeFound("transactionAmountDR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the customerAccountList where transactionAmountDR is less than or equal to SMALLER_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldNotBeFound("transactionAmountDR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountDR is less than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldNotBeFound("transactionAmountDR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the customerAccountList where transactionAmountDR is less than UPDATED_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldBeFound("transactionAmountDR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountDR is greater than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldNotBeFound("transactionAmountDR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the customerAccountList where transactionAmountDR is greater than SMALLER_TRANSACTION_AMOUNT_DR
        defaultCustomerAccountShouldBeFound("transactionAmountDR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }


    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountCR equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldBeFound("transactionAmountCR.equals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the customerAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldNotBeFound("transactionAmountCR.equals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountCRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountCR not equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldNotBeFound("transactionAmountCR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the customerAccountList where transactionAmountCR not equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldBeFound("transactionAmountCR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountCR in DEFAULT_TRANSACTION_AMOUNT_CR or UPDATED_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldBeFound("transactionAmountCR.in=" + DEFAULT_TRANSACTION_AMOUNT_CR + "," + UPDATED_TRANSACTION_AMOUNT_CR);

        // Get all the customerAccountList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldNotBeFound("transactionAmountCR.in=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountCR is not null
        defaultCustomerAccountShouldBeFound("transactionAmountCR.specified=true");

        // Get all the customerAccountList where transactionAmountCR is null
        defaultCustomerAccountShouldNotBeFound("transactionAmountCR.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountCR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldBeFound("transactionAmountCR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the customerAccountList where transactionAmountCR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldNotBeFound("transactionAmountCR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountCR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldBeFound("transactionAmountCR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the customerAccountList where transactionAmountCR is less than or equal to SMALLER_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldNotBeFound("transactionAmountCR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountCR is less than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldNotBeFound("transactionAmountCR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the customerAccountList where transactionAmountCR is less than UPDATED_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldBeFound("transactionAmountCR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionAmountCR is greater than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldNotBeFound("transactionAmountCR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the customerAccountList where transactionAmountCR is greater than SMALLER_TRANSACTION_AMOUNT_CR
        defaultCustomerAccountShouldBeFound("transactionAmountCR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }


    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultCustomerAccountShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the customerAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultCustomerAccountShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionBalance not equals to DEFAULT_TRANSACTION_BALANCE
        defaultCustomerAccountShouldNotBeFound("transactionBalance.notEquals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the customerAccountList where transactionBalance not equals to UPDATED_TRANSACTION_BALANCE
        defaultCustomerAccountShouldBeFound("transactionBalance.notEquals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultCustomerAccountShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the customerAccountList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultCustomerAccountShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionBalance is not null
        defaultCustomerAccountShouldBeFound("transactionBalance.specified=true");

        // Get all the customerAccountList where transactionBalance is null
        defaultCustomerAccountShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultCustomerAccountShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the customerAccountList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultCustomerAccountShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultCustomerAccountShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the customerAccountList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultCustomerAccountShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultCustomerAccountShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the customerAccountList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultCustomerAccountShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);

        // Get all the customerAccountList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultCustomerAccountShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the customerAccountList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultCustomerAccountShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }


    @Test
    @Transactional
    public void getAllCustomerAccountsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        customerAccount.setLocation(location);
        customerAccountRepository.saveAndFlush(customerAccount);
        Long locationId = location.getId();

        // Get all the customerAccountList where location equals to locationId
        defaultCustomerAccountShouldBeFound("locationId.equals=" + locationId);

        // Get all the customerAccountList where location equals to locationId + 1
        defaultCustomerAccountShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllCustomerAccountsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);
        TransactionType transactionType = TransactionTypeResourceIT.createEntity(em);
        em.persist(transactionType);
        em.flush();
        customerAccount.setTransactionType(transactionType);
        customerAccountRepository.saveAndFlush(customerAccount);
        Long transactionTypeId = transactionType.getId();

        // Get all the customerAccountList where transactionType equals to transactionTypeId
        defaultCustomerAccountShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the customerAccountList where transactionType equals to transactionTypeId + 1
        defaultCustomerAccountShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllCustomerAccountsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountRepository.saveAndFlush(customerAccount);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        customerAccount.setCustomer(customer);
        customerAccountRepository.saveAndFlush(customerAccount);
        Long customerId = customer.getId();

        // Get all the customerAccountList where customer equals to customerId
        defaultCustomerAccountShouldBeFound("customerId.equals=" + customerId);

        // Get all the customerAccountList where customer equals to customerId + 1
        defaultCustomerAccountShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerAccountShouldBeFound(String filter) throws Exception {
        restCustomerAccountMockMvc.perform(get("/api/customer-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restCustomerAccountMockMvc.perform(get("/api/customer-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerAccountShouldNotBeFound(String filter) throws Exception {
        restCustomerAccountMockMvc.perform(get("/api/customer-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerAccountMockMvc.perform(get("/api/customer-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCustomerAccount() throws Exception {
        // Get the customerAccount
        restCustomerAccountMockMvc.perform(get("/api/customer-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerAccount() throws Exception {
        // Initialize the database
        customerAccountService.save(customerAccount);

        int databaseSizeBeforeUpdate = customerAccountRepository.findAll().size();

        // Update the customerAccount
        CustomerAccount updatedCustomerAccount = customerAccountRepository.findById(customerAccount.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerAccount are not directly saved in db
        em.detach(updatedCustomerAccount);
        updatedCustomerAccount
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restCustomerAccountMockMvc.perform(put("/api/customer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomerAccount)))
            .andExpect(status().isOk());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeUpdate);
        CustomerAccount testCustomerAccount = customerAccountList.get(customerAccountList.size() - 1);
        assertThat(testCustomerAccount.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCustomerAccount.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testCustomerAccount.getTransactionAmountDR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testCustomerAccount.getTransactionAmountCR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testCustomerAccount.getTransactionBalance()).isEqualTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerAccount() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountRepository.findAll().size();

        // Create the CustomerAccount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAccountMockMvc.perform(put("/api/customer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccount)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccount in the database
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomerAccount() throws Exception {
        // Initialize the database
        customerAccountService.save(customerAccount);

        int databaseSizeBeforeDelete = customerAccountRepository.findAll().size();

        // Delete the customerAccount
        restCustomerAccountMockMvc.perform(delete("/api/customer-accounts/{id}", customerAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerAccount> customerAccountList = customerAccountRepository.findAll();
        assertThat(customerAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerAccount.class);
        CustomerAccount customerAccount1 = new CustomerAccount();
        customerAccount1.setId(1L);
        CustomerAccount customerAccount2 = new CustomerAccount();
        customerAccount2.setId(customerAccount1.getId());
        assertThat(customerAccount1).isEqualTo(customerAccount2);
        customerAccount2.setId(2L);
        assertThat(customerAccount1).isNotEqualTo(customerAccount2);
        customerAccount1.setId(null);
        assertThat(customerAccount1).isNotEqualTo(customerAccount2);
    }
}
