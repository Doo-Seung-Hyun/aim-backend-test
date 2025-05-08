package com.aim.aimcodingtest.portfolio.service;

import com.aim.aimcodingtest.account.dto.response.AccountResponse;
import com.aim.aimcodingtest.account.service.AccountServiceImpl;
import com.aim.aimcodingtest.portfolio.dto.response.PortfolioAdviceResponse;
import com.aim.aimcodingtest.portfolio.entity.PortfolioAdvice;
import com.aim.aimcodingtest.portfolio.entity.PortfolioItem;
import com.aim.aimcodingtest.portfolio.enums.PortfolioType;
import com.aim.aimcodingtest.portfolio.repository.PortfolioAdviceRepository;
import com.aim.aimcodingtest.stock.entity.Stock;
import com.aim.aimcodingtest.stock.repository.StockRepository;
import com.aim.aimcodingtest.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {
    private final PortfolioAdviceRepository adviceRepository;
    private final AccountServiceImpl accountService;
    private final StockRepository stockRepository;

    public PortfolioAdviceResponse createAdvice(User user, String accountNumber, PortfolioType portfolioType) {
        // 유저 잔고 확인
        AccountResponse account = accountService.findAccount(accountNumber);
        Long balance = account.getBalance();

        // 포트폴리오 타입에 따라 투자가능금액 결정
        Long maxAllocateAmount = (long)(balance * portfolioType.getMaxRatio());

        // 증권 목록 가져오기
        List<Stock> stocks = stockRepository.findAll();

        // 포트폴리오 구성
        List<PortfolioItem> portfolio = createPortfolio(maxAllocateAmount, stocks, balance);

        // 저장
        PortfolioAdvice advice = PortfolioAdvice.create(user, accountNumber, portfolioType, balance);
        for(PortfolioItem portfolioItem : portfolio) {
            advice.addPortfolioItem(portfolioItem);
        }

        return PortfolioAdviceResponse.of(adviceRepository.save(advice));
    }

    private List<PortfolioItem> createPortfolio(Long maxAllocateAmount, List<Stock> stocks, Long balance) {
        Map<Long, Portfolio> curr = new HashMap<>(); //현재까지의 증권조합 (가격누적합-증권코드별수량)
        List<Portfolio> candidates = new ArrayList(); //증권조합 후보
        Set<Long> visited = new HashSet(); // 처리한적이 있는 누적투자금액목록
        
        //증권코드별 가격 맵
        Map<String, Stock> codeStockMap = stocks.stream().collect(
                Collectors.toMap(Stock::getCode, stock->stock));

        curr.put(0L, new Portfolio());

        // 최대 탐색시간 설정
        long limit_ms = 5000L;
        long endTime = System.currentTimeMillis() + limit_ms;

        while(!curr.isEmpty()) {
            if(endTime < System.currentTimeMillis()) {
                log.warn(String.format("최대 허용시간 %.1f초가 경과하여 추가 탐색을 중단합니다.",limit_ms/1000.0f));
                break;
            }
            Map<Long, Portfolio> next = new HashMap<>();

            for(Map.Entry<Long, Portfolio> entry : curr.entrySet()) {
                Portfolio portfolio = new Portfolio(entry.getValue());

                for(Stock stock : stocks) {
                    // 새로운 포트폴리오 객체 생성
                    Portfolio newPortfolio = new Portfolio(portfolio);

                    //새로운 증권을 추가하는 경우 누적 투자금액 산출
                    Long newTotalAmount = newPortfolio.getTotalAmount() + stock.getPrice();
                    
                    //이미 처리한적이 있는 누적 투자금액은 pass
                    if(visited.contains(newTotalAmount))
                        continue;

                    visited.add(newTotalAmount);

                    //증권조합의 누적 투자금액이 투자한도를 초과하면
                    if(newTotalAmount > maxAllocateAmount) {
                        //조합 후보군에 add
                        candidates.add(new Portfolio(newPortfolio));

                        //누적투자금액이 투자한도를 초과했으나 잔고보다 적은 경우
                        //근사값을 찾기 위해 투자한도를 초과한 조합도 후보군에 add
                        if(newTotalAmount < balance) {
                            Portfolio overPortfolio = new Portfolio(newPortfolio);
                            overPortfolio.addStock(stock.getCode(), stock.getPrice());
                            candidates.add(overPortfolio);
                        }
                        continue;
                    }

                    //증권조합의 누적 투자금액이 투자한도를 초과하지 않으면 새 증권을 조합에 추가
                    newPortfolio.addStock(stock.getCode(), stock.getPrice());

                    //만약 새 조합의 누적합이 이미 탐색된 조합이라면
                    //증권종류가 다양한 조합을 선택
                    Portfolio existingPortfolio = next.get(newPortfolio.getTotalAmount());
                    if(existingPortfolio != null) {
                        // 기존 포트폴리오의 증권 종류가 더 다양하면 유지, 아니면 새 포트폴리오로 교체
                        if(existingPortfolio.getStockTypeCount() < newPortfolio.getStockTypeCount()) {
                            next.put(newPortfolio.getTotalAmount(), newPortfolio);
                        }
                    } else {
                        // 아직 없는 조합이면 추가
                        next.put(newPortfolio.getTotalAmount(), newPortfolio);
                    }
                }
            }
            curr = next;
        }

        //만약 candidates가 아직 비어있다면, 마지막으로 탐색한 결과를 후보로
        if(candidates.isEmpty()) {
            candidates = new ArrayList<>(curr.values());
        }

        // 투자한도와 가장 근접한 투자조합 선택
        Portfolio bestPortfolio = candidates.stream().min((a,b)->
                Math.toIntExact(Math.abs(a.getTotalAmount() - maxAllocateAmount)
                        - Math.abs(b.getTotalAmount() - maxAllocateAmount))
        ).orElse(null);

        if(bestPortfolio == null)
            return List.of();

        //portfolioItem 리스트로 변환
        return bestPortfolio.getStockQuantityMap().entrySet().stream()
                .map(entry->PortfolioItem.builder()
                        .stock(codeStockMap.get(entry.getKey()))
                        .quantity(entry.getValue())
                        .stockPrice(codeStockMap.get(entry.getKey()).getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Getter
    class Portfolio {
        private Map<String, Integer> stockQuantityMap; // 증권코드-수량
        private Long totalAmount; // 총 투자금액
        private int stockTypeCount; // 증권종류 숫자

        public Portfolio() {
            this.stockQuantityMap = new HashMap<>();
            this.totalAmount = 0L;
            this.stockTypeCount = 0;
        }

        public Portfolio(Portfolio other) {
            this.stockQuantityMap = new HashMap<>(other.stockQuantityMap);
            this.totalAmount = other.totalAmount;
            this.stockTypeCount = other.stockTypeCount;
        }

        public void addStock(String code, long price) {
            //기존 조합에 없던 새로운 증권종류면 증권종류숫자를 증가
            if(!stockQuantityMap.containsKey(code))
                this.stockTypeCount++;
            //증권수량맵 업데이트
            stockQuantityMap.put(code, stockQuantityMap.getOrDefault(code, 0) + 1);
            //총 투자금액 업데이트
            totalAmount += price;
        }

    }
}
