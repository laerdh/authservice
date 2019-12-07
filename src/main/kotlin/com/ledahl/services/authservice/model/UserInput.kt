package com.ledahl.services.authservice.model

data class UserInput(val firebaseId: String,
                     val phoneNumber: String,
                     val firstName: String,
                     val lastName: String)