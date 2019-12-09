package com.ledahl.services.authservice.controller

import com.ledahl.services.authservice.model.CustomUserDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
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
        val userDetail = SecurityContextHolder.getContext().authentication.principal as? CustomUserDetail
        val model = TreeMap<String, Any?>()
        model["auth_request"] = clientAuth
        model["client_id"] = client.clientId
        model["username"] = userDetail?.user?.email
        model["additional_information"] = client.additionalInformation

        return ModelAndView("confirm_access", model)
    }
}