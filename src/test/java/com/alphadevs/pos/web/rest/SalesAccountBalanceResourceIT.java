package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.SalesAccountBalance;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.repository.SalesAccountBalanceRepository;
import com.alphadevs.pos.service.SalesAccountBalanceService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.SalesAccountBalanceCriteria;
import com.alphadevs.pos.service.SalesAccountBalanceQueryService;

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
    private static final Double SMALLER_BALANCE = 1D - 1D;

    @Autowired
    private SalesAccountBalanceRepository salesAccountBalanceRepository;

    @Autowired
    private SalesAccountBalanceService salesAccountBalanceService;

    @Autowired
    private SalesAccountBalanceQueryService salesAccountBalanceQueryService;

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
        final SalesAccountBalanceResource salesAccountBalanceResource = new SalesAccountBalanceResource(salesAccountBalanceService, salesAccountBalanceQueryService);
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
    public void getAllSalesAccountBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        // Get all the salesAccountBalanceList where balance equals to DEFAULT_BALANCE
        defaultSalesAccountBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the salesAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultSalesAccountBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountBalancesByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        // Get all the salesAccountBalanceList where balance not equals to DEFAULT_BALANCE
        defaultSalesAccountBalanceShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the salesAccountBalanceList where balance not equals to UPDATED_BALANCE
        defaultSalesAccountBalanceShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        // Get all the salesAccountBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultSalesAccountBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the salesAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultSalesAccountBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        // Get all the salesAccountBalanceList where balance is not null
        defaultSalesAccountBalanceShouldBeFound("balance.specified=true");

        // Get all the salesAccountBalanceList where balance is null
        defaultSalesAccountBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalesAccountBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        // Get all the salesAccountBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultSalesAccountBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the salesAccountBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultSalesAccountBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        // Get all the salesAccountBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultSalesAccountBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the salesAccountBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultSalesAccountBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        // Get all the salesAccountBalanceList where balance is less than DEFAULT_BALANCE
        defaultSalesAccountBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the salesAccountBalanceList where balance is less than UPDATED_BALANCE
        defaultSalesAccountBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSalesAccountBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);

        // Get all the salesAccountBalanceList where balance is greater than DEFAULT_BALANCE
        defaultSalesAccountBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the salesAccountBalanceList where balance is greater than SMALLER_BALANCE
        defaultSalesAccountBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }


    @Test
    @Transactional
    public void getAllSalesAccountBalancesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        salesAccountBalance.setLocation(location);
        salesAccountBalanceRepository.saveAndFlush(salesAccountBalance);
        Long locationId = location.getId();

        // Get all the salesAccountBalanceList where location equals to locationId
        defaultSalesAccountBalanceShouldBeFound("locationId.equals=" + locationId);

        // Get all the salesAccountBalanceList where location equals to locationId + 1
        defaultSalesAccountBalanceShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSalesAccountBalanceShouldBeFound(String filter) throws Exception {
        restSalesAccountBalanceMockMvc.perform(get("/api/sales-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restSalesAccountBalanceMockMvc.perform(get("/api/sales-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSalesAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restSalesAccountBalanceMockMvc.perform(get("/api/sales-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSalesAccountBalanceMockMvc.perform(get("/api/sales-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
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
        salesAccountBalanceService.save(salesAccountBalance);

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
        salesAccountBalanceService.save(salesAccountBalance);

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
