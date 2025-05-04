package com.aim.aimcodingtest.stock.controller;

import com.aim.aimcodingtest.stock.dto.request.StockCreateRequest;
import com.aim.aimcodingtest.stock.dto.request.StockUpdateRequest;
import com.aim.aimcodingtest.stock.dto.response.StockCreateResponse;
import com.aim.aimcodingtest.stock.dto.response.StockDeleteResponse;
import com.aim.aimcodingtest.stock.dto.response.StockInfoResponse;
import com.aim.aimcodingtest.stock.dto.response.StockUpdateResponse;
import com.aim.aimcodingtest.stock.entity.Stock;
import com.aim.aimcodingtest.stock.entity.StockBuilder;
import com.aim.aimcodingtest.stock.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
@EnableMethodSecurity
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StockService stockService;

    Stock stock = StockBuilder.builder().build();
    StockInfoResponse stockInfoResponse = StockInfoResponse.fromStock(stock);

    @Test
    void 로그인없이_전체증권조회_하는경우_오류() throws Exception {
        mockMvc.perform(get("/api/stock"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void ADMIN_권한없는_사용자가_전체증권조회_하는경우_오류() throws Exception {
        mockMvc.perform(get("/api/stocks"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void ADMIN_권한으로_전체증권조회_정상() throws Exception {
        PageImpl<StockInfoResponse> stockPage = new PageImpl<>(
                List.of(stockInfoResponse)
        );
        when(stockService.findAllStocks(isA(Pageable.class))).thenReturn(stockPage);

        mockMvc.perform(get("/api/stocks"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void ADMIN_권한으로_증권찾기_정상() throws Exception {
        String code = stockInfoResponse.getCode();
        when(stockService.findStockByCode(anyString())).thenReturn(stockInfoResponse);

        mockMvc.perform(get("/api/stocks/" + code))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void ADMIN_권한으로_증권등록_정상() throws Exception {
        StockCreateRequest request = StockCreateRequest.builder()
                .name(stock.getName())
                .code(stock.getCode())
                .price(stock.getPrice())
                .build();
        StockCreateResponse response = StockCreateResponse.builder()
                .name(request.getName())
                .code(request.getCode())
                .price(request.getPrice())
                .createdAt(LocalDateTime.now())
                .build();
        when(stockService.createStock(isA(StockCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/stocks" )
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void ADMIN_권한으로_증권삭제_정상() throws Exception {
        String code = stock.getCode();
        StockDeleteResponse response = StockDeleteResponse.builder()
                .name(stock.getName())
                .code(stock.getCode())
                .deletedAt(LocalDateTime.now())
                .build();
        when(stockService.deleteStock(anyString())).thenReturn(response);

        mockMvc.perform(delete("/api/stocks/"+ code)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void ADMIN_권한으로_증권가격업데이트_정상() throws Exception {
        String code = stock.getCode();
        Long priceToUpdate = 9999L;

        StockUpdateRequest request = StockUpdateRequest.builder()
                .price(priceToUpdate) // 가격 업데이트
                .build();
        StockUpdateResponse response = StockUpdateResponse.builder()
                .name(stock.getName())
                .code(stock.getCode())
                .price(priceToUpdate)
                .updatedAt(LocalDateTime.now())
                .build();
        when(stockService.updateStock(anyString(),isA(StockUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/stocks/"+ code)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}