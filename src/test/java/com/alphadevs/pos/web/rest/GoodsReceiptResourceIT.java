package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.GoodsReceipt;
import com.alphadevs.pos.domain.GoodsReceiptDetails;
import com.alphadevs.pos.domain.PurchaseOrder;
import com.alphadevs.pos.domain.Supplier;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.repository.GoodsReceiptRepository;
import com.alphadevs.pos.service.GoodsReceiptService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.GoodsReceiptCriteria;
import com.alphadevs.pos.service.GoodsReceiptQueryService;

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
 * Integration tests for the {@link GoodsReceiptResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class GoodsReceiptResourceIT {

    private static final String DEFAULT_GRN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_GRN_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GRN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GRN_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_GRN_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PO_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PO_NUMBER = "BBBBBBBBBB";

    @Autowired
    private GoodsReceiptRepository goodsReceiptRepository;

    @Autowired
    private GoodsReceiptService goodsReceiptService;

    @Autowired
    private GoodsReceiptQueryService goodsReceiptQueryService;

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

    private MockMvc restGoodsReceiptMockMvc;

    private GoodsReceipt goodsReceipt;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GoodsReceiptResource goodsReceiptResource = new GoodsReceiptResource(goodsReceiptService, goodsReceiptQueryService);
        this.restGoodsReceiptMockMvc = MockMvcBuilders.standaloneSetup(goodsReceiptResource)
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
    public static GoodsReceipt createEntity(EntityManager em) {
        GoodsReceipt goodsReceipt = new GoodsReceipt()
            .grnNumber(DEFAULT_GRN_NUMBER)
            .grnDate(DEFAULT_GRN_DATE)
            .poNumber(DEFAULT_PO_NUMBER);
        return goodsReceipt;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoodsReceipt createUpdatedEntity(EntityManager em) {
        GoodsReceipt goodsReceipt = new GoodsReceipt()
            .grnNumber(UPDATED_GRN_NUMBER)
            .grnDate(UPDATED_GRN_DATE)
            .poNumber(UPDATED_PO_NUMBER);
        return goodsReceipt;
    }

    @BeforeEach
    public void initTest() {
        goodsReceipt = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoodsReceipt() throws Exception {
        int databaseSizeBeforeCreate = goodsReceiptRepository.findAll().size();

        // Create the GoodsReceipt
        restGoodsReceiptMockMvc.perform(post("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceipt)))
            .andExpect(status().isCreated());

        // Validate the GoodsReceipt in the database
        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeCreate + 1);
        GoodsReceipt testGoodsReceipt = goodsReceiptList.get(goodsReceiptList.size() - 1);
        assertThat(testGoodsReceipt.getGrnNumber()).isEqualTo(DEFAULT_GRN_NUMBER);
        assertThat(testGoodsReceipt.getGrnDate()).isEqualTo(DEFAULT_GRN_DATE);
        assertThat(testGoodsReceipt.getPoNumber()).isEqualTo(DEFAULT_PO_NUMBER);
    }

    @Test
    @Transactional
    public void createGoodsReceiptWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = goodsReceiptRepository.findAll().size();

        // Create the GoodsReceipt with an existing ID
        goodsReceipt.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoodsReceiptMockMvc.perform(post("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceipt)))
            .andExpect(status().isBadRequest());

        // Validate the GoodsReceipt in the database
        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGrnNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = goodsReceiptRepository.findAll().size();
        // set the field null
        goodsReceipt.setGrnNumber(null);

        // Create the GoodsReceipt, which fails.

        restGoodsReceiptMockMvc.perform(post("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceipt)))
            .andExpect(status().isBadRequest());

        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGrnDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = goodsReceiptRepository.findAll().size();
        // set the field null
        goodsReceipt.setGrnDate(null);

        // Create the GoodsReceipt, which fails.

        restGoodsReceiptMockMvc.perform(post("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceipt)))
            .andExpect(status().isBadRequest());

        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGoodsReceipts() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodsReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].grnNumber").value(hasItem(DEFAULT_GRN_NUMBER)))
            .andExpect(jsonPath("$.[*].grnDate").value(hasItem(DEFAULT_GRN_DATE.toString())))
            .andExpect(jsonPath("$.[*].poNumber").value(hasItem(DEFAULT_PO_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getGoodsReceipt() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get the goodsReceipt
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts/{id}", goodsReceipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(goodsReceipt.getId().intValue()))
            .andExpect(jsonPath("$.grnNumber").value(DEFAULT_GRN_NUMBER))
            .andExpect(jsonPath("$.grnDate").value(DEFAULT_GRN_DATE.toString()))
            .andExpect(jsonPath("$.poNumber").value(DEFAULT_PO_NUMBER));
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber equals to DEFAULT_GRN_NUMBER
        defaultGoodsReceiptShouldBeFound("grnNumber.equals=" + DEFAULT_GRN_NUMBER);

        // Get all the goodsReceiptList where grnNumber equals to UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldNotBeFound("grnNumber.equals=" + UPDATED_GRN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber not equals to DEFAULT_GRN_NUMBER
        defaultGoodsReceiptShouldNotBeFound("grnNumber.notEquals=" + DEFAULT_GRN_NUMBER);

        // Get all the goodsReceiptList where grnNumber not equals to UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldBeFound("grnNumber.notEquals=" + UPDATED_GRN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber in DEFAULT_GRN_NUMBER or UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldBeFound("grnNumber.in=" + DEFAULT_GRN_NUMBER + "," + UPDATED_GRN_NUMBER);

        // Get all the goodsReceiptList where grnNumber equals to UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldNotBeFound("grnNumber.in=" + UPDATED_GRN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber is not null
        defaultGoodsReceiptShouldBeFound("grnNumber.specified=true");

        // Get all the goodsReceiptList where grnNumber is null
        defaultGoodsReceiptShouldNotBeFound("grnNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberContainsSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber contains DEFAULT_GRN_NUMBER
        defaultGoodsReceiptShouldBeFound("grnNumber.contains=" + DEFAULT_GRN_NUMBER);

        // Get all the goodsReceiptList where grnNumber contains UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldNotBeFound("grnNumber.contains=" + UPDATED_GRN_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnNumberNotContainsSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnNumber does not contain DEFAULT_GRN_NUMBER
        defaultGoodsReceiptShouldNotBeFound("grnNumber.doesNotContain=" + DEFAULT_GRN_NUMBER);

        // Get all the goodsReceiptList where grnNumber does not contain UPDATED_GRN_NUMBER
        defaultGoodsReceiptShouldBeFound("grnNumber.doesNotContain=" + UPDATED_GRN_NUMBER);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate equals to DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.equals=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate equals to UPDATED_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.equals=" + UPDATED_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate not equals to DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.notEquals=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate not equals to UPDATED_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.notEquals=" + UPDATED_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate in DEFAULT_GRN_DATE or UPDATED_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.in=" + DEFAULT_GRN_DATE + "," + UPDATED_GRN_DATE);

        // Get all the goodsReceiptList where grnDate equals to UPDATED_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.in=" + UPDATED_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate is not null
        defaultGoodsReceiptShouldBeFound("grnDate.specified=true");

        // Get all the goodsReceiptList where grnDate is null
        defaultGoodsReceiptShouldNotBeFound("grnDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate is greater than or equal to DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.greaterThanOrEqual=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate is greater than or equal to UPDATED_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.greaterThanOrEqual=" + UPDATED_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate is less than or equal to DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.lessThanOrEqual=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate is less than or equal to SMALLER_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.lessThanOrEqual=" + SMALLER_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsLessThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate is less than DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.lessThan=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate is less than UPDATED_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.lessThan=" + UPDATED_GRN_DATE);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByGrnDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where grnDate is greater than DEFAULT_GRN_DATE
        defaultGoodsReceiptShouldNotBeFound("grnDate.greaterThan=" + DEFAULT_GRN_DATE);

        // Get all the goodsReceiptList where grnDate is greater than SMALLER_GRN_DATE
        defaultGoodsReceiptShouldBeFound("grnDate.greaterThan=" + SMALLER_GRN_DATE);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber equals to DEFAULT_PO_NUMBER
        defaultGoodsReceiptShouldBeFound("poNumber.equals=" + DEFAULT_PO_NUMBER);

        // Get all the goodsReceiptList where poNumber equals to UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldNotBeFound("poNumber.equals=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber not equals to DEFAULT_PO_NUMBER
        defaultGoodsReceiptShouldNotBeFound("poNumber.notEquals=" + DEFAULT_PO_NUMBER);

        // Get all the goodsReceiptList where poNumber not equals to UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldBeFound("poNumber.notEquals=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberIsInShouldWork() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber in DEFAULT_PO_NUMBER or UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldBeFound("poNumber.in=" + DEFAULT_PO_NUMBER + "," + UPDATED_PO_NUMBER);

        // Get all the goodsReceiptList where poNumber equals to UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldNotBeFound("poNumber.in=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber is not null
        defaultGoodsReceiptShouldBeFound("poNumber.specified=true");

        // Get all the goodsReceiptList where poNumber is null
        defaultGoodsReceiptShouldNotBeFound("poNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberContainsSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber contains DEFAULT_PO_NUMBER
        defaultGoodsReceiptShouldBeFound("poNumber.contains=" + DEFAULT_PO_NUMBER);

        // Get all the goodsReceiptList where poNumber contains UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldNotBeFound("poNumber.contains=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllGoodsReceiptsByPoNumberNotContainsSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

        // Get all the goodsReceiptList where poNumber does not contain DEFAULT_PO_NUMBER
        defaultGoodsReceiptShouldNotBeFound("poNumber.doesNotContain=" + DEFAULT_PO_NUMBER);

        // Get all the goodsReceiptList where poNumber does not contain UPDATED_PO_NUMBER
        defaultGoodsReceiptShouldBeFound("poNumber.doesNotContain=" + UPDATED_PO_NUMBER);
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        GoodsReceiptDetails details = GoodsReceiptDetailsResourceIT.createEntity(em);
        em.persist(details);
        em.flush();
        goodsReceipt.addDetails(details);
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        Long detailsId = details.getId();

        // Get all the goodsReceiptList where details equals to detailsId
        defaultGoodsReceiptShouldBeFound("detailsId.equals=" + detailsId);

        // Get all the goodsReceiptList where details equals to detailsId + 1
        defaultGoodsReceiptShouldNotBeFound("detailsId.equals=" + (detailsId + 1));
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByLinkedPOsIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        PurchaseOrder linkedPOs = PurchaseOrderResourceIT.createEntity(em);
        em.persist(linkedPOs);
        em.flush();
        goodsReceipt.addLinkedPOs(linkedPOs);
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        Long linkedPOsId = linkedPOs.getId();

        // Get all the goodsReceiptList where linkedPOs equals to linkedPOsId
        defaultGoodsReceiptShouldBeFound("linkedPOsId.equals=" + linkedPOsId);

        // Get all the goodsReceiptList where linkedPOs equals to linkedPOsId + 1
        defaultGoodsReceiptShouldNotBeFound("linkedPOsId.equals=" + (linkedPOsId + 1));
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsBySupplierIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        Supplier supplier = SupplierResourceIT.createEntity(em);
        em.persist(supplier);
        em.flush();
        goodsReceipt.setSupplier(supplier);
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        Long supplierId = supplier.getId();

        // Get all the goodsReceiptList where supplier equals to supplierId
        defaultGoodsReceiptShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the goodsReceiptList where supplier equals to supplierId + 1
        defaultGoodsReceiptShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }


    @Test
    @Transactional
    public void getAllGoodsReceiptsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        goodsReceipt.setLocation(location);
        goodsReceiptRepository.saveAndFlush(goodsReceipt);
        Long locationId = location.getId();

        // Get all the goodsReceiptList where location equals to locationId
        defaultGoodsReceiptShouldBeFound("locationId.equals=" + locationId);

        // Get all the goodsReceiptList where location equals to locationId + 1
        defaultGoodsReceiptShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGoodsReceiptShouldBeFound(String filter) throws Exception {
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodsReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].grnNumber").value(hasItem(DEFAULT_GRN_NUMBER)))
            .andExpect(jsonPath("$.[*].grnDate").value(hasItem(DEFAULT_GRN_DATE.toString())))
            .andExpect(jsonPath("$.[*].poNumber").value(hasItem(DEFAULT_PO_NUMBER)));

        // Check, that the count call also returns 1
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGoodsReceiptShouldNotBeFound(String filter) throws Exception {
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGoodsReceipt() throws Exception {
        // Get the goodsReceipt
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoodsReceipt() throws Exception {
        // Initialize the database
        goodsReceiptService.save(goodsReceipt);

        int databaseSizeBeforeUpdate = goodsReceiptRepository.findAll().size();

        // Update the goodsReceipt
        GoodsReceipt updatedGoodsReceipt = goodsReceiptRepository.findById(goodsReceipt.getId()).get();
        // Disconnect from session so that the updates on updatedGoodsReceipt are not directly saved in db
        em.detach(updatedGoodsReceipt);
        updatedGoodsReceipt
            .grnNumber(UPDATED_GRN_NUMBER)
            .grnDate(UPDATED_GRN_DATE)
            .poNumber(UPDATED_PO_NUMBER);

        restGoodsReceiptMockMvc.perform(put("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGoodsReceipt)))
            .andExpect(status().isOk());

        // Validate the GoodsReceipt in the database
        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeUpdate);
        GoodsReceipt testGoodsReceipt = goodsReceiptList.get(goodsReceiptList.size() - 1);
        assertThat(testGoodsReceipt.getGrnNumber()).isEqualTo(UPDATED_GRN_NUMBER);
        assertThat(testGoodsReceipt.getGrnDate()).isEqualTo(UPDATED_GRN_DATE);
        assertThat(testGoodsReceipt.getPoNumber()).isEqualTo(UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingGoodsReceipt() throws Exception {
        int databaseSizeBeforeUpdate = goodsReceiptRepository.findAll().size();

        // Create the GoodsReceipt

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoodsReceiptMockMvc.perform(put("/api/goods-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodsReceipt)))
            .andExpect(status().isBadRequest());

        // Validate the GoodsReceipt in the database
        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGoodsReceipt() throws Exception {
        // Initialize the database
        goodsReceiptService.save(goodsReceipt);

        int databaseSizeBeforeDelete = goodsReceiptRepository.findAll().size();

        // Delete the goodsReceipt
        restGoodsReceiptMockMvc.perform(delete("/api/goods-receipts/{id}", goodsReceipt.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GoodsReceipt> goodsReceiptList = goodsReceiptRepository.findAll();
        assertThat(goodsReceiptList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoodsReceipt.class);
        GoodsReceipt goodsReceipt1 = new GoodsReceipt();
        goodsReceipt1.setId(1L);
        GoodsReceipt goodsReceipt2 = new GoodsReceipt();
        goodsReceipt2.setId(goodsReceipt1.getId());
        assertThat(goodsReceipt1).isEqualTo(goodsReceipt2);
        goodsReceipt2.setId(2L);
        assertThat(goodsReceipt1).isNotEqualTo(goodsReceipt2);
        goodsReceipt1.setId(null);
        assertThat(goodsReceipt1).isNotEqualTo(goodsReceipt2);
    }
}
