package com.mhm.taskate.service.dto;

import com.mhm.taskate.domain.enumeration.TaskStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mhm.taskate.domain.Task} entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private Integer numberOfTaskerRequired;

    private BigDecimal budget;

    private Instant startDate;

    private TaskStatus status;

    private LocationDTO location;

    private TaskCategoryDTO taskCategory;

    private TaskerDTO taskers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfTaskerRequired() {
        return numberOfTaskerRequired;
    }

    public void setNumberOfTaskerRequired(Integer numberOfTaskerRequired) {
        this.numberOfTaskerRequired = numberOfTaskerRequired;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public TaskCategoryDTO getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(TaskCategoryDTO taskCategory) {
        this.taskCategory = taskCategory;
    }

    public TaskerDTO getTaskers() {
        return taskers;
    }

    public void setTaskers(TaskerDTO taskers) {
        this.taskers = taskers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDTO)) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", numberOfTaskerRequired=" + getNumberOfTaskerRequired() +
            ", budget=" + getBudget() +
            ", startDate='" + getStartDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", location=" + getLocation() +
            ", taskCategory=" + getTaskCategory() +
            ", taskers=" + getTaskers() +
            "}";
    }
}
