package com.ledahl.services.authservice.model

data class User(val id: Long,
                val externalId: String,
                val firstName: String,
                val lastName: String,
                val email: String,
                val password: String,
                val phoneNumber: String?,
                val authMethod: AuthMethod,
                val enabled: Boolean)