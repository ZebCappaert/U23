
package softball.app.fundraising.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import softball.app.fundraising.entity.SupportCard;
import softball.app.fundraising.repository.SupportCardRepository;

@RestController
@RequestMapping("/api/fundraising/cards")
public class SupportCardController {

    private final SupportCardRepository supportCardRepository;

    public SupportCardController(
            SupportCardRepository supportCardRepository) {
        this.supportCardRepository = supportCardRepository;
    }

    @GetMapping
    public List<SupportCard> findAll() {
        return supportCardRepository.findAllByOrderByIdAsc();
    }

    @GetMapping("/{cardId}")
    public SupportCard findById(@PathVariable Long cardId) {
        return supportCardRepository.findById(cardId)
                .orElseThrow(() -> new java.util.NoSuchElementException(
                        "Steunkaart niet gevonden."));
    }
}
