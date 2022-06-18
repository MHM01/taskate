package com.mhm.taskate.service.mapper;

import com.mhm.taskate.domain.Location;
import com.mhm.taskate.domain.Task;
import com.mhm.taskate.domain.TaskCategory;
import com.mhm.taskate.domain.Tasker;
import com.mhm.taskate.service.dto.LocationDTO;
import com.mhm.taskate.service.dto.TaskCategoryDTO;
import com.mhm.taskate.service.dto.TaskDTO;
import com.mhm.taskate.service.dto.TaskerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "location", source = "location", qualifiedByName = "locationId")
    @Mapping(target = "taskCategory", source = "taskCategory", qualifiedByName = "taskCategoryId")
    @Mapping(target = "taskers", source = "taskers", qualifiedByName = "taskerId")
    TaskDTO toDto(Task s);

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoLocationId(Location location);

    @Named("taskCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskCategoryDTO toDtoTaskCategoryId(TaskCategory taskCategory);

    @Named("taskerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskerDTO toDtoTaskerId(Tasker tasker);
}
