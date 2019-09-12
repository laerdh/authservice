package com.ledahl.services.authservice.config

import com.ledahl.services.authservice.service.CustomUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@Order(1)
class WebSecurityConfiguration(@Autowired private val customUserDetailsService: CustomUserDetailsService): WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http?.requestMatchers()
                ?.antMatchers("/login", "/oauth/authorize")
                ?.and()
                ?.authorizeRequests()
                ?.anyRequest()?.authenticated()
                ?.and()
                ?.formLogin()?.permitAll()
    }

    @Autowired
    fun globalUserDetails(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(customUserDetailsService)
                ?.passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}