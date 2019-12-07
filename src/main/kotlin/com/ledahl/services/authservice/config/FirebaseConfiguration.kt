package com.ledahl.services.authservice.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException

@Configuration
class FirebaseConfiguration {

    @Value("\${FIREBASE_CONFIGURATION_FILE}")
    private lateinit var firebaseConfig: String

    @Value("\${FIREBASE_CONFIGURATION_URL}")
    private lateinit var firebaseUrl: String

    @Bean
    @Throws(IOException::class)
    fun firebaseAuth(): FirebaseAuth {
        val classLoader = Thread.currentThread().contextClassLoader
        val serviceAccount = classLoader.getResourceAsStream(firebaseConfig)
        val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(firebaseUrl)
                .build()

        FirebaseApp.initializeApp(options)
        return FirebaseAuth.getInstance()
    }
}