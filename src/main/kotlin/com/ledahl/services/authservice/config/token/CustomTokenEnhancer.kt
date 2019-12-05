package com.ledahl.services.authservice.config.token

import com.ledahl.services.authservice.model.CustomUserDetail
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.stereotype.Component

@Component
class CustomTokenEnhancer: TokenEnhancer {
    override fun enhance(accessToken: OAuth2AccessToken?, authentication: OAuth2Authentication?): OAuth2AccessToken {
        val userDetails = authentication?.userAuthentication?.principal as? CustomUserDetail
        userDetails?.let {
            val additionalInfo = HashMap<String, Any>()
            additionalInfo[TokenClaims.USER_ID] = it.user.externalId
            additionalInfo[TokenClaims.FIRST_NAME] = it.user.firstName
            additionalInfo[TokenClaims.LAST_NAME] = it.user.lastName
            (accessToken as? DefaultOAuth2AccessToken)?.additionalInformation = additionalInfo
        }
        return accessToken ?: throw IllegalArgumentException("Could not enhance token")
    }
}