package de.adesso.camunda.configuration.keycloak;

import java.util.Collections;

import org.camunda.bpm.webapp.impl.security.auth.ContainerBasedAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * Camunda Web application SSO configuration for usage with
 * KeycloakIdentityProviderPlugin.
 */
@ConditionalOnMissingClass("org.springframework.test.context.junit4.SpringJUnit4ClassRunner")
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private KeycloakLogoutHandler keycloakLogoutHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.csrf().ignoringAntMatchers("/api/**", "/engine-rest/**").and().requestMatchers().antMatchers("/**").and()
		.authorizeRequests(authorizeRequests -> authorizeRequests.antMatchers("/app/**", "/api/**", "/lib/**")
			.authenticated().anyRequest().permitAll())
		.oauth2Login().and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/app/**/logout"))
		.logoutSuccessHandler(keycloakLogoutHandler);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FilterRegistrationBean containerBasedAuthenticationFilter() {

	FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
	filterRegistration.setFilter(new ContainerBasedAuthenticationFilter());
	filterRegistration.setInitParameters(Collections.singletonMap("authentication-provider",
		"de.adesso.camunda.configuration.KeycloakAuthenticationProvider"));
	filterRegistration.setOrder(101); // make sure the filter is registered after the Spring Security Filter Chain
	filterRegistration.addUrlPatterns("/app/*");
	return filterRegistration;
    }

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
	FilterRegistrationBean<ForwardedHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<>();
	filterRegistrationBean.setFilter(new ForwardedHeaderFilter());
	filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
	return filterRegistrationBean;
    }

    @Bean
    @Order(0)
    public RequestContextListener requestContextListener() {
	return new RequestContextListener();
    }
}
