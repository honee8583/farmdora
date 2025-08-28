package com.farmdora.farmdora.search.admin.service;

import com.farmdora.farmdora.admin.dto.UserSearchRequestDto;
import com.farmdora.farmdora.admin.dto.UserSearchResponseDto;
import com.farmdora.farmdora.admin.dto.UserType;
import com.farmdora.farmdora.admin.repository.UserRepository;
import com.farmdora.farmdora.common.response.PageResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자 목록 검색 서비스 레이어 테스트")
    void testSearchUsers() {
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
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserSearchResponseDto> userPage = new PageImpl<>(users, pageable, 2);
        when(userRepository.searchUsers(any(UserSearchRequestDto.class), any(Pageable.class))).thenReturn(userPage);

        // when
        UserSearchRequestDto searchCondition = UserSearchRequestDto.builder()
                .keyword("user")
                .types(List.of(UserType.USER))
                .build();
        PageResponseDto<UserSearchResponseDto> result = userService.searchUsers(searchCondition, pageable);

        // then
        assertThat(result.getContents().size()).isEqualTo(2);
    }
}