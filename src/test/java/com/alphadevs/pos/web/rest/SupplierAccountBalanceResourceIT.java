package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.SupplierAccountBalance;
import com.alphadevs.pos.repository.SupplierAccountBalanceRepository;
import com.alphadevs.pos.service.SupplierAccountBalanceService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.SupplierAccountBalanceCriteria;
import com.alphadevs.pos.service.SupplierAccountBalanceQueryService;

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
 * Integration tests for the {@link SupplierAccountBalanceResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class SupplierAccountBalanceResourceIT {

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;
    private static final Double SMALLER_BALANCE = 1D - 1D;

    @Autowired
    private SupplierAccountBalanceRepository supplierAccountBalanceRepository;

    @Autowired
    private SupplierAccountBalanceService supplierAccountBalanceService;

    @Autowired
    private SupplierAccountBalanceQueryService supplierAccountBalanceQueryService;

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

    private MockMvc restSupplierAccountBalanceMockMvc;

    private SupplierAccountBalance supplierAccountBalance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SupplierAccountBalanceResource supplierAccountBalanceResource = new SupplierAccountBalanceResource(supplierAccountBalanceService, supplierAccountBalanceQueryService);
        this.restSupplierAccountBalanceMockMvc = MockMvcBuilders.standaloneSetup(supplierAccountBalanceResource)
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
    public static SupplierAccountBalance createEntity(EntityManager em) {
        SupplierAccountBalance supplierAccountBalance = new SupplierAccountBalance()
            .balance(DEFAULT_BALANCE);
        return supplierAccountBalance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierAccountBalance createUpdatedEntity(EntityManager em) {
        SupplierAccountBalance supplierAccountBalance = new SupplierAccountBalance()
            .balance(UPDATED_BALANCE);
        return supplierAccountBalance;
    }

    @BeforeEach
    public void initTest() {
        supplierAccountBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createSupplierAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = supplierAccountBalanceRepository.findAll().size();

        // Create the SupplierAccountBalance
        restSupplierAccountBalanceMockMvc.perform(post("/api/supplier-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance)))
            .andExpect(status().isCreated());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        SupplierAccountBalance testSupplierAccountBalance = supplierAccountBalanceList.get(supplierAccountBalanceList.size() - 1);
        assertThat(testSupplierAccountBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createSupplierAccountBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = supplierAccountBalanceRepository.findAll().size();

        // Create the SupplierAccountBalance with an existing ID
        supplierAccountBalance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierAccountBalanceMockMvc.perform(post("/api/supplier-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierAccountBalanceRepository.findAll().size();
        // set the field null
        supplierAccountBalance.setBalance(null);

        // Create the SupplierAccountBalance, which fails.

        restSupplierAccountBalanceMockMvc.perform(post("/api/supplier-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance)))
            .andExpect(status().isBadRequest());

        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountBalances() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList
        restSupplierAccountBalanceMockMvc.perform(get("/api/supplier-account-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSupplierAccountBalance() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get the supplierAccountBalance
        restSupplierAccountBalanceMockMvc.perform(get("/api/supplier-account-balances/{id}", supplierAccountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(supplierAccountBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllSupplierAccountBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance equals to DEFAULT_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the supplierAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountBalancesByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance not equals to DEFAULT_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the supplierAccountBalanceList where balance not equals to UPDATED_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the supplierAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance is not null
        defaultSupplierAccountBalanceShouldBeFound("balance.specified=true");

        // Get all the supplierAccountBalanceList where balance is null
        defaultSupplierAccountBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllSupplierAccountBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the supplierAccountBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the supplierAccountBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance is less than DEFAULT_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the supplierAccountBalanceList where balance is less than UPDATED_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllSupplierAccountBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierAccountBalanceRepository.saveAndFlush(supplierAccountBalance);

        // Get all the supplierAccountBalanceList where balance is greater than DEFAULT_BALANCE
        defaultSupplierAccountBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the supplierAccountBalanceList where balance is greater than SMALLER_BALANCE
        defaultSupplierAccountBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierAccountBalanceShouldBeFound(String filter) throws Exception {
        restSupplierAccountBalanceMockMvc.perform(get("/api/supplier-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restSupplierAccountBalanceMockMvc.perform(get("/api/supplier-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restSupplierAccountBalanceMockMvc.perform(get("/api/supplier-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierAccountBalanceMockMvc.perform(get("/api/supplier-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSupplierAccountBalance() throws Exception {
        // Get the supplierAccountBalance
        restSupplierAccountBalanceMockMvc.perform(get("/api/supplier-account-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupplierAccountBalance() throws Exception {
        // Initialize the database
        supplierAccountBalanceService.save(supplierAccountBalance);

        int databaseSizeBeforeUpdate = supplierAccountBalanceRepository.findAll().size();

        // Update the supplierAccountBalance
        SupplierAccountBalance updatedSupplierAccountBalance = supplierAccountBalanceRepository.findById(supplierAccountBalance.getId()).get();
        // Disconnect from session so that the updates on updatedSupplierAccountBalance are not directly saved in db
        em.detach(updatedSupplierAccountBalance);
        updatedSupplierAccountBalance
            .balance(UPDATED_BALANCE);

        restSupplierAccountBalanceMockMvc.perform(put("/api/supplier-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSupplierAccountBalance)))
            .andExpect(status().isOk());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        SupplierAccountBalance testSupplierAccountBalance = supplierAccountBalanceList.get(supplierAccountBalanceList.size() - 1);
        assertThat(testSupplierAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingSupplierAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = supplierAccountBalanceRepository.findAll().size();

        // Create the SupplierAccountBalance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierAccountBalanceMockMvc.perform(put("/api/supplier-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supplierAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierAccountBalance in the database
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSupplierAccountBalance() throws Exception {
        // Initialize the database
        supplierAccountBalanceService.save(supplierAccountBalance);

        int databaseSizeBeforeDelete = supplierAccountBalanceRepository.findAll().size();

        // Delete the supplierAccountBalance
        restSupplierAccountBalanceMockMvc.perform(delete("/api/supplier-account-balances/{id}", supplierAccountBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SupplierAccountBalance> supplierAccountBalanceList = supplierAccountBalanceRepository.findAll();
        assertThat(supplierAccountBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierAccountBalance.class);
        SupplierAccountBalance supplierAccountBalance1 = new SupplierAccountBalance();
        supplierAccountBalance1.setId(1L);
        SupplierAccountBalance supplierAccountBalance2 = new SupplierAccountBalance();
        supplierAccountBalance2.setId(supplierAccountBalance1.getId());
        assertThat(supplierAccountBalance1).isEqualTo(supplierAccountBalance2);
        supplierAccountBalance2.setId(2L);
        assertThat(supplierAccountBalance1).isNotEqualTo(supplierAccountBalance2);
        supplierAccountBalance1.setId(null);
        assertThat(supplierAccountBalance1).isNotEqualTo(supplierAccountBalance2);
    }
}
