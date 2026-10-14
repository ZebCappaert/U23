
package softball.app.fundraising.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import softball.app.fundraising.dto.TeamFundraisingSummaryDTO;
import softball.app.fundraising.dto.TeamFundraisingTotalsDTO;
import softball.app.fundraising.service.TeamFundraisingService;

@RestController
@RequestMapping("/api/fundraising/team")
public class TeamFundraisingController {

    private final TeamFundraisingService teamFundraisingService;

    public TeamFundraisingController(
            TeamFundraisingService teamFundraisingService) {
        this.teamFundraisingService = teamFundraisingService;
    }

    @GetMapping("/summary")
    public List<TeamFundraisingSummaryDTO> getTeamSummary() {
        return teamFundraisingService.getTeamSummary();
    }

    @GetMapping("/totals")
    public TeamFundraisingTotalsDTO getTeamTotals() {
        return teamFundraisingService.getTeamTotals();
    }
}
