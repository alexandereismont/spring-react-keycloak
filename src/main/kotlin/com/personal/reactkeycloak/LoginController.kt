package com.personal.reactkeycloak

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {

	@GetMapping("/login")
	fun login() = "login"

}