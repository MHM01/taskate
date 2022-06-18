package com.mhm.taskate.service.impl;

import com.mhm.taskate.domain.Tasker;
import com.mhm.taskate.repository.TaskerRepository;
import com.mhm.taskate.service.TaskerService;
import com.mhm.taskate.service.dto.TaskerDTO;
import com.mhm.taskate.service.mapper.TaskerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tasker}.
 */
@Service
@Transactional
public class TaskerServiceImpl implements TaskerService {

    private final Logger log = LoggerFactory.getLogger(TaskerServiceImpl.class);

    private final TaskerRepository taskerRepository;

    private final TaskerMapper taskerMapper;

    public TaskerServiceImpl(TaskerRepository taskerRepository, TaskerMapper taskerMapper) {
        this.taskerRepository = taskerRepository;
        this.taskerMapper = taskerMapper;
    }

    @Override
    public TaskerDTO save(TaskerDTO taskerDTO) {
        log.debug("Request to save Tasker : {}", taskerDTO);
        Tasker tasker = taskerMapper.toEntity(taskerDTO);
        tasker = taskerRepository.save(tasker);
        return taskerMapper.toDto(tasker);
    }

    @Override
    public TaskerDTO update(TaskerDTO taskerDTO) {
        log.debug("Request to save Tasker : {}", taskerDTO);
        Tasker tasker = taskerMapper.toEntity(taskerDTO);
        tasker = taskerRepository.save(tasker);
        return taskerMapper.toDto(tasker);
    }

    @Override
    public Optional<TaskerDTO> partialUpdate(TaskerDTO taskerDTO) {
        log.debug("Request to partially update Tasker : {}", taskerDTO);

        return taskerRepository
            .findById(taskerDTO.getId())
            .map(existingTasker -> {
                taskerMapper.partialUpdate(existingTasker, taskerDTO);

                return existingTasker;
            })
            .map(taskerRepository::save)
            .map(taskerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Taskers");
        return taskerRepository.findAll(pageable).map(taskerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskerDTO> findOne(Long id) {
        log.debug("Request to get Tasker : {}", id);
        return taskerRepository.findById(id).map(taskerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tasker : {}", id);
        taskerRepository.deleteById(id);
    }
}
