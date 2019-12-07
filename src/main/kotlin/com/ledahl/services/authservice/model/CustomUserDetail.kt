package com.ledahl.services.authservice.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class CustomUserDetail(val user: User): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(GrantedAuthority { "USER" })
    }

    override fun isEnabled(): Boolean {
        return user.enabled
    }

    override fun getUsername(): String {
        return user.phoneNumber
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return user.firebaseId
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }
}