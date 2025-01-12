package com.tvpss.configuration; 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Returns BCryptPasswordEncoder bean
    }
  
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests ->
            authorizeRequests
            // Only Admin can access AdminDashboard and UserManagement/editProfile
            .antMatchers("/TVPSS_Core/AdminDashboard").hasAuthority("Admin")
            .antMatchers("/TVPSS_Core/UserManagement/editProfile").hasAuthority("Admin")

            // Restrict other URLs to the Admin role
            .antMatchers("/**").denyAll() // Deny access to all other pages

            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/UserManagement/login")  // Custom login page
                    .permitAll()
            )
            .logout(logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            );

        return http.build();
    }
}
