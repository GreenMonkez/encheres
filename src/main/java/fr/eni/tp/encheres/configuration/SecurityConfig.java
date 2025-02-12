package fr.eni.tp.encheres.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final String SELECT_USER = "select pseudo, mot_de_passe, 'true' as enable from UTILISATEURS where pseudo=?";
	private final String SELECT_ROLES = "select u.pseudo, r.role from UTILISATEURS u inner join ROLES r on r.IS_ADMIN = u.administrateur where u.pseudo = ?";

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/*").permitAll().requestMatchers("/").permitAll().requestMatchers("/css/*")
					.permitAll().requestMatchers("/images/*").permitAll().requestMatchers("/js/*").permitAll()
					.requestMatchers("/login").permitAll().requestMatchers("/login/session").permitAll()
					.requestMatchers("/inscription").permitAll().requestMatchers("/encheres").permitAll()
					.requestMatchers("/encheres/nouvelleVente").hasRole("USER").requestMatchers("/encheres/search")
					.permitAll().anyRequest().denyAll();
		});

		// Customiser le formulaire

		http.formLogin(form -> {
			form.usernameParameter("pseudo").passwordParameter("mot_de_passe").loginPage("/login")
					.defaultSuccessUrl("/login/session");

			form.usernameParameter("pseudo").passwordParameter("mot_de_passe");
			form.loginPage("/login").permitAll().failureUrl("/login?error=true").defaultSuccessUrl("/").permitAll();
		});

		// /logout --> vider la session

		http.logout(logout -> logout.invalidateHttpSession(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).logoutSuccessUrl("/"));

		return http.build();

	}

	@Bean
	UserDetailsManager userDetailsManager(DataSource dataSource) {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.setUsersByUsernameQuery(SELECT_USER);
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(SELECT_ROLES);
		return jdbcUserDetailsManager;
	}

}
