package com.aim.aimcodingtest.portfolio.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PortfolioType {
    DEFFENSIVE_TYPE(0.5, "잔고를 중간만 사용하는 유형"),
    AGGRESIVE_TYPE(1, "잔고를 최대한 많이 사용하는 유형");

    private final double maxRatio;
    private final String description;
}
