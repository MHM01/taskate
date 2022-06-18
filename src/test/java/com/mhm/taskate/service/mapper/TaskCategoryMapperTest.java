package com.mhm.taskate.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskCategoryMapperTest {

    private TaskCategoryMapper taskCategoryMapper;

    @BeforeEach
    public void setUp() {
        taskCategoryMapper = new TaskCategoryMapperImpl();
    }
}
