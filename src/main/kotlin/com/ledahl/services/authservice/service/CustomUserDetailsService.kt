package com.ledahl.services.authservice.service

import com.ledahl.services.authservice.model.CustomUserDetail
import com.ledahl.services.authservice.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(@Autowired private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        username ?: throw UsernameNotFoundException("Username cannot be null")
        val user = userRepository.getUserByUsername(username) ?: throw UsernameNotFoundException("User not found")
        return CustomUserDetail(user)
    }
}