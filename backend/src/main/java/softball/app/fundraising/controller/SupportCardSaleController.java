
package softball.app.fundraising.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import softball.app.fundraising.dto.CreateSupportCardSaleDTO;
import softball.app.fundraising.entity.SupportCard;
import softball.app.fundraising.entity.SupportCardSale;
import softball.app.fundraising.service.SupportCardSaleService;

@RestController
@RequestMapping("/api/fundraising/sales")
public class SupportCardSaleController {

    private final SupportCardSaleService saleService;

    public SupportCardSaleController(
            SupportCardSaleService saleService) {
        this.saleService = saleService;
    }

    /**
     * Registreert een nieuwe verkoop.
     * Alleen ingelogde spelers mogen dit endpoint gebruiken.
     * De toegestane rol wordt ook gecontroleerd door Spring Security.
     */
    @PostMapping
    public SupportCardSale create(
            @Valid @RequestBody CreateSupportCardSaleDTO dto,
            Authentication authentication) {

        requirePlayer(authentication);

        try {
            return saleService.create(dto);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ex.getMessage());
        }
    }

    /**
     * Verkoopgeschiedenis van een speler.
     * Verkoopgegevens mogen door spelers en coaches bekeken worden.
     */
    @GetMapping("/seller/{sellerUserId}")
    public List<SupportCardSale> findBySeller(
            @PathVariable Long sellerUserId) {
        return saleService.findBySeller(sellerUserId);
    }

    /**
     * Samenvatting van een speler.
     */
    @GetMapping("/seller/{sellerUserId}/summary")
    public Map<String, Object> getSummary(
            @PathVariable Long sellerUserId) {
        return Map.of(
                "sellerUserId", sellerUserId,
                "cardsSold", saleService.getCardsSold(sellerUserId),
                "revenue", saleService.getRevenue(sellerUserId));
    }

    /**
     * Kaarten die bij een specifieke verkoop horen.
     */
    @GetMapping("/{saleId}/cards")
    public List<SupportCard> getCardsBySale(
            @PathVariable Long saleId) {
        return saleService.getCardsBySale(saleId);
    }

    /**
     * Alle kaarten van een speler.
     */
    @GetMapping("/seller/{sellerUserId}/cards")
    public List<SupportCard> getCardsBySeller(
            @PathVariable Long sellerUserId) {
        return saleService.getCardsBySeller(sellerUserId);
    }

    private void requirePlayer(Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getAuthorities().stream()
                        .noneMatch(authority -> authority.getAuthority().equals("PLAYER")
                                || authority.getAuthority().equals("ROLE_PLAYER"))) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Alleen spelers mogen verkopen registreren.");
        }
    }
}
