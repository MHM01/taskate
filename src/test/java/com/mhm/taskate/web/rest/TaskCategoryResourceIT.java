package com.mhm.taskate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mhm.taskate.IntegrationTest;
import com.mhm.taskate.domain.TaskCategory;
import com.mhm.taskate.repository.TaskCategoryRepository;
import com.mhm.taskate.service.dto.TaskCategoryDTO;
import com.mhm.taskate.service.mapper.TaskCategoryMapper;
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
 * Integration tests for the {@link TaskCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/task-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    @Autowired
    private TaskCategoryMapper taskCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskCategoryMockMvc;

    private TaskCategory taskCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskCategory createEntity(EntityManager em) {
        TaskCategory taskCategory = new TaskCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return taskCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskCategory createUpdatedEntity(EntityManager em) {
        TaskCategory taskCategory = new TaskCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return taskCategory;
    }

    @BeforeEach
    public void initTest() {
        taskCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createTaskCategory() throws Exception {
        int databaseSizeBeforeCreate = taskCategoryRepository.findAll().size();
        // Create the TaskCategory
        TaskCategoryDTO taskCategoryDTO = taskCategoryMapper.toDto(taskCategory);
        restTaskCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TaskCategory in the database
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        TaskCategory testTaskCategory = taskCategoryList.get(taskCategoryList.size() - 1);
        assertThat(testTaskCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTaskCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createTaskCategoryWithExistingId() throws Exception {
        // Create the TaskCategory with an existing ID
        taskCategory.setId(1L);
        TaskCategoryDTO taskCategoryDTO = taskCategoryMapper.toDto(taskCategory);

        int databaseSizeBeforeCreate = taskCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskCategory in the database
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTaskCategories() throws Exception {
        // Initialize the database
        taskCategoryRepository.saveAndFlush(taskCategory);

        // Get all the taskCategoryList
        restTaskCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTaskCategory() throws Exception {
        // Initialize the database
        taskCategoryRepository.saveAndFlush(taskCategory);

        // Get the taskCategory
        restTaskCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, taskCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingTaskCategory() throws Exception {
        // Get the taskCategory
        restTaskCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTaskCategory() throws Exception {
        // Initialize the database
        taskCategoryRepository.saveAndFlush(taskCategory);

        int databaseSizeBeforeUpdate = taskCategoryRepository.findAll().size();

        // Update the taskCategory
        TaskCategory updatedTaskCategory = taskCategoryRepository.findById(taskCategory.getId()).get();
        // Disconnect from session so that the updates on updatedTaskCategory are not directly saved in db
        em.detach(updatedTaskCategory);
        updatedTaskCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        TaskCategoryDTO taskCategoryDTO = taskCategoryMapper.toDto(updatedTaskCategory);

        restTaskCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaskCategory in the database
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeUpdate);
        TaskCategory testTaskCategory = taskCategoryList.get(taskCategoryList.size() - 1);
        assertThat(testTaskCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaskCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingTaskCategory() throws Exception {
        int databaseSizeBeforeUpdate = taskCategoryRepository.findAll().size();
        taskCategory.setId(count.incrementAndGet());

        // Create the TaskCategory
        TaskCategoryDTO taskCategoryDTO = taskCategoryMapper.toDto(taskCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskCategory in the database
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaskCategory() throws Exception {
        int databaseSizeBeforeUpdate = taskCategoryRepository.findAll().size();
        taskCategory.setId(count.incrementAndGet());

        // Create the TaskCategory
        TaskCategoryDTO taskCategoryDTO = taskCategoryMapper.toDto(taskCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskCategory in the database
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaskCategory() throws Exception {
        int databaseSizeBeforeUpdate = taskCategoryRepository.findAll().size();
        taskCategory.setId(count.incrementAndGet());

        // Create the TaskCategory
        TaskCategoryDTO taskCategoryDTO = taskCategoryMapper.toDto(taskCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskCategory in the database
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskCategoryWithPatch() throws Exception {
        // Initialize the database
        taskCategoryRepository.saveAndFlush(taskCategory);

        int databaseSizeBeforeUpdate = taskCategoryRepository.findAll().size();

        // Update the taskCategory using partial update
        TaskCategory partialUpdatedTaskCategory = new TaskCategory();
        partialUpdatedTaskCategory.setId(taskCategory.getId());

        partialUpdatedTaskCategory.name(UPDATED_NAME);

        restTaskCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskCategory))
            )
            .andExpect(status().isOk());

        // Validate the TaskCategory in the database
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeUpdate);
        TaskCategory testTaskCategory = taskCategoryList.get(taskCategoryList.size() - 1);
        assertThat(testTaskCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaskCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateTaskCategoryWithPatch() throws Exception {
        // Initialize the database
        taskCategoryRepository.saveAndFlush(taskCategory);

        int databaseSizeBeforeUpdate = taskCategoryRepository.findAll().size();

        // Update the taskCategory using partial update
        TaskCategory partialUpdatedTaskCategory = new TaskCategory();
        partialUpdatedTaskCategory.setId(taskCategory.getId());

        partialUpdatedTaskCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restTaskCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskCategory))
            )
            .andExpect(status().isOk());

        // Validate the TaskCategory in the database
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeUpdate);
        TaskCategory testTaskCategory = taskCategoryList.get(taskCategoryList.size() - 1);
        assertThat(testTaskCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaskCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTaskCategory() throws Exception {
        int databaseSizeBeforeUpdate = taskCategoryRepository.findAll().size();
        taskCategory.setId(count.incrementAndGet());

        // Create the TaskCategory
        TaskCategoryDTO taskCategoryDTO = taskCategoryMapper.toDto(taskCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskCategory in the database
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaskCategory() throws Exception {
        int databaseSizeBeforeUpdate = taskCategoryRepository.findAll().size();
        taskCategory.setId(count.incrementAndGet());

        // Create the TaskCategory
        TaskCategoryDTO taskCategoryDTO = taskCategoryMapper.toDto(taskCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskCategory in the database
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaskCategory() throws Exception {
        int databaseSizeBeforeUpdate = taskCategoryRepository.findAll().size();
        taskCategory.setId(count.incrementAndGet());

        // Create the TaskCategory
        TaskCategoryDTO taskCategoryDTO = taskCategoryMapper.toDto(taskCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskCategory in the database
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaskCategory() throws Exception {
        // Initialize the database
        taskCategoryRepository.saveAndFlush(taskCategory);

        int databaseSizeBeforeDelete = taskCategoryRepository.findAll().size();

        // Delete the taskCategory
        restTaskCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, taskCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaskCategory> taskCategoryList = taskCategoryRepository.findAll();
        assertThat(taskCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
