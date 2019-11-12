package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.PurchaseOrderDetails;
import com.alphadevs.pos.domain.Items;
import com.alphadevs.pos.domain.PurchaseOrder;
import com.alphadevs.pos.repository.PurchaseOrderDetailsRepository;
import com.alphadevs.pos.service.PurchaseOrderDetailsService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.PurchaseOrderDetailsCriteria;
import com.alphadevs.pos.service.PurchaseOrderDetailsQueryService;

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
 * Integration tests for the {@link PurchaseOrderDetailsResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class PurchaseOrderDetailsResourceIT {

    private static final Integer DEFAULT_ITEM_QTY = 1;
    private static final Integer UPDATED_ITEM_QTY = 2;
    private static final Integer SMALLER_ITEM_QTY = 1 - 1;

    @Autowired
    private PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    @Autowired
    private PurchaseOrderDetailsService purchaseOrderDetailsService;

    @Autowired
    private PurchaseOrderDetailsQueryService purchaseOrderDetailsQueryService;

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

    private MockMvc restPurchaseOrderDetailsMockMvc;

    private PurchaseOrderDetails purchaseOrderDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseOrderDetailsResource purchaseOrderDetailsResource = new PurchaseOrderDetailsResource(purchaseOrderDetailsService, purchaseOrderDetailsQueryService);
        this.restPurchaseOrderDetailsMockMvc = MockMvcBuilders.standaloneSetup(purchaseOrderDetailsResource)
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
    public static PurchaseOrderDetails createEntity(EntityManager em) {
        PurchaseOrderDetails purchaseOrderDetails = new PurchaseOrderDetails()
            .itemQty(DEFAULT_ITEM_QTY);
        return purchaseOrderDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderDetails createUpdatedEntity(EntityManager em) {
        PurchaseOrderDetails purchaseOrderDetails = new PurchaseOrderDetails()
            .itemQty(UPDATED_ITEM_QTY);
        return purchaseOrderDetails;
    }

    @BeforeEach
    public void initTest() {
        purchaseOrderDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderDetailsRepository.findAll().size();

        // Create the PurchaseOrderDetails
        restPurchaseOrderDetailsMockMvc.perform(post("/api/purchase-order-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails)))
            .andExpect(status().isCreated());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailsList.get(purchaseOrderDetailsList.size() - 1);
        assertThat(testPurchaseOrderDetails.getItemQty()).isEqualTo(DEFAULT_ITEM_QTY);
    }

    @Test
    @Transactional
    public void createPurchaseOrderDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderDetailsRepository.findAll().size();

        // Create the PurchaseOrderDetails with an existing ID
        purchaseOrderDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderDetailsMockMvc.perform(post("/api/purchase-order-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkItemQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderDetailsRepository.findAll().size();
        // set the field null
        purchaseOrderDetails.setItemQty(null);

        // Create the PurchaseOrderDetails, which fails.

        restPurchaseOrderDetailsMockMvc.perform(post("/api/purchase-order-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails)))
            .andExpect(status().isBadRequest());

        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList
        restPurchaseOrderDetailsMockMvc.perform(get("/api/purchase-order-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemQty").value(hasItem(DEFAULT_ITEM_QTY)));
    }
    
    @Test
    @Transactional
    public void getPurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc.perform(get("/api/purchase-order-details/{id}", purchaseOrderDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrderDetails.getId().intValue()))
            .andExpect(jsonPath("$.itemQty").value(DEFAULT_ITEM_QTY));
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderDetailsByItemQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where itemQty equals to DEFAULT_ITEM_QTY
        defaultPurchaseOrderDetailsShouldBeFound("itemQty.equals=" + DEFAULT_ITEM_QTY);

        // Get all the purchaseOrderDetailsList where itemQty equals to UPDATED_ITEM_QTY
        defaultPurchaseOrderDetailsShouldNotBeFound("itemQty.equals=" + UPDATED_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderDetailsByItemQtyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where itemQty not equals to DEFAULT_ITEM_QTY
        defaultPurchaseOrderDetailsShouldNotBeFound("itemQty.notEquals=" + DEFAULT_ITEM_QTY);

        // Get all the purchaseOrderDetailsList where itemQty not equals to UPDATED_ITEM_QTY
        defaultPurchaseOrderDetailsShouldBeFound("itemQty.notEquals=" + UPDATED_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderDetailsByItemQtyIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where itemQty in DEFAULT_ITEM_QTY or UPDATED_ITEM_QTY
        defaultPurchaseOrderDetailsShouldBeFound("itemQty.in=" + DEFAULT_ITEM_QTY + "," + UPDATED_ITEM_QTY);

        // Get all the purchaseOrderDetailsList where itemQty equals to UPDATED_ITEM_QTY
        defaultPurchaseOrderDetailsShouldNotBeFound("itemQty.in=" + UPDATED_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderDetailsByItemQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where itemQty is not null
        defaultPurchaseOrderDetailsShouldBeFound("itemQty.specified=true");

        // Get all the purchaseOrderDetailsList where itemQty is null
        defaultPurchaseOrderDetailsShouldNotBeFound("itemQty.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderDetailsByItemQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where itemQty is greater than or equal to DEFAULT_ITEM_QTY
        defaultPurchaseOrderDetailsShouldBeFound("itemQty.greaterThanOrEqual=" + DEFAULT_ITEM_QTY);

        // Get all the purchaseOrderDetailsList where itemQty is greater than or equal to UPDATED_ITEM_QTY
        defaultPurchaseOrderDetailsShouldNotBeFound("itemQty.greaterThanOrEqual=" + UPDATED_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderDetailsByItemQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where itemQty is less than or equal to DEFAULT_ITEM_QTY
        defaultPurchaseOrderDetailsShouldBeFound("itemQty.lessThanOrEqual=" + DEFAULT_ITEM_QTY);

        // Get all the purchaseOrderDetailsList where itemQty is less than or equal to SMALLER_ITEM_QTY
        defaultPurchaseOrderDetailsShouldNotBeFound("itemQty.lessThanOrEqual=" + SMALLER_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderDetailsByItemQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where itemQty is less than DEFAULT_ITEM_QTY
        defaultPurchaseOrderDetailsShouldNotBeFound("itemQty.lessThan=" + DEFAULT_ITEM_QTY);

        // Get all the purchaseOrderDetailsList where itemQty is less than UPDATED_ITEM_QTY
        defaultPurchaseOrderDetailsShouldBeFound("itemQty.lessThan=" + UPDATED_ITEM_QTY);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderDetailsByItemQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where itemQty is greater than DEFAULT_ITEM_QTY
        defaultPurchaseOrderDetailsShouldNotBeFound("itemQty.greaterThan=" + DEFAULT_ITEM_QTY);

        // Get all the purchaseOrderDetailsList where itemQty is greater than SMALLER_ITEM_QTY
        defaultPurchaseOrderDetailsShouldBeFound("itemQty.greaterThan=" + SMALLER_ITEM_QTY);
    }


    @Test
    @Transactional
    public void getAllPurchaseOrderDetailsByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);
        Items item = ItemsResourceIT.createEntity(em);
        em.persist(item);
        em.flush();
        purchaseOrderDetails.setItem(item);
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);
        Long itemId = item.getId();

        // Get all the purchaseOrderDetailsList where item equals to itemId
        defaultPurchaseOrderDetailsShouldBeFound("itemId.equals=" + itemId);

        // Get all the purchaseOrderDetailsList where item equals to itemId + 1
        defaultPurchaseOrderDetailsShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }


    @Test
    @Transactional
    public void getAllPurchaseOrderDetailsByPoIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);
        PurchaseOrder po = PurchaseOrderResourceIT.createEntity(em);
        em.persist(po);
        em.flush();
        purchaseOrderDetails.setPo(po);
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);
        Long poId = po.getId();

        // Get all the purchaseOrderDetailsList where po equals to poId
        defaultPurchaseOrderDetailsShouldBeFound("poId.equals=" + poId);

        // Get all the purchaseOrderDetailsList where po equals to poId + 1
        defaultPurchaseOrderDetailsShouldNotBeFound("poId.equals=" + (poId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPurchaseOrderDetailsShouldBeFound(String filter) throws Exception {
        restPurchaseOrderDetailsMockMvc.perform(get("/api/purchase-order-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemQty").value(hasItem(DEFAULT_ITEM_QTY)));

        // Check, that the count call also returns 1
        restPurchaseOrderDetailsMockMvc.perform(get("/api/purchase-order-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPurchaseOrderDetailsShouldNotBeFound(String filter) throws Exception {
        restPurchaseOrderDetailsMockMvc.perform(get("/api/purchase-order-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseOrderDetailsMockMvc.perform(get("/api/purchase-order-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPurchaseOrderDetails() throws Exception {
        // Get the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc.perform(get("/api/purchase-order-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsService.save(purchaseOrderDetails);

        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();

        // Update the purchaseOrderDetails
        PurchaseOrderDetails updatedPurchaseOrderDetails = purchaseOrderDetailsRepository.findById(purchaseOrderDetails.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseOrderDetails are not directly saved in db
        em.detach(updatedPurchaseOrderDetails);
        updatedPurchaseOrderDetails
            .itemQty(UPDATED_ITEM_QTY);

        restPurchaseOrderDetailsMockMvc.perform(put("/api/purchase-order-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseOrderDetails)))
            .andExpect(status().isOk());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailsList.get(purchaseOrderDetailsList.size() - 1);
        assertThat(testPurchaseOrderDetails.getItemQty()).isEqualTo(UPDATED_ITEM_QTY);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();

        // Create the PurchaseOrderDetails

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc.perform(put("/api/purchase-order-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsService.save(purchaseOrderDetails);

        int databaseSizeBeforeDelete = purchaseOrderDetailsRepository.findAll().size();

        // Delete the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc.perform(delete("/api/purchase-order-details/{id}", purchaseOrderDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrderDetails.class);
        PurchaseOrderDetails purchaseOrderDetails1 = new PurchaseOrderDetails();
        purchaseOrderDetails1.setId(1L);
        PurchaseOrderDetails purchaseOrderDetails2 = new PurchaseOrderDetails();
        purchaseOrderDetails2.setId(purchaseOrderDetails1.getId());
        assertThat(purchaseOrderDetails1).isEqualTo(purchaseOrderDetails2);
        purchaseOrderDetails2.setId(2L);
        assertThat(purchaseOrderDetails1).isNotEqualTo(purchaseOrderDetails2);
        purchaseOrderDetails1.setId(null);
        assertThat(purchaseOrderDetails1).isNotEqualTo(purchaseOrderDetails2);
    }
}
