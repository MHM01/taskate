package com.mhm.taskate.web.rest;

import com.mhm.taskate.repository.TaskerRepository;
import com.mhm.taskate.service.TaskerService;
import com.mhm.taskate.service.dto.TaskerDTO;
import com.mhm.taskate.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mhm.taskate.domain.Tasker}.
 */
@RestController
@RequestMapping("/api")
public class TaskerResource {

    private final Logger log = LoggerFactory.getLogger(TaskerResource.class);

    private static final String ENTITY_NAME = "tasker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskerService taskerService;

    private final TaskerRepository taskerRepository;

    public TaskerResource(TaskerService taskerService, TaskerRepository taskerRepository) {
        this.taskerService = taskerService;
        this.taskerRepository = taskerRepository;
    }

    /**
     * {@code POST  /taskers} : Create a new tasker.
     *
     * @param taskerDTO the taskerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskerDTO, or with status {@code 400 (Bad Request)} if the tasker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/taskers")
    public ResponseEntity<TaskerDTO> createTasker(@RequestBody TaskerDTO taskerDTO) throws URISyntaxException {
        log.debug("REST request to save Tasker : {}", taskerDTO);
        if (taskerDTO.getId() != null) {
            throw new BadRequestAlertException("A new tasker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskerDTO result = taskerService.save(taskerDTO);
        return ResponseEntity
            .created(new URI("/api/taskers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /taskers/:id} : Updates an existing tasker.
     *
     * @param id the id of the taskerDTO to save.
     * @param taskerDTO the taskerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskerDTO,
     * or with status {@code 400 (Bad Request)} if the taskerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/taskers/{id}")
    public ResponseEntity<TaskerDTO> updateTasker(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskerDTO taskerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tasker : {}, {}", id, taskerDTO);
        if (taskerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaskerDTO result = taskerService.update(taskerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /taskers/:id} : Partial updates given fields of an existing tasker, field will ignore if it is null
     *
     * @param id the id of the taskerDTO to save.
     * @param taskerDTO the taskerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskerDTO,
     * or with status {@code 400 (Bad Request)} if the taskerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taskerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/taskers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaskerDTO> partialUpdateTasker(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskerDTO taskerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tasker partially : {}, {}", id, taskerDTO);
        if (taskerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskerDTO> result = taskerService.partialUpdate(taskerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /taskers} : get all the taskers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskers in body.
     */
    @GetMapping("/taskers")
    public ResponseEntity<List<TaskerDTO>> getAllTaskers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Taskers");
        Page<TaskerDTO> page = taskerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /taskers/:id} : get the "id" tasker.
     *
     * @param id the id of the taskerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/taskers/{id}")
    public ResponseEntity<TaskerDTO> getTasker(@PathVariable Long id) {
        log.debug("REST request to get Tasker : {}", id);
        Optional<TaskerDTO> taskerDTO = taskerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskerDTO);
    }

    /**
     * {@code DELETE  /taskers/:id} : delete the "id" tasker.
     *
     * @param id the id of the taskerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/taskers/{id}")
    public ResponseEntity<Void> deleteTasker(@PathVariable Long id) {
        log.debug("REST request to delete Tasker : {}", id);
        taskerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
