package com.ledahl.services.authservice.config

import com.ledahl.services.authservice.authprovider.PhoneAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.filter.CommonsRequestLoggingFilter

@EnableWebSecurity
class WebSecurityConfiguration(@Autowired private val phoneAuthProvider: PhoneAuthenticationProvider): WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()
                    ?.antMatchers("/login", "/error", "/css/**", "/js/**")?.permitAll()
                ?.anyRequest()?.authenticated()
                ?.and()
                ?.formLogin()
                    ?.loginPage("/login")
                    ?.usernameParameter("username")
                    ?.passwordParameter("password")
                    ?.permitAll()
    }
    
    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.authenticationProvider(phoneAuthProvider)
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun requestLoggingFilter(): CommonsRequestLoggingFilter {
        val loggingFilter = CommonsRequestLoggingFilter()
        loggingFilter.setIncludeClientInfo(true)
        loggingFilter.setIncludeHeaders(true)
        loggingFilter.setIncludePayload(true)
        loggingFilter.setIncludeQueryString(true)
        return loggingFilter
    }
}