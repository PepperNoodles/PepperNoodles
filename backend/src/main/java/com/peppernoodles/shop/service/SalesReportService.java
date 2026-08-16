package com.peppernoodles.shop.service;

import com.peppernoodles.shop.api.dto.SalesReportDto;
import com.peppernoodles.shop.repository.SalesReportRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 業績報表. */
@Service
public class SalesReportService {

    private final SalesReportRepository reports;

    public SalesReportService(SalesReportRepository reports) {
        this.reports = reports;
    }

    @Transactional(readOnly = true)
    public SalesReportDto report(LocalDate from, LocalDate to, int topN) {
        return new SalesReportDto(
                reports.dailySales(from, to).stream()
                        .map(r -> new SalesReportDto.DailyPoint(
                                r.getSalesDate(), r.getOrderCount() == null ? 0 : r.getOrderCount(), r.getRevenue()))
                        .toList(),
                reports.monthlySales(from, to).stream()
                        .map(r -> new SalesReportDto.MonthlyPoint(
                                r.getMonth(), r.getOrderCount() == null ? 0 : r.getOrderCount(), r.getRevenue()))
                        .toList(),
                reports.topProducts(topN).stream()
                        .map(r -> new SalesReportDto.TopProduct(
                                r.getProductId(),
                                r.getProductName(),
                                r.getUnitsSold() == null ? 0 : r.getUnitsSold(),
                                r.getRevenue()))
                        .toList());
    }
}
