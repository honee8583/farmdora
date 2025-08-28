package com.farmdora.farmdora.search.admin.controller;

import com.farmdora.farmdora.ControllerTest;
import com.farmdora.farmdora.admin.dto.UserSearchRequestDto;
import com.farmdora.farmdora.admin.dto.UserSearchResponseDto;
import com.farmdora.farmdora.common.response.PageResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_USERS_SUCCESS;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTest {

    @Test
    @DisplayName("관리자의 사용자 목록 검색 API 테스트")
    void testSearchUsers() throws Exception {
        // given
        List<UserSearchResponseDto> users = List.of(
                UserSearchResponseDto.builder()
                        .userId(1)
                        .isBlind(true)
                        .name("user1")
                        .isSeller(false)
                        .build(),
                UserSearchResponseDto.builder()
                        .userId(2)
                        .isBlind(true)
                        .name("user2")
                        .isSeller(true)
                        .build()
        );
        PageResponseDto<UserSearchResponseDto> pageResult = new PageResponseDto<>();
        pageResult.setContents(users);
        pageResult.setTotalElements(2L);
        when(userService.searchUsers(any(UserSearchRequestDto.class), any(Pageable.class))).thenReturn(pageResult);

        // when
        // then
        mvc.perform(get("/api/search/admin/user")
                .param("keyword", "user")
                .param("types", "USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.message", equalTo(SEARCH_USERS_SUCCESS.getMessage())))
                .andExpect(jsonPath("$.data.contents.size()", equalTo(2)));
    }
}