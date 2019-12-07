package com.ledahl.services.authservice.model

data class User(val id: Long,
                val externalId: String,
                val firebaseId: String,
                val phoneNumber: String,
                val firstName: String,
                val lastName: String,
                val email: String?,
                val enabled: Boolean)