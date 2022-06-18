package com.mhm.taskate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mhm.taskate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskCategoryDTO.class);
        TaskCategoryDTO taskCategoryDTO1 = new TaskCategoryDTO();
        taskCategoryDTO1.setId(1L);
        TaskCategoryDTO taskCategoryDTO2 = new TaskCategoryDTO();
        assertThat(taskCategoryDTO1).isNotEqualTo(taskCategoryDTO2);
        taskCategoryDTO2.setId(taskCategoryDTO1.getId());
        assertThat(taskCategoryDTO1).isEqualTo(taskCategoryDTO2);
        taskCategoryDTO2.setId(2L);
        assertThat(taskCategoryDTO1).isNotEqualTo(taskCategoryDTO2);
        taskCategoryDTO1.setId(null);
        assertThat(taskCategoryDTO1).isNotEqualTo(taskCategoryDTO2);
    }
}
