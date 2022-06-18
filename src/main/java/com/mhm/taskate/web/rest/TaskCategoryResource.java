package com.mhm.taskate.web.rest;

import com.mhm.taskate.repository.TaskCategoryRepository;
import com.mhm.taskate.service.TaskCategoryService;
import com.mhm.taskate.service.dto.TaskCategoryDTO;
import com.mhm.taskate.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mhm.taskate.domain.TaskCategory}.
 */
@RestController
@RequestMapping("/api")
public class TaskCategoryResource {

    private final Logger log = LoggerFactory.getLogger(TaskCategoryResource.class);

    private static final String ENTITY_NAME = "taskCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskCategoryService taskCategoryService;

    private final TaskCategoryRepository taskCategoryRepository;

    public TaskCategoryResource(TaskCategoryService taskCategoryService, TaskCategoryRepository taskCategoryRepository) {
        this.taskCategoryService = taskCategoryService;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    /**
     * {@code POST  /task-categories} : Create a new taskCategory.
     *
     * @param taskCategoryDTO the taskCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskCategoryDTO, or with status {@code 400 (Bad Request)} if the taskCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/task-categories")
    public ResponseEntity<TaskCategoryDTO> createTaskCategory(@RequestBody TaskCategoryDTO taskCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save TaskCategory : {}", taskCategoryDTO);
        if (taskCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new taskCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskCategoryDTO result = taskCategoryService.save(taskCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/task-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /task-categories/:id} : Updates an existing taskCategory.
     *
     * @param id the id of the taskCategoryDTO to save.
     * @param taskCategoryDTO the taskCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the taskCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/task-categories/{id}")
    public ResponseEntity<TaskCategoryDTO> updateTaskCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskCategoryDTO taskCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaskCategory : {}, {}", id, taskCategoryDTO);
        if (taskCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaskCategoryDTO result = taskCategoryService.update(taskCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /task-categories/:id} : Partial updates given fields of an existing taskCategory, field will ignore if it is null
     *
     * @param id the id of the taskCategoryDTO to save.
     * @param taskCategoryDTO the taskCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the taskCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taskCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/task-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaskCategoryDTO> partialUpdateTaskCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskCategoryDTO taskCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaskCategory partially : {}, {}", id, taskCategoryDTO);
        if (taskCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskCategoryDTO> result = taskCategoryService.partialUpdate(taskCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /task-categories} : get all the taskCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskCategories in body.
     */
    @GetMapping("/task-categories")
    public List<TaskCategoryDTO> getAllTaskCategories() {
        log.debug("REST request to get all TaskCategories");
        return taskCategoryService.findAll();
    }

    /**
     * {@code GET  /task-categories/:id} : get the "id" taskCategory.
     *
     * @param id the id of the taskCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/task-categories/{id}")
    public ResponseEntity<TaskCategoryDTO> getTaskCategory(@PathVariable Long id) {
        log.debug("REST request to get TaskCategory : {}", id);
        Optional<TaskCategoryDTO> taskCategoryDTO = taskCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskCategoryDTO);
    }

    /**
     * {@code DELETE  /task-categories/:id} : delete the "id" taskCategory.
     *
     * @param id the id of the taskCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/task-categories/{id}")
    public ResponseEntity<Void> deleteTaskCategory(@PathVariable Long id) {
        log.debug("REST request to delete TaskCategory : {}", id);
        taskCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
