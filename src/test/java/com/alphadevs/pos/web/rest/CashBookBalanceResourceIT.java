package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.CashBookBalance;
import com.alphadevs.pos.repository.CashBookBalanceRepository;
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
import java.util.List;

import static com.alphadevs.pos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CashBookBalanceResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class CashBookBalanceResourceIT {

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    @Autowired
    private CashBookBalanceRepository cashBookBalanceRepository;

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

    private MockMvc restCashBookBalanceMockMvc;

    private CashBookBalance cashBookBalance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CashBookBalanceResource cashBookBalanceResource = new CashBookBalanceResource(cashBookBalanceRepository);
        this.restCashBookBalanceMockMvc = MockMvcBuilders.standaloneSetup(cashBookBalanceResource)
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
    public static CashBookBalance createEntity(EntityManager em) {
        CashBookBalance cashBookBalance = new CashBookBalance()
            .balance(DEFAULT_BALANCE);
        return cashBookBalance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBookBalance createUpdatedEntity(EntityManager em) {
        CashBookBalance cashBookBalance = new CashBookBalance()
            .balance(UPDATED_BALANCE);
        return cashBookBalance;
    }

    @BeforeEach
    public void initTest() {
        cashBookBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashBookBalance() throws Exception {
        int databaseSizeBeforeCreate = cashBookBalanceRepository.findAll().size();

        // Create the CashBookBalance
        restCashBookBalanceMockMvc.perform(post("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isCreated());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        CashBookBalance testCashBookBalance = cashBookBalanceList.get(cashBookBalanceList.size() - 1);
        assertThat(testCashBookBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createCashBookBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashBookBalanceRepository.findAll().size();

        // Create the CashBookBalance with an existing ID
        cashBookBalance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashBookBalanceMockMvc.perform(post("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isBadRequest());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookBalanceRepository.findAll().size();
        // set the field null
        cashBookBalance.setBalance(null);

        // Create the CashBookBalance, which fails.

        restCashBookBalanceMockMvc.perform(post("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isBadRequest());

        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCashBookBalances() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get all the cashBookBalanceList
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBookBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        // Get the cashBookBalance
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances/{id}", cashBookBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashBookBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCashBookBalance() throws Exception {
        // Get the cashBookBalance
        restCashBookBalanceMockMvc.perform(get("/api/cash-book-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();

        // Update the cashBookBalance
        CashBookBalance updatedCashBookBalance = cashBookBalanceRepository.findById(cashBookBalance.getId()).get();
        // Disconnect from session so that the updates on updatedCashBookBalance are not directly saved in db
        em.detach(updatedCashBookBalance);
        updatedCashBookBalance
            .balance(UPDATED_BALANCE);

        restCashBookBalanceMockMvc.perform(put("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashBookBalance)))
            .andExpect(status().isOk());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
        CashBookBalance testCashBookBalance = cashBookBalanceList.get(cashBookBalanceList.size() - 1);
        assertThat(testCashBookBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingCashBookBalance() throws Exception {
        int databaseSizeBeforeUpdate = cashBookBalanceRepository.findAll().size();

        // Create the CashBookBalance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookBalanceMockMvc.perform(put("/api/cash-book-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBookBalance)))
            .andExpect(status().isBadRequest());

        // Validate the CashBookBalance in the database
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCashBookBalance() throws Exception {
        // Initialize the database
        cashBookBalanceRepository.saveAndFlush(cashBookBalance);

        int databaseSizeBeforeDelete = cashBookBalanceRepository.findAll().size();

        // Delete the cashBookBalance
        restCashBookBalanceMockMvc.perform(delete("/api/cash-book-balances/{id}", cashBookBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashBookBalance> cashBookBalanceList = cashBookBalanceRepository.findAll();
        assertThat(cashBookBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashBookBalance.class);
        CashBookBalance cashBookBalance1 = new CashBookBalance();
        cashBookBalance1.setId(1L);
        CashBookBalance cashBookBalance2 = new CashBookBalance();
        cashBookBalance2.setId(cashBookBalance1.getId());
        assertThat(cashBookBalance1).isEqualTo(cashBookBalance2);
        cashBookBalance2.setId(2L);
        assertThat(cashBookBalance1).isNotEqualTo(cashBookBalance2);
        cashBookBalance1.setId(null);
        assertThat(cashBookBalance1).isNotEqualTo(cashBookBalance2);
    }
}
