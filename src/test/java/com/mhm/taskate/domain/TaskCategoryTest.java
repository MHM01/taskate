package com.mhm.taskate.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mhm.taskate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskCategory.class);
        TaskCategory taskCategory1 = new TaskCategory();
        taskCategory1.setId(1L);
        TaskCategory taskCategory2 = new TaskCategory();
        taskCategory2.setId(taskCategory1.getId());
        assertThat(taskCategory1).isEqualTo(taskCategory2);
        taskCategory2.setId(2L);
        assertThat(taskCategory1).isNotEqualTo(taskCategory2);
        taskCategory1.setId(null);
        assertThat(taskCategory1).isNotEqualTo(taskCategory2);
    }
}
