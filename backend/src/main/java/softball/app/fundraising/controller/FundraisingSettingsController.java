
package softball.app.fundraising.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import softball.app.fundraising.dto.UpdateFundraisingSettingsDTO;
import softball.app.fundraising.entity.FundraisingSettings;
import softball.app.fundraising.service.FundraisingSettingsService;

@RestController
@RequestMapping("/api/fundraising/settings")
public class FundraisingSettingsController {

    private final FundraisingSettingsService service;

    public FundraisingSettingsController(
            FundraisingSettingsService service) {
        this.service = service;
    }

    @GetMapping
    public FundraisingSettings getSettings() {
        return service.getSettings();
    }

    @PutMapping
    public FundraisingSettings updateSettings(
            @Valid @RequestBody UpdateFundraisingSettingsDTO dto,
            Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getAuthorities().stream()
                        .noneMatch(authority -> authority.getAuthority().equals("COACH")
                                || authority.getAuthority()
                                        .equals("ROLE_COACH"))) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Alleen coaches mogen de fundraisinginstellingen wijzigen.");
        }

        try {
            return service.updateSettings(dto);
        } catch (ArithmeticException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Het doelbedrag mag maximaal twee decimalen hebben.");
        }
    }
}
