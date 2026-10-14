package softball.app.fundraising.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import softball.app.fundraising.entity.SupportCardSale;

public interface SupportCardSaleRepository
        extends JpaRepository<SupportCardSale, Long> {

    List<SupportCardSale> findBySellerUserIdOrderByCreatedAtDesc(
            Long sellerUserId);

    @Query("""
            SELECT COALESCE(SUM(s.quantity), 0)
            FROM SupportCardSale s
            WHERE s.sellerUserId = :sellerUserId
            """)
    Long countCardsSoldBySeller(
            @Param("sellerUserId") Long sellerUserId);

    @Query("""
            SELECT COALESCE(SUM(s.quantity * s.unitPrice), 0)
            FROM SupportCardSale s
            WHERE s.sellerUserId = :sellerUserId
            """)
    BigDecimal calculateRevenueBySeller(
            @Param("sellerUserId") Long sellerUserId);
}
