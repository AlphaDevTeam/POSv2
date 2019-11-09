package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.GoodsReceipt;
import com.alphadevs.pos.repository.GoodsReceiptRepository;
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
 * Integration tests for the {@link GoodsReceiptResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class GoodsReceiptResourceIT {

    private static final String DEFAULT_GRN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_GRN_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GRN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GRN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PO_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PO_NUMBER = "BBBBBBBBBB";

    @Autowired
    private GoodsReceiptRepository goodsReceiptRepository;

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
        final GoodsReceiptResource goodsReceiptResource = new GoodsReceiptResource(goodsReceiptRepository);
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
    public void getNonExistingGoodsReceipt() throws Exception {
        // Get the goodsReceipt
        restGoodsReceiptMockMvc.perform(get("/api/goods-receipts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoodsReceipt() throws Exception {
        // Initialize the database
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

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
        goodsReceiptRepository.saveAndFlush(goodsReceipt);

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
