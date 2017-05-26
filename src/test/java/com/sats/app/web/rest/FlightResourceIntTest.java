package com.sats.app.web.rest;

import com.sats.app.TestApp;

import com.sats.app.domain.Flight;
import com.sats.app.repository.FlightRepository;
import com.sats.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.sats.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FlightResource REST controller.
 *
 * @see FlightResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApp.class)
public class FlightResourceIntTest {

    private static final String DEFAULT_FLNO = "AAAAAAAAAA";
    private static final String UPDATED_FLNO = "BBBBBBBBBB";

    private static final String DEFAULT_ORGN = "AAAAAAAAAA";
    private static final String UPDATED_ORGN = "BBBBBBBBBB";

    private static final String DEFAULT_DEST = "AAAAAAAAAA";
    private static final String UPDATED_DEST = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FLDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FLDA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFlightMockMvc;

    private Flight flight;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FlightResource flightResource = new FlightResource(flightRepository);
        this.restFlightMockMvc = MockMvcBuilders.standaloneSetup(flightResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flight createEntity(EntityManager em) {
        Flight flight = new Flight()
            .flno(DEFAULT_FLNO)
            .orgn(DEFAULT_ORGN)
            .dest(DEFAULT_DEST)
            .flda(DEFAULT_FLDA);
        return flight;
    }

    @Before
    public void initTest() {
        flight = createEntity(em);
    }

    @Test
    @Transactional
    public void createFlight() throws Exception {
        int databaseSizeBeforeCreate = flightRepository.findAll().size();

        // Create the Flight
        restFlightMockMvc.perform(post("/api/flights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isCreated());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeCreate + 1);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getFlno()).isEqualTo(DEFAULT_FLNO);
        assertThat(testFlight.getOrgn()).isEqualTo(DEFAULT_ORGN);
        assertThat(testFlight.getDest()).isEqualTo(DEFAULT_DEST);
        assertThat(testFlight.getFlda()).isEqualTo(DEFAULT_FLDA);
    }

    @Test
    @Transactional
    public void createFlightWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = flightRepository.findAll().size();

        // Create the Flight with an existing ID
        flight.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlightMockMvc.perform(post("/api/flights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFlights() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList
        restFlightMockMvc.perform(get("/api/flights?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flight.getId().intValue())))
            .andExpect(jsonPath("$.[*].flno").value(hasItem(DEFAULT_FLNO.toString())))
            .andExpect(jsonPath("$.[*].orgn").value(hasItem(DEFAULT_ORGN.toString())))
            .andExpect(jsonPath("$.[*].dest").value(hasItem(DEFAULT_DEST.toString())))
            .andExpect(jsonPath("$.[*].flda").value(hasItem(sameInstant(DEFAULT_FLDA))));
    }

    @Test
    @Transactional
    public void getFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get the flight
        restFlightMockMvc.perform(get("/api/flights/{id}", flight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(flight.getId().intValue()))
            .andExpect(jsonPath("$.flno").value(DEFAULT_FLNO.toString()))
            .andExpect(jsonPath("$.orgn").value(DEFAULT_ORGN.toString()))
            .andExpect(jsonPath("$.dest").value(DEFAULT_DEST.toString()))
            .andExpect(jsonPath("$.flda").value(sameInstant(DEFAULT_FLDA)));
    }

    @Test
    @Transactional
    public void getNonExistingFlight() throws Exception {
        // Get the flight
        restFlightMockMvc.perform(get("/api/flights/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // Update the flight
        Flight updatedFlight = flightRepository.findOne(flight.getId());
        updatedFlight
            .flno(UPDATED_FLNO)
            .orgn(UPDATED_ORGN)
            .dest(UPDATED_DEST)
            .flda(UPDATED_FLDA);

        restFlightMockMvc.perform(put("/api/flights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFlight)))
            .andExpect(status().isOk());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getFlno()).isEqualTo(UPDATED_FLNO);
        assertThat(testFlight.getOrgn()).isEqualTo(UPDATED_ORGN);
        assertThat(testFlight.getDest()).isEqualTo(UPDATED_DEST);
        assertThat(testFlight.getFlda()).isEqualTo(UPDATED_FLDA);
    }

    @Test
    @Transactional
    public void updateNonExistingFlight() throws Exception {
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // Create the Flight

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFlightMockMvc.perform(put("/api/flights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isCreated());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);
        int databaseSizeBeforeDelete = flightRepository.findAll().size();

        // Get the flight
        restFlightMockMvc.perform(delete("/api/flights/{id}", flight.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Flight.class);
        Flight flight1 = new Flight();
        flight1.setId(1L);
        Flight flight2 = new Flight();
        flight2.setId(flight1.getId());
        assertThat(flight1).isEqualTo(flight2);
        flight2.setId(2L);
        assertThat(flight1).isNotEqualTo(flight2);
        flight1.setId(null);
        assertThat(flight1).isNotEqualTo(flight2);
    }
}
