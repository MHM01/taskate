package com.mhm.taskate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TaskCategory.
 */
@Entity
@Table(name = "task_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TaskCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "taskCategories")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "address", "tasks", "taskCategories" }, allowSetters = true)
    private Set<Tasker> taskers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TaskCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TaskCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public TaskCategory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Tasker> getTaskers() {
        return this.taskers;
    }

    public void setTaskers(Set<Tasker> taskers) {
        if (this.taskers != null) {
            this.taskers.forEach(i -> i.setTaskCategories(null));
        }
        if (taskers != null) {
            taskers.forEach(i -> i.setTaskCategories(this));
        }
        this.taskers = taskers;
    }

    public TaskCategory taskers(Set<Tasker> taskers) {
        this.setTaskers(taskers);
        return this;
    }

    public TaskCategory addTasker(Tasker tasker) {
        this.taskers.add(tasker);
        tasker.setTaskCategories(this);
        return this;
    }

    public TaskCategory removeTasker(Tasker tasker) {
        this.taskers.remove(tasker);
        tasker.setTaskCategories(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskCategory)) {
            return false;
        }
        return id != null && id.equals(((TaskCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
