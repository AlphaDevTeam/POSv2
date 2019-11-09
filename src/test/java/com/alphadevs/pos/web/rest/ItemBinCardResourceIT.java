package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.ItemBinCard;
import com.alphadevs.pos.repository.ItemBinCardRepository;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;

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

    private static final String DEFAULT_TRANSACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_TRANSACTION_QTY = 1D;
    private static final Double UPDATED_TRANSACTION_QTY = 2D;

    private static final Double DEFAULT_TRANSACTION_BALANCE = 1D;
    private static final Double UPDATED_TRANSACTION_BALANCE = 2D;

    @Autowired
    private ItemBinCardRepository itemBinCardRepository;

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
        final ItemBinCardResource itemBinCardResource = new ItemBinCardResource(itemBinCardRepository);
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
    public void getNonExistingItemBinCard() throws Exception {
        // Get the itemBinCard
        restItemBinCardMockMvc.perform(get("/api/item-bin-cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemBinCard() throws Exception {
        // Initialize the database
        itemBinCardRepository.saveAndFlush(itemBinCard);

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
        itemBinCardRepository.saveAndFlush(itemBinCard);

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
