package com.mhm.taskate.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mhm.taskate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskerDTO.class);
        TaskerDTO taskerDTO1 = new TaskerDTO();
        taskerDTO1.setId(1L);
        TaskerDTO taskerDTO2 = new TaskerDTO();
        assertThat(taskerDTO1).isNotEqualTo(taskerDTO2);
        taskerDTO2.setId(taskerDTO1.getId());
        assertThat(taskerDTO1).isEqualTo(taskerDTO2);
        taskerDTO2.setId(2L);
        assertThat(taskerDTO1).isNotEqualTo(taskerDTO2);
        taskerDTO1.setId(null);
        assertThat(taskerDTO1).isNotEqualTo(taskerDTO2);
    }
}
