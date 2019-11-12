package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.PurchaseOrder;
import com.alphadevs.pos.domain.PurchaseOrderDetails;
import com.alphadevs.pos.domain.Supplier;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.domain.GoodsReceipt;
import com.alphadevs.pos.repository.PurchaseOrderRepository;
import com.alphadevs.pos.service.PurchaseOrderService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.PurchaseOrderCriteria;
import com.alphadevs.pos.service.PurchaseOrderQueryService;

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
 * Integration tests for the {@link PurchaseOrderResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class PurchaseOrderResourceIT {

    private static final String DEFAULT_PO_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PO_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PO_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PO_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PurchaseOrderQueryService purchaseOrderQueryService;

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

    private MockMvc restPurchaseOrderMockMvc;

    private PurchaseOrder purchaseOrder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseOrderResource purchaseOrderResource = new PurchaseOrderResource(purchaseOrderService, purchaseOrderQueryService);
        this.restPurchaseOrderMockMvc = MockMvcBuilders.standaloneSetup(purchaseOrderResource)
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
    public static PurchaseOrder createEntity(EntityManager em) {
        PurchaseOrder purchaseOrder = new PurchaseOrder()
            .poNumber(DEFAULT_PO_NUMBER)
            .poDate(DEFAULT_PO_DATE);
        return purchaseOrder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrder createUpdatedEntity(EntityManager em) {
        PurchaseOrder purchaseOrder = new PurchaseOrder()
            .poNumber(UPDATED_PO_NUMBER)
            .poDate(UPDATED_PO_DATE);
        return purchaseOrder;
    }

    @BeforeEach
    public void initTest() {
        purchaseOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseOrder() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();

        // Create the PurchaseOrder
        restPurchaseOrderMockMvc.perform(post("/api/purchase-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrder)))
            .andExpect(status().isCreated());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getPoNumber()).isEqualTo(DEFAULT_PO_NUMBER);
        assertThat(testPurchaseOrder.getPoDate()).isEqualTo(DEFAULT_PO_DATE);
    }

    @Test
    @Transactional
    public void createPurchaseOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();

        // Create the PurchaseOrder with an existing ID
        purchaseOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderMockMvc.perform(post("/api/purchase-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrder)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPoNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderRepository.findAll().size();
        // set the field null
        purchaseOrder.setPoNumber(null);

        // Create the PurchaseOrder, which fails.

        restPurchaseOrderMockMvc.perform(post("/api/purchase-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrder)))
            .andExpect(status().isBadRequest());

        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPoDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderRepository.findAll().size();
        // set the field null
        purchaseOrder.setPoDate(null);

        // Create the PurchaseOrder, which fails.

        restPurchaseOrderMockMvc.perform(post("/api/purchase-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrder)))
            .andExpect(status().isBadRequest());

        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrders() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList
        restPurchaseOrderMockMvc.perform(get("/api/purchase-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].poNumber").value(hasItem(DEFAULT_PO_NUMBER)))
            .andExpect(jsonPath("$.[*].poDate").value(hasItem(DEFAULT_PO_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getPurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get the purchaseOrder
        restPurchaseOrderMockMvc.perform(get("/api/purchase-orders/{id}", purchaseOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrder.getId().intValue()))
            .andExpect(jsonPath("$.poNumber").value(DEFAULT_PO_NUMBER))
            .andExpect(jsonPath("$.poDate").value(DEFAULT_PO_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poNumber equals to DEFAULT_PO_NUMBER
        defaultPurchaseOrderShouldBeFound("poNumber.equals=" + DEFAULT_PO_NUMBER);

        // Get all the purchaseOrderList where poNumber equals to UPDATED_PO_NUMBER
        defaultPurchaseOrderShouldNotBeFound("poNumber.equals=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poNumber not equals to DEFAULT_PO_NUMBER
        defaultPurchaseOrderShouldNotBeFound("poNumber.notEquals=" + DEFAULT_PO_NUMBER);

        // Get all the purchaseOrderList where poNumber not equals to UPDATED_PO_NUMBER
        defaultPurchaseOrderShouldBeFound("poNumber.notEquals=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoNumberIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poNumber in DEFAULT_PO_NUMBER or UPDATED_PO_NUMBER
        defaultPurchaseOrderShouldBeFound("poNumber.in=" + DEFAULT_PO_NUMBER + "," + UPDATED_PO_NUMBER);

        // Get all the purchaseOrderList where poNumber equals to UPDATED_PO_NUMBER
        defaultPurchaseOrderShouldNotBeFound("poNumber.in=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poNumber is not null
        defaultPurchaseOrderShouldBeFound("poNumber.specified=true");

        // Get all the purchaseOrderList where poNumber is null
        defaultPurchaseOrderShouldNotBeFound("poNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllPurchaseOrdersByPoNumberContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poNumber contains DEFAULT_PO_NUMBER
        defaultPurchaseOrderShouldBeFound("poNumber.contains=" + DEFAULT_PO_NUMBER);

        // Get all the purchaseOrderList where poNumber contains UPDATED_PO_NUMBER
        defaultPurchaseOrderShouldNotBeFound("poNumber.contains=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoNumberNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poNumber does not contain DEFAULT_PO_NUMBER
        defaultPurchaseOrderShouldNotBeFound("poNumber.doesNotContain=" + DEFAULT_PO_NUMBER);

        // Get all the purchaseOrderList where poNumber does not contain UPDATED_PO_NUMBER
        defaultPurchaseOrderShouldBeFound("poNumber.doesNotContain=" + UPDATED_PO_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoDateIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate equals to DEFAULT_PO_DATE
        defaultPurchaseOrderShouldBeFound("poDate.equals=" + DEFAULT_PO_DATE);

        // Get all the purchaseOrderList where poDate equals to UPDATED_PO_DATE
        defaultPurchaseOrderShouldNotBeFound("poDate.equals=" + UPDATED_PO_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate not equals to DEFAULT_PO_DATE
        defaultPurchaseOrderShouldNotBeFound("poDate.notEquals=" + DEFAULT_PO_DATE);

        // Get all the purchaseOrderList where poDate not equals to UPDATED_PO_DATE
        defaultPurchaseOrderShouldBeFound("poDate.notEquals=" + UPDATED_PO_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoDateIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate in DEFAULT_PO_DATE or UPDATED_PO_DATE
        defaultPurchaseOrderShouldBeFound("poDate.in=" + DEFAULT_PO_DATE + "," + UPDATED_PO_DATE);

        // Get all the purchaseOrderList where poDate equals to UPDATED_PO_DATE
        defaultPurchaseOrderShouldNotBeFound("poDate.in=" + UPDATED_PO_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate is not null
        defaultPurchaseOrderShouldBeFound("poDate.specified=true");

        // Get all the purchaseOrderList where poDate is null
        defaultPurchaseOrderShouldNotBeFound("poDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate is greater than or equal to DEFAULT_PO_DATE
        defaultPurchaseOrderShouldBeFound("poDate.greaterThanOrEqual=" + DEFAULT_PO_DATE);

        // Get all the purchaseOrderList where poDate is greater than or equal to UPDATED_PO_DATE
        defaultPurchaseOrderShouldNotBeFound("poDate.greaterThanOrEqual=" + UPDATED_PO_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate is less than or equal to DEFAULT_PO_DATE
        defaultPurchaseOrderShouldBeFound("poDate.lessThanOrEqual=" + DEFAULT_PO_DATE);

        // Get all the purchaseOrderList where poDate is less than or equal to SMALLER_PO_DATE
        defaultPurchaseOrderShouldNotBeFound("poDate.lessThanOrEqual=" + SMALLER_PO_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoDateIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate is less than DEFAULT_PO_DATE
        defaultPurchaseOrderShouldNotBeFound("poDate.lessThan=" + DEFAULT_PO_DATE);

        // Get all the purchaseOrderList where poDate is less than UPDATED_PO_DATE
        defaultPurchaseOrderShouldBeFound("poDate.lessThan=" + UPDATED_PO_DATE);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByPoDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate is greater than DEFAULT_PO_DATE
        defaultPurchaseOrderShouldNotBeFound("poDate.greaterThan=" + DEFAULT_PO_DATE);

        // Get all the purchaseOrderList where poDate is greater than SMALLER_PO_DATE
        defaultPurchaseOrderShouldBeFound("poDate.greaterThan=" + SMALLER_PO_DATE);
    }


    @Test
    @Transactional
    public void getAllPurchaseOrdersByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        PurchaseOrderDetails details = PurchaseOrderDetailsResourceIT.createEntity(em);
        em.persist(details);
        em.flush();
        purchaseOrder.addDetails(details);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long detailsId = details.getId();

        // Get all the purchaseOrderList where details equals to detailsId
        defaultPurchaseOrderShouldBeFound("detailsId.equals=" + detailsId);

        // Get all the purchaseOrderList where details equals to detailsId + 1
        defaultPurchaseOrderShouldNotBeFound("detailsId.equals=" + (detailsId + 1));
    }


    @Test
    @Transactional
    public void getAllPurchaseOrdersBySupplierIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Supplier supplier = SupplierResourceIT.createEntity(em);
        em.persist(supplier);
        em.flush();
        purchaseOrder.setSupplier(supplier);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long supplierId = supplier.getId();

        // Get all the purchaseOrderList where supplier equals to supplierId
        defaultPurchaseOrderShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the purchaseOrderList where supplier equals to supplierId + 1
        defaultPurchaseOrderShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }


    @Test
    @Transactional
    public void getAllPurchaseOrdersByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        purchaseOrder.setLocation(location);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long locationId = location.getId();

        // Get all the purchaseOrderList where location equals to locationId
        defaultPurchaseOrderShouldBeFound("locationId.equals=" + locationId);

        // Get all the purchaseOrderList where location equals to locationId + 1
        defaultPurchaseOrderShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllPurchaseOrdersByRelatedGRNIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        GoodsReceipt relatedGRN = GoodsReceiptResourceIT.createEntity(em);
        em.persist(relatedGRN);
        em.flush();
        purchaseOrder.setRelatedGRN(relatedGRN);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long relatedGRNId = relatedGRN.getId();

        // Get all the purchaseOrderList where relatedGRN equals to relatedGRNId
        defaultPurchaseOrderShouldBeFound("relatedGRNId.equals=" + relatedGRNId);

        // Get all the purchaseOrderList where relatedGRN equals to relatedGRNId + 1
        defaultPurchaseOrderShouldNotBeFound("relatedGRNId.equals=" + (relatedGRNId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPurchaseOrderShouldBeFound(String filter) throws Exception {
        restPurchaseOrderMockMvc.perform(get("/api/purchase-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].poNumber").value(hasItem(DEFAULT_PO_NUMBER)))
            .andExpect(jsonPath("$.[*].poDate").value(hasItem(DEFAULT_PO_DATE.toString())));

        // Check, that the count call also returns 1
        restPurchaseOrderMockMvc.perform(get("/api/purchase-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPurchaseOrderShouldNotBeFound(String filter) throws Exception {
        restPurchaseOrderMockMvc.perform(get("/api/purchase-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseOrderMockMvc.perform(get("/api/purchase-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPurchaseOrder() throws Exception {
        // Get the purchaseOrder
        restPurchaseOrderMockMvc.perform(get("/api/purchase-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderService.save(purchaseOrder);

        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Update the purchaseOrder
        PurchaseOrder updatedPurchaseOrder = purchaseOrderRepository.findById(purchaseOrder.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseOrder are not directly saved in db
        em.detach(updatedPurchaseOrder);
        updatedPurchaseOrder
            .poNumber(UPDATED_PO_NUMBER)
            .poDate(UPDATED_PO_DATE);

        restPurchaseOrderMockMvc.perform(put("/api/purchase-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseOrder)))
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getPoNumber()).isEqualTo(UPDATED_PO_NUMBER);
        assertThat(testPurchaseOrder.getPoDate()).isEqualTo(UPDATED_PO_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Create the PurchaseOrder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc.perform(put("/api/purchase-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrder)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderService.save(purchaseOrder);

        int databaseSizeBeforeDelete = purchaseOrderRepository.findAll().size();

        // Delete the purchaseOrder
        restPurchaseOrderMockMvc.perform(delete("/api/purchase-orders/{id}", purchaseOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrder.class);
        PurchaseOrder purchaseOrder1 = new PurchaseOrder();
        purchaseOrder1.setId(1L);
        PurchaseOrder purchaseOrder2 = new PurchaseOrder();
        purchaseOrder2.setId(purchaseOrder1.getId());
        assertThat(purchaseOrder1).isEqualTo(purchaseOrder2);
        purchaseOrder2.setId(2L);
        assertThat(purchaseOrder1).isNotEqualTo(purchaseOrder2);
        purchaseOrder1.setId(null);
        assertThat(purchaseOrder1).isNotEqualTo(purchaseOrder2);
    }
}
