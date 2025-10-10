package com.sanya.mg.sanyademo.repository

import com.sanya.mg.sanyademo.repository.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?

    fun findByEmail(email: String): UserEntity?
}
