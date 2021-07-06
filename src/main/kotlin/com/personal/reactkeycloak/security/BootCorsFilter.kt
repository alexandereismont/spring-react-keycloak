package com.personal.reactkeycloak.security;

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class BootCorsFilter() : Filter {
	private val urlWhitelist: List<String> = listOf(
			"http://localhost:3000",
			"http://localhost:8180"
	)

	@Throws(IOException::class, ServletException::class)
	override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
		val response = res as HttpServletResponse
		val request = req as HttpServletRequest
		val origin = req.getHeader("Origin")
		urlWhitelist.stream().filter { s: String -> s == origin }.findFirst().ifPresent { s: String? ->
			response.setHeader("Access-Control-Allow-Origin", s)
			response.setHeader("Access-Control-Allow-Credentials", "true")
			response.setHeader("Access-Control-Allow-Methods",
					"ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL")
			response.setHeader("Access-Control-Max-Age", "3600")
			response.setHeader("Access-Control-Expose-Headers",
					"Origin, Location, X-Requested-With, Content-Type, Accept, Key, Authorization")
			response.setHeader("Access-Control-Allow-Headers",
					"Origin, Location, X-Requested-With, Content-Type, Accept, Key, Authorization")
		}
		if ("OPTIONS".equals(request.method, ignoreCase = true)) {
			response.status = HttpServletResponse.SC_OK
		} else {
			chain.doFilter(req, res)
		}
	}

	override fun init(filterConfig: FilterConfig) {
		// not needed
	}

	override fun destroy() {
		//not needed
	}

}**/