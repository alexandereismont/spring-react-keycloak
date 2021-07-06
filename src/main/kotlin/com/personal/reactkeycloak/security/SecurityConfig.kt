package com.personal.reactkeycloak.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import java.lang.Exception
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Scope

@Configuration
@EnableWebSecurity
class SecurityConfig(): KeycloakWebSecurityConfigurerAdapter() {

	@Throws(Exception::class)
	override fun configure(http: HttpSecurity) {
		super.configure(http);
		http
				.authorizeRequests()
				.antMatchers("/admin/hello").hasRole("manager")
		http.cors().and().csrf().disable()
	}

	@Bean
	override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy? {
		return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
	}

	@Bean
	fun KeycloakConfigResolver(): KeycloakSpringBootConfigResolver? {
		return KeycloakSpringBootConfigResolver()
	}

	@Autowired
	@Throws(Exception::class)
	fun configureGlobal(
			auth: AuthenticationManagerBuilder) {
		val keycloakAuthenticationProvider = keycloakAuthenticationProvider()
		keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(SimpleAuthorityMapper())
		auth.authenticationProvider(keycloakAuthenticationProvider)
	}

	/*
	@Autowired
	fun configureGlobal(auth: AuthenticationManagerBuilder) {
		val grantedAuthorityMapper = SimpleAuthorityMapper()
		grantedAuthorityMapper.setPrefix("ROLE_")
		grantedAuthorityMapper.setConvertToUpperCase(true)
		val keycloakAuthenticationProvider = keycloakAuthenticationProvider()
		keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper)
		auth.authenticationProvider(keycloakAuthenticationProvider)
	}*/
}