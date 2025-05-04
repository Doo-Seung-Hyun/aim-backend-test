package com.aim.aimcodingtest.stock.service;

import com.aim.aimcodingtest.account.entity.Account;
import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.common.exception.ErrorCode;
import com.aim.aimcodingtest.stock.dto.request.StockCreateRequest;
import com.aim.aimcodingtest.stock.dto.request.StockUpdateRequest;
import com.aim.aimcodingtest.stock.dto.response.StockCreateResponse;
import com.aim.aimcodingtest.stock.dto.response.StockDeleteResponse;
import com.aim.aimcodingtest.stock.dto.response.StockInfoResponse;
import com.aim.aimcodingtest.stock.dto.response.StockUpdateResponse;
import com.aim.aimcodingtest.stock.entity.Stock;
import com.aim.aimcodingtest.stock.entity.StockBuilder;
import com.aim.aimcodingtest.stock.repository.StockRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    Stock stock = StockBuilder.builder().build();

    @Test
    void 증권코드로_증권찾기_정상(){
        String code = stock.getCode();
        when(stockRepository.findByCode(anyString())).thenReturn(Optional.of(stock));
        StockInfoResponse response = stockService.findStockByCode(code);

        assertNotNull(response);
        assertEquals(code, response.getCode());
    }

    @Test
    void 증권코드로_증권찾기_실패(){
        String code = "wrong-code";
        when(stockRepository.findByCode(anyString())).thenReturn(Optional.empty());

        ApiException apiException = assertThrows(ApiException.class, () -> stockService.findStockByCode(code));
        assertEquals(ErrorCode.NOT_FOUND, apiException.getErrorCode());
    }

    @Test
    void 전체_증권찾기시_증권목록이_없는경우_빈_리스트_리턴(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Stock> stocks = new PageImpl<>(List.of(), pageable, 0);
        when(stockRepository.findAll(any(Pageable.class))).thenReturn(stocks);

        Page<StockInfoResponse> responses = stockService.findAllStocks(pageable);
        assertNotNull(responses);
        assertEquals(0, responses.getTotalElements());
        assertTrue(responses.getContent().isEmpty());
    }

    @Test
    void 새로운_증권등록_정상(){
        StockCreateRequest request = StockCreateRequest.builder()
                .code(stock.getCode())
                .name(stock.getName())
                .price(stock.getPrice())
                .build();
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        StockCreateResponse response = stockService.createStock(request);
        assertNotNull(response);
        assertEquals(stock.getCode(), response.getCode());
    }

    @Test
    void 증권가격_업데이트_정상(){
        String code = stock.getCode();
        Long priceToUpdate = 9999L;
        StockUpdateRequest request = StockUpdateRequest.builder()
                .price(priceToUpdate)
                .build();
        when(stockRepository.findByCode(anyString())).thenReturn(Optional.of(stock));

        StockUpdateResponse response = stockService.updateStock(code, request);
        assertNotNull(response);
        assertEquals(code, response.getCode());
        assertEquals(priceToUpdate, response.getPrice());
    }

    @Test
    void 증권가격_업데이트시_증권코드로_증권찾기_실패(){
        String code = "wrong-code";
        Long priceToUpdate = 9999L;
        StockUpdateRequest request = StockUpdateRequest.builder()
                .price(priceToUpdate)
                .build();
        when(stockRepository.findByCode(anyString())).thenReturn(Optional.empty());

        ApiException apiException = assertThrows(ApiException.class, () -> stockService.updateStock(code, request));
        assertEquals(ErrorCode.NOT_FOUND, apiException.getErrorCode());
    }

    @Test
    void 증권삭제_정상(){
        String code = stock.getCode();
        when(stockRepository.findByCode(anyString())).thenReturn(Optional.of(stock));
        doNothing().when(stockRepository).delete(any(Stock.class));

        StockDeleteResponse response = stockService.deleteStock(code);
        assertNotNull(response);
        assertEquals(code, response.getCode());
    }

    @Test
    void 증권삭제시_증권코드로_증권찾기_실패(){
        String code = "wrong-code";
        when(stockRepository.findByCode(anyString())).thenReturn(Optional.empty());

        ApiException apiException = assertThrows(ApiException.class, () -> stockService.deleteStock(code));
        assertEquals(ErrorCode.NOT_FOUND, apiException.getErrorCode());
    }
}