package com.ledahl.services.authservice.config

import com.ledahl.services.authservice.model.CustomUserDetail
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import java.util.*

class CustomTokenEnhancer: JwtAccessTokenConverter() {
    override fun enhance(accessToken: OAuth2AccessToken?, authentication: OAuth2Authentication?): OAuth2AccessToken {
        val userDetails = authentication?.userAuthentication?.principal as? CustomUserDetail
        userDetails?.let {
            val additionalInfo = HashMap<String, Any>()
            additionalInfo[Constants.JWT_CLAIM_FIRST_NAME] = userDetails.user.firstName
            additionalInfo[Constants.JWT_CLAIM_LAST_NAME] = userDetails.user.lastName

            val customAccessToken = DefaultOAuth2AccessToken(accessToken)
            customAccessToken.additionalInformation = additionalInfo

            return super.enhance(customAccessToken, authentication)
        }

        return super.enhance(accessToken, authentication)
    }
}