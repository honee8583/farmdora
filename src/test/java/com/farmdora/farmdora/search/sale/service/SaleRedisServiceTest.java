package com.farmdora.farmdora.search.sale.service;

import com.farmdora.farmdora.sale.dto.SaleRankingDto;
import com.farmdora.farmdora.sale.repository.SaleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleRedisServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private SaleRedisService saleRedisService;

    @Test
    @DisplayName("인기 상품 목록 캐싱 테스트")
    void testCachePopularSales() {
        // given
        List<SaleRankingDto> dummyList = IntStream.rangeClosed(1, 50)
                .mapToObj(i -> new SaleRankingDto(i, "상품" + i, i * 1000, null, "imageUrl"))
                .toList();
        Page<SaleRankingDto> dummyPage = new PageImpl<>(dummyList);

        when(saleRepository.findTop50ByOrderCount(any(Pageable.class))).thenReturn(dummyPage);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // when
        saleRedisService.cachePopularSales();

        // then
        verify(redisTemplate.opsForValue(), times(6)).set(anyString(), any(), any(Duration.class));
        verify(valueOperations, times(6)).set(anyString(), any(), any(Duration.class));
    }

    @Test
    @DisplayName("캐싱된 인기 상품 목록 조회 테스트")
    void testFindSaleRanks() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // when
        List<SaleRankingDto> saleRanks = saleRedisService.findSaleRanks(0);

        // then
        verify(valueOperations, times(1)).get(anyString());
    }

    @Test
    @DisplayName("캐싱된 인기 상품 목록 개수 조회 테스트")
    void testFindSaleRankCount() {
        // given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // when
        Integer result = saleRedisService.findSaleRankCount();

        // then
        verify(valueOperations, times(1)).get(anyString());
    }
}