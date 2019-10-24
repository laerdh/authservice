package com.ledahl.services.authservice.config.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.provider.AuthorizationRequest
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Controller
@SessionAttributes("authorizationRequest")
class ClientAuthorizationController(@Autowired private val clientDetailsService: ClientDetailsService) {

    @RequestMapping("/oauth/confirm_access")
    @Throws(Exception::class)
    fun getAccessConfirmation(@ModelAttribute clientAuth: AuthorizationRequest): ModelAndView {
        val client = clientDetailsService.loadClientByClientId(clientAuth.clientId)
        val model = TreeMap<String, Any>()
        model["auth_request"] = clientAuth
        model["client"] = client
        return ModelAndView("confirm_access", model)
    }
}