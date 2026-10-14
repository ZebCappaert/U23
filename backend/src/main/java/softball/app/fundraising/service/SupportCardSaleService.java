
package softball.app.fundraising.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import softball.app.fundraising.dto.CreateSupportCardSaleDTO;
import softball.app.fundraising.entity.SupportCard;
import softball.app.fundraising.entity.SupportCardSale;
import softball.app.fundraising.repository.SupportCardRepository;
import softball.app.fundraising.repository.SupportCardSaleRepository;
import softball.app.jpa.Role;
import softball.app.jpa.User;
import softball.app.repository.UserRepository;

@Service
public class SupportCardSaleService {

    private final SupportCardSaleRepository saleRepository;
    private final SupportCardRepository supportCardRepository;
    private final UserRepository userRepository;

    public SupportCardSaleService(
            SupportCardSaleRepository saleRepository,
            SupportCardRepository supportCardRepository,
            UserRepository userRepository) {
        this.saleRepository = saleRepository;
        this.supportCardRepository = supportCardRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SupportCardSale create(CreateSupportCardSaleDTO dto) {
        User seller = userRepository.findById(dto.sellerUserId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "De opgegeven speler bestaat niet."));

        if (seller.getRole() != Role.PLAYER) {
            throw new IllegalArgumentException(
                    "Steunkaartverkoop kan alleen aan een speler gekoppeld worden.");
        }

        SupportCardSale sale = new SupportCardSale();
        sale.setSellerUserId(seller.getId());
        sale.setBuyerName(dto.buyerName());
        sale.setQuantity(dto.quantity());
        sale.setUnitPrice(dto.unitPrice());
        sale.setNotes(dto.notes());

        SupportCardSale savedSale = saleRepository.save(sale);

        List<SupportCard> cards = new ArrayList<>();

        for (int i = 0; i < savedSale.getQuantity(); i++) {
            SupportCard card = new SupportCard();
            card.setSaleId(savedSale.getId());
            card.setSellerUserId(seller.getId());
            cards.add(card);
        }

        supportCardRepository.saveAll(cards);

        return savedSale;
    }

    @Transactional(readOnly = true)
    public List<SupportCardSale> findBySeller(Long sellerUserId) {
        return saleRepository
                .findBySellerUserIdOrderByCreatedAtDesc(sellerUserId);
    }

    @Transactional(readOnly = true)
    public Long getCardsSold(Long sellerUserId) {
        return saleRepository.countCardsSoldBySeller(sellerUserId);
    }

    @Transactional(readOnly = true)
    public BigDecimal getRevenue(Long sellerUserId) {
        return saleRepository.calculateRevenueBySeller(sellerUserId);
    }

    @Transactional(readOnly = true)
    public List<SupportCard> getCardsBySale(Long saleId) {
        return supportCardRepository
                .findBySaleIdOrderByIdAsc(saleId);
    }

    @Transactional(readOnly = true)
    public List<SupportCard> getCardsBySeller(Long sellerUserId) {
        return supportCardRepository
                .findBySellerUserIdOrderByIdAsc(sellerUserId);
    }
}
