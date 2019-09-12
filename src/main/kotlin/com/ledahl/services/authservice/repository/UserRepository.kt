package com.ledahl.services.authservice.repository

import com.ledahl.services.authservice.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Repository

@Repository
class UserRepository(@Autowired private val jdbcTemplate: JdbcTemplate) {
    fun getUserByUsername(username: String): User? {
        val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
        val parameters = MapSqlParameterSource()
        parameters["username"] = username

        return try {
            namedTemplate.queryForObject("SELECT * FROM users WHERE email = :username", parameters) { rs, _ ->
                User(
                        id = rs.getLong("id"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        email = rs.getString("email"),
                        password = rs.getString("password"),
                        phoneNumber = rs.getString("phone_number"),
                        enabled = rs.getBoolean("enabled")
                )
            }
        } catch (exception: DataAccessException) {
            null
        }
    }
}