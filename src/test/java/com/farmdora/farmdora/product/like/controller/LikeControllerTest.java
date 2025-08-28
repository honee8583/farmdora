package com.farmdora.farmdora.product.like.controller;

import static com.farmdora.farmdorabuyer.common.response.SuccessMessage.ADD_LIKE_SUCCESS;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.farmdora.farmdorabuyer.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LikeControllerTest extends ControllerTest {

    private static final String BASE_URL = "/api/buyer/like/{saleId}";

    @Test
    @DisplayName("찜 추가/삭제 API 테스트")
    void testUpdateLike() throws Exception {
        // given
        doNothing().when(likeService).updateLike(anyInt(), anyInt());

        // when
        // then
        mvc.perform(put(BASE_URL, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.message", equalTo(ADD_LIKE_SUCCESS.getMessage())));

        verify(likeService, times(1)).updateLike(anyInt(), anyInt());
    }
}