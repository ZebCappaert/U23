
package softball.app.fundraising.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import softball.app.fundraising.dto.FundraisingActivityDTO;
import softball.app.fundraising.entity.FundraisingActivity;
import softball.app.fundraising.service.FundraisingActivityService;

@RestController
@RequestMapping("/api/fundraising/activities")
public class FundraisingActivityController {

    private final FundraisingActivityService service;

    public FundraisingActivityController(
            FundraisingActivityService service) {
        this.service = service;
    }

    @GetMapping
    public List<FundraisingActivity> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public FundraisingActivity findById(@PathVariable Long id) {
        try {
            return service.findById(id);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping
    public FundraisingActivity create(
            @Valid @RequestBody FundraisingActivityDTO dto,
            Authentication authentication) {
        requireCoach(authentication);

        try {
            return service.create(dto);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public FundraisingActivity update(
            @PathVariable Long id,
            @Valid @RequestBody FundraisingActivityDTO dto,
            Authentication authentication) {
        requireCoach(authentication);

        try {
            return service.update(id, dto);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            Authentication authentication) {
        requireCoach(authentication);

        try {
            service.delete(id);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    private void requireCoach(Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getAuthorities().stream()
                        .noneMatch(authority -> authority.getAuthority().equals("COACH")
                                || authority.getAuthority()
                                        .equals("ROLE_COACH"))) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Alleen coaches mogen fundraisingacties beheren.");
        }
    }
}
