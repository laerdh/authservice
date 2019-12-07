package com.ledahl.services.authservice.repository

import com.ledahl.services.authservice.model.User
import com.ledahl.services.authservice.model.UserInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class UserRepository(@Autowired private val jdbcTemplate: JdbcTemplate) {

    fun getUserByUsername(username: String): User? {
        val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
        val parameters = MapSqlParameterSource()
        parameters["username"] = username

        return try {
            namedTemplate.queryForObject("SELECT * FROM users WHERE email = :username", parameters) { rs, _ ->
                mapToUser(rs)
            }
        } catch (exception: DataAccessException) {
            null
        }
    }

    fun getUserByPhoneNumber(phoneNumber: String): User? {
        val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
        val parameters = MapSqlParameterSource()
        parameters["phone_number"] = phoneNumber

        return try {
            namedTemplate.queryForObject("SELECT * FROM users WHERE phone_number = :phone_number", parameters) { rs, _ ->
                mapToUser(rs)
            }
        } catch (exception: DataAccessException) {
            null
        }
    }

    fun createUser(user: UserInput): Boolean {
        val simpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingColumns("firebase_id", "phone_number", "first_name", "last_name")
                .usingGeneratedKeyColumns("id")

        val parameters = MapSqlParameterSource()
        parameters["firebase_id"] = user.firebaseId
        parameters["phone_number"] = user.phoneNumber
        parameters["first_name"] = user.firstName
        parameters["last_name"] = user.lastName

        return try {
            simpleJdbcInsert.execute(parameters) > 0
        } catch (e: DataAccessException) {
            false
        }
    }

    private fun mapToUser(rs: ResultSet): User {
        return User(
                id = rs.getLong("id"),
                externalId = rs.getString("external_id"),
                firebaseId = rs.getString("firebase_id") ?: "",
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                email = rs.getString("email"),
                phoneNumber = rs.getString("phone_number"),
                enabled = rs.getBoolean("enabled")
        )
    }
}