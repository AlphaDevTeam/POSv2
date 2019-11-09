package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.SupplierAccount;
import com.alphadevs.pos.repository.SupplierAccountRepository;
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
 * Integration tests for the {@link SupplierAccountResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class SupplierAccountResourceIT {

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
    private SupplierAccountRepository supplierAccountRepository;

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
        final SupplierAccountResource supplierAccountResource = new SupplierAccountResource(supplierAccountRepository);
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
    public void getNonExistingSupplierAccount() throws Exception {
        // Get the supplierAccount
        restSupplierAccountMockMvc.perform(get("/api/supplier-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupplierAccount() throws Exception {
        // Initialize the database
        supplierAccountRepository.saveAndFlush(supplierAccount);

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
        supplierAccountRepository.saveAndFlush(supplierAccount);

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
