
package softball.app.fundraising.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import softball.app.fundraising.entity.SupportCard;

public interface SupportCardRepository
        extends JpaRepository<SupportCard, Long> {

    List<SupportCard> findBySaleIdOrderByIdAsc(Long saleId);

    List<SupportCard> findBySellerUserIdOrderByIdAsc(Long sellerUserId);

    List<SupportCard> findAllByOrderByIdAsc();
}
