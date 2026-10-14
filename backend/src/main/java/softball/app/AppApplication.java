
package softball.app;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import softball.app.config.SimpleAuthFilter;

@SpringBootApplication(exclude = {
		UserDetailsServiceAutoConfiguration.class
})
@EnableWebSecurity
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Primary
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			SimpleAuthFilter simpleAuthFilter) throws Exception {

		return http
				.cors(cors -> cors.configurationSource(request -> {
					CorsConfiguration config = new CorsConfiguration();

					config.setAllowedOrigins(List.of(
							"https://softballu23.eu",
							"http://softballu23.eu",
							"http://localhost:4200",
							"http://localhost:3000",
							"https://test.softballu23.eu",
							"http://test.softballu23.eu"));

					config.setAllowedMethods(List.of(
							"GET", "POST", "PUT", "DELETE", "OPTIONS"));

					config.setAllowedHeaders(List.of(
							"Authorization", "Content-Type"));

					config.setAllowCredentials(true);
					config.setExposedHeaders(List.of("Authorization"));

					return config;
				}))
				.csrf(csrf -> csrf.disable())
				.addFilterBefore(
						simpleAuthFilter,
						UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests(auth -> auth

						// CORS-preflight
						.requestMatchers(HttpMethod.OPTIONS, "/**")
						.permitAll()

						// Login en registratie
						.requestMatchers(
								HttpMethod.POST,
								"/api/users/login",
								"/api/users/")
						.permitAll()

						// Publiek leesbare fundraisinginformatie
						.requestMatchers(
								HttpMethod.GET,
								"/api/fundraising/activities",
								"/api/fundraising/activities/**",
								"/api/fundraising/settings",
								"/api/fundraising/team/**")
						.permitAll()

						// Alleen ingelogde gebruikers mogen kaarten bekijken
						.requestMatchers(
								HttpMethod.GET,
								"/api/fundraising/cards",
								"/api/fundraising/cards/**")
						.authenticated()

						// Verkoopgeschiedenis en kaartdetails:
						// ingelogde gebruikers; eigenaarscontrole volgt
						// aanvullend in de controller.
						.requestMatchers(
								HttpMethod.GET,
								"/api/fundraising/sales/**")
						.authenticated()

						// Verkopen registreren: alleen ingelogde spelers.
						// De controller moet ook controleren dat de speler
						// alleen voor zichzelf een verkoop registreert.
						.requestMatchers(
								HttpMethod.POST,
								"/api/fundraising/sales")
						.hasAuthority("PLAYER")

						// Fundraisingactiviteiten beheren: coaches
						.requestMatchers(
								HttpMethod.POST,
								"/api/fundraising/activities",
								"/api/fundraising/activities/**")
						.hasAuthority("COACH")

						.requestMatchers(
								HttpMethod.PUT,
								"/api/fundraising/activities/**",
								"/api/fundraising/settings")
						.hasAuthority("COACH")

						.requestMatchers(
								HttpMethod.DELETE,
								"/api/fundraising/activities/**")
						.hasAuthority("COACH")

						// Berichten
						.requestMatchers(
								HttpMethod.GET,
								"/api/posts/**")
						.permitAll()

						.requestMatchers(
								HttpMethod.DELETE,
								"/api/posts/**")
						.hasAuthority("COACH")

						.requestMatchers(
								HttpMethod.POST,
								"/api/posts/**")
						.hasAuthority("COACH")

						// Reflecties
						.requestMatchers(
								HttpMethod.POST,
								"/api/reflections")
						.hasAuthority("PLAYER")

						.requestMatchers(
								"/api/reflections/summary",
								"/api/reflections/player/**")
						.hasAuthority("COACH")

						// Overige endpoints vereisen authenticatie.
						.anyRequest().authenticated())
				.build();
	}
}
