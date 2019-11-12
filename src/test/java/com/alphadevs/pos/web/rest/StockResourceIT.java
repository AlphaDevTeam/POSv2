package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.Stock;
import com.alphadevs.pos.domain.Items;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.domain.Company;
import com.alphadevs.pos.repository.StockRepository;
import com.alphadevs.pos.service.StockService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.StockCriteria;
import com.alphadevs.pos.service.StockQueryService;

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
 * Integration tests for the {@link StockResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class StockResourceIT {

    private static final Double DEFAULT_STOCK_QTY = 1D;
    private static final Double UPDATED_STOCK_QTY = 2D;
    private static final Double SMALLER_STOCK_QTY = 1D - 1D;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockQueryService stockQueryService;

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

    private MockMvc restStockMockMvc;

    private Stock stock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockResource stockResource = new StockResource(stockService, stockQueryService);
        this.restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
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
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock()
            .stockQty(DEFAULT_STOCK_QTY);
        return stock;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createUpdatedEntity(EntityManager em) {
        Stock stock = new Stock()
            .stockQty(UPDATED_STOCK_QTY);
        return stock;
    }

    @BeforeEach
    public void initTest() {
        stock = createEntity(em);
    }

    @Test
    @Transactional
    public void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getStockQty()).isEqualTo(DEFAULT_STOCK_QTY);
    }

    @Test
    @Transactional
    public void createStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock with an existing ID
        stock.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStockQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setStockQty(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].stockQty").value(hasItem(DEFAULT_STOCK_QTY.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.stockQty").value(DEFAULT_STOCK_QTY.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllStocksByStockQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockQty equals to DEFAULT_STOCK_QTY
        defaultStockShouldBeFound("stockQty.equals=" + DEFAULT_STOCK_QTY);

        // Get all the stockList where stockQty equals to UPDATED_STOCK_QTY
        defaultStockShouldNotBeFound("stockQty.equals=" + UPDATED_STOCK_QTY);
    }

    @Test
    @Transactional
    public void getAllStocksByStockQtyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockQty not equals to DEFAULT_STOCK_QTY
        defaultStockShouldNotBeFound("stockQty.notEquals=" + DEFAULT_STOCK_QTY);

        // Get all the stockList where stockQty not equals to UPDATED_STOCK_QTY
        defaultStockShouldBeFound("stockQty.notEquals=" + UPDATED_STOCK_QTY);
    }

    @Test
    @Transactional
    public void getAllStocksByStockQtyIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockQty in DEFAULT_STOCK_QTY or UPDATED_STOCK_QTY
        defaultStockShouldBeFound("stockQty.in=" + DEFAULT_STOCK_QTY + "," + UPDATED_STOCK_QTY);

        // Get all the stockList where stockQty equals to UPDATED_STOCK_QTY
        defaultStockShouldNotBeFound("stockQty.in=" + UPDATED_STOCK_QTY);
    }

    @Test
    @Transactional
    public void getAllStocksByStockQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockQty is not null
        defaultStockShouldBeFound("stockQty.specified=true");

        // Get all the stockList where stockQty is null
        defaultStockShouldNotBeFound("stockQty.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByStockQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockQty is greater than or equal to DEFAULT_STOCK_QTY
        defaultStockShouldBeFound("stockQty.greaterThanOrEqual=" + DEFAULT_STOCK_QTY);

        // Get all the stockList where stockQty is greater than or equal to UPDATED_STOCK_QTY
        defaultStockShouldNotBeFound("stockQty.greaterThanOrEqual=" + UPDATED_STOCK_QTY);
    }

    @Test
    @Transactional
    public void getAllStocksByStockQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockQty is less than or equal to DEFAULT_STOCK_QTY
        defaultStockShouldBeFound("stockQty.lessThanOrEqual=" + DEFAULT_STOCK_QTY);

        // Get all the stockList where stockQty is less than or equal to SMALLER_STOCK_QTY
        defaultStockShouldNotBeFound("stockQty.lessThanOrEqual=" + SMALLER_STOCK_QTY);
    }

    @Test
    @Transactional
    public void getAllStocksByStockQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockQty is less than DEFAULT_STOCK_QTY
        defaultStockShouldNotBeFound("stockQty.lessThan=" + DEFAULT_STOCK_QTY);

        // Get all the stockList where stockQty is less than UPDATED_STOCK_QTY
        defaultStockShouldBeFound("stockQty.lessThan=" + UPDATED_STOCK_QTY);
    }

    @Test
    @Transactional
    public void getAllStocksByStockQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockQty is greater than DEFAULT_STOCK_QTY
        defaultStockShouldNotBeFound("stockQty.greaterThan=" + DEFAULT_STOCK_QTY);

        // Get all the stockList where stockQty is greater than SMALLER_STOCK_QTY
        defaultStockShouldBeFound("stockQty.greaterThan=" + SMALLER_STOCK_QTY);
    }


    @Test
    @Transactional
    public void getAllStocksByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);
        Items item = ItemsResourceIT.createEntity(em);
        em.persist(item);
        em.flush();
        stock.setItem(item);
        stockRepository.saveAndFlush(stock);
        Long itemId = item.getId();

        // Get all the stockList where item equals to itemId
        defaultStockShouldBeFound("itemId.equals=" + itemId);

        // Get all the stockList where item equals to itemId + 1
        defaultStockShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }


    @Test
    @Transactional
    public void getAllStocksByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        stock.setLocation(location);
        stockRepository.saveAndFlush(stock);
        Long locationId = location.getId();

        // Get all the stockList where location equals to locationId
        defaultStockShouldBeFound("locationId.equals=" + locationId);

        // Get all the stockList where location equals to locationId + 1
        defaultStockShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllStocksByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);
        Company company = CompanyResourceIT.createEntity(em);
        em.persist(company);
        em.flush();
        stock.setCompany(company);
        stockRepository.saveAndFlush(stock);
        Long companyId = company.getId();

        // Get all the stockList where company equals to companyId
        defaultStockShouldBeFound("companyId.equals=" + companyId);

        // Get all the stockList where company equals to companyId + 1
        defaultStockShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStockShouldBeFound(String filter) throws Exception {
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].stockQty").value(hasItem(DEFAULT_STOCK_QTY.doubleValue())));

        // Check, that the count call also returns 1
        restStockMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStockShouldNotBeFound(String filter) throws Exception {
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = stockRepository.findById(stock.getId()).get();
        // Disconnect from session so that the updates on updatedStock are not directly saved in db
        em.detach(updatedStock);
        updatedStock
            .stockQty(UPDATED_STOCK_QTY);

        restStockMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStock)))
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getStockQty()).isEqualTo(UPDATED_STOCK_QTY);
    }

    @Test
    @Transactional
    public void updateNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Create the Stock

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Delete the stock
        restStockMockMvc.perform(delete("/api/stocks/{id}", stock.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stock.class);
        Stock stock1 = new Stock();
        stock1.setId(1L);
        Stock stock2 = new Stock();
        stock2.setId(stock1.getId());
        assertThat(stock1).isEqualTo(stock2);
        stock2.setId(2L);
        assertThat(stock1).isNotEqualTo(stock2);
        stock1.setId(null);
        assertThat(stock1).isNotEqualTo(stock2);
    }
}
