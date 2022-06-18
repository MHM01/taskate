package com.mhm.taskate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mhm.taskate.domain.enumeration.TaskStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_tasker_required")
    private Integer numberOfTaskerRequired;

    @Column(name = "budget", precision = 21, scale = 2)
    private BigDecimal budget;

    @Column(name = "start_date")
    private Instant startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    @JsonIgnoreProperties(value = { "taskers" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private TaskCategory taskCategory;

    @OneToMany(mappedBy = "tasks")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "address", "tasks" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "address", "tasks", "taskCategories" }, allowSetters = true)
    private Tasker taskers;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Task id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Task title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Task description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfTaskerRequired() {
        return this.numberOfTaskerRequired;
    }

    public Task numberOfTaskerRequired(Integer numberOfTaskerRequired) {
        this.setNumberOfTaskerRequired(numberOfTaskerRequired);
        return this;
    }

    public void setNumberOfTaskerRequired(Integer numberOfTaskerRequired) {
        this.numberOfTaskerRequired = numberOfTaskerRequired;
    }

    public BigDecimal getBudget() {
        return this.budget;
    }

    public Task budget(BigDecimal budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Task startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public Task status(TaskStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Task location(Location location) {
        this.setLocation(location);
        return this;
    }

    public TaskCategory getTaskCategory() {
        return this.taskCategory;
    }

    public void setTaskCategory(TaskCategory taskCategory) {
        this.taskCategory = taskCategory;
    }

    public Task taskCategory(TaskCategory taskCategory) {
        this.setTaskCategory(taskCategory);
        return this;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.setTasks(null));
        }
        if (clients != null) {
            clients.forEach(i -> i.setTasks(this));
        }
        this.clients = clients;
    }

    public Task clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Task addClient(Client client) {
        this.clients.add(client);
        client.setTasks(this);
        return this;
    }

    public Task removeClient(Client client) {
        this.clients.remove(client);
        client.setTasks(null);
        return this;
    }

    public Tasker getTaskers() {
        return this.taskers;
    }

    public void setTaskers(Tasker tasker) {
        this.taskers = tasker;
    }

    public Task taskers(Tasker tasker) {
        this.setTaskers(tasker);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", numberOfTaskerRequired=" + getNumberOfTaskerRequired() +
            ", budget=" + getBudget() +
            ", startDate='" + getStartDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
