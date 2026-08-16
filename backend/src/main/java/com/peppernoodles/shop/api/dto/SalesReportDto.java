package com.peppernoodles.shop.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** 業績報表 — feeds the Chart.js dashboards. */
public record SalesReportDto(
        List<DailyPoint> daily, List<MonthlyPoint> monthly, List<TopProduct> topProducts) {

    public record DailyPoint(LocalDate date, long orderCount, BigDecimal revenue) {}

    public record MonthlyPoint(String month, long orderCount, BigDecimal revenue) {}

    public record TopProduct(Long productId, String productName, long unitsSold, BigDecimal revenue) {}
}
