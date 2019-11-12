package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.PurchaseAccountBalance;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.repository.PurchaseAccountBalanceRepository;
import com.alphadevs.pos.service.PurchaseAccountBalanceService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.PurchaseAccountBalanceCriteria;
import com.alphadevs.pos.service.PurchaseAccountBalanceQueryService;

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
 * Integration tests for the {@link PurchaseAccountBalanceResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class PurchaseAccountBalanceResourceIT {

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;
    private static final Double SMALLER_BALANCE = 1D - 1D;

    @Autowired
    private PurchaseAccountBalanceRepository purchaseAccountBalanceRepository;

    @Autowired
    private PurchaseAccountBalanceService purchaseAccountBalanceService;

    @Autowired
    private PurchaseAccountBalanceQueryService purchaseAccountBalanceQueryService;

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

    private MockMvc restPurchaseAccountBalanceMockMvc;

    private PurchaseAccountBalance purchaseAccountBalance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseAccountBalanceResource purchaseAccountBalanceResource = new PurchaseAccountBalanceResource(purchaseAccountBalanceService, purchaseAccountBalanceQueryService);
        this.restPurchaseAccountBalanceMockMvc = MockMvcBuilders.standaloneSetup(purchaseAccountBalanceResource)
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
    public static PurchaseAccountBalance createEntity(EntityManager em) {
        PurchaseAccountBalance purchaseAccountBalance = new PurchaseAccountBalance()
            .balance(DEFAULT_BALANCE);
        return purchaseAccountBalance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseAccountBalance createUpdatedEntity(EntityManager em) {
        PurchaseAccountBalance purchaseAccountBalance = new PurchaseAccountBalance()
            .balance(UPDATED_BALANCE);
        return purchaseAccountBalance;
    }

    @BeforeEach
    public void initTest() {
        purchaseAccountBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = purchaseAccountBalanceRepository.findAll().size();

        // Create the PurchaseAccountBalance
        restPurchaseAccountBalanceMockMvc.perform(post("/api/purchase-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccountBalance)))
            .andExpect(status().isCreated());

        // Validate the PurchaseAccountBalance in the database
        List<PurchaseAccountBalance> purchaseAccountBalanceList = purchaseAccountBalanceRepository.findAll();
        assertThat(purchaseAccountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseAccountBalance testPurchaseAccountBalance = purchaseAccountBalanceList.get(purchaseAccountBalanceList.size() - 1);
        assertThat(testPurchaseAccountBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createPurchaseAccountBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseAccountBalanceRepository.findAll().size();

        // Create the PurchaseAccountBalance with an existing ID
        purchaseAccountBalance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseAccountBalanceMockMvc.perform(post("/api/purchase-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseAccountBalance in the database
        List<PurchaseAccountBalance> purchaseAccountBalanceList = purchaseAccountBalanceRepository.findAll();
        assertThat(purchaseAccountBalanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseAccountBalanceRepository.findAll().size();
        // set the field null
        purchaseAccountBalance.setBalance(null);

        // Create the PurchaseAccountBalance, which fails.

        restPurchaseAccountBalanceMockMvc.perform(post("/api/purchase-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccountBalance)))
            .andExpect(status().isBadRequest());

        List<PurchaseAccountBalance> purchaseAccountBalanceList = purchaseAccountBalanceRepository.findAll();
        assertThat(purchaseAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountBalances() throws Exception {
        // Initialize the database
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);

        // Get all the purchaseAccountBalanceList
        restPurchaseAccountBalanceMockMvc.perform(get("/api/purchase-account-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getPurchaseAccountBalance() throws Exception {
        // Initialize the database
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);

        // Get the purchaseAccountBalance
        restPurchaseAccountBalanceMockMvc.perform(get("/api/purchase-account-balances/{id}", purchaseAccountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseAccountBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);

        // Get all the purchaseAccountBalanceList where balance equals to DEFAULT_BALANCE
        defaultPurchaseAccountBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the purchaseAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultPurchaseAccountBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountBalancesByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);

        // Get all the purchaseAccountBalanceList where balance not equals to DEFAULT_BALANCE
        defaultPurchaseAccountBalanceShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the purchaseAccountBalanceList where balance not equals to UPDATED_BALANCE
        defaultPurchaseAccountBalanceShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);

        // Get all the purchaseAccountBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultPurchaseAccountBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the purchaseAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultPurchaseAccountBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);

        // Get all the purchaseAccountBalanceList where balance is not null
        defaultPurchaseAccountBalanceShouldBeFound("balance.specified=true");

        // Get all the purchaseAccountBalanceList where balance is null
        defaultPurchaseAccountBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);

        // Get all the purchaseAccountBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultPurchaseAccountBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the purchaseAccountBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultPurchaseAccountBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);

        // Get all the purchaseAccountBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultPurchaseAccountBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the purchaseAccountBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultPurchaseAccountBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);

        // Get all the purchaseAccountBalanceList where balance is less than DEFAULT_BALANCE
        defaultPurchaseAccountBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the purchaseAccountBalanceList where balance is less than UPDATED_BALANCE
        defaultPurchaseAccountBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPurchaseAccountBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);

        // Get all the purchaseAccountBalanceList where balance is greater than DEFAULT_BALANCE
        defaultPurchaseAccountBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the purchaseAccountBalanceList where balance is greater than SMALLER_BALANCE
        defaultPurchaseAccountBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }


    @Test
    @Transactional
    public void getAllPurchaseAccountBalancesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        purchaseAccountBalance.setLocation(location);
        purchaseAccountBalanceRepository.saveAndFlush(purchaseAccountBalance);
        Long locationId = location.getId();

        // Get all the purchaseAccountBalanceList where location equals to locationId
        defaultPurchaseAccountBalanceShouldBeFound("locationId.equals=" + locationId);

        // Get all the purchaseAccountBalanceList where location equals to locationId + 1
        defaultPurchaseAccountBalanceShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPurchaseAccountBalanceShouldBeFound(String filter) throws Exception {
        restPurchaseAccountBalanceMockMvc.perform(get("/api/purchase-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restPurchaseAccountBalanceMockMvc.perform(get("/api/purchase-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPurchaseAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restPurchaseAccountBalanceMockMvc.perform(get("/api/purchase-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseAccountBalanceMockMvc.perform(get("/api/purchase-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPurchaseAccountBalance() throws Exception {
        // Get the purchaseAccountBalance
        restPurchaseAccountBalanceMockMvc.perform(get("/api/purchase-account-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseAccountBalance() throws Exception {
        // Initialize the database
        purchaseAccountBalanceService.save(purchaseAccountBalance);

        int databaseSizeBeforeUpdate = purchaseAccountBalanceRepository.findAll().size();

        // Update the purchaseAccountBalance
        PurchaseAccountBalance updatedPurchaseAccountBalance = purchaseAccountBalanceRepository.findById(purchaseAccountBalance.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseAccountBalance are not directly saved in db
        em.detach(updatedPurchaseAccountBalance);
        updatedPurchaseAccountBalance
            .balance(UPDATED_BALANCE);

        restPurchaseAccountBalanceMockMvc.perform(put("/api/purchase-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseAccountBalance)))
            .andExpect(status().isOk());

        // Validate the PurchaseAccountBalance in the database
        List<PurchaseAccountBalance> purchaseAccountBalanceList = purchaseAccountBalanceRepository.findAll();
        assertThat(purchaseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        PurchaseAccountBalance testPurchaseAccountBalance = purchaseAccountBalanceList.get(purchaseAccountBalanceList.size() - 1);
        assertThat(testPurchaseAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = purchaseAccountBalanceRepository.findAll().size();

        // Create the PurchaseAccountBalance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseAccountBalanceMockMvc.perform(put("/api/purchase-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseAccountBalance in the database
        List<PurchaseAccountBalance> purchaseAccountBalanceList = purchaseAccountBalanceRepository.findAll();
        assertThat(purchaseAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePurchaseAccountBalance() throws Exception {
        // Initialize the database
        purchaseAccountBalanceService.save(purchaseAccountBalance);

        int databaseSizeBeforeDelete = purchaseAccountBalanceRepository.findAll().size();

        // Delete the purchaseAccountBalance
        restPurchaseAccountBalanceMockMvc.perform(delete("/api/purchase-account-balances/{id}", purchaseAccountBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseAccountBalance> purchaseAccountBalanceList = purchaseAccountBalanceRepository.findAll();
        assertThat(purchaseAccountBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseAccountBalance.class);
        PurchaseAccountBalance purchaseAccountBalance1 = new PurchaseAccountBalance();
        purchaseAccountBalance1.setId(1L);
        PurchaseAccountBalance purchaseAccountBalance2 = new PurchaseAccountBalance();
        purchaseAccountBalance2.setId(purchaseAccountBalance1.getId());
        assertThat(purchaseAccountBalance1).isEqualTo(purchaseAccountBalance2);
        purchaseAccountBalance2.setId(2L);
        assertThat(purchaseAccountBalance1).isNotEqualTo(purchaseAccountBalance2);
        purchaseAccountBalance1.setId(null);
        assertThat(purchaseAccountBalance1).isNotEqualTo(purchaseAccountBalance2);
    }
}
