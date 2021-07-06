package com.personal.reactkeycloak

import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RealmRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.lang.Exception
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashMap
/**
@Slf4j
@RequiredArgsConstructor
@Component
class KeycloakInitializerRunner : CommandLineRunner {
	@Value("\${keycloak.auth-server-url}")
	private val keycloakServerUrl: String? = null
	private val keycloakAdmin: Keycloak? = null

	@Throws(Exception::class)
	override fun run(vararg args: String) {
		log.info("Initializing '{}' realm in Keycloak ...", COMPANY_SERVICE_REALM_NAME)
		val representationOptional: Optional<RealmRepresentation> = keycloakAdmin.realms().findAll().stream()
				.filter { r -> r.getRealm().equals(COMPANY_SERVICE_REALM_NAME) }.findAny()
		if (representationOptional.isPresent) {
			log.info("Removing already pre-configured '{}' realm", COMPANY_SERVICE_REALM_NAME)
			keycloakAdmin.realm(COMPANY_SERVICE_REALM_NAME).remove()
		}

		// Realm
		val realmRepresentation = RealmRepresentation()
		realmRepresentation.realm = COMPANY_SERVICE_REALM_NAME
		realmRepresentation.isEnabled = true
		realmRepresentation.isRegistrationAllowed = true

		// Client
		val clientRepresentation = ClientRepresentation()
		clientRepresentation.clientId = MOVIES_APP_CLIENT_ID
		clientRepresentation.isDirectAccessGrantsEnabled = true
		clientRepresentation.defaultRoles = arrayOf(MOVIES_APP_ROLES[0])
		clientRepresentation.isPublicClient = true
		clientRepresentation.redirectUris = listOf(MOVIES_APP_REDIRECT_URL)
		realmRepresentation.clients = listOf(clientRepresentation)

		// Users
		val userRepresentations = MOVIES_APP_USERS.stream().map { userPass: UserPass ->
			// Client roles
			val clientRoles: MutableMap<String, List<String>> = HashMap()
			if ("admin" == userPass.getUsername()) {
				clientRoles[MOVIES_APP_CLIENT_ID] = MOVIES_APP_ROLES
			} else {
				clientRoles[MOVIES_APP_CLIENT_ID] = listOf(MOVIES_APP_ROLES[0])
			}

			// User Credentials
			val credentialRepresentation = CredentialRepresentation()
			credentialRepresentation.type = CredentialRepresentation.PASSWORD
			credentialRepresentation.value = userPass.getPassword()

			// User
			val userRepresentation = UserRepresentation()
			userRepresentation.username = userPass.getUsername()
			userRepresentation.isEnabled = true
			userRepresentation.credentials = listOf(credentialRepresentation)
			userRepresentation.clientRoles = clientRoles
			userRepresentation
		}.collect(Collectors.toList())
		realmRepresentation.users = userRepresentations

		// Create Realm
		keycloakAdmin.realms().create(realmRepresentation)

		// Testing
		val admin = MOVIES_APP_USERS[0]
		log.info("Testing getting token for '{}' ...", admin.getUsername())
		val keycloakMovieApp: Keycloak = KeycloakBuilder.builder().serverUrl(keycloakServerUrl)
				.realm(COMPANY_SERVICE_REALM_NAME).username(admin.getUsername()).password(admin.getPassword())
				.clientId(MOVIES_APP_CLIENT_ID).build()
		log.info("'{}' token: {}", admin.getUsername(), keycloakMovieApp.tokenManager().grantToken().getToken())
		log.info("'{}' initialization completed successfully!", COMPANY_SERVICE_REALM_NAME)
	}

	@Data
	@AllArgsConstructor
	private class UserPass {
		private val username: String? = null
		private val password: String? = null
	}

	companion object {
		private const val COMPANY_SERVICE_REALM_NAME = "company-services"
		private const val MOVIES_APP_CLIENT_ID = "movies-app"
		private val MOVIES_APP_ROLES = Arrays.asList<String>(WebSecurityConfig.USER,
				WebSecurityConfig.MOVIES_MANAGER)
		private const val MOVIES_APP_REDIRECT_URL = "http://localhost:3000/*"
		private val MOVIES_APP_USERS = Arrays.asList(UserPass("admin", "admin"),
				UserPass("user", "user"))
	}
}
		**/