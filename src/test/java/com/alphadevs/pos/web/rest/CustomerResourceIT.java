package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.Customer;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.repository.CustomerRepository;
import com.alphadevs.pos.service.CustomerService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.CustomerCriteria;
import com.alphadevs.pos.service.CustomerQueryService;

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
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class CustomerResourceIT {

    private static final String DEFAULT_CUSTOMER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_CUSTOMER_LIMIT = 1D;
    private static final Double UPDATED_CUSTOMER_LIMIT = 2D;
    private static final Double SMALLER_CUSTOMER_LIMIT = 1D - 1D;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerQueryService customerQueryService;

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

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerResource customerResource = new CustomerResource(customerService, customerQueryService);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
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
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .customerCode(DEFAULT_CUSTOMER_CODE)
            .customerName(DEFAULT_CUSTOMER_NAME)
            .customerLimit(DEFAULT_CUSTOMER_LIMIT)
            .isActive(DEFAULT_IS_ACTIVE);
        return customer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .customerCode(UPDATED_CUSTOMER_CODE)
            .customerName(UPDATED_CUSTOMER_NAME)
            .customerLimit(UPDATED_CUSTOMER_LIMIT)
            .isActive(UPDATED_IS_ACTIVE);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerCode()).isEqualTo(DEFAULT_CUSTOMER_CODE);
        assertThat(testCustomer.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testCustomer.getCustomerLimit()).isEqualTo(DEFAULT_CUSTOMER_LIMIT);
        assertThat(testCustomer.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCustomerCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerCode(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerName(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerLimitIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerLimit(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerCode").value(hasItem(DEFAULT_CUSTOMER_CODE)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].customerLimit").value(hasItem(DEFAULT_CUSTOMER_LIMIT.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.customerCode").value(DEFAULT_CUSTOMER_CODE))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME))
            .andExpect(jsonPath("$.customerLimit").value(DEFAULT_CUSTOMER_LIMIT.doubleValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode equals to DEFAULT_CUSTOMER_CODE
        defaultCustomerShouldBeFound("customerCode.equals=" + DEFAULT_CUSTOMER_CODE);

        // Get all the customerList where customerCode equals to UPDATED_CUSTOMER_CODE
        defaultCustomerShouldNotBeFound("customerCode.equals=" + UPDATED_CUSTOMER_CODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode not equals to DEFAULT_CUSTOMER_CODE
        defaultCustomerShouldNotBeFound("customerCode.notEquals=" + DEFAULT_CUSTOMER_CODE);

        // Get all the customerList where customerCode not equals to UPDATED_CUSTOMER_CODE
        defaultCustomerShouldBeFound("customerCode.notEquals=" + UPDATED_CUSTOMER_CODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode in DEFAULT_CUSTOMER_CODE or UPDATED_CUSTOMER_CODE
        defaultCustomerShouldBeFound("customerCode.in=" + DEFAULT_CUSTOMER_CODE + "," + UPDATED_CUSTOMER_CODE);

        // Get all the customerList where customerCode equals to UPDATED_CUSTOMER_CODE
        defaultCustomerShouldNotBeFound("customerCode.in=" + UPDATED_CUSTOMER_CODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode is not null
        defaultCustomerShouldBeFound("customerCode.specified=true");

        // Get all the customerList where customerCode is null
        defaultCustomerShouldNotBeFound("customerCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByCustomerCodeContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode contains DEFAULT_CUSTOMER_CODE
        defaultCustomerShouldBeFound("customerCode.contains=" + DEFAULT_CUSTOMER_CODE);

        // Get all the customerList where customerCode contains UPDATED_CUSTOMER_CODE
        defaultCustomerShouldNotBeFound("customerCode.contains=" + UPDATED_CUSTOMER_CODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerCodeNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerCode does not contain DEFAULT_CUSTOMER_CODE
        defaultCustomerShouldNotBeFound("customerCode.doesNotContain=" + DEFAULT_CUSTOMER_CODE);

        // Get all the customerList where customerCode does not contain UPDATED_CUSTOMER_CODE
        defaultCustomerShouldBeFound("customerCode.doesNotContain=" + UPDATED_CUSTOMER_CODE);
    }


    @Test
    @Transactional
    public void getAllCustomersByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName not equals to DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.notEquals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName not equals to UPDATED_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.notEquals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the customerList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName is not null
        defaultCustomerShouldBeFound("customerName.specified=true");

        // Get all the customerList where customerName is null
        defaultCustomerShouldNotBeFound("customerName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByCustomerNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName contains DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.contains=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName contains UPDATED_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.contains=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerName does not contain DEFAULT_CUSTOMER_NAME
        defaultCustomerShouldNotBeFound("customerName.doesNotContain=" + DEFAULT_CUSTOMER_NAME);

        // Get all the customerList where customerName does not contain UPDATED_CUSTOMER_NAME
        defaultCustomerShouldBeFound("customerName.doesNotContain=" + UPDATED_CUSTOMER_NAME);
    }


    @Test
    @Transactional
    public void getAllCustomersByCustomerLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerLimit equals to DEFAULT_CUSTOMER_LIMIT
        defaultCustomerShouldBeFound("customerLimit.equals=" + DEFAULT_CUSTOMER_LIMIT);

        // Get all the customerList where customerLimit equals to UPDATED_CUSTOMER_LIMIT
        defaultCustomerShouldNotBeFound("customerLimit.equals=" + UPDATED_CUSTOMER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerLimitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerLimit not equals to DEFAULT_CUSTOMER_LIMIT
        defaultCustomerShouldNotBeFound("customerLimit.notEquals=" + DEFAULT_CUSTOMER_LIMIT);

        // Get all the customerList where customerLimit not equals to UPDATED_CUSTOMER_LIMIT
        defaultCustomerShouldBeFound("customerLimit.notEquals=" + UPDATED_CUSTOMER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerLimitIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerLimit in DEFAULT_CUSTOMER_LIMIT or UPDATED_CUSTOMER_LIMIT
        defaultCustomerShouldBeFound("customerLimit.in=" + DEFAULT_CUSTOMER_LIMIT + "," + UPDATED_CUSTOMER_LIMIT);

        // Get all the customerList where customerLimit equals to UPDATED_CUSTOMER_LIMIT
        defaultCustomerShouldNotBeFound("customerLimit.in=" + UPDATED_CUSTOMER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerLimit is not null
        defaultCustomerShouldBeFound("customerLimit.specified=true");

        // Get all the customerList where customerLimit is null
        defaultCustomerShouldNotBeFound("customerLimit.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerLimit is greater than or equal to DEFAULT_CUSTOMER_LIMIT
        defaultCustomerShouldBeFound("customerLimit.greaterThanOrEqual=" + DEFAULT_CUSTOMER_LIMIT);

        // Get all the customerList where customerLimit is greater than or equal to UPDATED_CUSTOMER_LIMIT
        defaultCustomerShouldNotBeFound("customerLimit.greaterThanOrEqual=" + UPDATED_CUSTOMER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerLimit is less than or equal to DEFAULT_CUSTOMER_LIMIT
        defaultCustomerShouldBeFound("customerLimit.lessThanOrEqual=" + DEFAULT_CUSTOMER_LIMIT);

        // Get all the customerList where customerLimit is less than or equal to SMALLER_CUSTOMER_LIMIT
        defaultCustomerShouldNotBeFound("customerLimit.lessThanOrEqual=" + SMALLER_CUSTOMER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerLimit is less than DEFAULT_CUSTOMER_LIMIT
        defaultCustomerShouldNotBeFound("customerLimit.lessThan=" + DEFAULT_CUSTOMER_LIMIT);

        // Get all the customerList where customerLimit is less than UPDATED_CUSTOMER_LIMIT
        defaultCustomerShouldBeFound("customerLimit.lessThan=" + UPDATED_CUSTOMER_LIMIT);
    }

    @Test
    @Transactional
    public void getAllCustomersByCustomerLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerLimit is greater than DEFAULT_CUSTOMER_LIMIT
        defaultCustomerShouldNotBeFound("customerLimit.greaterThan=" + DEFAULT_CUSTOMER_LIMIT);

        // Get all the customerList where customerLimit is greater than SMALLER_CUSTOMER_LIMIT
        defaultCustomerShouldBeFound("customerLimit.greaterThan=" + SMALLER_CUSTOMER_LIMIT);
    }


    @Test
    @Transactional
    public void getAllCustomersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isActive equals to DEFAULT_IS_ACTIVE
        defaultCustomerShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the customerList where isActive equals to UPDATED_IS_ACTIVE
        defaultCustomerShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCustomersByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultCustomerShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the customerList where isActive not equals to UPDATED_IS_ACTIVE
        defaultCustomerShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCustomersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultCustomerShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the customerList where isActive equals to UPDATED_IS_ACTIVE
        defaultCustomerShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCustomersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where isActive is not null
        defaultCustomerShouldBeFound("isActive.specified=true");

        // Get all the customerList where isActive is null
        defaultCustomerShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        customer.setLocation(location);
        customerRepository.saveAndFlush(customer);
        Long locationId = location.getId();

        // Get all the customerList where location equals to locationId
        defaultCustomerShouldBeFound("locationId.equals=" + locationId);

        // Get all the customerList where location equals to locationId + 1
        defaultCustomerShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerCode").value(hasItem(DEFAULT_CUSTOMER_CODE)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].customerLimit").value(hasItem(DEFAULT_CUSTOMER_LIMIT.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restCustomerMockMvc.perform(get("/api/customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc.perform(get("/api/customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .customerCode(UPDATED_CUSTOMER_CODE)
            .customerName(UPDATED_CUSTOMER_NAME)
            .customerLimit(UPDATED_CUSTOMER_LIMIT)
            .isActive(UPDATED_IS_ACTIVE);

        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomer)))
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerCode()).isEqualTo(UPDATED_CUSTOMER_CODE);
        assertThat(testCustomer.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testCustomer.getCustomerLimit()).isEqualTo(UPDATED_CUSTOMER_LIMIT);
        assertThat(testCustomer.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Create the Customer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = new Customer();
        customer1.setId(1L);
        Customer customer2 = new Customer();
        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);
        customer2.setId(2L);
        assertThat(customer1).isNotEqualTo(customer2);
        customer1.setId(null);
        assertThat(customer1).isNotEqualTo(customer2);
    }
}
