package com.alphadevs.pos.web.rest;

import com.alphadevs.pos.PoSv2App;
import com.alphadevs.pos.domain.Location;
import com.alphadevs.pos.domain.Company;
import com.alphadevs.pos.domain.ExUser;
import com.alphadevs.pos.repository.LocationRepository;
import com.alphadevs.pos.service.LocationService;
import com.alphadevs.pos.service.UserService;
import com.alphadevs.pos.web.rest.errors.ExceptionTranslator;
import com.alphadevs.pos.service.LocationQueryService;

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
 * Integration tests for the {@link LocationResource} REST controller.
 */
@SpringBootTest(classes = PoSv2App.class)
public class LocationResourceIT {

    private static final String DEFAULT_LOCATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_LOCATION_PROF_MARGIN = 1D;
    private static final Double UPDATED_LOCATION_PROF_MARGIN = 2D;
    private static final Double SMALLER_LOCATION_PROF_MARGIN = 1D - 1D;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationQueryService locationQueryService;

    @Autowired
    private UserService userService;

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

    private MockMvc restLocationMockMvc;

    private Location location;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LocationResource locationResource = new LocationResource(locationService, userService, locationQueryService);
        this.restLocationMockMvc = MockMvcBuilders.standaloneSetup(locationResource)
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
    public static Location createEntity(EntityManager em) {
        Location location = new Location()
            .locationCode(DEFAULT_LOCATION_CODE)
            .locationName(DEFAULT_LOCATION_NAME)
            .locationProfMargin(DEFAULT_LOCATION_PROF_MARGIN)
            .isActive(DEFAULT_IS_ACTIVE);
        return location;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createUpdatedEntity(EntityManager em) {
        Location location = new Location()
            .locationCode(UPDATED_LOCATION_CODE)
            .locationName(UPDATED_LOCATION_NAME)
            .locationProfMargin(UPDATED_LOCATION_PROF_MARGIN)
            .isActive(UPDATED_IS_ACTIVE);
        return location;
    }

    @BeforeEach
    public void initTest() {
        location = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location
        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getLocationCode()).isEqualTo(DEFAULT_LOCATION_CODE);
        assertThat(testLocation.getLocationName()).isEqualTo(DEFAULT_LOCATION_NAME);
        assertThat(testLocation.getLocationProfMargin()).isEqualTo(DEFAULT_LOCATION_PROF_MARGIN);
        assertThat(testLocation.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location with an existing ID
        location.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLocationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLocationCode(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLocationName(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationProfMarginIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLocationProfMargin(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].locationName").value(hasItem(DEFAULT_LOCATION_NAME)))
            .andExpect(jsonPath("$.[*].locationProfMargin").value(hasItem(DEFAULT_LOCATION_PROF_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.locationCode").value(DEFAULT_LOCATION_CODE))
            .andExpect(jsonPath("$.locationName").value(DEFAULT_LOCATION_NAME))
            .andExpect(jsonPath("$.locationProfMargin").value(DEFAULT_LOCATION_PROF_MARGIN.doubleValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode equals to DEFAULT_LOCATION_CODE
        defaultLocationShouldBeFound("locationCode.equals=" + DEFAULT_LOCATION_CODE);

        // Get all the locationList where locationCode equals to UPDATED_LOCATION_CODE
        defaultLocationShouldNotBeFound("locationCode.equals=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode not equals to DEFAULT_LOCATION_CODE
        defaultLocationShouldNotBeFound("locationCode.notEquals=" + DEFAULT_LOCATION_CODE);

        // Get all the locationList where locationCode not equals to UPDATED_LOCATION_CODE
        defaultLocationShouldBeFound("locationCode.notEquals=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode in DEFAULT_LOCATION_CODE or UPDATED_LOCATION_CODE
        defaultLocationShouldBeFound("locationCode.in=" + DEFAULT_LOCATION_CODE + "," + UPDATED_LOCATION_CODE);

        // Get all the locationList where locationCode equals to UPDATED_LOCATION_CODE
        defaultLocationShouldNotBeFound("locationCode.in=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode is not null
        defaultLocationShouldBeFound("locationCode.specified=true");

        // Get all the locationList where locationCode is null
        defaultLocationShouldNotBeFound("locationCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocationsByLocationCodeContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode contains DEFAULT_LOCATION_CODE
        defaultLocationShouldBeFound("locationCode.contains=" + DEFAULT_LOCATION_CODE);

        // Get all the locationList where locationCode contains UPDATED_LOCATION_CODE
        defaultLocationShouldNotBeFound("locationCode.contains=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationCode does not contain DEFAULT_LOCATION_CODE
        defaultLocationShouldNotBeFound("locationCode.doesNotContain=" + DEFAULT_LOCATION_CODE);

        // Get all the locationList where locationCode does not contain UPDATED_LOCATION_CODE
        defaultLocationShouldBeFound("locationCode.doesNotContain=" + UPDATED_LOCATION_CODE);
    }


    @Test
    @Transactional
    public void getAllLocationsByLocationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName equals to DEFAULT_LOCATION_NAME
        defaultLocationShouldBeFound("locationName.equals=" + DEFAULT_LOCATION_NAME);

        // Get all the locationList where locationName equals to UPDATED_LOCATION_NAME
        defaultLocationShouldNotBeFound("locationName.equals=" + UPDATED_LOCATION_NAME);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName not equals to DEFAULT_LOCATION_NAME
        defaultLocationShouldNotBeFound("locationName.notEquals=" + DEFAULT_LOCATION_NAME);

        // Get all the locationList where locationName not equals to UPDATED_LOCATION_NAME
        defaultLocationShouldBeFound("locationName.notEquals=" + UPDATED_LOCATION_NAME);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationNameIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName in DEFAULT_LOCATION_NAME or UPDATED_LOCATION_NAME
        defaultLocationShouldBeFound("locationName.in=" + DEFAULT_LOCATION_NAME + "," + UPDATED_LOCATION_NAME);

        // Get all the locationList where locationName equals to UPDATED_LOCATION_NAME
        defaultLocationShouldNotBeFound("locationName.in=" + UPDATED_LOCATION_NAME);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName is not null
        defaultLocationShouldBeFound("locationName.specified=true");

        // Get all the locationList where locationName is null
        defaultLocationShouldNotBeFound("locationName.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocationsByLocationNameContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName contains DEFAULT_LOCATION_NAME
        defaultLocationShouldBeFound("locationName.contains=" + DEFAULT_LOCATION_NAME);

        // Get all the locationList where locationName contains UPDATED_LOCATION_NAME
        defaultLocationShouldNotBeFound("locationName.contains=" + UPDATED_LOCATION_NAME);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationNameNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationName does not contain DEFAULT_LOCATION_NAME
        defaultLocationShouldNotBeFound("locationName.doesNotContain=" + DEFAULT_LOCATION_NAME);

        // Get all the locationList where locationName does not contain UPDATED_LOCATION_NAME
        defaultLocationShouldBeFound("locationName.doesNotContain=" + UPDATED_LOCATION_NAME);
    }


    @Test
    @Transactional
    public void getAllLocationsByLocationProfMarginIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationProfMargin equals to DEFAULT_LOCATION_PROF_MARGIN
        defaultLocationShouldBeFound("locationProfMargin.equals=" + DEFAULT_LOCATION_PROF_MARGIN);

        // Get all the locationList where locationProfMargin equals to UPDATED_LOCATION_PROF_MARGIN
        defaultLocationShouldNotBeFound("locationProfMargin.equals=" + UPDATED_LOCATION_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationProfMarginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationProfMargin not equals to DEFAULT_LOCATION_PROF_MARGIN
        defaultLocationShouldNotBeFound("locationProfMargin.notEquals=" + DEFAULT_LOCATION_PROF_MARGIN);

        // Get all the locationList where locationProfMargin not equals to UPDATED_LOCATION_PROF_MARGIN
        defaultLocationShouldBeFound("locationProfMargin.notEquals=" + UPDATED_LOCATION_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationProfMarginIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationProfMargin in DEFAULT_LOCATION_PROF_MARGIN or UPDATED_LOCATION_PROF_MARGIN
        defaultLocationShouldBeFound("locationProfMargin.in=" + DEFAULT_LOCATION_PROF_MARGIN + "," + UPDATED_LOCATION_PROF_MARGIN);

        // Get all the locationList where locationProfMargin equals to UPDATED_LOCATION_PROF_MARGIN
        defaultLocationShouldNotBeFound("locationProfMargin.in=" + UPDATED_LOCATION_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationProfMarginIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationProfMargin is not null
        defaultLocationShouldBeFound("locationProfMargin.specified=true");

        // Get all the locationList where locationProfMargin is null
        defaultLocationShouldNotBeFound("locationProfMargin.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationProfMarginIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationProfMargin is greater than or equal to DEFAULT_LOCATION_PROF_MARGIN
        defaultLocationShouldBeFound("locationProfMargin.greaterThanOrEqual=" + DEFAULT_LOCATION_PROF_MARGIN);

        // Get all the locationList where locationProfMargin is greater than or equal to UPDATED_LOCATION_PROF_MARGIN
        defaultLocationShouldNotBeFound("locationProfMargin.greaterThanOrEqual=" + UPDATED_LOCATION_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationProfMarginIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationProfMargin is less than or equal to DEFAULT_LOCATION_PROF_MARGIN
        defaultLocationShouldBeFound("locationProfMargin.lessThanOrEqual=" + DEFAULT_LOCATION_PROF_MARGIN);

        // Get all the locationList where locationProfMargin is less than or equal to SMALLER_LOCATION_PROF_MARGIN
        defaultLocationShouldNotBeFound("locationProfMargin.lessThanOrEqual=" + SMALLER_LOCATION_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationProfMarginIsLessThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationProfMargin is less than DEFAULT_LOCATION_PROF_MARGIN
        defaultLocationShouldNotBeFound("locationProfMargin.lessThan=" + DEFAULT_LOCATION_PROF_MARGIN);

        // Get all the locationList where locationProfMargin is less than UPDATED_LOCATION_PROF_MARGIN
        defaultLocationShouldBeFound("locationProfMargin.lessThan=" + UPDATED_LOCATION_PROF_MARGIN);
    }

    @Test
    @Transactional
    public void getAllLocationsByLocationProfMarginIsGreaterThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where locationProfMargin is greater than DEFAULT_LOCATION_PROF_MARGIN
        defaultLocationShouldNotBeFound("locationProfMargin.greaterThan=" + DEFAULT_LOCATION_PROF_MARGIN);

        // Get all the locationList where locationProfMargin is greater than SMALLER_LOCATION_PROF_MARGIN
        defaultLocationShouldBeFound("locationProfMargin.greaterThan=" + SMALLER_LOCATION_PROF_MARGIN);
    }


    @Test
    @Transactional
    public void getAllLocationsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where isActive equals to DEFAULT_IS_ACTIVE
        defaultLocationShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the locationList where isActive equals to UPDATED_IS_ACTIVE
        defaultLocationShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllLocationsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultLocationShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the locationList where isActive not equals to UPDATED_IS_ACTIVE
        defaultLocationShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllLocationsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultLocationShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the locationList where isActive equals to UPDATED_IS_ACTIVE
        defaultLocationShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllLocationsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where isActive is not null
        defaultLocationShouldBeFound("isActive.specified=true");

        // Get all the locationList where isActive is null
        defaultLocationShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocationsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        Company company = CompanyResourceIT.createEntity(em);
        em.persist(company);
        em.flush();
        location.setCompany(company);
        locationRepository.saveAndFlush(location);
        Long companyId = company.getId();

        // Get all the locationList where company equals to companyId
        defaultLocationShouldBeFound("companyId.equals=" + companyId);

        // Get all the locationList where company equals to companyId + 1
        defaultLocationShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }


    @Test
    @Transactional
    public void getAllLocationsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        ExUser users = ExUserResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        location.addUsers(users);
        locationRepository.saveAndFlush(location);
        Long usersId = users.getId();

        // Get all the locationList where users equals to usersId
        defaultLocationShouldBeFound("usersId.equals=" + usersId);

        // Get all the locationList where users equals to usersId + 1
        defaultLocationShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationShouldBeFound(String filter) throws Exception {
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].locationName").value(hasItem(DEFAULT_LOCATION_NAME)))
            .andExpect(jsonPath("$.[*].locationProfMargin").value(hasItem(DEFAULT_LOCATION_PROF_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restLocationMockMvc.perform(get("/api/locations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationShouldNotBeFound(String filter) throws Exception {
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationMockMvc.perform(get("/api/locations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocation() throws Exception {
        // Initialize the database
        locationService.save(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).get();
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation);
        updatedLocation
            .locationCode(UPDATED_LOCATION_CODE)
            .locationName(UPDATED_LOCATION_NAME)
            .locationProfMargin(UPDATED_LOCATION_PROF_MARGIN)
            .isActive(UPDATED_IS_ACTIVE);

        restLocationMockMvc.perform(put("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLocation)))
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testLocation.getLocationName()).isEqualTo(UPDATED_LOCATION_NAME);
        assertThat(testLocation.getLocationProfMargin()).isEqualTo(UPDATED_LOCATION_PROF_MARGIN);
        assertThat(testLocation.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Create the Location

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc.perform(put("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(location)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLocation() throws Exception {
        // Initialize the database
        locationService.save(location);

        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Delete the location
        restLocationMockMvc.perform(delete("/api/locations/{id}", location.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = new Location();
        location1.setId(1L);
        Location location2 = new Location();
        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);
        location2.setId(2L);
        assertThat(location1).isNotEqualTo(location2);
        location1.setId(null);
        assertThat(location1).isNotEqualTo(location2);
    }
}
