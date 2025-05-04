package com.aim.aimcodingtest.stock.service;

import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.common.exception.ErrorCode;
import com.aim.aimcodingtest.stock.dto.request.StockCreateRequest;
import com.aim.aimcodingtest.stock.dto.request.StockUpdateRequest;
import com.aim.aimcodingtest.stock.dto.response.StockCreateResponse;
import com.aim.aimcodingtest.stock.dto.response.StockDeleteResponse;
import com.aim.aimcodingtest.stock.dto.response.StockInfoResponse;
import com.aim.aimcodingtest.stock.dto.response.StockUpdateResponse;
import com.aim.aimcodingtest.stock.entity.Stock;
import com.aim.aimcodingtest.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    /**
     * 증권 찾기
     */
    @Transactional(readOnly = true)
    public StockInfoResponse findStockByCode(String code){
        Stock stock = stockRepository.findByCode(code)
                .orElseThrow(()->new ApiException(ErrorCode.NOT_FOUND));
        return StockInfoResponse.fromStock(stock);
    }

    /**
     * 전체 증권 불러오기 (페이징)
     */
    @Transactional(readOnly = true)
    public Page<StockInfoResponse> findAllStocks(Pageable pageable){
        return stockRepository.findAll(pageable).map(StockInfoResponse::fromStock);
    }

    /**
     * 전체 증권 불러오기 (리스트)
     */
    @Transactional(readOnly = true)
    public List<StockInfoResponse> findAllStocks(){
        return stockRepository.findAll().stream().map(StockInfoResponse::fromStock).collect(Collectors.toList());
    }

    /**
     * 새로운 증권 등록
     */
    public StockCreateResponse createStock(StockCreateRequest request){
        Stock stock = stockRepository.save(Stock.of(request));
        return StockCreateResponse.builder()
                .code(stock.getCode())
                .name(stock.getName())
                .price(stock.getPrice())
                .createdAt(stock.getCreatedAt())
                .build();
    }

    /**
     * 증권 수정 (가격 업데이트)
     */
    public StockUpdateResponse updateStock(String code, StockUpdateRequest request){
        Stock stock = stockRepository.findByCode(code)
                .orElseThrow(()->new ApiException(ErrorCode.NOT_FOUND));

        boolean isUpdated = false;

        if(request.getCode() != null && !request.getCode().equals(stock.getCode())){
            stock.setCode(code);
            isUpdated = true;
        }

        if(request.getName() != null && !request.getName().equals(stock.getName())){
            stock.setName(request.getName());
            isUpdated = true;
        }

        if(request.getPrice() != null && !request.getPrice().equals(stock.getPrice())){
            stock.setPrice(request.getPrice());
            isUpdated = true;
        }

        return StockUpdateResponse.builder()
                .code(stock.getCode())
                .name(stock.getName())
                .price(stock.getPrice())
                .isUpdated(isUpdated)
                .updatedAt(isUpdated ? LocalDateTime.now() : null)
                .build();
    }

    /**
     * 증권 삭제
     */
    public StockDeleteResponse deleteStock(String code){
        Stock stock = stockRepository.findByCode(code)
                .orElseThrow(()->new ApiException(ErrorCode.NOT_FOUND));
        stockRepository.delete(stock);
        return StockDeleteResponse.builder()
                .code(code)
                .name(stock.getName())
                .deletedAt(LocalDateTime.now())
                .build();
    }
}
