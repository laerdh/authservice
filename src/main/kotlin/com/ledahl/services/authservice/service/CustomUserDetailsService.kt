package com.ledahl.services.authservice.service

import com.ledahl.services.authservice.model.CustomUserDetail
import com.ledahl.services.authservice.model.UserInput
import com.ledahl.services.authservice.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(@Autowired private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username.isNullOrEmpty()) {
            throw UsernameNotFoundException("Missing credentials")
        }

        val user = userRepository.getUserByPhoneNumber(username) ?: throw UsernameNotFoundException("User not found")
        return CustomUserDetail(user)
    }

    fun createUser(username: String?, uid: String?): Boolean {
        if (username.isNullOrEmpty() || uid.isNullOrEmpty()) {
            throw BadCredentialsException("Bad credentials")
        }

        val newUser = UserInput(
                firebaseId = uid,
                phoneNumber = username,
                firstName = "Anonym",
                lastName = "Bruker"
        )

        return userRepository.createUser(newUser)
    }
}