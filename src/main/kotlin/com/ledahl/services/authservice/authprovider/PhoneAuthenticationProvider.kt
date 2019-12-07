package com.ledahl.services.authservice.authprovider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.ledahl.services.authservice.model.CustomUserDetail
import com.ledahl.services.authservice.service.CustomUserDetailsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class PhoneAuthenticationProvider(@Autowired private val userDetailsService: CustomUserDetailsService,
                                  @Autowired private val firebaseAuth: FirebaseAuth): AbstractUserDetailsAuthenticationProvider() {

    private val log = LoggerFactory.getLogger(PhoneAuthenticationProvider::class.java)
    private val cachedUid = HashSet<String>()

    @Throws(AuthenticationException::class)
    override fun retrieveUser(username: String?, authentication: UsernamePasswordAuthenticationToken?): UserDetails {
        return try {
            userDetailsService.loadUserByUsername(username)
        } catch (e: UsernameNotFoundException) {
            log.info("User does not exist.", username)

            authenticateWithFirebase(username, authentication)?.let { uid ->
                log.info("Authenticated user ({}) with Firebase. Creating new user.", username)
                cachedUid.add(uid)
                userDetailsService.createUser(username, uid)
                return userDetailsService.loadUserByUsername(username)
            }

            throw e
        }
    }

    @Throws(AuthenticationException::class)
    override fun additionalAuthenticationChecks(userDetails: UserDetails?, authentication: UsernamePasswordAuthenticationToken?) {
        val user = (userDetails as CustomUserDetail).user
        authenticateWithFirebase(user.phoneNumber, authentication)
    }

    @Throws(AuthenticationException::class)
    private fun authenticateWithFirebase(phoneNumber: String?, authentication: UsernamePasswordAuthenticationToken?): String? {
        val uid = authentication?.credentials as? String ?: throw BadCredentialsException("Bad credentials")

        if (cachedUid.contains(uid)) {
            cachedUid.remove(uid)
            return uid
        }

        if (phoneNumber.isNullOrEmpty()) {
            throw BadCredentialsException("Bad credentials")
        }

        try {
            val firebaseUser = firebaseAuth.getUser(uid)
            if (firebaseUser.uid.isNullOrEmpty()
                    || firebaseUser.phoneNumber.isNullOrEmpty()
                    || firebaseUser.phoneNumber != phoneNumber) {

                log.info(
                        "Firebase authentication failed (Provided phone number: {}, Firebase phone number: {}, uid: {})",
                        phoneNumber,
                        firebaseUser.phoneNumber,
                        firebaseUser.uid
                )

                throw BadCredentialsException("Bad credentials")
            }

            log.info("Authenticated user ({}) with Firebase", phoneNumber)
            return uid
        } catch (e: FirebaseAuthException) {
            log.info("Failed to fetch Firebase user", e)
            throw BadCredentialsException("Bad credentials")
        }
    }
}