package com.personal.reactkeycloak

import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestContextHolder
import java.security.Principal

@RestController
class Hello {

	@GetMapping("/hello")
	fun hello(model: Model, principal: Principal) : String {
		val keyPrincipal: KeycloakAuthenticationToken = principal as KeycloakAuthenticationToken
		val token = (keyPrincipal.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token
		return "Hello"
	}

	@GetMapping("/admin/hello")
	fun helloAdmin() = "Admin Hello"


}