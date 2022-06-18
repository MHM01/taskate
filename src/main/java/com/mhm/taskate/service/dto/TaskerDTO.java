package com.mhm.taskate.service.dto;

import com.mhm.taskate.domain.enumeration.LicenseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mhm.taskate.domain.Tasker} entity.
 */
@Schema(description = "The Tasker entity.")
public class TaskerDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Instant subscribedAt;

    private LicenseStatus licenseStatus;

    private LocationDTO address;

    private TaskCategoryDTO taskCategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getSubscribedAt() {
        return subscribedAt;
    }

    public void setSubscribedAt(Instant subscribedAt) {
        this.subscribedAt = subscribedAt;
    }

    public LicenseStatus getLicenseStatus() {
        return licenseStatus;
    }

    public void setLicenseStatus(LicenseStatus licenseStatus) {
        this.licenseStatus = licenseStatus;
    }

    public LocationDTO getAddress() {
        return address;
    }

    public void setAddress(LocationDTO address) {
        this.address = address;
    }

    public TaskCategoryDTO getTaskCategories() {
        return taskCategories;
    }

    public void setTaskCategories(TaskCategoryDTO taskCategories) {
        this.taskCategories = taskCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskerDTO)) {
            return false;
        }

        TaskerDTO taskerDTO = (TaskerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskerDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", subscribedAt='" + getSubscribedAt() + "'" +
            ", licenseStatus='" + getLicenseStatus() + "'" +
            ", address=" + getAddress() +
            ", taskCategories=" + getTaskCategories() +
            "}";
    }
}
