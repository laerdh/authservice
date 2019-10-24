package com.ledahl.services.authservice.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.approval.ApprovalStore
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import javax.sql.DataSource

@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(JwtProperties::class)
class AuthorizationServerConfiguration(@Autowired private val dataSource: DataSource,
                                       @Autowired private val passwordEncoder: BCryptPasswordEncoder,
                                       @Autowired private val authenticationConfiguration: AuthenticationConfiguration,
                                       @Autowired private val jwtProperties: JwtProperties): AuthorizationServerConfigurerAdapter() {

    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security
                ?.tokenKeyAccess("permitAll()")
                ?.checkTokenAccess("isAuthenticated()")
                ?.allowFormAuthenticationForClients()
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints?.authenticationManager(authenticationConfiguration.authenticationManager)
                ?.tokenServices(tokenServices())
                ?.accessTokenConverter(accessTokenConverter())
                ?.userApprovalHandler(userApprovalHandler())
                ?.tokenEnhancer(accessTokenConverter())
    }

    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients?.withClientDetails(jdbcClientDetailsService())
    }

    @Bean
    fun jdbcClientDetailsService(): ClientDetailsService {
        return JdbcClientDetailsServiceBuilder()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .build()
    }

    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(accessTokenConverter())
    }

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        val keyStoreFactory = KeyStoreKeyFactory(jwtProperties.keyStore, jwtProperties.keyStorePassword.toCharArray())
        val keyPair = keyStoreFactory.getKeyPair(jwtProperties.keyPairAlias, jwtProperties.keyPairPassword.toCharArray())
        val converter = CustomTokenEnhancer()
        converter.setKeyPair(keyPair)
        return converter
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices {
        val defaultTokenServices = DefaultTokenServices()
        defaultTokenServices.setTokenStore(tokenStore())
        defaultTokenServices.setSupportRefreshToken(true)
        defaultTokenServices.setTokenEnhancer(accessTokenConverter())
        return defaultTokenServices
    }

    @Bean
    fun userApprovalHandler(): UserApprovalHandler {
        val userApprovalHandler = ApprovalStoreUserApprovalHandler()
        userApprovalHandler.setApprovalStore(approvalStore())
        userApprovalHandler.setClientDetailsService(jdbcClientDetailsService())
        userApprovalHandler.setRequestFactory(requestFactory(jdbcClientDetailsService()))
        return userApprovalHandler
    }

    @Bean
    fun approvalStore(): ApprovalStore {
        return JdbcApprovalStore(dataSource)
    }

    @Bean
    fun requestFactory(clientDetailsService: ClientDetailsService): DefaultOAuth2RequestFactory {
        return DefaultOAuth2RequestFactory(clientDetailsService)
    }
}