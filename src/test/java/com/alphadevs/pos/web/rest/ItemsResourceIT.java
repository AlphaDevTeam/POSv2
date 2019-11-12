package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.Items;
import com.alphadevs.pos.domain.Designs;
import com.alphadevs.pos.domain.Products;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.repository.ItemsRepository;
import com.alphadevs.pos.service.ItemsService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.ItemsCriteria;
import com.alphadevs.pos.service.ItemsQueryService;

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
 * Integration tests for the {@link ItemsResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class ItemsResourceIT {

    private static final String DEFAULT_ITEM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_ITEM_PRICE = 1D;
    private static final Double UPDATED_ITEM_PRICE = 2D;
    private static final Double SMALLER_ITEM_PRICE = 1D - 1D;

    private static final String DEFAULT_ITEM_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_SERIAL = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_SUPPLIER_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_SUPPLIER_SERIAL = "BBBBBBBBBB";

    private static final Double DEFAULT_ITEM_COST = 1D;
    private static final Double UPDATED_ITEM_COST = 2D;
    private static final Double SMALLER_ITEM_COST = 1D - 1D;

    private static final Double DEFAULT_ITEM_SALE_PRICE = 1D;
    private static final Double UPDATED_ITEM_SALE_PRICE = 2D;
    private static final Double SMALLER_ITEM_SALE_PRICE = 1D - 1D;

    private static final LocalDate DEFAULT_ORIGINAL_STOCK_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORIGINAL_STOCK_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ORIGINAL_STOCK_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MODIFIED_STOCK_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_STOCK_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_STOCK_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private ItemsQueryService itemsQueryService;

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

    private MockMvc restItemsMockMvc;

    private Items items;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemsResource itemsResource = new ItemsResource(itemsService, itemsQueryService);
        this.restItemsMockMvc = MockMvcBuilders.standaloneSetup(itemsResource)
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
    public static Items createEntity(EntityManager em) {
        Items items = new Items()
            .itemCode(DEFAULT_ITEM_CODE)
            .itemName(DEFAULT_ITEM_NAME)
            .itemDescription(DEFAULT_ITEM_DESCRIPTION)
            .itemPrice(DEFAULT_ITEM_PRICE)
            .itemSerial(DEFAULT_ITEM_SERIAL)
            .itemSupplierSerial(DEFAULT_ITEM_SUPPLIER_SERIAL)
            .itemCost(DEFAULT_ITEM_COST)
            .itemSalePrice(DEFAULT_ITEM_SALE_PRICE)
            .originalStockDate(DEFAULT_ORIGINAL_STOCK_DATE)
            .modifiedStockDate(DEFAULT_MODIFIED_STOCK_DATE);
        return items;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Items createUpdatedEntity(EntityManager em) {
        Items items = new Items()
            .itemCode(UPDATED_ITEM_CODE)
            .itemName(UPDATED_ITEM_NAME)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemSerial(UPDATED_ITEM_SERIAL)
            .itemSupplierSerial(UPDATED_ITEM_SUPPLIER_SERIAL)
            .itemCost(UPDATED_ITEM_COST)
            .itemSalePrice(UPDATED_ITEM_SALE_PRICE)
            .originalStockDate(UPDATED_ORIGINAL_STOCK_DATE)
            .modifiedStockDate(UPDATED_MODIFIED_STOCK_DATE);
        return items;
    }

    @BeforeEach
    public void initTest() {
        items = createEntity(em);
    }

    @Test
    @Transactional
    public void createItems() throws Exception {
        int databaseSizeBeforeCreate = itemsRepository.findAll().size();

        // Create the Items
        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isCreated());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeCreate + 1);
        Items testItems = itemsList.get(itemsList.size() - 1);
        assertThat(testItems.getItemCode()).isEqualTo(DEFAULT_ITEM_CODE);
        assertThat(testItems.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testItems.getItemDescription()).isEqualTo(DEFAULT_ITEM_DESCRIPTION);
        assertThat(testItems.getItemPrice()).isEqualTo(DEFAULT_ITEM_PRICE);
        assertThat(testItems.getItemSerial()).isEqualTo(DEFAULT_ITEM_SERIAL);
        assertThat(testItems.getItemSupplierSerial()).isEqualTo(DEFAULT_ITEM_SUPPLIER_SERIAL);
        assertThat(testItems.getItemCost()).isEqualTo(DEFAULT_ITEM_COST);
        assertThat(testItems.getItemSalePrice()).isEqualTo(DEFAULT_ITEM_SALE_PRICE);
        assertThat(testItems.getOriginalStockDate()).isEqualTo(DEFAULT_ORIGINAL_STOCK_DATE);
        assertThat(testItems.getModifiedStockDate()).isEqualTo(DEFAULT_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void createItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemsRepository.findAll().size();

        // Create the Items with an existing ID
        items.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkItemCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setItemCode(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setItemName(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setItemDescription(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setItemPrice(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemSerialIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setItemSerial(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setItemCost(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOriginalStockDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setOriginalStockDate(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList
        restItemsMockMvc.perform(get("/api/items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(items.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].itemDescription").value(hasItem(DEFAULT_ITEM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(DEFAULT_ITEM_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].itemSerial").value(hasItem(DEFAULT_ITEM_SERIAL)))
            .andExpect(jsonPath("$.[*].itemSupplierSerial").value(hasItem(DEFAULT_ITEM_SUPPLIER_SERIAL)))
            .andExpect(jsonPath("$.[*].itemCost").value(hasItem(DEFAULT_ITEM_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].itemSalePrice").value(hasItem(DEFAULT_ITEM_SALE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].originalStockDate").value(hasItem(DEFAULT_ORIGINAL_STOCK_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedStockDate").value(hasItem(DEFAULT_MODIFIED_STOCK_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getItems() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get the items
        restItemsMockMvc.perform(get("/api/items/{id}", items.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(items.getId().intValue()))
            .andExpect(jsonPath("$.itemCode").value(DEFAULT_ITEM_CODE))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.itemDescription").value(DEFAULT_ITEM_DESCRIPTION))
            .andExpect(jsonPath("$.itemPrice").value(DEFAULT_ITEM_PRICE.doubleValue()))
            .andExpect(jsonPath("$.itemSerial").value(DEFAULT_ITEM_SERIAL))
            .andExpect(jsonPath("$.itemSupplierSerial").value(DEFAULT_ITEM_SUPPLIER_SERIAL))
            .andExpect(jsonPath("$.itemCost").value(DEFAULT_ITEM_COST.doubleValue()))
            .andExpect(jsonPath("$.itemSalePrice").value(DEFAULT_ITEM_SALE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.originalStockDate").value(DEFAULT_ORIGINAL_STOCK_DATE.toString()))
            .andExpect(jsonPath("$.modifiedStockDate").value(DEFAULT_MODIFIED_STOCK_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllItemsByItemCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode equals to DEFAULT_ITEM_CODE
        defaultItemsShouldBeFound("itemCode.equals=" + DEFAULT_ITEM_CODE);

        // Get all the itemsList where itemCode equals to UPDATED_ITEM_CODE
        defaultItemsShouldNotBeFound("itemCode.equals=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode not equals to DEFAULT_ITEM_CODE
        defaultItemsShouldNotBeFound("itemCode.notEquals=" + DEFAULT_ITEM_CODE);

        // Get all the itemsList where itemCode not equals to UPDATED_ITEM_CODE
        defaultItemsShouldBeFound("itemCode.notEquals=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCodeIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode in DEFAULT_ITEM_CODE or UPDATED_ITEM_CODE
        defaultItemsShouldBeFound("itemCode.in=" + DEFAULT_ITEM_CODE + "," + UPDATED_ITEM_CODE);

        // Get all the itemsList where itemCode equals to UPDATED_ITEM_CODE
        defaultItemsShouldNotBeFound("itemCode.in=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode is not null
        defaultItemsShouldBeFound("itemCode.specified=true");

        // Get all the itemsList where itemCode is null
        defaultItemsShouldNotBeFound("itemCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemsByItemCodeContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode contains DEFAULT_ITEM_CODE
        defaultItemsShouldBeFound("itemCode.contains=" + DEFAULT_ITEM_CODE);

        // Get all the itemsList where itemCode contains UPDATED_ITEM_CODE
        defaultItemsShouldNotBeFound("itemCode.contains=" + UPDATED_ITEM_CODE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCodeNotContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCode does not contain DEFAULT_ITEM_CODE
        defaultItemsShouldNotBeFound("itemCode.doesNotContain=" + DEFAULT_ITEM_CODE);

        // Get all the itemsList where itemCode does not contain UPDATED_ITEM_CODE
        defaultItemsShouldBeFound("itemCode.doesNotContain=" + UPDATED_ITEM_CODE);
    }


    @Test
    @Transactional
    public void getAllItemsByItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName equals to DEFAULT_ITEM_NAME
        defaultItemsShouldBeFound("itemName.equals=" + DEFAULT_ITEM_NAME);

        // Get all the itemsList where itemName equals to UPDATED_ITEM_NAME
        defaultItemsShouldNotBeFound("itemName.equals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemsByItemNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName not equals to DEFAULT_ITEM_NAME
        defaultItemsShouldNotBeFound("itemName.notEquals=" + DEFAULT_ITEM_NAME);

        // Get all the itemsList where itemName not equals to UPDATED_ITEM_NAME
        defaultItemsShouldBeFound("itemName.notEquals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemsByItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName in DEFAULT_ITEM_NAME or UPDATED_ITEM_NAME
        defaultItemsShouldBeFound("itemName.in=" + DEFAULT_ITEM_NAME + "," + UPDATED_ITEM_NAME);

        // Get all the itemsList where itemName equals to UPDATED_ITEM_NAME
        defaultItemsShouldNotBeFound("itemName.in=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemsByItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName is not null
        defaultItemsShouldBeFound("itemName.specified=true");

        // Get all the itemsList where itemName is null
        defaultItemsShouldNotBeFound("itemName.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemsByItemNameContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName contains DEFAULT_ITEM_NAME
        defaultItemsShouldBeFound("itemName.contains=" + DEFAULT_ITEM_NAME);

        // Get all the itemsList where itemName contains UPDATED_ITEM_NAME
        defaultItemsShouldNotBeFound("itemName.contains=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllItemsByItemNameNotContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemName does not contain DEFAULT_ITEM_NAME
        defaultItemsShouldNotBeFound("itemName.doesNotContain=" + DEFAULT_ITEM_NAME);

        // Get all the itemsList where itemName does not contain UPDATED_ITEM_NAME
        defaultItemsShouldBeFound("itemName.doesNotContain=" + UPDATED_ITEM_NAME);
    }


    @Test
    @Transactional
    public void getAllItemsByItemDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription equals to DEFAULT_ITEM_DESCRIPTION
        defaultItemsShouldBeFound("itemDescription.equals=" + DEFAULT_ITEM_DESCRIPTION);

        // Get all the itemsList where itemDescription equals to UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldNotBeFound("itemDescription.equals=" + UPDATED_ITEM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemsByItemDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription not equals to DEFAULT_ITEM_DESCRIPTION
        defaultItemsShouldNotBeFound("itemDescription.notEquals=" + DEFAULT_ITEM_DESCRIPTION);

        // Get all the itemsList where itemDescription not equals to UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldBeFound("itemDescription.notEquals=" + UPDATED_ITEM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemsByItemDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription in DEFAULT_ITEM_DESCRIPTION or UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldBeFound("itemDescription.in=" + DEFAULT_ITEM_DESCRIPTION + "," + UPDATED_ITEM_DESCRIPTION);

        // Get all the itemsList where itemDescription equals to UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldNotBeFound("itemDescription.in=" + UPDATED_ITEM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemsByItemDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription is not null
        defaultItemsShouldBeFound("itemDescription.specified=true");

        // Get all the itemsList where itemDescription is null
        defaultItemsShouldNotBeFound("itemDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemsByItemDescriptionContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription contains DEFAULT_ITEM_DESCRIPTION
        defaultItemsShouldBeFound("itemDescription.contains=" + DEFAULT_ITEM_DESCRIPTION);

        // Get all the itemsList where itemDescription contains UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldNotBeFound("itemDescription.contains=" + UPDATED_ITEM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemsByItemDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemDescription does not contain DEFAULT_ITEM_DESCRIPTION
        defaultItemsShouldNotBeFound("itemDescription.doesNotContain=" + DEFAULT_ITEM_DESCRIPTION);

        // Get all the itemsList where itemDescription does not contain UPDATED_ITEM_DESCRIPTION
        defaultItemsShouldBeFound("itemDescription.doesNotContain=" + UPDATED_ITEM_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllItemsByItemPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice equals to DEFAULT_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.equals=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice equals to UPDATED_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.equals=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice not equals to DEFAULT_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.notEquals=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice not equals to UPDATED_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.notEquals=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice in DEFAULT_ITEM_PRICE or UPDATED_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.in=" + DEFAULT_ITEM_PRICE + "," + UPDATED_ITEM_PRICE);

        // Get all the itemsList where itemPrice equals to UPDATED_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.in=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice is not null
        defaultItemsShouldBeFound("itemPrice.specified=true");

        // Get all the itemsList where itemPrice is null
        defaultItemsShouldNotBeFound("itemPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice is greater than or equal to DEFAULT_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.greaterThanOrEqual=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice is greater than or equal to UPDATED_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.greaterThanOrEqual=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice is less than or equal to DEFAULT_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.lessThanOrEqual=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice is less than or equal to SMALLER_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.lessThanOrEqual=" + SMALLER_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice is less than DEFAULT_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.lessThan=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice is less than UPDATED_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.lessThan=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemPrice is greater than DEFAULT_ITEM_PRICE
        defaultItemsShouldNotBeFound("itemPrice.greaterThan=" + DEFAULT_ITEM_PRICE);

        // Get all the itemsList where itemPrice is greater than SMALLER_ITEM_PRICE
        defaultItemsShouldBeFound("itemPrice.greaterThan=" + SMALLER_ITEM_PRICE);
    }


    @Test
    @Transactional
    public void getAllItemsByItemSerialIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial equals to DEFAULT_ITEM_SERIAL
        defaultItemsShouldBeFound("itemSerial.equals=" + DEFAULT_ITEM_SERIAL);

        // Get all the itemsList where itemSerial equals to UPDATED_ITEM_SERIAL
        defaultItemsShouldNotBeFound("itemSerial.equals=" + UPDATED_ITEM_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSerialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial not equals to DEFAULT_ITEM_SERIAL
        defaultItemsShouldNotBeFound("itemSerial.notEquals=" + DEFAULT_ITEM_SERIAL);

        // Get all the itemsList where itemSerial not equals to UPDATED_ITEM_SERIAL
        defaultItemsShouldBeFound("itemSerial.notEquals=" + UPDATED_ITEM_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSerialIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial in DEFAULT_ITEM_SERIAL or UPDATED_ITEM_SERIAL
        defaultItemsShouldBeFound("itemSerial.in=" + DEFAULT_ITEM_SERIAL + "," + UPDATED_ITEM_SERIAL);

        // Get all the itemsList where itemSerial equals to UPDATED_ITEM_SERIAL
        defaultItemsShouldNotBeFound("itemSerial.in=" + UPDATED_ITEM_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial is not null
        defaultItemsShouldBeFound("itemSerial.specified=true");

        // Get all the itemsList where itemSerial is null
        defaultItemsShouldNotBeFound("itemSerial.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemsByItemSerialContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial contains DEFAULT_ITEM_SERIAL
        defaultItemsShouldBeFound("itemSerial.contains=" + DEFAULT_ITEM_SERIAL);

        // Get all the itemsList where itemSerial contains UPDATED_ITEM_SERIAL
        defaultItemsShouldNotBeFound("itemSerial.contains=" + UPDATED_ITEM_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSerialNotContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSerial does not contain DEFAULT_ITEM_SERIAL
        defaultItemsShouldNotBeFound("itemSerial.doesNotContain=" + DEFAULT_ITEM_SERIAL);

        // Get all the itemsList where itemSerial does not contain UPDATED_ITEM_SERIAL
        defaultItemsShouldBeFound("itemSerial.doesNotContain=" + UPDATED_ITEM_SERIAL);
    }


    @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial equals to DEFAULT_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldBeFound("itemSupplierSerial.equals=" + DEFAULT_ITEM_SUPPLIER_SERIAL);

        // Get all the itemsList where itemSupplierSerial equals to UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldNotBeFound("itemSupplierSerial.equals=" + UPDATED_ITEM_SUPPLIER_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial not equals to DEFAULT_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldNotBeFound("itemSupplierSerial.notEquals=" + DEFAULT_ITEM_SUPPLIER_SERIAL);

        // Get all the itemsList where itemSupplierSerial not equals to UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldBeFound("itemSupplierSerial.notEquals=" + UPDATED_ITEM_SUPPLIER_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial in DEFAULT_ITEM_SUPPLIER_SERIAL or UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldBeFound("itemSupplierSerial.in=" + DEFAULT_ITEM_SUPPLIER_SERIAL + "," + UPDATED_ITEM_SUPPLIER_SERIAL);

        // Get all the itemsList where itemSupplierSerial equals to UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldNotBeFound("itemSupplierSerial.in=" + UPDATED_ITEM_SUPPLIER_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial is not null
        defaultItemsShouldBeFound("itemSupplierSerial.specified=true");

        // Get all the itemsList where itemSupplierSerial is null
        defaultItemsShouldNotBeFound("itemSupplierSerial.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial contains DEFAULT_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldBeFound("itemSupplierSerial.contains=" + DEFAULT_ITEM_SUPPLIER_SERIAL);

        // Get all the itemsList where itemSupplierSerial contains UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldNotBeFound("itemSupplierSerial.contains=" + UPDATED_ITEM_SUPPLIER_SERIAL);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSupplierSerialNotContainsSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSupplierSerial does not contain DEFAULT_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldNotBeFound("itemSupplierSerial.doesNotContain=" + DEFAULT_ITEM_SUPPLIER_SERIAL);

        // Get all the itemsList where itemSupplierSerial does not contain UPDATED_ITEM_SUPPLIER_SERIAL
        defaultItemsShouldBeFound("itemSupplierSerial.doesNotContain=" + UPDATED_ITEM_SUPPLIER_SERIAL);
    }


    @Test
    @Transactional
    public void getAllItemsByItemCostIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost equals to DEFAULT_ITEM_COST
        defaultItemsShouldBeFound("itemCost.equals=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost equals to UPDATED_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.equals=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost not equals to DEFAULT_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.notEquals=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost not equals to UPDATED_ITEM_COST
        defaultItemsShouldBeFound("itemCost.notEquals=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost in DEFAULT_ITEM_COST or UPDATED_ITEM_COST
        defaultItemsShouldBeFound("itemCost.in=" + DEFAULT_ITEM_COST + "," + UPDATED_ITEM_COST);

        // Get all the itemsList where itemCost equals to UPDATED_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.in=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost is not null
        defaultItemsShouldBeFound("itemCost.specified=true");

        // Get all the itemsList where itemCost is null
        defaultItemsShouldNotBeFound("itemCost.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost is greater than or equal to DEFAULT_ITEM_COST
        defaultItemsShouldBeFound("itemCost.greaterThanOrEqual=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost is greater than or equal to UPDATED_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.greaterThanOrEqual=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost is less than or equal to DEFAULT_ITEM_COST
        defaultItemsShouldBeFound("itemCost.lessThanOrEqual=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost is less than or equal to SMALLER_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.lessThanOrEqual=" + SMALLER_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsLessThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost is less than DEFAULT_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.lessThan=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost is less than UPDATED_ITEM_COST
        defaultItemsShouldBeFound("itemCost.lessThan=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    public void getAllItemsByItemCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemCost is greater than DEFAULT_ITEM_COST
        defaultItemsShouldNotBeFound("itemCost.greaterThan=" + DEFAULT_ITEM_COST);

        // Get all the itemsList where itemCost is greater than SMALLER_ITEM_COST
        defaultItemsShouldBeFound("itemCost.greaterThan=" + SMALLER_ITEM_COST);
    }


    @Test
    @Transactional
    public void getAllItemsByItemSalePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSalePrice equals to DEFAULT_ITEM_SALE_PRICE
        defaultItemsShouldBeFound("itemSalePrice.equals=" + DEFAULT_ITEM_SALE_PRICE);

        // Get all the itemsList where itemSalePrice equals to UPDATED_ITEM_SALE_PRICE
        defaultItemsShouldNotBeFound("itemSalePrice.equals=" + UPDATED_ITEM_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSalePriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSalePrice not equals to DEFAULT_ITEM_SALE_PRICE
        defaultItemsShouldNotBeFound("itemSalePrice.notEquals=" + DEFAULT_ITEM_SALE_PRICE);

        // Get all the itemsList where itemSalePrice not equals to UPDATED_ITEM_SALE_PRICE
        defaultItemsShouldBeFound("itemSalePrice.notEquals=" + UPDATED_ITEM_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSalePriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSalePrice in DEFAULT_ITEM_SALE_PRICE or UPDATED_ITEM_SALE_PRICE
        defaultItemsShouldBeFound("itemSalePrice.in=" + DEFAULT_ITEM_SALE_PRICE + "," + UPDATED_ITEM_SALE_PRICE);

        // Get all the itemsList where itemSalePrice equals to UPDATED_ITEM_SALE_PRICE
        defaultItemsShouldNotBeFound("itemSalePrice.in=" + UPDATED_ITEM_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSalePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSalePrice is not null
        defaultItemsShouldBeFound("itemSalePrice.specified=true");

        // Get all the itemsList where itemSalePrice is null
        defaultItemsShouldNotBeFound("itemSalePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByItemSalePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSalePrice is greater than or equal to DEFAULT_ITEM_SALE_PRICE
        defaultItemsShouldBeFound("itemSalePrice.greaterThanOrEqual=" + DEFAULT_ITEM_SALE_PRICE);

        // Get all the itemsList where itemSalePrice is greater than or equal to UPDATED_ITEM_SALE_PRICE
        defaultItemsShouldNotBeFound("itemSalePrice.greaterThanOrEqual=" + UPDATED_ITEM_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSalePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSalePrice is less than or equal to DEFAULT_ITEM_SALE_PRICE
        defaultItemsShouldBeFound("itemSalePrice.lessThanOrEqual=" + DEFAULT_ITEM_SALE_PRICE);

        // Get all the itemsList where itemSalePrice is less than or equal to SMALLER_ITEM_SALE_PRICE
        defaultItemsShouldNotBeFound("itemSalePrice.lessThanOrEqual=" + SMALLER_ITEM_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSalePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSalePrice is less than DEFAULT_ITEM_SALE_PRICE
        defaultItemsShouldNotBeFound("itemSalePrice.lessThan=" + DEFAULT_ITEM_SALE_PRICE);

        // Get all the itemsList where itemSalePrice is less than UPDATED_ITEM_SALE_PRICE
        defaultItemsShouldBeFound("itemSalePrice.lessThan=" + UPDATED_ITEM_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemsByItemSalePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where itemSalePrice is greater than DEFAULT_ITEM_SALE_PRICE
        defaultItemsShouldNotBeFound("itemSalePrice.greaterThan=" + DEFAULT_ITEM_SALE_PRICE);

        // Get all the itemsList where itemSalePrice is greater than SMALLER_ITEM_SALE_PRICE
        defaultItemsShouldBeFound("itemSalePrice.greaterThan=" + SMALLER_ITEM_SALE_PRICE);
    }


    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate equals to DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.equals=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate equals to UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.equals=" + UPDATED_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate not equals to DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.notEquals=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate not equals to UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.notEquals=" + UPDATED_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate in DEFAULT_ORIGINAL_STOCK_DATE or UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.in=" + DEFAULT_ORIGINAL_STOCK_DATE + "," + UPDATED_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate equals to UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.in=" + UPDATED_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate is not null
        defaultItemsShouldBeFound("originalStockDate.specified=true");

        // Get all the itemsList where originalStockDate is null
        defaultItemsShouldNotBeFound("originalStockDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate is greater than or equal to DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.greaterThanOrEqual=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate is greater than or equal to UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.greaterThanOrEqual=" + UPDATED_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate is less than or equal to DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.lessThanOrEqual=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate is less than or equal to SMALLER_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.lessThanOrEqual=" + SMALLER_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsLessThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate is less than DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.lessThan=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate is less than UPDATED_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.lessThan=" + UPDATED_ORIGINAL_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByOriginalStockDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where originalStockDate is greater than DEFAULT_ORIGINAL_STOCK_DATE
        defaultItemsShouldNotBeFound("originalStockDate.greaterThan=" + DEFAULT_ORIGINAL_STOCK_DATE);

        // Get all the itemsList where originalStockDate is greater than SMALLER_ORIGINAL_STOCK_DATE
        defaultItemsShouldBeFound("originalStockDate.greaterThan=" + SMALLER_ORIGINAL_STOCK_DATE);
    }


    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate equals to DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.equals=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate equals to UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.equals=" + UPDATED_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate not equals to DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.notEquals=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate not equals to UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.notEquals=" + UPDATED_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsInShouldWork() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate in DEFAULT_MODIFIED_STOCK_DATE or UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.in=" + DEFAULT_MODIFIED_STOCK_DATE + "," + UPDATED_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate equals to UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.in=" + UPDATED_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate is not null
        defaultItemsShouldBeFound("modifiedStockDate.specified=true");

        // Get all the itemsList where modifiedStockDate is null
        defaultItemsShouldNotBeFound("modifiedStockDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate is greater than or equal to DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate is greater than or equal to UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.greaterThanOrEqual=" + UPDATED_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate is less than or equal to DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.lessThanOrEqual=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate is less than or equal to SMALLER_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.lessThanOrEqual=" + SMALLER_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsLessThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate is less than DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.lessThan=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate is less than UPDATED_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.lessThan=" + UPDATED_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void getAllItemsByModifiedStockDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList where modifiedStockDate is greater than DEFAULT_MODIFIED_STOCK_DATE
        defaultItemsShouldNotBeFound("modifiedStockDate.greaterThan=" + DEFAULT_MODIFIED_STOCK_DATE);

        // Get all the itemsList where modifiedStockDate is greater than SMALLER_MODIFIED_STOCK_DATE
        defaultItemsShouldBeFound("modifiedStockDate.greaterThan=" + SMALLER_MODIFIED_STOCK_DATE);
    }


    @Test
    @Transactional
    public void getAllItemsByRelatedDesignIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);
        Designs relatedDesign = DesignsResourceIT.createEntity(em);
        em.persist(relatedDesign);
        em.flush();
        items.setRelatedDesign(relatedDesign);
        itemsRepository.saveAndFlush(items);
        Long relatedDesignId = relatedDesign.getId();

        // Get all the itemsList where relatedDesign equals to relatedDesignId
        defaultItemsShouldBeFound("relatedDesignId.equals=" + relatedDesignId);

        // Get all the itemsList where relatedDesign equals to relatedDesignId + 1
        defaultItemsShouldNotBeFound("relatedDesignId.equals=" + (relatedDesignId + 1));
    }


    @Test
    @Transactional
    public void getAllItemsByRelatedProductIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);
        Products relatedProduct = ProductsResourceIT.createEntity(em);
        em.persist(relatedProduct);
        em.flush();
        items.setRelatedProduct(relatedProduct);
        itemsRepository.saveAndFlush(items);
        Long relatedProductId = relatedProduct.getId();

        // Get all the itemsList where relatedProduct equals to relatedProductId
        defaultItemsShouldBeFound("relatedProductId.equals=" + relatedProductId);

        // Get all the itemsList where relatedProduct equals to relatedProductId + 1
        defaultItemsShouldNotBeFound("relatedProductId.equals=" + (relatedProductId + 1));
    }


    @Test
    @Transactional
    public void getAllItemsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        items.setLocation(location);
        itemsRepository.saveAndFlush(items);
        Long locationId = location.getId();

        // Get all the itemsList where location equals to locationId
        defaultItemsShouldBeFound("locationId.equals=" + locationId);

        // Get all the itemsList where location equals to locationId + 1
        defaultItemsShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemsShouldBeFound(String filter) throws Exception {
        restItemsMockMvc.perform(get("/api/items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(items.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].itemDescription").value(hasItem(DEFAULT_ITEM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(DEFAULT_ITEM_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].itemSerial").value(hasItem(DEFAULT_ITEM_SERIAL)))
            .andExpect(jsonPath("$.[*].itemSupplierSerial").value(hasItem(DEFAULT_ITEM_SUPPLIER_SERIAL)))
            .andExpect(jsonPath("$.[*].itemCost").value(hasItem(DEFAULT_ITEM_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].itemSalePrice").value(hasItem(DEFAULT_ITEM_SALE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].originalStockDate").value(hasItem(DEFAULT_ORIGINAL_STOCK_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedStockDate").value(hasItem(DEFAULT_MODIFIED_STOCK_DATE.toString())));

        // Check, that the count call also returns 1
        restItemsMockMvc.perform(get("/api/items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemsShouldNotBeFound(String filter) throws Exception {
        restItemsMockMvc.perform(get("/api/items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemsMockMvc.perform(get("/api/items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingItems() throws Exception {
        // Get the items
        restItemsMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItems() throws Exception {
        // Initialize the database
        itemsService.save(items);

        int databaseSizeBeforeUpdate = itemsRepository.findAll().size();

        // Update the items
        Items updatedItems = itemsRepository.findById(items.getId()).get();
        // Disconnect from session so that the updates on updatedItems are not directly saved in db
        em.detach(updatedItems);
        updatedItems
            .itemCode(UPDATED_ITEM_CODE)
            .itemName(UPDATED_ITEM_NAME)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemSerial(UPDATED_ITEM_SERIAL)
            .itemSupplierSerial(UPDATED_ITEM_SUPPLIER_SERIAL)
            .itemCost(UPDATED_ITEM_COST)
            .itemSalePrice(UPDATED_ITEM_SALE_PRICE)
            .originalStockDate(UPDATED_ORIGINAL_STOCK_DATE)
            .modifiedStockDate(UPDATED_MODIFIED_STOCK_DATE);

        restItemsMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItems)))
            .andExpect(status().isOk());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeUpdate);
        Items testItems = itemsList.get(itemsList.size() - 1);
        assertThat(testItems.getItemCode()).isEqualTo(UPDATED_ITEM_CODE);
        assertThat(testItems.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testItems.getItemDescription()).isEqualTo(UPDATED_ITEM_DESCRIPTION);
        assertThat(testItems.getItemPrice()).isEqualTo(UPDATED_ITEM_PRICE);
        assertThat(testItems.getItemSerial()).isEqualTo(UPDATED_ITEM_SERIAL);
        assertThat(testItems.getItemSupplierSerial()).isEqualTo(UPDATED_ITEM_SUPPLIER_SERIAL);
        assertThat(testItems.getItemCost()).isEqualTo(UPDATED_ITEM_COST);
        assertThat(testItems.getItemSalePrice()).isEqualTo(UPDATED_ITEM_SALE_PRICE);
        assertThat(testItems.getOriginalStockDate()).isEqualTo(UPDATED_ORIGINAL_STOCK_DATE);
        assertThat(testItems.getModifiedStockDate()).isEqualTo(UPDATED_MODIFIED_STOCK_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingItems() throws Exception {
        int databaseSizeBeforeUpdate = itemsRepository.findAll().size();

        // Create the Items

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemsMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItems() throws Exception {
        // Initialize the database
        itemsService.save(items);

        int databaseSizeBeforeDelete = itemsRepository.findAll().size();

        // Delete the items
        restItemsMockMvc.perform(delete("/api/items/{id}", items.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Items.class);
        Items items1 = new Items();
        items1.setId(1L);
        Items items2 = new Items();
        items2.setId(items1.getId());
        assertThat(items1).isEqualTo(items2);
        items2.setId(2L);
        assertThat(items1).isNotEqualTo(items2);
        items1.setId(null);
        assertThat(items1).isNotEqualTo(items2);
    }
}
