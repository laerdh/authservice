package com.ledahl.services.authservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration(@Autowired private val passwordEncoder: BCryptPasswordEncoder): AuthorizationServerConfigurerAdapter() {
    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security
                ?.tokenKeyAccess("permitAll()")
                ?.checkTokenAccess("isAuthenticated()")
                ?.allowFormAuthenticationForClients()
    }

    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients?.inMemory()
                ?.withClient("clientapp")
                ?.secret(passwordEncoder.encode("123456"))
                ?.authorizedGrantTypes("password", "authorization_code", "refresh_token")
                ?.authorities("READ_ONLY_CLIENT")
                ?.scopes("read_profile_info")
                ?.resourceIds("oauth2-resource")
                ?.redirectUris("http://www.vg.no")
                ?.accessTokenValiditySeconds(5000)
                ?.refreshTokenValiditySeconds(50000)
    }
}