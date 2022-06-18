package com.mhm.taskate.repository;

import com.mhm.taskate.domain.TaskCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TaskCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {}
