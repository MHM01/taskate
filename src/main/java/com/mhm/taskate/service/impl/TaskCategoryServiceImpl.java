package com.mhm.taskate.service.impl;

import com.mhm.taskate.domain.TaskCategory;
import com.mhm.taskate.repository.TaskCategoryRepository;
import com.mhm.taskate.service.TaskCategoryService;
import com.mhm.taskate.service.dto.TaskCategoryDTO;
import com.mhm.taskate.service.mapper.TaskCategoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TaskCategory}.
 */
@Service
@Transactional
public class TaskCategoryServiceImpl implements TaskCategoryService {

    private final Logger log = LoggerFactory.getLogger(TaskCategoryServiceImpl.class);

    private final TaskCategoryRepository taskCategoryRepository;

    private final TaskCategoryMapper taskCategoryMapper;

    public TaskCategoryServiceImpl(TaskCategoryRepository taskCategoryRepository, TaskCategoryMapper taskCategoryMapper) {
        this.taskCategoryRepository = taskCategoryRepository;
        this.taskCategoryMapper = taskCategoryMapper;
    }

    @Override
    public TaskCategoryDTO save(TaskCategoryDTO taskCategoryDTO) {
        log.debug("Request to save TaskCategory : {}", taskCategoryDTO);
        TaskCategory taskCategory = taskCategoryMapper.toEntity(taskCategoryDTO);
        taskCategory = taskCategoryRepository.save(taskCategory);
        return taskCategoryMapper.toDto(taskCategory);
    }

    @Override
    public TaskCategoryDTO update(TaskCategoryDTO taskCategoryDTO) {
        log.debug("Request to save TaskCategory : {}", taskCategoryDTO);
        TaskCategory taskCategory = taskCategoryMapper.toEntity(taskCategoryDTO);
        taskCategory = taskCategoryRepository.save(taskCategory);
        return taskCategoryMapper.toDto(taskCategory);
    }

    @Override
    public Optional<TaskCategoryDTO> partialUpdate(TaskCategoryDTO taskCategoryDTO) {
        log.debug("Request to partially update TaskCategory : {}", taskCategoryDTO);

        return taskCategoryRepository
            .findById(taskCategoryDTO.getId())
            .map(existingTaskCategory -> {
                taskCategoryMapper.partialUpdate(existingTaskCategory, taskCategoryDTO);

                return existingTaskCategory;
            })
            .map(taskCategoryRepository::save)
            .map(taskCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskCategoryDTO> findAll() {
        log.debug("Request to get all TaskCategories");
        return taskCategoryRepository.findAll().stream().map(taskCategoryMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskCategoryDTO> findOne(Long id) {
        log.debug("Request to get TaskCategory : {}", id);
        return taskCategoryRepository.findById(id).map(taskCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskCategory : {}", id);
        taskCategoryRepository.deleteById(id);
    }
}
