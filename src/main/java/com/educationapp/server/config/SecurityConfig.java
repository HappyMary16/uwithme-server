package com.educationapp.server.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;


@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    public KeycloakClientRequestFactory keycloakClientRequestFactory;

    /**
     * Filters to prevent stack overflow exception caused by Spring/Keycloak integration
     *
     * @param filter instance of {@link KeycloakAuthenticationProcessingFilter}
     * @return the {@link FilterRegistrationBean}
     */
    @Bean
    @SuppressWarnings("unchecked")
    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
            final KeycloakAuthenticationProcessingFilter filter) {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    /**
     * Filters to prevent stack overflow exception caused by Spring/Keycloak integration
     *
     * @param filter instance of {@link KeycloakPreAuthActionsFilter}
     * @return the {@link FilterRegistrationBean}
     */
    @Bean
    @SuppressWarnings("unchecked")
    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
            final KeycloakPreAuthActionsFilter filter) {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    /**
     * Filters to prevent stack overflow exception caused by Spring/Keycloak integration
     *
     * @param filter instance of {@link KeycloakAuthenticatedActionsFilter}
     * @return the {@link FilterRegistrationBean}
     */
    @Bean
    @SuppressWarnings("unchecked")
    public FilterRegistrationBean keycloakAuthenticatedActionsFilterBean(
            final KeycloakAuthenticatedActionsFilter filter) {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    /**
     * Filters to prevent stack overflow exception caused by Spring/Keycloak integration
     *
     * @param filter instance of {@link KeycloakSecurityContextRequestFilter}
     * @return the {@link FilterRegistrationBean}
     */
    @Bean
    @SuppressWarnings("unchecked")
    public FilterRegistrationBean keycloakSecurityContextRequestFilterBean(
            final KeycloakSecurityContextRequestFilter filter) {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    /**
     * Registers the KeycloakAuthenticationProvider with the authentication manager.
     */
    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    /**
     * Defines the session authentication strategy.
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * Instructs Keycloak/Spring to use application.properties file instead of keycloak.json file.
     *
     * @return instance of {@link KeycloakConfigResolver}
     */
    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    /**
     * Specifies the Keycloak Rest Template
     *
     * @return instance of {@link KeycloakRestTemplate}
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakRestTemplate keycloakRestTemplate() {
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }

    /**
     * Whitelists the endpoint to get a token
     *
     * @param web Instance of {@link WebSecurity}
     */
    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/login", "/api/tc/*/*/Log", "/api/tc/*/*/terminal-info");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);

        http.csrf()
            .disable();

        http.authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers("/users/**").hasRole("USER")
            .anyRequest()
            .permitAll();
    }
}