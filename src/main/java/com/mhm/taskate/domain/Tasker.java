package com.mhm.taskate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mhm.taskate.domain.enumeration.LicenseStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Tasker entity.
 */
@Entity
@Table(name = "tasker")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tasker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "subscribed_at")
    private Instant subscribedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_status")
    private LicenseStatus licenseStatus;

    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Location address;

    @OneToMany(mappedBy = "taskers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location", "taskCategory", "clients", "taskers" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "taskers" }, allowSetters = true)
    private TaskCategory taskCategories;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tasker id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Tasker firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Tasker lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Tasker email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Tasker phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getSubscribedAt() {
        return this.subscribedAt;
    }

    public Tasker subscribedAt(Instant subscribedAt) {
        this.setSubscribedAt(subscribedAt);
        return this;
    }

    public void setSubscribedAt(Instant subscribedAt) {
        this.subscribedAt = subscribedAt;
    }

    public LicenseStatus getLicenseStatus() {
        return this.licenseStatus;
    }

    public Tasker licenseStatus(LicenseStatus licenseStatus) {
        this.setLicenseStatus(licenseStatus);
        return this;
    }

    public void setLicenseStatus(LicenseStatus licenseStatus) {
        this.licenseStatus = licenseStatus;
    }

    public Location getAddress() {
        return this.address;
    }

    public void setAddress(Location location) {
        this.address = location;
    }

    public Tasker address(Location location) {
        this.setAddress(location);
        return this;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setTaskers(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setTaskers(this));
        }
        this.tasks = tasks;
    }

    public Tasker tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public Tasker addTask(Task task) {
        this.tasks.add(task);
        task.setTaskers(this);
        return this;
    }

    public Tasker removeTask(Task task) {
        this.tasks.remove(task);
        task.setTaskers(null);
        return this;
    }

    public TaskCategory getTaskCategories() {
        return this.taskCategories;
    }

    public void setTaskCategories(TaskCategory taskCategory) {
        this.taskCategories = taskCategory;
    }

    public Tasker taskCategories(TaskCategory taskCategory) {
        this.setTaskCategories(taskCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tasker)) {
            return false;
        }
        return id != null && id.equals(((Tasker) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tasker{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", subscribedAt='" + getSubscribedAt() + "'" +
            ", licenseStatus='" + getLicenseStatus() + "'" +
            "}";
    }
}
