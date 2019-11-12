package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.ItemBinCard;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.domain.Items;
import com.alphadevs.pos.repository.ItemBinCardRepository;
import com.alphadevs.pos.service.ItemBinCardService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.ItemBinCardCriteria;
import com.alphadevs.pos.service.ItemBinCardQueryService;

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
 * Integration tests for the {@link ItemBinCardResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class ItemBinCardResourceIT {

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TRANSACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_TRANSACTION_QTY = 1D;
    private static final Double UPDATED_TRANSACTION_QTY = 2D;
    private static final Double SMALLER_TRANSACTION_QTY = 1D - 1D;

    private static final Double DEFAULT_TRANSACTION_BALANCE = 1D;
    private static final Double UPDATED_TRANSACTION_BALANCE = 2D;
    private static final Double SMALLER_TRANSACTION_BALANCE = 1D - 1D;

    @Autowired
    private ItemBinCardRepository itemBinCardRepository;

    @Autowired
    private ItemBinCardService itemBinCardService;

    @Autowired
    private ItemBinCardQueryService itemBinCardQueryService;

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

    private MockMvc restItemBinCardMockMvc;

    private ItemBinCard itemBinCard;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemBinCardResource itemBinCardResource = new ItemBinCardResource(itemBinCardService, itemBinCardQueryService);
        this.restItemBinCardMockMvc = MockMvcBuilders.standaloneSetup(itemBinCardResource)
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
    public static ItemBinCard createEntity(EntityManager em) {
        ItemBinCard itemBinCard = new ItemBinCard()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionQty(DEFAULT_TRANSACTION_QTY)
            .transactionBalance(DEFAULT_TRANSACTION_BALANCE);
        return itemBinCard;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemBinCard createUpdatedEntity(EntityManager em) {
        ItemBinCard itemBinCard = new ItemBinCard()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionQty(UPDATED_TRANSACTION_QTY)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);
        return itemBinCard;
    }

    @BeforeEach
    public void initTest() {
        itemBinCard = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemBinCard() throws Exception {
        int databaseSizeBeforeCreate = itemBinCardRepository.findAll().size();

        // Create the ItemBinCard
        restItemBinCardMockMvc.perform(post("/api/item-bin-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemBinCard)))
            .andExpect(status().isCreated());

        // Validate the ItemBinCard in the database
        List<ItemBinCard> itemBinCardList = itemBinCardRepository.findAll();
        assertThat(itemBinCardList).hasSize(databaseSizeBeforeCreate + 1);
        ItemBinCard testItemBinCard = itemBinCardList.get(itemBinCardList.size() - 1);
        assertThat(testItemBinCard.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testItemBinCard.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testItemBinCard.getTransactionQty()).isEqualTo(DEFAULT_TRANSACTION_QTY);
        assertThat(testItemBinCard.getTransactionBalance()).isEqualTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void createItemBinCardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemBinCardRepository.findAll().size();

        // Create the ItemBinCard with an existing ID
        itemBinCard.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemBinCardMockMvc.perform(post("/api/item-bin-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemBinCard)))
            .andExpect(status().isBadRequest());

        // Validate the ItemBinCard in the database
        List<ItemBinCard> itemBinCardList = itemBinCardRepository.findAll();
        assertThat(itemBinCardList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemBinCardRepository.findAll().size();
        // set the field null
        itemBinCard.setTransactionDate(null);

        // Create the ItemBinCard, which fails.

        restItemBinCardMockMvc.perform(post("/api/item-bin-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemBinCard)))
            .andExpect(status().isBadRequest());

        List<ItemBinCard> itemBinCardList = itemBinCardRepository.findAll();
        assertThat(itemBinCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemBinCardRepository.findAll().size();
        // set the field null
        itemBinCard.setTransactionDescription(null);

        // Create the ItemBinCard, which fails.

        restItemBinCardMockMvc.perform(post("/api/item-bin-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemBinCard)))
            .andExpect(status().isBadRequest());

        List<ItemBinCard> itemBinCardList = itemBinCardRepository.findAll();
        assertThat(itemBinCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemBinCardRepository.findAll().size();
        // set the field null
        itemBinCard.setTransactionQty(null);

        // Create the ItemBinCard, which fails.

        restItemBinCardMockMvc.perform(post("/api/item-bin-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemBinCard)))
            .andExpect(status().isBadRequest());

        List<ItemBinCard> itemBinCardList = itemBinCardRepository.findAll();
        assertThat(itemBinCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemBinCardRepository.findAll().size();
        // set the field null
        itemBinCard.setTransactionBalance(null);

        // Create the ItemBinCard, which fails.

        restItemBinCardMockMvc.perform(post("/api/item-bin-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemBinCard)))
            .andExpect(status().isBadRequest());

        List<ItemBinCard> itemBinCardList = itemBinCardRepository.findAll();
        assertThat(itemBinCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemBinCards() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList
        restItemBinCardMockMvc.perform(get("/api/item-bin-cards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemBinCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionQty").value(hasItem(DEFAULT_TRANSACTION_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getItemBinCard() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get the itemBinCard
        restItemBinCardMockMvc.perform(get("/api/item-bin-cards/{id}", itemBinCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemBinCard.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionQty").value(DEFAULT_TRANSACTION_QTY.doubleValue()))
            .andExpect(jsonPath("$.transactionBalance").value(DEFAULT_TRANSACTION_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultItemBinCardShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the itemBinCardList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultItemBinCardShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultItemBinCardShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the itemBinCardList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultItemBinCardShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultItemBinCardShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the itemBinCardList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultItemBinCardShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDate is not null
        defaultItemBinCardShouldBeFound("transactionDate.specified=true");

        // Get all the itemBinCardList where transactionDate is null
        defaultItemBinCardShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultItemBinCardShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the itemBinCardList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultItemBinCardShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultItemBinCardShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the itemBinCardList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultItemBinCardShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultItemBinCardShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the itemBinCardList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultItemBinCardShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultItemBinCardShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the itemBinCardList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultItemBinCardShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultItemBinCardShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the itemBinCardList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultItemBinCardShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultItemBinCardShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the itemBinCardList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultItemBinCardShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultItemBinCardShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the itemBinCardList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultItemBinCardShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDescription is not null
        defaultItemBinCardShouldBeFound("transactionDescription.specified=true");

        // Get all the itemBinCardList where transactionDescription is null
        defaultItemBinCardShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultItemBinCardShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the itemBinCardList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultItemBinCardShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultItemBinCardShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the itemBinCardList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultItemBinCardShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionQty equals to DEFAULT_TRANSACTION_QTY
        defaultItemBinCardShouldBeFound("transactionQty.equals=" + DEFAULT_TRANSACTION_QTY);

        // Get all the itemBinCardList where transactionQty equals to UPDATED_TRANSACTION_QTY
        defaultItemBinCardShouldNotBeFound("transactionQty.equals=" + UPDATED_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionQtyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionQty not equals to DEFAULT_TRANSACTION_QTY
        defaultItemBinCardShouldNotBeFound("transactionQty.notEquals=" + DEFAULT_TRANSACTION_QTY);

        // Get all the itemBinCardList where transactionQty not equals to UPDATED_TRANSACTION_QTY
        defaultItemBinCardShouldBeFound("transactionQty.notEquals=" + UPDATED_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionQtyIsInShouldWork() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionQty in DEFAULT_TRANSACTION_QTY or UPDATED_TRANSACTION_QTY
        defaultItemBinCardShouldBeFound("transactionQty.in=" + DEFAULT_TRANSACTION_QTY + "," + UPDATED_TRANSACTION_QTY);

        // Get all the itemBinCardList where transactionQty equals to UPDATED_TRANSACTION_QTY
        defaultItemBinCardShouldNotBeFound("transactionQty.in=" + UPDATED_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionQty is not null
        defaultItemBinCardShouldBeFound("transactionQty.specified=true");

        // Get all the itemBinCardList where transactionQty is null
        defaultItemBinCardShouldNotBeFound("transactionQty.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionQty is greater than or equal to DEFAULT_TRANSACTION_QTY
        defaultItemBinCardShouldBeFound("transactionQty.greaterThanOrEqual=" + DEFAULT_TRANSACTION_QTY);

        // Get all the itemBinCardList where transactionQty is greater than or equal to UPDATED_TRANSACTION_QTY
        defaultItemBinCardShouldNotBeFound("transactionQty.greaterThanOrEqual=" + UPDATED_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionQty is less than or equal to DEFAULT_TRANSACTION_QTY
        defaultItemBinCardShouldBeFound("transactionQty.lessThanOrEqual=" + DEFAULT_TRANSACTION_QTY);

        // Get all the itemBinCardList where transactionQty is less than or equal to SMALLER_TRANSACTION_QTY
        defaultItemBinCardShouldNotBeFound("transactionQty.lessThanOrEqual=" + SMALLER_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionQty is less than DEFAULT_TRANSACTION_QTY
        defaultItemBinCardShouldNotBeFound("transactionQty.lessThan=" + DEFAULT_TRANSACTION_QTY);

        // Get all the itemBinCardList where transactionQty is less than UPDATED_TRANSACTION_QTY
        defaultItemBinCardShouldBeFound("transactionQty.lessThan=" + UPDATED_TRANSACTION_QTY);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionQty is greater than DEFAULT_TRANSACTION_QTY
        defaultItemBinCardShouldNotBeFound("transactionQty.greaterThan=" + DEFAULT_TRANSACTION_QTY);

        // Get all the itemBinCardList where transactionQty is greater than SMALLER_TRANSACTION_QTY
        defaultItemBinCardShouldBeFound("transactionQty.greaterThan=" + SMALLER_TRANSACTION_QTY);
    }


    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultItemBinCardShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the itemBinCardList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultItemBinCardShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionBalance not equals to DEFAULT_TRANSACTION_BALANCE
        defaultItemBinCardShouldNotBeFound("transactionBalance.notEquals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the itemBinCardList where transactionBalance not equals to UPDATED_TRANSACTION_BALANCE
        defaultItemBinCardShouldBeFound("transactionBalance.notEquals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultItemBinCardShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the itemBinCardList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultItemBinCardShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionBalance is not null
        defaultItemBinCardShouldBeFound("transactionBalance.specified=true");

        // Get all the itemBinCardList where transactionBalance is null
        defaultItemBinCardShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultItemBinCardShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the itemBinCardList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultItemBinCardShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultItemBinCardShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the itemBinCardList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultItemBinCardShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultItemBinCardShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the itemBinCardList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultItemBinCardShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllItemBinCardsByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

        // Get all the itemBinCardList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultItemBinCardShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the itemBinCardList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultItemBinCardShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }


    @Test
    @Transactional
    public void getAllItemBinCardsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        itemBinCard.setLocation(location);
        itemBinCardRepository.saveAndFlush(itemBinCard);
        Long locationId = location.getId();

        // Get all the itemBinCardList where location equals to locationId
        defaultItemBinCardShouldBeFound("locationId.equals=" + locationId);

        // Get all the itemBinCardList where location equals to locationId + 1
        defaultItemBinCardShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllItemBinCardsByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);
        Items item = ItemsResourceIT.createEntity(em);
        em.persist(item);
        em.flush();
        itemBinCard.setItem(item);
        itemBinCardRepository.saveAndFlush(itemBinCard);
        Long itemId = item.getId();

        // Get all the itemBinCardList where item equals to itemId
        defaultItemBinCardShouldBeFound("itemId.equals=" + itemId);

        // Get all the itemBinCardList where item equals to itemId + 1
        defaultItemBinCardShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemBinCardShouldBeFound(String filter) throws Exception {
        restItemBinCardMockMvc.perform(get("/api/item-bin-cards?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemBinCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionQty").value(hasItem(DEFAULT_TRANSACTION_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restItemBinCardMockMvc.perform(get("/api/item-bin-cards/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemBinCardShouldNotBeFound(String filter) throws Exception {
        restItemBinCardMockMvc.perform(get("/api/item-bin-cards?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemBinCardMockMvc.perform(get("/api/item-bin-cards/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingItemBinCard() throws Exception {
        // Get the itemBinCard
        restItemBinCardMockMvc.perform(get("/api/item-bin-cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemBinCard() throws Exception {
        // Initialize the database
        itemBinCardService.save(itemBinCard);

        int databaseSizeBeforeUpdate = itemBinCardRepository.findAll().size();

        // Update the itemBinCard
        ItemBinCard updatedItemBinCard = itemBinCardRepository.findById(itemBinCard.getId()).get();
        // Disconnect from session so that the updates on updatedItemBinCard are not directly saved in db
        em.detach(updatedItemBinCard);
        updatedItemBinCard
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionQty(UPDATED_TRANSACTION_QTY)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restItemBinCardMockMvc.perform(put("/api/item-bin-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItemBinCard)))
            .andExpect(status().isOk());

        // Validate the ItemBinCard in the database
        List<ItemBinCard> itemBinCardList = itemBinCardRepository.findAll();
        assertThat(itemBinCardList).hasSize(databaseSizeBeforeUpdate);
        ItemBinCard testItemBinCard = itemBinCardList.get(itemBinCardList.size() - 1);
        assertThat(testItemBinCard.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testItemBinCard.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testItemBinCard.getTransactionQty()).isEqualTo(UPDATED_TRANSACTION_QTY);
        assertThat(testItemBinCard.getTransactionBalance()).isEqualTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingItemBinCard() throws Exception {
        int databaseSizeBeforeUpdate = itemBinCardRepository.findAll().size();

        // Create the ItemBinCard

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemBinCardMockMvc.perform(put("/api/item-bin-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemBinCard)))
            .andExpect(status().isBadRequest());

        // Validate the ItemBinCard in the database
        List<ItemBinCard> itemBinCardList = itemBinCardRepository.findAll();
        assertThat(itemBinCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItemBinCard() throws Exception {
        // Initialize the database
        itemBinCardService.save(itemBinCard);

        int databaseSizeBeforeDelete = itemBinCardRepository.findAll().size();

        // Delete the itemBinCard
        restItemBinCardMockMvc.perform(delete("/api/item-bin-cards/{id}", itemBinCard.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemBinCard> itemBinCardList = itemBinCardRepository.findAll();
        assertThat(itemBinCardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemBinCard.class);
        ItemBinCard itemBinCard1 = new ItemBinCard();
        itemBinCard1.setId(1L);
        ItemBinCard itemBinCard2 = new ItemBinCard();
        itemBinCard2.setId(itemBinCard1.getId());
        assertThat(itemBinCard1).isEqualTo(itemBinCard2);
        itemBinCard2.setId(2L);
        assertThat(itemBinCard1).isNotEqualTo(itemBinCard2);
        itemBinCard1.setId(null);
        assertThat(itemBinCard1).isNotEqualTo(itemBinCard2);
    }
}
