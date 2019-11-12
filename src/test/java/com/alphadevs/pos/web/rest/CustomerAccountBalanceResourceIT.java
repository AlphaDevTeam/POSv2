package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.CustomerAccountBalance;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.repository.CustomerAccountBalanceRepository;
import com.alphadevs.pos.service.CustomerAccountBalanceService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.CustomerAccountBalanceCriteria;
import com.alphadevs.pos.service.CustomerAccountBalanceQueryService;

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
 * Integration tests for the {@link CustomerAccountBalanceResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class CustomerAccountBalanceResourceIT {

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;
    private static final Double SMALLER_BALANCE = 1D - 1D;

    @Autowired
    private CustomerAccountBalanceRepository customerAccountBalanceRepository;

    @Autowired
    private CustomerAccountBalanceService customerAccountBalanceService;

    @Autowired
    private CustomerAccountBalanceQueryService customerAccountBalanceQueryService;

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

    private MockMvc restCustomerAccountBalanceMockMvc;

    private CustomerAccountBalance customerAccountBalance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerAccountBalanceResource customerAccountBalanceResource = new CustomerAccountBalanceResource(customerAccountBalanceService, customerAccountBalanceQueryService);
        this.restCustomerAccountBalanceMockMvc = MockMvcBuilders.standaloneSetup(customerAccountBalanceResource)
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
    public static CustomerAccountBalance createEntity(EntityManager em) {
        CustomerAccountBalance customerAccountBalance = new CustomerAccountBalance()
            .balance(DEFAULT_BALANCE);
        return customerAccountBalance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAccountBalance createUpdatedEntity(EntityManager em) {
        CustomerAccountBalance customerAccountBalance = new CustomerAccountBalance()
            .balance(UPDATED_BALANCE);
        return customerAccountBalance;
    }

    @BeforeEach
    public void initTest() {
        customerAccountBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerAccountBalance() throws Exception {
        int databaseSizeBeforeCreate = customerAccountBalanceRepository.findAll().size();

        // Create the CustomerAccountBalance
        restCustomerAccountBalanceMockMvc.perform(post("/api/customer-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance)))
            .andExpect(status().isCreated());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerAccountBalance testCustomerAccountBalance = customerAccountBalanceList.get(customerAccountBalanceList.size() - 1);
        assertThat(testCustomerAccountBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createCustomerAccountBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerAccountBalanceRepository.findAll().size();

        // Create the CustomerAccountBalance with an existing ID
        customerAccountBalance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerAccountBalanceMockMvc.perform(post("/api/customer-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAccountBalanceRepository.findAll().size();
        // set the field null
        customerAccountBalance.setBalance(null);

        // Create the CustomerAccountBalance, which fails.

        restCustomerAccountBalanceMockMvc.perform(post("/api/customer-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance)))
            .andExpect(status().isBadRequest());

        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountBalances() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList
        restCustomerAccountBalanceMockMvc.perform(get("/api/customer-account-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCustomerAccountBalance() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get the customerAccountBalance
        restCustomerAccountBalanceMockMvc.perform(get("/api/customer-account-balances/{id}", customerAccountBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerAccountBalance.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllCustomerAccountBalancesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance equals to DEFAULT_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the customerAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountBalancesByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance not equals to DEFAULT_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the customerAccountBalanceList where balance not equals to UPDATED_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountBalancesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the customerAccountBalanceList where balance equals to UPDATED_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountBalancesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance is not null
        defaultCustomerAccountBalanceShouldBeFound("balance.specified=true");

        // Get all the customerAccountBalanceList where balance is null
        defaultCustomerAccountBalanceShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomerAccountBalancesByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance is greater than or equal to DEFAULT_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the customerAccountBalanceList where balance is greater than or equal to UPDATED_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountBalancesByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance is less than or equal to DEFAULT_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the customerAccountBalanceList where balance is less than or equal to SMALLER_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountBalancesByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance is less than DEFAULT_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the customerAccountBalanceList where balance is less than UPDATED_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerAccountBalancesByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);

        // Get all the customerAccountBalanceList where balance is greater than DEFAULT_BALANCE
        defaultCustomerAccountBalanceShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the customerAccountBalanceList where balance is greater than SMALLER_BALANCE
        defaultCustomerAccountBalanceShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }


    @Test
    @Transactional
    public void getAllCustomerAccountBalancesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        customerAccountBalance.setLocation(location);
        customerAccountBalanceRepository.saveAndFlush(customerAccountBalance);
        Long locationId = location.getId();

        // Get all the customerAccountBalanceList where location equals to locationId
        defaultCustomerAccountBalanceShouldBeFound("locationId.equals=" + locationId);

        // Get all the customerAccountBalanceList where location equals to locationId + 1
        defaultCustomerAccountBalanceShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerAccountBalanceShouldBeFound(String filter) throws Exception {
        restCustomerAccountBalanceMockMvc.perform(get("/api/customer-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAccountBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restCustomerAccountBalanceMockMvc.perform(get("/api/customer-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerAccountBalanceShouldNotBeFound(String filter) throws Exception {
        restCustomerAccountBalanceMockMvc.perform(get("/api/customer-account-balances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerAccountBalanceMockMvc.perform(get("/api/customer-account-balances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCustomerAccountBalance() throws Exception {
        // Get the customerAccountBalance
        restCustomerAccountBalanceMockMvc.perform(get("/api/customer-account-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerAccountBalance() throws Exception {
        // Initialize the database
        customerAccountBalanceService.save(customerAccountBalance);

        int databaseSizeBeforeUpdate = customerAccountBalanceRepository.findAll().size();

        // Update the customerAccountBalance
        CustomerAccountBalance updatedCustomerAccountBalance = customerAccountBalanceRepository.findById(customerAccountBalance.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerAccountBalance are not directly saved in db
        em.detach(updatedCustomerAccountBalance);
        updatedCustomerAccountBalance
            .balance(UPDATED_BALANCE);

        restCustomerAccountBalanceMockMvc.perform(put("/api/customer-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomerAccountBalance)))
            .andExpect(status().isOk());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
        CustomerAccountBalance testCustomerAccountBalance = customerAccountBalanceList.get(customerAccountBalanceList.size() - 1);
        assertThat(testCustomerAccountBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerAccountBalance() throws Exception {
        int databaseSizeBeforeUpdate = customerAccountBalanceRepository.findAll().size();

        // Create the CustomerAccountBalance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAccountBalanceMockMvc.perform(put("/api/customer-account-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAccountBalance)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerAccountBalance in the database
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomerAccountBalance() throws Exception {
        // Initialize the database
        customerAccountBalanceService.save(customerAccountBalance);

        int databaseSizeBeforeDelete = customerAccountBalanceRepository.findAll().size();

        // Delete the customerAccountBalance
        restCustomerAccountBalanceMockMvc.perform(delete("/api/customer-account-balances/{id}", customerAccountBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerAccountBalance> customerAccountBalanceList = customerAccountBalanceRepository.findAll();
        assertThat(customerAccountBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerAccountBalance.class);
        CustomerAccountBalance customerAccountBalance1 = new CustomerAccountBalance();
        customerAccountBalance1.setId(1L);
        CustomerAccountBalance customerAccountBalance2 = new CustomerAccountBalance();
        customerAccountBalance2.setId(customerAccountBalance1.getId());
        assertThat(customerAccountBalance1).isEqualTo(customerAccountBalance2);
        customerAccountBalance2.setId(2L);
        assertThat(customerAccountBalance1).isNotEqualTo(customerAccountBalance2);
        customerAccountBalance1.setId(null);
        assertThat(customerAccountBalance1).isNotEqualTo(customerAccountBalance2);
    }
}
