package com.ledahl.services.authservice.model

data class User(val id: Long,
                val firstName: String,
                val lastName: String,
                val email: String,
                val password: String,
                val phoneNumber: String,
                val enabled: Boolean)