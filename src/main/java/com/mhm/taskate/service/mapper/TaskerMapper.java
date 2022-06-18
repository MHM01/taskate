package com.mhm.taskate.service.mapper;

import com.mhm.taskate.domain.Location;
import com.mhm.taskate.domain.TaskCategory;
import com.mhm.taskate.domain.Tasker;
import com.mhm.taskate.service.dto.LocationDTO;
import com.mhm.taskate.service.dto.TaskCategoryDTO;
import com.mhm.taskate.service.dto.TaskerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tasker} and its DTO {@link TaskerDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskerMapper extends EntityMapper<TaskerDTO, Tasker> {
    @Mapping(target = "address", source = "address", qualifiedByName = "locationId")
    @Mapping(target = "taskCategories", source = "taskCategories", qualifiedByName = "taskCategoryId")
    TaskerDTO toDto(Tasker s);

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoLocationId(Location location);

    @Named("taskCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskCategoryDTO toDtoTaskCategoryId(TaskCategory taskCategory);
}
