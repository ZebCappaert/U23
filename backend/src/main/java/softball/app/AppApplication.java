package softball.app;

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

import java.util.List;
import softball.app.config.SimpleAuthFilter;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
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
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		System.out.println("DEBUG: SecurityFilterChain wordt geconfigureerd met SimpleAuthFilter!");

		return http
				.cors(cors -> cors.configurationSource(request -> {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(
							List.of("https://softballu23.eu", "http://softballu23.eu", "http://localhost:4200",
									"https://test.softballu23.eu", "http://test.softballu23.eu"));
					config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
					config.setAllowedHeaders(List.of("*"));
					config.setAllowCredentials(true);
					config.setExposedHeaders(java.util.List.of("Authorization"));
					return config;
				}))

				.csrf(csrf -> csrf.disable())

				.addFilterBefore(new SimpleAuthFilter(), UsernamePasswordAuthenticationFilter.class)

				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/api/posts/{id}").hasAnyAuthority("COACH")
						.requestMatchers(HttpMethod.DELETE, "/api/posts/**").hasAnyAuthority("COACH")
						.requestMatchers(HttpMethod.POST, "/api/posts/**").hasAnyAuthority("COACH")
						.requestMatchers(HttpMethod.POST, "/api/reflections").hasAnyAuthority("PLAYER")
						.requestMatchers("/api/reflections/summary").hasAnyAuthority("COACH")
						.requestMatchers("/api/reflections/player/**").hasAnyAuthority("COACH")
						.anyRequest().permitAll())
				.build();
	}
}