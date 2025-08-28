package com.farmdora.farmdora.search.admin.repository;

import com.farmdora.farmdora.admin.dto.UserSearchRequestDto;
import com.farmdora.farmdora.admin.dto.UserSearchResponseDto;
import com.farmdora.farmdora.admin.dto.UserType;
import com.farmdora.farmdora.entity.Seller;
import com.farmdora.farmdora.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomUserRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("관리자 사용자 검색 QueryDsl 테스트")
    void testSearchUsers() {
        // given
        for (int i = 1; i <= 15; i++) {
            User user = User.builder()
                    .id("user" + i)
                    .name("user" + i)
                    .isBlind(false)
                    .build();
            em.persist(user);

            if (i % 2 == 0) {
                Seller seller1 = Seller.builder()
                        .user(user)
                        .build();
                em.persist(seller1);
                Seller seller2 = Seller.builder()
                        .user(user)
                        .build();
                em.persist(seller2);
            }
        }

        // when
        UserSearchRequestDto searchSellerCondition = UserSearchRequestDto.builder()
                .keyword("user")
                .types(List.of(UserType.SELLER))
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserSearchResponseDto> sellers = userRepository.searchUsers(searchSellerCondition, pageable);

        UserSearchRequestDto searchUserCondition = UserSearchRequestDto.builder()
                .keyword("user")
                .types(List.of(UserType.USER))
                .build();
        Page<UserSearchResponseDto> users = userRepository.searchUsers(searchUserCondition, pageable);

        UserSearchRequestDto searchAllCondition = UserSearchRequestDto.builder()
                .keyword("user")
                .types(List.of(UserType.USER, UserType.SELLER))
                .build();
        Page<UserSearchResponseDto> allUsers = userRepository.searchUsers(searchAllCondition, pageable);

        // then
        assertThat(sellers.getContent().size()).isEqualTo(7);
        assertThat(sellers.getContent().get(0).getIsSeller()).isEqualTo(true);
        assertThat(users.getContent().size()).isEqualTo(8);
        assertThat(users.getContent().get(0).getIsSeller()).isEqualTo(false);
        assertThat(allUsers.getContent().size()).isEqualTo(10);
        assertThat(allUsers.getTotalElements()).isEqualTo(15);
    }
}