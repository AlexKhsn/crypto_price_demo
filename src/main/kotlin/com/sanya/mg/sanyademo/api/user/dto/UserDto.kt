package com.sanya.mg.sanyademo.api.user.dto

import com.sanya.mg.sanyademo.repository.entity.User
import java.time.LocalDateTime

data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
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
