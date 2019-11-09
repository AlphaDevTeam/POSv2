package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.SalesAccountBalance;
import com.alphadevs.pos.repository.SalesAccountBalanceRepository;
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
 * Integration tests for the {@link SalesAccountBalanceResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class SalesAccountBalanceResourceIT {

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    @Autowired
    private SalesAccountBalanceRepository salesAccountBalanceRepository;

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

    private MockMvc restSalesAccountBalanceMockMvc;

    private SalesAccountBalance salesAccountBalance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalesAccountBalanceResource salesAccountBalanceResource = new SalesAccountBalanceResource(salesAccountBalanceRepository);
        this.restSalesAccountBalanceMockMvc = MockMvcBuilders.standaloneSetup(salesAccountBalanceResource)
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
    public static SalesAccountBalance createEntity(EntityManager em) {
        SalesAccountBalance salesAccountBalance = new SalesAccountBalance()
            .balance(DEFAULT_BALANCE);
        return salesAccountBalance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesAccountBalance createUpdatedEntity(EntityManager em) {
        SalesAccountBalance salesAccountBalance = new SalesAccountBalance()
            .balance(UPDATED_BALANCE);
        return salesAccountBalance;
    }

    @BeforeEach
    public void initTest() {
        salesAccountBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalesAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = salesAccountBalanceRepository.findAll().size();

        // Create the SalesAccountBalance
        restSalesAccountBalanceMockMvc.perform(post("/api/sales-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccountBalance)))
            .andExpect(status().isCreated());

        // Validate the SalesAccountBalance in the database
        List<SalesAccountBalance> salesAccountBalanceList = salesAccountBalanceRepository.findAll();
        assertThat(salesAccountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        SalesAccountBalance testSalesAccountBalance = salesAccountBalanceList.get(salesAccountBalanceList.size() - 1);
        assertThat(testSalesAccountBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createSalesAccountBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesAccountBalanceRepository.findAll().size();

        // Create the SalesAccountBalance with an existing ID
        salesAccountBalance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesAccountBalanceMockMvc.perform(post("/api/sales-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the SalesAccountBalance in the database
        List<SalesAccountBalance> salesAccountBalanceList = salesAccountBalanceRepository.findAll();
        assertThat(salesAccountBalanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesAccountBalanceRepository.findAll().size();
        // set the field null
        salesAccountBalance.setBalance(null);

        // Create the SalesAccountBalance, which fails.

        restSalesAccountBalanceMockMvc.perform(post("/api/sales-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccountBalance)))
            .andExpect(status().isBadRequest());

        List<SalesAccountBalance> salesAccountBalanceList = salesAccountBalanceRepository.findAll();
        assertThat(salesAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSalesAccountBalances() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        // Get all the salesAccountBalanceList
        restSalesAccountBalanceMockMvc.perform(get("/api/sales-account-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSalesAccountBalance() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        // Get the salesAccountBalance
        restSalesAccountBalanceMockMvc.perform(get("/api/sales-account-balances/{id}", salesAccountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salesAccountBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSalesAccountBalance() throws Exception {
        // Get the salesAccountBalance
        restSalesAccountBalanceMockMvc.perform(get("/api/sales-account-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalesAccountBalance() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        int databaseSizeBeforeUpdate = salesAccountBalanceRepository.findAll().size();

        // Update the salesAccountBalance
        SalesAccountBalance updatedSalesAccountBalance = salesAccountBalanceRepository.findById(salesAccountBalance.getId()).get();
        // Disconnect from session so that the updates on updatedSalesAccountBalance are not directly saved in db
        em.detach(updatedSalesAccountBalance);
        updatedSalesAccountBalance
            .balance(UPDATED_BALANCE);

        restSalesAccountBalanceMockMvc.perform(put("/api/sales-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSalesAccountBalance)))
            .andExpect(status().isOk());

        // Validate the SalesAccountBalance in the database
        List<SalesAccountBalance> salesAccountBalanceList = salesAccountBalanceRepository.findAll();
        assertThat(salesAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        SalesAccountBalance testSalesAccountBalance = salesAccountBalanceList.get(salesAccountBalanceList.size() - 1);
        assertThat(testSalesAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingSalesAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = salesAccountBalanceRepository.findAll().size();

        // Create the SalesAccountBalance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesAccountBalanceMockMvc.perform(put("/api/sales-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the SalesAccountBalance in the database
        List<SalesAccountBalance> salesAccountBalanceList = salesAccountBalanceRepository.findAll();
        assertThat(salesAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSalesAccountBalance() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        int databaseSizeBeforeDelete = salesAccountBalanceRepository.findAll().size();

        // Delete the salesAccountBalance
        restSalesAccountBalanceMockMvc.perform(delete("/api/sales-account-balances/{id}", salesAccountBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SalesAccountBalance> salesAccountBalanceList = salesAccountBalanceRepository.findAll();
        assertThat(salesAccountBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesAccountBalance.class);
        SalesAccountBalance salesAccountBalance1 = new SalesAccountBalance();
        salesAccountBalance1.setId(1L);
        SalesAccountBalance salesAccountBalance2 = new SalesAccountBalance();
        salesAccountBalance2.setId(salesAccountBalance1.getId());
        assertThat(salesAccountBalance1).isEqualTo(salesAccountBalance2);
        salesAccountBalance2.setId(2L);
        assertThat(salesAccountBalance1).isNotEqualTo(salesAccountBalance2);
        salesAccountBalance1.setId(null);
        assertThat(salesAccountBalance1).isNotEqualTo(salesAccountBalance2);
    }
}
