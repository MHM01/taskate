package com.mhm.taskate.service.mapper;

import com.mhm.taskate.domain.Client;
import com.mhm.taskate.domain.Location;
import com.mhm.taskate.domain.Task;
import com.mhm.taskate.service.dto.ClientDTO;
import com.mhm.taskate.service.dto.LocationDTO;
import com.mhm.taskate.service.dto.TaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {
    @Mapping(target = "address", source = "address", qualifiedByName = "locationId")
    @Mapping(target = "tasks", source = "tasks", qualifiedByName = "taskId")
    ClientDTO toDto(Client s);

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoLocationId(Location location);

    @Named("taskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskDTO toDtoTaskId(Task task);
}
