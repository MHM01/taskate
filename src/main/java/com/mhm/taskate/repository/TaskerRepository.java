package com.mhm.taskate.repository;

import com.mhm.taskate.domain.Tasker;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tasker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskerRepository extends JpaRepository<Tasker, Long> {}
