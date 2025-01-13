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
                    // Role-based access control
                    .antMatchers("/AdminDashboard", "/UserManagement/approveUsers").hasAuthority("Admin")
                    .antMatchers("/DistrictDashboard", "/districtSchoolsStudio", "/DistrictContent",
                                 "/districtMainView", "/districtSchoolsView").hasAuthority("District Officer")
                    .antMatchers("/StateDashboard", "/stateDistrictsStudioInfo", "/StateContent",
                                 "/stateMainView", "/stateDistrictsInfo").hasAuthority("State Officer")
                    .antMatchers("/addEquip", "/SchoolContent", "/teacherMainView",
                                 "/teacherSchoolView").hasAuthority("Teacher")
                    .antMatchers("/UserManagement/editProfile").authenticated()
                    .antMatchers("/UserManagement/logout").authenticated()
                    // Permit access to static resources and login page
                    .antMatchers("/css/**", "/js/**", "/images/**", "/UserManagement/login").permitAll()
                    // Deny access to all other URLs
                    .anyRequest().denyAll()
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/UserManagement/login") // Custom login page
                    .permitAll()
            )
            .logout(logout ->
                logout
                    .logoutUrl("/UserManagement/logout")
                    .logoutSuccessUrl("/UserManagement/login?logout")
                    .permitAll()
            )
            .csrf().disable(); // Disable CSRF for development purposes (enable in production)

        return http.build();
    }
}
