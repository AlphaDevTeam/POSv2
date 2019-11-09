package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.SalesAccount;
import com.alphadevs.pos.repository.SalesAccountRepository;
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

    private static final String DEFAULT_TRANSACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_TRANSACTION_AMOUNT_DR = 1D;
    private static final Double UPDATED_TRANSACTION_AMOUNT_DR = 2D;

    private static final Double DEFAULT_TRANSACTION_AMOUNT_CR = 1D;
    private static final Double UPDATED_TRANSACTION_AMOUNT_CR = 2D;

    private static final Double DEFAULT_TRANSACTION_BALANCE = 1D;
    private static final Double UPDATED_TRANSACTION_BALANCE = 2D;

    @Autowired
    private SalesAccountRepository salesAccountRepository;

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
        final SalesAccountResource salesAccountResource = new SalesAccountResource(salesAccountRepository);
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
    public void getNonExistingSalesAccount() throws Exception {
        // Get the salesAccount
        restSalesAccountMockMvc.perform(get("/api/sales-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalesAccount() throws Exception {
        // Initialize the database
        salesAccountRepository.saveAndFlush(salesAccount);

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
        salesAccountRepository.saveAndFlush(salesAccount);

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
