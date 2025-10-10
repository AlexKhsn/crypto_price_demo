package com.sanya.mg.sanyademo.service.model

import com.sanya.mg.sanyademo.repository.entity.UserEntity
import java.time.LocalDateTime

data class UserModel(
    val id: Long,
    val username: String,
    val email: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun UserEntity.toModel() = UserModel(
            id = this.id!!,
            username = this.username,
            email = this.email,
            createdAt = this.createdAt,
        )
    }
}
