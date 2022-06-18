package com.mhm.taskate.service;

import com.mhm.taskate.service.dto.TaskCategoryDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mhm.taskate.domain.TaskCategory}.
 */
public interface TaskCategoryService {
    /**
     * Save a taskCategory.
     *
     * @param taskCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    TaskCategoryDTO save(TaskCategoryDTO taskCategoryDTO);

    /**
     * Updates a taskCategory.
     *
     * @param taskCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    TaskCategoryDTO update(TaskCategoryDTO taskCategoryDTO);

    /**
     * Partially updates a taskCategory.
     *
     * @param taskCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaskCategoryDTO> partialUpdate(TaskCategoryDTO taskCategoryDTO);

    /**
     * Get all the taskCategories.
     *
     * @return the list of entities.
     */
    List<TaskCategoryDTO> findAll();

    /**
     * Get the "id" taskCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" taskCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
