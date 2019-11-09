package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.PurchaseAccount;
import com.alphadevs.pos.repository.PurchaseAccountRepository;
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
 * Integration tests for the {@link PurchaseAccountResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class PurchaseAccountResourceIT {

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
    private PurchaseAccountRepository purchaseAccountRepository;

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
        final PurchaseAccountResource purchaseAccountResource = new PurchaseAccountResource(purchaseAccountRepository);
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
    public void getNonExistingPurchaseAccount() throws Exception {
        // Get the purchaseAccount
        restPurchaseAccountMockMvc.perform(get("/api/purchase-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseAccount() throws Exception {
        // Initialize the database
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

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
        purchaseAccountRepository.saveAndFlush(purchaseAccount);

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
