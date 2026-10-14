
package softball.app.config;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import softball.app.repository.UserRepository;
import softball.app.service.AuthTokenService;

@Component
public class SimpleAuthFilter extends OncePerRequestFilter {

    private final AuthTokenService tokenService;
    private final UserRepository userRepository;

    public SimpleAuthFilter(
            AuthTokenService tokenService,
            UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7).trim();
            Long userId = tokenService.validateToken(token);

            if (userId != null) {
                userRepository.findById(userId).ifPresent(user -> {
                    var authority = new SimpleGrantedAuthority(
                            user.getRole().name());

                    var authentication = new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            null,
                            java.util.List.of(authority));

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                });
            }
        }

        filterChain.doFilter(request, response);
    }
}
