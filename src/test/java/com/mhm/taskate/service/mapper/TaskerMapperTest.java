package com.mhm.taskate.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskerMapperTest {

    private TaskerMapper taskerMapper;

    @BeforeEach
    public void setUp() {
        taskerMapper = new TaskerMapperImpl();
    }
}
