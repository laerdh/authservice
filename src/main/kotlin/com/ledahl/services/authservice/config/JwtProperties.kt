package com.ledahl.services.authservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

@ConfigurationProperties(prefix = "security.jwt")
class JwtProperties {
    lateinit var keyStore: Resource
    lateinit var keyStorePassword: String
    lateinit var keyPairAlias: String
    lateinit var keyPairPassword: String
}