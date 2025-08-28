package com.farmdora.farmdora.search.sale.service;

import com.farmdora.farmdora.common.NcpImageProperties;
import com.farmdora.farmdora.common.exception.ResourceNotFoundException;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.entity.*;
import com.farmdora.farmdora.opinion.repository.QuestionRepository;
import com.farmdora.farmdora.opinion.repository.ReviewRepository;
import com.farmdora.farmdora.sale.dto.*;
import com.farmdora.farmdora.sale.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private SaleFileRepository saleFileRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ReviewFileRepository reviewFileRepository;

    @Mock
    private SaleRedisService saleRedisService;

    @Mock
    private NcpImageProperties imageProperties;

    @InjectMocks
    private SaleService saleService;

    @Nested
    @DisplayName("상품 상세 정보 조회 서비스 레이어 테스트")
    class GetSaleDetailsTests {

        @Test
        @DisplayName("상품 상세 정보 조회 성공")
        void testGetSaleDetail() {
            // given
            Sale mockSale = Sale.builder()
                    .id(1)
                    .title("상추")
                    .content("유기농 상추")
                    .origin("국산")
                    .build();
            when(saleRepository.findById(anyInt())).thenReturn(Optional.of(mockSale));

            List<Option> options = List.of(
                    Option.builder()
                            .id(1)
                            .sale(mockSale)
                            .name("상추 옵션1")
                            .price(1000)
                            .build(),
                    Option.builder()
                            .id(2)
                            .sale(mockSale)
                            .name("상추 옵션2")
                            .price(2000)
                            .build()
            );
            when(optionRepository.findAllBySale(any(Sale.class))).thenReturn(options);

            List<SaleFile> saleFiles = List.of(
                    SaleFile.builder()
                            .sale(mockSale)
                            .saveFile("URL1")
                            .build(),
                    SaleFile.builder()
                            .sale(mockSale)
                            .saveFile("URL2")
                            .build()
            );
            when(saleFileRepository.findAllBySale(any(Sale.class))).thenReturn(saleFiles);

            when(likeRepository.existsByUserUserIdAndSaleId(anyInt(), anyInt())).thenReturn(true);

            NcpImageProperties.ImageInfo bannerMock = Mockito.mock(NcpImageProperties.ImageInfo.class);
            when(imageProperties.getProduct()).thenReturn(bannerMock);
            when(bannerMock.createImageUrl(anyString())).thenReturn("https://mocked-url/image.jpg");

            // when
            SaleDetailDto saleDetail = saleService.getSaleDetail(1, 1);

            // then
            assertThat(saleDetail.getContent()).isEqualTo("유기농 상추");
            assertThat(saleDetail.getOptions().size()).isEqualTo(2);
            assertThat(saleDetail.getFiles().size()).isEqualTo(2);
        }

        @Test
        @DisplayName("상세정보를 조회하고자 하는 상품이 존재하지 않을 경우 예외 발생테스트")
        void testGetSaleDetail_SaleNotFoundException() {
            // given
            when(saleRepository.findById(anyInt())).thenReturn(Optional.empty());

            // when
            // then
            assertThatThrownBy(() -> saleService.getSaleDetail(1, 1))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("관련 상품 목록 조회 서비스 레이어 테스트")
    class GetRelatedSalesTests {

        @Test
        @DisplayName("관련 상품 목록 조회 성공")
        void testGetRelatedSales() {
            // given
            Sale mockSale = Sale.builder()
                    .id(1)
                    .type(new SaleType())
                    .build();
            when(saleRepository.findById(anyInt())).thenReturn(Optional.of(mockSale));

            List<SaleRelatedInfoDto> relatedSaleDetails = List.of(
                    SaleRelatedInfoDto.builder()
                            .saleId(2)
                            .title("sale2")
                            .price(10000)
                            .reviewCount(10L)
                            .score(3.5)
                            .build(),
                    SaleRelatedInfoDto.builder()
                            .saleId(3)
                            .title("sale3")
                            .price(20000)
                            .reviewCount(12L)
                            .score(4.0)
                            .build()
            );
            when(saleRepository.findTop10SalesWithReviewCountByTypeAndExcludedId(any(SaleType.class), anyInt(), any(Pageable.class))).thenReturn(relatedSaleDetails);

            when(likeRepository.existsByUserUserIdAndSaleId(anyInt(), anyInt())).thenReturn(true);

            SaleFile mockSaleFile = SaleFile.builder()
                    .sale(mockSale)
                    .isMain(true)
                    .originFile("origin_file")
                    .saveFile("save_file")
                    .build();
            when(saleFileRepository.findBySaleIdAndIsMainIsTrue(anyInt())).thenReturn(Optional.of(mockSaleFile));

            NcpImageProperties.ImageInfo bannerMock = Mockito.mock(NcpImageProperties.ImageInfo.class);
            when(imageProperties.getProduct()).thenReturn(bannerMock);
            when(bannerMock.createImageUrl(anyString())).thenReturn("https://mocked-url/image.jpg");

            // when
            Pageable pageable = PageRequest.of(0, 10);
            List<SaleRelatedDto> relatedSales = saleService.getRelatedSales(1, 1, pageable);

            // then
            assertThat(relatedSales.size()).isEqualTo(2);
            assertThat(relatedSales.get(0).getSaleId()).isEqualTo(2);
            assertThat(relatedSales.get(1).getSaleId()).isEqualTo(3);
        }

        @Test
        @DisplayName("관련상품을 조회할 상품이 존재하지 않을 경우 예외 발생")
        void testGetRelatedSales_ResourceNotFoundException() {
            // given
            when(saleRepository.findById(anyInt())).thenReturn(Optional.empty());

            // when
            // then
            Pageable pageable = PageRequest.of(0, 10);
            assertThatThrownBy(() -> saleService.getRelatedSales(1, 1, pageable))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("상품의 리뷰 목록 조회 서비스 레이어 테스트")
    class GetSaleReviewsTests {

        @Test
        @DisplayName("상품의 리뷰 목록 조회 성공")
        void testGetSaleReviews() {
            // given
            Sale sale = Sale.builder()
                    .id(1)
                    .build();
            when(saleRepository.findById(anyInt())).thenReturn(Optional.of(sale));

            Order order = Order.builder()
                    .user(User.builder().name("user").build())
                    .build();

            List<Review> reviews = List.of(
                Review.builder()
                        .order(order)
                        .content("review1")
                        .score((byte) 2)
                        .build(),
                Review.builder()
                        .order(order)
                        .content("review2")
                        .score((byte) 3)
                        .build()
            );
            Pageable pageable = PageRequest.of(0, 10);
            PageImpl<Review> reviewPages = new PageImpl<>(reviews, pageable, 2);
            when(reviewRepository.findAllBySale(any(Sale.class), any(Pageable.class))).thenReturn(reviewPages);

            when(reviewFileRepository.findByReviewIdIn(anyList())).thenReturn(new ArrayList<>());

            // when
            PageResponseDto<ReviewDetailDto> result = saleService.getSaleReviews(1, pageable);

            // then
            assertThat(result.getContents().size()).isEqualTo(2);
        }

        @Test
        @DisplayName("상품의 리뷰 목록 조회시 상품이 존재하지 않을 경우 예외 발생")
        void testGetSaleReviews_ResourceNotFoundException() {
            // given
            when(saleRepository.findById(anyInt())).thenReturn(Optional.empty());

            // when
            // then
            Pageable pageable = PageRequest.of(0, 10);
            assertThatThrownBy(() -> saleService.getSaleReviews(1, pageable))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Test
    @DisplayName("상품의 문의 목록 조회 서비스 레이어 테스트")
    void testGetSaleQuestions() {
        // given
        Integer saleId = 1;
        Sale mockSale = Sale.builder()
                .id(saleId)
                .build();
        when(saleRepository.findById(anyInt())).thenReturn(Optional.of(mockSale));

        List<QuestionResponseDto> questionPages = List.of(
                QuestionResponseDto.builder()
                        .id(1)
                        .title("question1")
                        .writer("user1")
                        .createdDate(LocalDateTime.now())
                        .build(),
                QuestionResponseDto.builder()
                        .id(2)
                        .title("question2")
                        .writer("user2")
                        .createdDate(LocalDateTime.now())
                        .build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<QuestionResponseDto> questions = new PageImpl<>(questionPages, pageable, questionPages.size());
        when(questionRepository.findQuestionsBySaleId(any(Sale.class), any(Pageable.class))).thenReturn(questions);

        // when
        PageResponseDto<QuestionResponseDto> result = saleService.getSaleQuestions(saleId, pageable);

        // then
        System.out.println(result.toString());
        assertThat(result.getContents().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품의 랭킹 정보 DB 조회 서비스 레이어 테스트")
    void testGetSaleRankByDB() {
        // given
        when(saleRedisService.findSaleRanks(anyInt())).thenReturn(null);
        when(saleRedisService.findSaleRankCount()).thenReturn(null);

        Pageable pageable = PageRequest.of(0, 10);
        List<SaleRankingDto> sales = List.of(
                SaleRankingDto.builder()
                        .saleId(1)
                        .title("sale1")
                        .minPrice(10000)
                        .orderCount(10L)
                        .imageUrl("mocked-url")
                        .build(),
                SaleRankingDto.builder()
                        .saleId(2)
                        .title("sale2")
                        .minPrice(20000)
                        .orderCount(20L)
                        .imageUrl("mocked-url")
                        .build()
        );
        Page<SaleRankingDto> saleRanks = new PageImpl<>(sales, pageable, 2);
        when(saleRepository.findTop50ByOrderCount(pageable)).thenReturn(saleRanks);

        when(likeRepository.findSaleIdsByUserId(anyInt())).thenReturn(Set.of(1, 2, 3, 4));

        NcpImageProperties.ImageInfo bannerMock = Mockito.mock(NcpImageProperties.ImageInfo.class);
        when(imageProperties.getProduct()).thenReturn(bannerMock);
        when(bannerMock.createImageUrl(anyString())).thenReturn("https://mocked-url/image.jpg");

        // when
        PageResponseDto<SaleRankingDto> result = saleService.getTop50Sales(1, pageable);

        // then
        assertThat(result.getContents().size()).isEqualTo(2);
        assertThat(result.getPageSize()).isEqualTo(10);
        assertThat(result.getCurrentPage()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품의 랭킹 정보 캐시 조회 서비스 레이어 테스트")
    void testGetSaleRankByCache() {
        // given
        List<SaleRankingDto> saleRanks = List.of(
                SaleRankingDto.builder()
                        .imageUrl("mocked-url")
                        .build(),
                SaleRankingDto.builder()
                        .imageUrl("mocked-url")
                        .build()
        );
        when(saleRedisService.findSaleRanks(anyInt())).thenReturn(saleRanks);
        when(saleRedisService.findSaleRankCount()).thenReturn(2);

        NcpImageProperties.ImageInfo bannerMock = Mockito.mock(NcpImageProperties.ImageInfo.class);
        when(imageProperties.getProduct()).thenReturn(bannerMock);
        when(bannerMock.createImageUrl(anyString())).thenReturn("https://mocked-url/image.jpg");

        // when
        Pageable pageable = PageRequest.of(0, 10);
        PageResponseDto<SaleRankingDto> result = saleService.getTop50Sales(1, pageable);

        // then
        verify(saleRepository, times(0)).findTop50ByOrderCount(pageable);
        assertThat(result.getContents().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("특정 카테고리의 상품 목록 조회 서비스 레이어 테스트")
    void testGetSalesByCategory() {
        // given
        List<SaleSummaryDto> saleSummaries = List.of(
                SaleSummaryDto.builder()
                        .saleId(1)
                        .title("sale1")
                        .minPrice(10000)
                        .isLiked(false)
                        .build(),
                SaleSummaryDto.builder()
                        .saleId(2)
                        .title("sale2")
                        .minPrice(20000)
                        .isLiked(false)
                        .build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<SaleSummaryDto> sales = new PageImpl<>(saleSummaries, pageable, 2);
        when(saleRepository.searchSalesByCategories(anyInt(), any(CategorySearchRequestDto.class), any(Pageable.class))).thenReturn(sales);

        // when
        CategorySearchRequestDto searchCondition = CategorySearchRequestDto.builder()
                .keyword(null)
                .typeId(Short.valueOf("1"))
                .bigTypeId(Short.valueOf("2"))
                .sort(SaleSortType.PRICE_DESC)
                .build();
        PageResponseDto<SaleSummaryDto> result = saleService.getSalesByCategory(1, searchCondition, pageable);

        // then
        assertThat(result.getContents().size()).isEqualTo(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }
}