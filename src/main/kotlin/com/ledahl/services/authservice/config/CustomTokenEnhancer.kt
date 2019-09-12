package com.ledahl.services.authservice.config

import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import java.lang.RuntimeException
import java.util.*

class CustomTokenEnhancer: TokenEnhancer {
    override fun enhance(accessToken: OAuth2AccessToken?, authentication: OAuth2Authentication?): OAuth2AccessToken {
        val additionalInfo = HashMap<String, Any>()
        additionalInfo["organization"] = "LEDAHL.COM"
        return accessToken ?: throw RuntimeException("No access token provided")
    }
}