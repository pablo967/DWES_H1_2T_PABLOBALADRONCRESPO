package org.example.hito_2_fasttrackcourier.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;

@Configuration
public class DatabaseWebSecurity  {

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.setUsersByUsernameQuery("select dni, password, activo from usuario where dni=?");
        users.setAuthoritiesByUsernameQuery("SELECT dni, rol FROM usuario WHERE dni=?");
        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/imagenes/**").permitAll()
                .requestMatchers("/", "/login", "/signup").permitAll()
                .requestMatchers("/admin").hasAuthority("ADMINISTRADOR")
                .requestMatchers("/conductor").hasAuthority("CONDUCTOR")
                .anyRequest().authenticated());

        http.formLogin(form -> form.loginPage("/login").permitAll());

        http.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll());
        http.exceptionHandling((exception)-> exception.accessDeniedPage("/denegado"));

        return http.build();
    }
}