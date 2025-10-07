package com.sanya.mg.sanyademo.api.user.dto

import com.sanya.mg.sanyademo.repository.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class UserDto(
    @field:Schema(description = "User ID", example = "1")
    val id: Long,
    @field:Schema(description = "Username", example = "john_doe")
    val username: String,
    @field:Schema(description = "Email address", example = "john.doe@example.com")
    val email: String,
    @field:Schema(description = "Account creation timestamp", example = "2025-10-01T12:00:00")
    val createdAt: LocalDateTime,
) {
    companion object {
        infix fun fromEntity(user: User): UserDto = UserDto(
            user.id!!,
            user.username,
            user.email,
            user.createdAt,
        )
    }
}
