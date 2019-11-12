package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.CashBook;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.domain.TransactionType;
import com.alphadevs.pos.repository.CashBookRepository;
import com.alphadevs.pos.service.CashBookService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.dto.CashBookCriteria;
import com.alphadevs.pos.service.CashBookQueryService;

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
 * Integration tests for the {@link CashBookResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class CashBookResourceIT {

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TRANSACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_TRANSACTION_AMOUNT_DR = 1D;
    private static final Double UPDATED_TRANSACTION_AMOUNT_DR = 2D;
    private static final Double SMALLER_TRANSACTION_AMOUNT_DR = 1D - 1D;

    private static final Double DEFAULT_TRANSACTION_AMOUNT_CR = 1D;
    private static final Double UPDATED_TRANSACTION_AMOUNT_CR = 2D;
    private static final Double SMALLER_TRANSACTION_AMOUNT_CR = 1D - 1D;

    private static final Double DEFAULT_TRANSACTION_BALANCE = 1D;
    private static final Double UPDATED_TRANSACTION_BALANCE = 2D;
    private static final Double SMALLER_TRANSACTION_BALANCE = 1D - 1D;

    @Autowired
    private CashBookRepository cashBookRepository;

    @Autowired
    private CashBookService cashBookService;

    @Autowired
    private CashBookQueryService cashBookQueryService;

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

    private MockMvc restCashBookMockMvc;

    private CashBook cashBook;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CashBookResource cashBookResource = new CashBookResource(cashBookService, cashBookQueryService);
        this.restCashBookMockMvc = MockMvcBuilders.standaloneSetup(cashBookResource)
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
    public static CashBook createEntity(EntityManager em) {
        CashBook cashBook = new CashBook()
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionDescription(DEFAULT_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(DEFAULT_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(DEFAULT_TRANSACTION_AMOUNT_CR)
            .transactionBalance(DEFAULT_TRANSACTION_BALANCE);
        return cashBook;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashBook createUpdatedEntity(EntityManager em) {
        CashBook cashBook = new CashBook()
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);
        return cashBook;
    }

    @BeforeEach
    public void initTest() {
        cashBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createCashBook() throws Exception {
        int databaseSizeBeforeCreate = cashBookRepository.findAll().size();

        // Create the CashBook
        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isCreated());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeCreate + 1);
        CashBook testCashBook = cashBookList.get(cashBookList.size() - 1);
        assertThat(testCashBook.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCashBook.getTransactionDescription()).isEqualTo(DEFAULT_TRANSACTION_DESCRIPTION);
        assertThat(testCashBook.getTransactionAmountDR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_DR);
        assertThat(testCashBook.getTransactionAmountCR()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT_CR);
        assertThat(testCashBook.getTransactionBalance()).isEqualTo(DEFAULT_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void createCashBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashBookRepository.findAll().size();

        // Create the CashBook with an existing ID
        cashBook.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setTransactionDate(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setTransactionDescription(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountDRIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setTransactionAmountDR(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionAmountCRIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setTransactionAmountCR(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashBookRepository.findAll().size();
        // set the field null
        cashBook.setTransactionBalance(null);

        // Create the CashBook, which fails.

        restCashBookMockMvc.perform(post("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCashBooks() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList
        restCashBookMockMvc.perform(get("/api/cash-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCashBook() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get the cashBook
        restCashBookMockMvc.perform(get("/api/cash-books/{id}", cashBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cashBook.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionDescription").value(DEFAULT_TRANSACTION_DESCRIPTION))
            .andExpect(jsonPath("$.transactionAmountDR").value(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue()))
            .andExpect(jsonPath("$.transactionAmountCR").value(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue()))
            .andExpect(jsonPath("$.transactionBalance").value(DEFAULT_TRANSACTION_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is not null
        defaultCashBookShouldBeFound("transactionDate.specified=true");

        // Get all the cashBookList where transactionDate is null
        defaultCashBookShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultCashBookShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cashBookList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultCashBookShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllCashBooksByTransactionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDescription equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashBookShouldBeFound("transactionDescription.equals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashBookList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashBookShouldNotBeFound("transactionDescription.equals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDescription not equals to DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashBookShouldNotBeFound("transactionDescription.notEquals=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashBookList where transactionDescription not equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashBookShouldBeFound("transactionDescription.notEquals=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDescription in DEFAULT_TRANSACTION_DESCRIPTION or UPDATED_TRANSACTION_DESCRIPTION
        defaultCashBookShouldBeFound("transactionDescription.in=" + DEFAULT_TRANSACTION_DESCRIPTION + "," + UPDATED_TRANSACTION_DESCRIPTION);

        // Get all the cashBookList where transactionDescription equals to UPDATED_TRANSACTION_DESCRIPTION
        defaultCashBookShouldNotBeFound("transactionDescription.in=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDescription is not null
        defaultCashBookShouldBeFound("transactionDescription.specified=true");

        // Get all the cashBookList where transactionDescription is null
        defaultCashBookShouldNotBeFound("transactionDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllCashBooksByTransactionDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDescription contains DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashBookShouldBeFound("transactionDescription.contains=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashBookList where transactionDescription contains UPDATED_TRANSACTION_DESCRIPTION
        defaultCashBookShouldNotBeFound("transactionDescription.contains=" + UPDATED_TRANSACTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionDescription does not contain DEFAULT_TRANSACTION_DESCRIPTION
        defaultCashBookShouldNotBeFound("transactionDescription.doesNotContain=" + DEFAULT_TRANSACTION_DESCRIPTION);

        // Get all the cashBookList where transactionDescription does not contain UPDATED_TRANSACTION_DESCRIPTION
        defaultCashBookShouldBeFound("transactionDescription.doesNotContain=" + UPDATED_TRANSACTION_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountDRIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.equals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.equals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountDRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR not equals to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR not equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountDRIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR in DEFAULT_TRANSACTION_AMOUNT_DR or UPDATED_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.in=" + DEFAULT_TRANSACTION_AMOUNT_DR + "," + UPDATED_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR equals to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.in=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountDRIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR is not null
        defaultCashBookShouldBeFound("transactionAmountDR.specified=true");

        // Get all the cashBookList where transactionAmountDR is null
        defaultCashBookShouldNotBeFound("transactionAmountDR.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountDRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountDRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR is less than or equal to SMALLER_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountDRIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR is less than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR is less than UPDATED_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_DR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountDRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountDR is greater than DEFAULT_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldNotBeFound("transactionAmountDR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_DR);

        // Get all the cashBookList where transactionAmountDR is greater than SMALLER_TRANSACTION_AMOUNT_DR
        defaultCashBookShouldBeFound("transactionAmountDR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_DR);
    }


    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountCRIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.equals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.equals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountCRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR not equals to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.notEquals=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR not equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.notEquals=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountCRIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR in DEFAULT_TRANSACTION_AMOUNT_CR or UPDATED_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.in=" + DEFAULT_TRANSACTION_AMOUNT_CR + "," + UPDATED_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR equals to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.in=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountCRIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR is not null
        defaultCashBookShouldBeFound("transactionAmountCR.specified=true");

        // Get all the cashBookList where transactionAmountCR is null
        defaultCashBookShouldNotBeFound("transactionAmountCR.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountCRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR is greater than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR is greater than or equal to UPDATED_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountCRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR is less than or equal to DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR is less than or equal to SMALLER_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountCRIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR is less than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.lessThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR is less than UPDATED_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.lessThan=" + UPDATED_TRANSACTION_AMOUNT_CR);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionAmountCRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionAmountCR is greater than DEFAULT_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldNotBeFound("transactionAmountCR.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT_CR);

        // Get all the cashBookList where transactionAmountCR is greater than SMALLER_TRANSACTION_AMOUNT_CR
        defaultCashBookShouldBeFound("transactionAmountCR.greaterThan=" + SMALLER_TRANSACTION_AMOUNT_CR);
    }


    @Test
    @Transactional
    public void getAllCashBooksByTransactionBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance equals to DEFAULT_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.equals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.equals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance not equals to DEFAULT_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.notEquals=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance not equals to UPDATED_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.notEquals=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance in DEFAULT_TRANSACTION_BALANCE or UPDATED_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.in=" + DEFAULT_TRANSACTION_BALANCE + "," + UPDATED_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance equals to UPDATED_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.in=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance is not null
        defaultCashBookShouldBeFound("transactionBalance.specified=true");

        // Get all the cashBookList where transactionBalance is null
        defaultCashBookShouldNotBeFound("transactionBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance is greater than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.greaterThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance is greater than or equal to UPDATED_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.greaterThanOrEqual=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance is less than or equal to DEFAULT_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.lessThanOrEqual=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance is less than or equal to SMALLER_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.lessThanOrEqual=" + SMALLER_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance is less than DEFAULT_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.lessThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance is less than UPDATED_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.lessThan=" + UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void getAllCashBooksByTransactionBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);

        // Get all the cashBookList where transactionBalance is greater than DEFAULT_TRANSACTION_BALANCE
        defaultCashBookShouldNotBeFound("transactionBalance.greaterThan=" + DEFAULT_TRANSACTION_BALANCE);

        // Get all the cashBookList where transactionBalance is greater than SMALLER_TRANSACTION_BALANCE
        defaultCashBookShouldBeFound("transactionBalance.greaterThan=" + SMALLER_TRANSACTION_BALANCE);
    }


    @Test
    @Transactional
    public void getAllCashBooksByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        cashBook.setLocation(location);
        cashBookRepository.saveAndFlush(cashBook);
        Long locationId = location.getId();

        // Get all the cashBookList where location equals to locationId
        defaultCashBookShouldBeFound("locationId.equals=" + locationId);

        // Get all the cashBookList where location equals to locationId + 1
        defaultCashBookShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllCashBooksByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cashBookRepository.saveAndFlush(cashBook);
        TransactionType transactionType = TransactionTypeResourceIT.createEntity(em);
        em.persist(transactionType);
        em.flush();
        cashBook.setTransactionType(transactionType);
        cashBookRepository.saveAndFlush(cashBook);
        Long transactionTypeId = transactionType.getId();

        // Get all the cashBookList where transactionType equals to transactionTypeId
        defaultCashBookShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the cashBookList where transactionType equals to transactionTypeId + 1
        defaultCashBookShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCashBookShouldBeFound(String filter) throws Exception {
        restCashBookMockMvc.perform(get("/api/cash-books?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDescription").value(hasItem(DEFAULT_TRANSACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].transactionAmountDR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_DR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionAmountCR").value(hasItem(DEFAULT_TRANSACTION_AMOUNT_CR.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionBalance").value(hasItem(DEFAULT_TRANSACTION_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restCashBookMockMvc.perform(get("/api/cash-books/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCashBookShouldNotBeFound(String filter) throws Exception {
        restCashBookMockMvc.perform(get("/api/cash-books?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCashBookMockMvc.perform(get("/api/cash-books/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCashBook() throws Exception {
        // Get the cashBook
        restCashBookMockMvc.perform(get("/api/cash-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCashBook() throws Exception {
        // Initialize the database
        cashBookService.save(cashBook);

        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();

        // Update the cashBook
        CashBook updatedCashBook = cashBookRepository.findById(cashBook.getId()).get();
        // Disconnect from session so that the updates on updatedCashBook are not directly saved in db
        em.detach(updatedCashBook);
        updatedCashBook
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionDescription(UPDATED_TRANSACTION_DESCRIPTION)
            .transactionAmountDR(UPDATED_TRANSACTION_AMOUNT_DR)
            .transactionAmountCR(UPDATED_TRANSACTION_AMOUNT_CR)
            .transactionBalance(UPDATED_TRANSACTION_BALANCE);

        restCashBookMockMvc.perform(put("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCashBook)))
            .andExpect(status().isOk());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
        CashBook testCashBook = cashBookList.get(cashBookList.size() - 1);
        assertThat(testCashBook.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCashBook.getTransactionDescription()).isEqualTo(UPDATED_TRANSACTION_DESCRIPTION);
        assertThat(testCashBook.getTransactionAmountDR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_DR);
        assertThat(testCashBook.getTransactionAmountCR()).isEqualTo(UPDATED_TRANSACTION_AMOUNT_CR);
        assertThat(testCashBook.getTransactionBalance()).isEqualTo(UPDATED_TRANSACTION_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingCashBook() throws Exception {
        int databaseSizeBeforeUpdate = cashBookRepository.findAll().size();

        // Create the CashBook

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashBookMockMvc.perform(put("/api/cash-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cashBook)))
            .andExpect(status().isBadRequest());

        // Validate the CashBook in the database
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCashBook() throws Exception {
        // Initialize the database
        cashBookService.save(cashBook);

        int databaseSizeBeforeDelete = cashBookRepository.findAll().size();

        // Delete the cashBook
        restCashBookMockMvc.perform(delete("/api/cash-books/{id}", cashBook.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashBook> cashBookList = cashBookRepository.findAll();
        assertThat(cashBookList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashBook.class);
        CashBook cashBook1 = new CashBook();
        cashBook1.setId(1L);
        CashBook cashBook2 = new CashBook();
        cashBook2.setId(cashBook1.getId());
        assertThat(cashBook1).isEqualTo(cashBook2);
        cashBook2.setId(2L);
        assertThat(cashBook1).isNotEqualTo(cashBook2);
        cashBook1.setId(null);
        assertThat(cashBook1).isNotEqualTo(cashBook2);
    }
}
