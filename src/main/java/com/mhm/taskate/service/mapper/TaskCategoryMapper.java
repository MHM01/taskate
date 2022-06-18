package com.mhm.taskate.service.mapper;

import com.mhm.taskate.domain.TaskCategory;
import com.mhm.taskate.service.dto.TaskCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaskCategory} and its DTO {@link TaskCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskCategoryMapper extends EntityMapper<TaskCategoryDTO, TaskCategory> {}
