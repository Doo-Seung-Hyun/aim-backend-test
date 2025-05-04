package com.aim.aimcodingtest.stock.controller;

import com.aim.aimcodingtest.common.dto.response.ApiResponse;
import com.aim.aimcodingtest.stock.dto.request.StockCreateRequest;
import com.aim.aimcodingtest.stock.dto.request.StockUpdateRequest;
import com.aim.aimcodingtest.stock.dto.response.StockDeleteResponse;
import com.aim.aimcodingtest.stock.dto.response.StockCreateResponse;
import com.aim.aimcodingtest.stock.dto.response.StockInfoResponse;
import com.aim.aimcodingtest.stock.dto.response.StockUpdateResponse;
import com.aim.aimcodingtest.stock.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN')") // 관리자만 주식정보에 접근할 수 있음
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    /**
     * 전체 증권 불러오기 API
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<StockInfoResponse>>> getAllStocks(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable){
        ApiResponse<Page<StockInfoResponse>> responses = ApiResponse.success(
            stockService.findAllStocks(pageable)
        );
        return responses.toResponseEntity();
    }

    /**
     * 증권 찾기 API
     */
    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<StockInfoResponse>> getStockByCode(@PathVariable String code){
        ApiResponse<StockInfoResponse> response = ApiResponse.success(
            stockService.findStockByCode(code)
        );
        return response.toResponseEntity();
    }

    /**
     * 새로운 증권 등록 API
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StockCreateResponse>> createStock(@Valid @RequestBody StockCreateRequest request){
        ApiResponse<StockCreateResponse> response = ApiResponse.success(HttpStatus.CREATED,
            stockService.createStock(request)
        );
        return response.toResponseEntity();
    }

    /**
     * 증권 수정 API (가격 업데이트)
     */
    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<StockUpdateResponse>> updateStock(
            @PathVariable String code,
            @Valid @RequestBody StockUpdateRequest request){
        ApiResponse<StockUpdateResponse> response = ApiResponse.success(
                stockService.updateStock(code, request)
        );
        return response.toResponseEntity();
    }

    /**
     * 중권 삭제 API
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<StockDeleteResponse>> deleteStock(@PathVariable String code){
        ApiResponse<StockDeleteResponse> response = ApiResponse.success(
                stockService.deleteStock(code)
        );
        return response.toResponseEntity();
    }
}
