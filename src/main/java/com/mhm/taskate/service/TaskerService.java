package com.mhm.taskate.service;

import com.mhm.taskate.service.dto.TaskerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mhm.taskate.domain.Tasker}.
 */
public interface TaskerService {
    /**
     * Save a tasker.
     *
     * @param taskerDTO the entity to save.
     * @return the persisted entity.
     */
    TaskerDTO save(TaskerDTO taskerDTO);

    /**
     * Updates a tasker.
     *
     * @param taskerDTO the entity to update.
     * @return the persisted entity.
     */
    TaskerDTO update(TaskerDTO taskerDTO);

    /**
     * Partially updates a tasker.
     *
     * @param taskerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaskerDTO> partialUpdate(TaskerDTO taskerDTO);

    /**
     * Get all the taskers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tasker.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskerDTO> findOne(Long id);

    /**
     * Delete the "id" tasker.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
