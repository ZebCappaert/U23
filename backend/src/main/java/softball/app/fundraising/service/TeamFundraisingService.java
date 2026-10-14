
package softball.app.fundraising.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import softball.app.fundraising.dto.TeamFundraisingSummaryDTO;
import softball.app.fundraising.dto.TeamFundraisingTotalsDTO;
import softball.app.fundraising.repository.SupportCardSaleRepository;
import softball.app.jpa.Role;
import softball.app.jpa.User;
import softball.app.repository.UserRepository;

@Service
public class TeamFundraisingService {

    private final UserRepository userRepository;
    private final SupportCardSaleRepository saleRepository;

    public TeamFundraisingService(
            UserRepository userRepository,
            SupportCardSaleRepository saleRepository) {
        this.userRepository = userRepository;
        this.saleRepository = saleRepository;
    }

    @Transactional(readOnly = true)
    public List<TeamFundraisingSummaryDTO> getTeamSummary() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.PLAYER)
                .map(this::toSummary)
                .sorted(
                        Comparator.comparing(
                                TeamFundraisingSummaryDTO::cardsSold,
                                Comparator.reverseOrder())
                                .thenComparing(
                                        TeamFundraisingSummaryDTO::lastName,
                                        Comparator.nullsLast(
                                                String.CASE_INSENSITIVE_ORDER))
                                .thenComparing(
                                        TeamFundraisingSummaryDTO::firstName,
                                        Comparator.nullsLast(
                                                String.CASE_INSENSITIVE_ORDER)))
                .toList();
    }

    @Transactional(readOnly = true)
    public TeamFundraisingTotalsDTO getTeamTotals() {
        List<TeamFundraisingSummaryDTO> players = getTeamSummary();

        long totalCardsSold = players.stream()
                .mapToLong(TeamFundraisingSummaryDTO::cardsSold)
                .sum();

        BigDecimal totalRevenue = players.stream()
                .map(TeamFundraisingSummaryDTO::revenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long activeSellers = players.stream()
                .filter(player -> player.cardsSold() > 0)
                .count();

        return new TeamFundraisingTotalsDTO(
                totalCardsSold,
                totalRevenue,
                activeSellers);
    }

    private TeamFundraisingSummaryDTO toSummary(User user) {
        Long cardsSold = saleRepository.countCardsSoldBySeller(user.getId());

        BigDecimal revenue = saleRepository.calculateRevenueBySeller(user.getId());

        return new TeamFundraisingSummaryDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                cardsSold,
                revenue);
    }
}
