package com.mhm.taskate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mhm.taskate.IntegrationTest;
import com.mhm.taskate.domain.Tasker;
import com.mhm.taskate.domain.enumeration.LicenseStatus;
import com.mhm.taskate.repository.TaskerRepository;
import com.mhm.taskate.service.dto.TaskerDTO;
import com.mhm.taskate.service.mapper.TaskerMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TaskerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskerResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SUBSCRIBED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBSCRIBED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LicenseStatus DEFAULT_LICENSE_STATUS = LicenseStatus.EXPIRED;
    private static final LicenseStatus UPDATED_LICENSE_STATUS = LicenseStatus.VALID;

    private static final String ENTITY_API_URL = "/api/taskers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskerRepository taskerRepository;

    @Autowired
    private TaskerMapper taskerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskerMockMvc;

    private Tasker tasker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tasker createEntity(EntityManager em) {
        Tasker tasker = new Tasker()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .subscribedAt(DEFAULT_SUBSCRIBED_AT)
            .licenseStatus(DEFAULT_LICENSE_STATUS);
        return tasker;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tasker createUpdatedEntity(EntityManager em) {
        Tasker tasker = new Tasker()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .subscribedAt(UPDATED_SUBSCRIBED_AT)
            .licenseStatus(UPDATED_LICENSE_STATUS);
        return tasker;
    }

    @BeforeEach
    public void initTest() {
        tasker = createEntity(em);
    }

    @Test
    @Transactional
    void createTasker() throws Exception {
        int databaseSizeBeforeCreate = taskerRepository.findAll().size();
        // Create the Tasker
        TaskerDTO taskerDTO = taskerMapper.toDto(tasker);
        restTaskerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskerDTO)))
            .andExpect(status().isCreated());

        // Validate the Tasker in the database
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeCreate + 1);
        Tasker testTasker = taskerList.get(taskerList.size() - 1);
        assertThat(testTasker.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTasker.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTasker.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTasker.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testTasker.getSubscribedAt()).isEqualTo(DEFAULT_SUBSCRIBED_AT);
        assertThat(testTasker.getLicenseStatus()).isEqualTo(DEFAULT_LICENSE_STATUS);
    }

    @Test
    @Transactional
    void createTaskerWithExistingId() throws Exception {
        // Create the Tasker with an existing ID
        tasker.setId(1L);
        TaskerDTO taskerDTO = taskerMapper.toDto(tasker);

        int databaseSizeBeforeCreate = taskerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tasker in the database
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTaskers() throws Exception {
        // Initialize the database
        taskerRepository.saveAndFlush(tasker);

        // Get all the taskerList
        restTaskerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tasker.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].subscribedAt").value(hasItem(DEFAULT_SUBSCRIBED_AT.toString())))
            .andExpect(jsonPath("$.[*].licenseStatus").value(hasItem(DEFAULT_LICENSE_STATUS.toString())));
    }

    @Test
    @Transactional
    void getTasker() throws Exception {
        // Initialize the database
        taskerRepository.saveAndFlush(tasker);

        // Get the tasker
        restTaskerMockMvc
            .perform(get(ENTITY_API_URL_ID, tasker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tasker.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.subscribedAt").value(DEFAULT_SUBSCRIBED_AT.toString()))
            .andExpect(jsonPath("$.licenseStatus").value(DEFAULT_LICENSE_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTasker() throws Exception {
        // Get the tasker
        restTaskerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTasker() throws Exception {
        // Initialize the database
        taskerRepository.saveAndFlush(tasker);

        int databaseSizeBeforeUpdate = taskerRepository.findAll().size();

        // Update the tasker
        Tasker updatedTasker = taskerRepository.findById(tasker.getId()).get();
        // Disconnect from session so that the updates on updatedTasker are not directly saved in db
        em.detach(updatedTasker);
        updatedTasker
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .subscribedAt(UPDATED_SUBSCRIBED_AT)
            .licenseStatus(UPDATED_LICENSE_STATUS);
        TaskerDTO taskerDTO = taskerMapper.toDto(updatedTasker);

        restTaskerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tasker in the database
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeUpdate);
        Tasker testTasker = taskerList.get(taskerList.size() - 1);
        assertThat(testTasker.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTasker.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTasker.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTasker.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTasker.getSubscribedAt()).isEqualTo(UPDATED_SUBSCRIBED_AT);
        assertThat(testTasker.getLicenseStatus()).isEqualTo(UPDATED_LICENSE_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingTasker() throws Exception {
        int databaseSizeBeforeUpdate = taskerRepository.findAll().size();
        tasker.setId(count.incrementAndGet());

        // Create the Tasker
        TaskerDTO taskerDTO = taskerMapper.toDto(tasker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasker in the database
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTasker() throws Exception {
        int databaseSizeBeforeUpdate = taskerRepository.findAll().size();
        tasker.setId(count.incrementAndGet());

        // Create the Tasker
        TaskerDTO taskerDTO = taskerMapper.toDto(tasker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasker in the database
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTasker() throws Exception {
        int databaseSizeBeforeUpdate = taskerRepository.findAll().size();
        tasker.setId(count.incrementAndGet());

        // Create the Tasker
        TaskerDTO taskerDTO = taskerMapper.toDto(tasker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tasker in the database
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskerWithPatch() throws Exception {
        // Initialize the database
        taskerRepository.saveAndFlush(tasker);

        int databaseSizeBeforeUpdate = taskerRepository.findAll().size();

        // Update the tasker using partial update
        Tasker partialUpdatedTasker = new Tasker();
        partialUpdatedTasker.setId(tasker.getId());

        partialUpdatedTasker.firstName(UPDATED_FIRST_NAME).phoneNumber(UPDATED_PHONE_NUMBER).licenseStatus(UPDATED_LICENSE_STATUS);

        restTaskerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTasker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTasker))
            )
            .andExpect(status().isOk());

        // Validate the Tasker in the database
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeUpdate);
        Tasker testTasker = taskerList.get(taskerList.size() - 1);
        assertThat(testTasker.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTasker.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTasker.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTasker.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTasker.getSubscribedAt()).isEqualTo(DEFAULT_SUBSCRIBED_AT);
        assertThat(testTasker.getLicenseStatus()).isEqualTo(UPDATED_LICENSE_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateTaskerWithPatch() throws Exception {
        // Initialize the database
        taskerRepository.saveAndFlush(tasker);

        int databaseSizeBeforeUpdate = taskerRepository.findAll().size();

        // Update the tasker using partial update
        Tasker partialUpdatedTasker = new Tasker();
        partialUpdatedTasker.setId(tasker.getId());

        partialUpdatedTasker
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .subscribedAt(UPDATED_SUBSCRIBED_AT)
            .licenseStatus(UPDATED_LICENSE_STATUS);

        restTaskerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTasker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTasker))
            )
            .andExpect(status().isOk());

        // Validate the Tasker in the database
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeUpdate);
        Tasker testTasker = taskerList.get(taskerList.size() - 1);
        assertThat(testTasker.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTasker.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTasker.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTasker.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTasker.getSubscribedAt()).isEqualTo(UPDATED_SUBSCRIBED_AT);
        assertThat(testTasker.getLicenseStatus()).isEqualTo(UPDATED_LICENSE_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingTasker() throws Exception {
        int databaseSizeBeforeUpdate = taskerRepository.findAll().size();
        tasker.setId(count.incrementAndGet());

        // Create the Tasker
        TaskerDTO taskerDTO = taskerMapper.toDto(tasker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasker in the database
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTasker() throws Exception {
        int databaseSizeBeforeUpdate = taskerRepository.findAll().size();
        tasker.setId(count.incrementAndGet());

        // Create the Tasker
        TaskerDTO taskerDTO = taskerMapper.toDto(tasker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasker in the database
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTasker() throws Exception {
        int databaseSizeBeforeUpdate = taskerRepository.findAll().size();
        tasker.setId(count.incrementAndGet());

        // Create the Tasker
        TaskerDTO taskerDTO = taskerMapper.toDto(tasker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taskerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tasker in the database
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTasker() throws Exception {
        // Initialize the database
        taskerRepository.saveAndFlush(tasker);

        int databaseSizeBeforeDelete = taskerRepository.findAll().size();

        // Delete the tasker
        restTaskerMockMvc
            .perform(delete(ENTITY_API_URL_ID, tasker.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tasker> taskerList = taskerRepository.findAll();
        assertThat(taskerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
