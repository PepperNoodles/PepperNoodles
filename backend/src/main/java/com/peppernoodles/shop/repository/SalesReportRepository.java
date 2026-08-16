package com.peppernoodles.shop.repository;

import com.peppernoodles.shop.domain.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 商品銷售報表.
 *
 * <p>The legacy reports bucketed by day with {@code convert(varchar, date, 111)},
 * a SQL Server built-in that does not exist in PostgreSQL — those queries could
 * not have run at all after the migration. These read the {@code daily_sales} and
 * {@code product_sales} views instead.
 */
@Repository
public interface SalesReportRepository extends JpaRepository<Order, Long> {

    @Query(value = """
            select sales_date as salesDate, order_count as orderCount, revenue as revenue
              from daily_sales
             where sales_date between :from and :to
             order by sales_date
            """, nativeQuery = true)
    List<DailySalesRow> dailySales(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query(value = """
            select to_char(date_trunc('month', sales_date), 'YYYY-MM') as month,
                   sum(order_count)                                    as orderCount,
                   sum(revenue)                                        as revenue
              from daily_sales
             where sales_date between :from and :to
             group by 1
             order by 1
            """, nativeQuery = true)
    List<MonthlySalesRow> monthlySales(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query(value = """
            select product_id as productId, product_name as productName,
                   units_sold as unitsSold, revenue as revenue
              from product_sales
             order by revenue desc nulls last
             limit :limit
            """, nativeQuery = true)
    List<ProductSalesRow> topProducts(@Param("limit") int limit);

    interface DailySalesRow {
        LocalDate getSalesDate();

        Long getOrderCount();

        BigDecimal getRevenue();
    }

    interface MonthlySalesRow {
        String getMonth();

        Long getOrderCount();

        BigDecimal getRevenue();
    }

    interface ProductSalesRow {
        Long getProductId();

        String getProductName();

        Long getUnitsSold();

        BigDecimal getRevenue();
    }
}
